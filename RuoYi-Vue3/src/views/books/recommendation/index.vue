<template>
  <div class="app-container">
    <el-table
        v-loading="loading"
        :data="recommendationList"
        @row-click="handleRowClick"
        class="clickable-table"
    >
      <el-table-column label="猜你喜欢" prop="bookName" align="center" />
    </el-table>

    <pagination
        v-show="total > 0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />
  </div>
</template>

<script setup name="Recommendation">
import { listRecommendation } from "@/api/books/recommendation";
import { ref, reactive, onMounted } from "vue";
import { useRouter } from "vue-router"; // 导入路由模块

const router = useRouter(); // 获取路由实例

const recommendationList = ref([]);
const loading = ref(true);
const total = ref(0);

const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
});

/** 查询个性化推荐列表 */
function getList() {
  loading.value = true;
  listRecommendation(queryParams)
      .then((response) => {
        recommendationList.value = response.rows || [];
        total.value = response.total || 0;
        loading.value = false;
      })
      .catch(() => {
        loading.value = false;
      });
}

/** 行点击事件 */
const handleRowClick = (row) => {
  router.push(`/detail/${row.bookIndex}`); // 跳转到详情页
};

onMounted(() => {
  getList();
});
</script>

<style scoped>
/* 行悬停效果 */
.clickable-table ::v-deep .el-table__body tr:hover > td {
  background: #f5f7fa;
  cursor: pointer;
}
</style>