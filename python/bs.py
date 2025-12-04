# ry_deepseek.py
from flask import Flask, request, jsonify
import pandas as pd
import numpy as np
from sqlalchemy import create_engine, text
from scipy.sparse import csr_matrix, save_npz, load_npz
from sklearn.neighbors import NearestNeighbors
from surprise import SVD, Dataset, Reader
from tqdm import tqdm  
import joblib
import logging
import os
import time
import schedule
from threading import Thread
from waitress import serve
from flask_cors import CORS
# ====================== 配置 ======================
# 数据库配置
DB_CONFIG = {
    'user': 'root',
    'password': '200259',
    'host': 'localhost',
    'port': '3306',
    'database': 'ry-vue'
}

# 全局路径
MODEL_DIR = "models"
os.makedirs(MODEL_DIR, exist_ok=True)

# ====================== 日志配置 ======================
logging.basicConfig(
    level=logging.INFO,
    format='[%(asctime)s] %(message)s',
    datefmt='%Y-%m-%d %H:%M:%S'
)
logger = logging.getLogger()

# ====================== 核心功能 ======================
def refresh_models():
    """全量更新模型和数据（命令行进度版）"""
    global user_item_sparse, svd_model, user_to_index, book_to_index
    
    try:
        # 数据加载
        logger.info("📥 加载评分数据...")
        engine = create_engine(
            f"mysql+pymysql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}?charset=utf8mb4"
        )
        ratings = pd.read_sql('SELECT userId, bookIndex, score FROM user_reviews_dataset', engine)
        
        # 数据清洗
        ratings = ratings.drop_duplicates(subset=['userId', 'bookIndex'], keep='last')
        ratings = ratings[(ratings['score'] >= 1) & (ratings['score'] <= 5)]
        logger.info(f"✅ 有效数据加载完成：{len(ratings)}条评分")

        # 重建映射
        user_ids = ratings['userId'].unique()
        book_ids = ratings['bookIndex'].unique()
        user_to_index = {user: idx for idx, user in enumerate(user_ids)}
        book_to_index = {book: idx for idx, book in enumerate(book_ids)}

        # 构建稀疏矩阵
        logger.info("🔨 构建稀疏矩阵...")
        row_indices = ratings['userId'].map(user_to_index)
        col_indices = ratings['bookIndex'].map(book_to_index)
        user_item_sparse = csr_matrix(
            (ratings['score'], (row_indices, col_indices)),
            shape=(len(user_ids), len(book_ids)))
        save_npz(os.path.join(MODEL_DIR, 'user_item_sparse.npz'), user_item_sparse)

        # 训练模型
        logger.info("🎯 开始训练SVD模型...")
        reader = Reader(rating_scale=(1, 5))
        data = Dataset.load_from_df(ratings[['userId', 'bookIndex', 'score']], reader)
        trainset = data.build_full_trainset()
        
        # 初始化模型
        svd_model = SVD(n_factors=50, n_epochs=20)
        
        # 使用标准fit方法训练
        with tqdm(total=svd_model.n_epochs, desc="模型训练", unit="epoch") as pbar:
            # 重写训练逻辑以支持进度条
            for _ in range(svd_model.n_epochs):
                svd_model.fit(trainset)  # 直接调用fit会完成所有epoch，需手动模拟进度
                pbar.update(1)
        
        joblib.dump(svd_model, os.path.join(MODEL_DIR, 'svd_model.pkl'))

        # 生成推荐结果
        logger.info("📤 生成推荐结果...")
        total_users = len(user_ids)
        batch_size = 500
        
        with engine.begin() as conn:
            conn.execute(text("TRUNCATE TABLE recommendation"))
            
        with tqdm(total=total_users, desc="用户处理进度", unit="user") as main_pbar:
            for i in range(0, total_users, batch_size):
                batch_users = user_ids[i:i+batch_size]
                recommendations = []
                
                for user_id in batch_users:
                    rated = ratings[ratings['userId'] == user_id]['bookIndex'].tolist()
                    unrated = list(set(book_ids) - set(rated))
                    
                    user_preds = []
                    for book in unrated:
                        try:
                            pred = svd_model.predict(user_id, book)
                            user_preds.append( (book, pred.est) )
                        except:
                            continue
                    top_50 = sorted(user_preds, key=lambda x: x[1], reverse=True)[:50]
                    recommendations.extend([(user_id, book, score) for book, score in top_50])
                
                if recommendations:
                    df = pd.DataFrame(recommendations, columns=['userId', 'bookIndex', 'predicted_score'])
                    df.to_sql('recommendation', engine, if_exists='append', index=False, method='multi')
                
                main_pbar.update(len(batch_users))
                main_pbar.set_postfix({"进度": f"{min(i+batch_size, total_users)}/{total_users}"})

        # 保存元数据
        np.save(os.path.join(MODEL_DIR, 'user_to_index.npy'), user_to_index)
        np.save(os.path.join(MODEL_DIR, 'book_to_index.npy'), book_to_index)
        
        logger.info("🎉 全量更新完成！")
        
    except Exception as e:
        logger.error(f"❌ 更新失败: {str(e)}")
        raise

def update_user_recommendations(user_id):
    """为单个用户更新推荐结果"""
    try:
        global svd_model, user_to_index, book_to_index
        
        # 加载模型和映射表（如果未加载）
        if 'svd_model' not in globals():
            svd_model = joblib.load(os.path.join(MODEL_DIR, 'svd_model.pkl'))
            user_to_index = np.load(os.path.join(MODEL_DIR, 'user_to_index.npy'), allow_pickle=True).item()
            book_to_index = np.load(os.path.join(MODEL_DIR, 'book_to_index.npy'), allow_pickle=True).item()
        
        # 获取用户评分数据
        engine = create_engine(
            f"mysql+pymysql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}?charset=utf8mb4"
        )
        ratings = pd.read_sql(f'SELECT bookIndex, score FROM user_reviews_dataset WHERE userId={user_id}', engine)
        rated_books = ratings['bookIndex'].tolist()
        
        # 获取所有书籍ID并过滤未评分的
        all_books = pd.read_sql('SELECT DISTINCT bookIndex FROM user_reviews_dataset', engine)['bookIndex'].tolist()
        unrated_books = list(set(all_books) - set(rated_books))
        
        # 生成预测评分
        recommendations = []
        for book in unrated_books:
            try:
                pred = svd_model.predict(user_id, book)
                recommendations.append((user_id, book, pred.est))
            except:
                continue
        
        # 取Top50并更新数据库
        top_50 = sorted(recommendations, key=lambda x: x[2], reverse=True)[:50]
        df = pd.DataFrame(top_50, columns=['userId', 'bookIndex', 'predicted_score'])
        
        with engine.begin() as conn:
            # 删除旧推荐
            conn.execute(text(f"DELETE FROM recommendation WHERE userId={user_id}"))
            # 插入新推荐
            if not df.empty:
                df.to_sql('recommendation', conn, if_exists='append', index=False, method='multi')
        
        logger.info(f"🔄 用户{user_id}推荐数据增量更新完成")
        
    except Exception as e:
        logger.error(f"❌ 用户{user_id}增量更新失败: {str(e)}")
        raise


# ====================== Flask接口 ======================
app = Flask(__name__)
CORS(app)  # 解决跨域问题

# 在 handle_rating 接口中添加数据库插入逻辑
@app.route('/update', methods=['POST'])
def handle_rating():
    data = request.json
    try:
        user_id = int(data.get('userId'))
        book_id = int(data.get('bookIndex'))
        score = float(data.get('score'))
        comment = data.get('comment', '')
    except (TypeError, ValueError) as e:
        logger.error(f"参数类型错误: {str(e)}")
        return jsonify({"status": "error", "message": "Invalid parameter type"}), 400

    engine = create_engine(f"mysql+pymysql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}?charset=utf8mb4")
    
    try:
        with engine.begin() as conn:
            conn.execute(text("""
                INSERT INTO user_reviews_dataset (userId, bookIndex, score, comment)
                VALUES (:uid, :bid, :score, :comment)
                ON DUPLICATE KEY UPDATE 
                    score = VALUES(score),
                    comment = VALUES(comment)
            """), {
                "uid": user_id,
                "bid": book_id,
                "score": score,
                "comment": comment
            })
            
        # 新增：触发增量更新
        update_user_recommendations(user_id)
        
        return jsonify({"status": "success", "message": "Recommendations updated"})
        
    except Exception as e:
        logger.error(f"❌ 用户{user_id}更新失败: {str(e)}")
        return jsonify({"status": "error", "message": str(e)}), 500

@app.route('/recommend/<int:user_id>')
def get_recommend(user_id):
    try:
        engine = create_engine(
            f"mysql+pymysql://{DB_CONFIG['user']}:{DB_CONFIG['password']}@{DB_CONFIG['host']}:{DB_CONFIG['port']}/{DB_CONFIG['database']}?charset=utf8mb4"
        )
        is_new = pd.read_sql(
            f"SELECT COUNT(*) AS cnt FROM user_reviews_dataset WHERE userId={user_id}", 
            engine
        ).iloc[0]['cnt'] == 0
        
        if is_new:
            hot_books = pd.read_sql(
                """SELECT bookIndex FROM user_reviews_dataset 
                   GROUP BY bookIndex HAVING COUNT(*) > 10 
                   ORDER BY AVG(score) DESC LIMIT 50""", 
                engine
            )['bookIndex'].tolist()
            return jsonify({"type": "hot", "books": hot_books})
        else:
            recs = pd.read_sql(
                f"""SELECT bookIndex, predicted_score 
                    FROM recommendation 
                    WHERE userId={user_id} 
                    ORDER BY predicted_score DESC LIMIT 50""",
                engine
            ).to_dict('records')
            return jsonify({"type": "personalized", "books": recs})
    except Exception as e:
        return jsonify({"status": "error", "message": str(e)}), 500

# ====================== 定时任务 ======================
def schedule_retrain():
    while True:
        schedule.every().day.at("03:00").do(refresh_models)
        time.sleep(60)
        schedule.run_pending()

# ====================== 主程序 ======================
if __name__ == '__main__':
    # 初始化检查
    if not os.path.exists(os.path.join(MODEL_DIR, 'svd_model.pkl')):
        logger.info("⏳ 未检测到模型，开始初始化训练...")
        refresh_models()
    
    # 启动定时任务
    Thread(target=schedule_retrain, daemon=True).start()
    
    # 启动服务
    logger.info("🚀 服务已启动：http://localhost:5000")
    serve(app, host='0.0.0.0', port=5000)