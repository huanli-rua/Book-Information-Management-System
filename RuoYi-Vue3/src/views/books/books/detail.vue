<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>书籍详情</span>
        <el-divider />
      </div>
      <!-- 使用可选链操作符保护数据渲染 -->
      <el-descriptions :column="2" border>
        <el-descriptions-item label="书籍名称">{{ detail.books?.bookName || '加载中...' }}</el-descriptions-item>
        <el-descriptions-item label="作者">{{ detail.books?.author || '未知作者' }}</el-descriptions-item>
        <el-descriptions-item label="评分">{{ detail.books?.rating || 0 }}</el-descriptions-item>
        <el-descriptions-item label="投票数">{{ detail.books?.numberOfVotes || 0 }}</el-descriptions-item>
        <el-descriptions-item label="综合得分">{{ detail.books?.score || 0 }}</el-descriptions-item>
        <el-descriptions-item label="我的评分">{{ detail.userRating?.score || '暂无评分' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider />

      <!-- 表单禁用状态绑定计算属性 -->
      <el-form :model="ratingForm" label-width="100px">
        <el-form-item label="评分" prop="score">
          <el-rate
              v-model="ratingForm.score"
              :max="5"
              :disabled="hasRated"
          />
        </el-form-item>
        <el-form-item label="评论">
          <el-input
              type="textarea"
              v-model="ratingForm.comment"
              placeholder="请输入评论"
              :disabled="hasRated"
              rows="4"
          />
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              @click="submitRating"
              :disabled="hasRated"
          >
            {{ hasRated ? '已评分' : '提交评分' }}
          </el-button>
        </el-form-item>
      </el-form>
      <el-divider />

      <!-- 评论列表 -->
      <div class="comment-section">
        <h3>全部评论（{{ commentList.length }}条）</h3>
        <el-empty v-if="displayCommentList.length === 0" description="暂无评论" />
        <div v-for="(comment, index) in displayCommentList" :key="index" class="comment-item">
          <div class="comment-header">
            <span class="user-id">匿名用户{{ comment.userId }}</span>
            <el-rate
                v-model="comment.score"
                :max="5"
                disabled
                class="comment-rating"
            />
          </div>
          <div class="comment-content">
            {{ comment.comment || '此用户没有留下文字评论' }}
          </div>
          <el-divider />
        </div>
        <!-- 分页组件 -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="currentPage"
            :page-sizes="[5, 10, 20]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="commentList.length"
        >
        </el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getBookDetail, getAllComments } from '@/api/books/books'
import { submitRating as submitRatingAPI } from '@/api/books/books'
import { ElMessage } from "element-plus"

const route = useRoute()
// 修复1：确保 bookIndex 为数值类型
const bookIndex = ref(Number(route.params.bookIndex))
const detail = ref({ books: {}, userRating: null })  // 初始化为 null 避免 undefined
const ratingForm = ref({ score: 0, comment: '' })
const commentList = ref([])
// 分页相关变量
const currentPage = ref(1)
const pageSize = ref(5)

// 修复2：正确使用计算属性判断是否已评分
const hasRated = computed(() => {
  return detail.value.userRating?.score !== undefined
})

// 计算当前页要显示的评论列表
const displayCommentList = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return commentList.value.slice(start, end)
})

// 加载数据（优化并行加载）
const loadData = async () => {
  try {
    // 并行请求详情和评论
    const [detailRes, commentRes] = await Promise.all([
      getBookDetail(bookIndex.value),
      getAllComments(bookIndex.value)
    ])

    // 更新数据
    detail.value = {
      books: detailRes.data?.books || {},  // 保护性赋值
      userRating: detailRes.data?.userRating || null
    }
    commentList.value = commentRes.data || []

    // 更新表单数据
    if (detail.value.userRating) {
      ratingForm.value = {
        score: detail.value.userRating.score,
        comment: detail.value.userRating.comment || ''
      }
    }
  } catch (error) {
    ElMessage.error('数据加载失败: ' + (error.response?.data?.message || error.message))
  }
}

// 提交评分（优化错误提示）
const submitRating = async () => {
  try {
    if (hasRated.value) {
      ElMessage.warning('您已评过分，不可重复提交')
      return
    }

    await submitRatingAPI({
      bookIndex: bookIndex.value,
      score: ratingForm.value.score,
      comment: ratingForm.value.comment
    })

    // 提交成功后强制刷新数据
    await loadData()
    ElMessage.success('评分成功')
    // ElMessage.success('数据已更新')
  } catch (error) {
    ElMessage.error(`提交失败: ${error.response?.data?.message || error.message}`)
  }
}

// 处理每页显示条数变化
const handleSizeChange = (newSize) => {
  pageSize.value = newSize
  currentPage.value = 1
}

// 处理当前页码变化
const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
/*
 * 全局样式：统一间距和容器样式
 * 限制内容最大宽度为 1200px，并使其水平居中
 * 添加内边距，使内容不紧贴容器边缘
 */
.app-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

/*
 * 书籍信息卡片样式
 * 为卡片添加底部外边距，使卡片之间有间隔
 * 使用线性渐变背景，增强视觉效果
 */
.book-info-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  border-radius: 8px; /* 添加圆角，使卡片更美观 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影，增强立体感 */
}

/* 书籍信息卡片头部样式 */
.book-info-card .book-header {
  padding: 20px;
  border-bottom: 1px solid #ebeef5;
}

/* 书籍标题样式 */
.book-info-card .book-header .book-title {
  font-size: 24px;
  color: #303133;
  margin-bottom: 10px;
  font-weight: 600; /* 增加标题字体粗细 */
}

/* 书籍元信息样式 */
.book-info-card .book-header .meta-info {
  display: flex;
  align-items: center;
  gap: 15px;
  color: #606266;
}

/* 元信息分隔符样式 */
.book-info-card .book-header .meta-info .divider {
  color: #dcdfe6;
}

/*
 * 数据面板样式
 * 添加内边距，使数据项有足够的空间
 */
.data-panel {
  padding: 20px;
  border-radius: 8px; /* 添加圆角 */
  background-color: #fafafa; /* 添加背景色 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}

/* 数据项样式 */
.data-panel .data-item {
  text-align: center;
}

/* 数据项标签样式 */
.data-panel .data-item .label {
  color: #909399;
  margin-bottom: 8px;
}

/* 数据项值样式 */
.data-panel .data-item .value {
  font-size: 24px;
  font-weight: 500;
}

/* 评分值样式 */
.data-panel .data-item .value.score {
  color: #f56c6c;
}

/* 用户评分值样式 */
.data-panel .data-item .value.user-score {
  color: #67c23a;
}

/*
 * 评分卡片样式
 * 添加底部外边距，使卡片与其他元素有间隔
 */
.rating-card {
  margin-bottom: 20px;
  border-radius: 8px; /* 添加圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 添加阴影 */
  padding: 20px; /* 添加内边距 */
}

/* 评分卡片标题样式 */
.rating-card .section-title {
  color: #303133;
  margin-bottom: 20px;
  font-size: 20px; /* 调整标题字体大小 */
  font-weight: 600; /* 增加标题字体粗细 */
}

/* 提交按钮样式 */
.rating-card .submit-btn {
  width: 200px;
  margin-top: 15px;
  border-radius: 4px; /* 添加圆角 */
  text-align: center; /* 文本居中 */
  cursor: pointer; /* 鼠标指针样式 */
  transition: background-color 0.3s ease; /* 添加过渡效果 */
}

/* 提交按钮悬停样式 */
.rating-card .submit-btn:hover {
  background-color: #e0e0e0; /* 悬停时背景色变化 */
}

/*
 * 评论列表卡片样式
 * 添加圆角和阴影，增强视觉效果
 */
.comment-card {
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

/* 评论列表头部样式 */
.comment-card .comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

/* 评论项样式 */
.comment-card .comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5; /* 添加底部边框，分隔评论项 */
}

/* 评论用户信息样式 */
.comment-card .comment-item .user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

/* 评论用户元信息样式 */
.comment-card .comment-item .user-info .meta {
  flex: 1;
}

/* 评论用户名样式 */
.comment-card .comment-item .user-info .meta .username {
  font-weight: 500;
  color: #303133;
}

/* 评论时间样式 */
.comment-card .comment-item .user-info .meta .time {
  color: #909399;
  font-size: 12px;
}

/* 评论内容样式 */
.comment-card .comment-item .content {
  color: #606266;
  line-height: 1.6;
  padding-left: 52px; /* 对齐头像 */
}
</style>