<template>
  <div class="app-container">
    <!-- 热门书籍和猜你喜欢同一行 -->
    <div class="section-row">
      <!-- 热门书籍 -->
      <div class="section">
        <h3 class="section-title">热门书籍</h3>
        <el-table
            v-loading="loading"
            :data="booksList"
            @row-click="handleRowClick"
            class="clickable-table"
            stripe
            highlight-current-row
        >
          <el-table-column label="十大热门" align="center" prop="bookName" />
        </el-table>
      </div>
      <!-- 猜你喜欢 -->
      <div class="section">
        <div class="title-container">
          <h3 class="section-title">猜你喜欢</h3>
          <el-button @click="getNextList">换一换</el-button>
        </div>
        <el-table
            v-loading="recommendationLoading"
            :data="recommendationList"
            @row-click="handleRowClick"
            class="clickable-table"
        >
          <el-table-column label="猜你喜欢" prop="bookName" align="center" />
        </el-table>
      </div>
    </div>

    <!-- 浏览历史 -->
    <div class="section">
      <h3 class="section-title">浏览历史</h3>
      <el-table
          v-loading="historyLoading"
          :data="ratingHistoryList"
          @row-click="handleRowClick"
          class="clickable-table"
          stripe
          highlight-current-row
      >
        <el-table-column label="我的浏览历史" align="center" prop="bookName" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listBooks } from "@/api/books/books";
import { listMyRating } from "@/api/UserRating/MyRating";  // 导入评分列表接口
import { listRecommendation } from "@/api/books/recommendation";

const router = useRouter()

// 数据定义
const booksList = ref([])          // 热门书籍数据
const ratingHistoryList = ref([])  // 浏览历史数据（仅书名）
const loading = ref(true)          // 热门书籍加载状态
const historyLoading = ref(true)   // 历史记录加载状态

// 猜你喜欢相关数据
const recommendationList = ref([]);
const recommendationLoading = ref(true);
const pageNum = ref(1); // 当前页码
const pageSize = 10; // 每页显示数量
const totalPages = ref(0); // 总页数

// 获取热门书籍列表
const getList = () => {
  loading.value = true
  listBooks().then(response => {
    booksList.value = response.rows
    loading.value = false
  }).catch(error => {
    console.error('获取书籍列表失败:', error)
    loading.value = false
  })
}

// 获取用户评分历史（仅书名）
const getRatingHistory = () => {
  historyLoading.value = true
  listMyRating().then(response => {
    ratingHistoryList.value = response.rows  // 直接使用接口返回的 bookName
    historyLoading.value = false
  }).catch(error => {
    console.error('获取评分历史失败:', error)
    historyLoading.value = false
  })
}

// 获取猜你喜欢列表
const getRecommendationList = () => {
  recommendationLoading.value = true;
  listRecommendation({ pageNum: pageNum.value, pageSize })
      .then((response) => {
        if (response.rows && response.rows.length > 0) {
          recommendationList.value = response.rows;
          // 计算总页数
          totalPages.value = Math.ceil(response.total / pageSize);
        }
        recommendationLoading.value = false;
      })
      .catch(() => {
        recommendationLoading.value = false;
      });
}

// 获取下一页数据
const getNextList = () => {
  if (pageNum.value < totalPages.value) {
    // 还有更多页，页码加1
    pageNum.value++;
  } else {
    // 已经是最后一页，回到第一页
    pageNum.value = 1;
  }
  getRecommendationList();
};

// 行点击跳转（复用热门书籍的跳转逻辑）
const handleRowClick = (row) => {
  router.push(`/detail/${row.bookIndex}`)
}

// 初始化加载
onMounted(() => {
  getList()
  getRatingHistory()
  getRecommendationList()
})
</script>

<style scoped lang="less">
.app-container {
  padding: 20px;
  background-color: #f9f9f9; // 整体背景色
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.section {
  flex: 1;
  background-color: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  }
}

.title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.section-title {
  color: #303133;
  font-size: 18px;
  border-left: 4px solid #409eff;
  padding-left: 8px;
}

.clickable-table {
  width: 100%;

  ::v-deep .el-table__body tr:hover > td {
    background: #f5f7fa;
    cursor: pointer;
    transition: background 0.3s ease;
  }

  ::v-deep .el-table__header {
    background-color: #f0f0f0;
  }

  ::v-deep .el-table__row {
    transition: background 0.3s ease;
  }
}
</style>