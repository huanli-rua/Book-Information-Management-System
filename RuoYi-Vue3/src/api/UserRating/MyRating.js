import request from '@/utils/request'

// 查询我的评分列表
export function listMyRating(query) {
  return request({
    url: '/UserRating/MyRating/list',
    method: 'get',
    params: query
  })
}



// 新增我的评分
export function addMyRating(data) {
  return request({
    url: '/UserRating/MyRating',
    method: 'post',
    data: data
  })
}


// 删除我的评分
export function delMyRating(bookIndex) {  // 添加 export 关键字
  return request({
    url: '/UserRating/MyRating/' + bookIndex,
    method: 'delete'
  });
}
