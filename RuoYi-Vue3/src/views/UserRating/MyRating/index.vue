<template>
  <div class="app-container">


    <el-table v-loading="loading" :data="MyRatingList" @selection-change="handleSelectionChange">
<!--      <el-table-column type="selection" width="55" align="center" />-->
      <el-table-column label="书籍索引" align="center" prop="bookIndex" v-if="false" /> <!-- 隐藏字段 -->
      <el-table-column label="书名" align="center" prop="bookName" />
      <el-table-column label="作者" align="center" prop="author" />
      <el-table-column label="本书评分" align="center" prop="rating" />
      <el-table-column label="我的评分" align="center" prop="score" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">

          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['UserRating:MyRating:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        v-model:page="queryParams.pageNum"
        v-model:limit="queryParams.pageSize"
        @pagination="getList"
    />


  </div>
</template>

<script setup name="MyRating">
import { listMyRating, delMyRating } from "@/api/UserRating/MyRating";

const { proxy } = getCurrentInstance();

const MyRatingList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
  },
  rules: {
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询我的评分列表 */
function getList() {
  loading.value = true;
  listMyRating(queryParams.value).then(response => {
    MyRatingList.value = response.rows;
    total.value = response.total;
    loading.value = false;
  });
}



// 修改handleDelete方法
function handleDelete(row) {
  const _bookIndex = row.bookIndex; // 使用bookIndex作为唯一标识
  proxy.$modal.confirm('是否确认删除书籍索引为"' + _bookIndex + '"的评分？').then(() => {
    return delMyRating(_bookIndex); // 传递bookIndex到接口
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {});
}

// 修改多选处理逻辑
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.bookIndex); // 收集bookIndex
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}



getList();
</script>
