import request from '@/utils/request'

// 查询个性化推荐列表
export function listRecommendation(query) {
  return request({
    url: '/books/recommendation/list',
    method: 'get',
    params: query
  })
}

// 查询个性化推荐详细
export function getRecommendation(userId) {
  return request({
    url: '/books/recommendation/' + userId,
    method: 'get'
  })
}

// 新增个性化推荐
export function addRecommendation(data) {
  return request({
    url: '/books/recommendation',
    method: 'post',
    data: data
  })
}

// 修改个性化推荐
export function updateRecommendation(data) {
  return request({
    url: '/books/recommendation',
    method: 'put',
    data: data
  })
}

// 删除个性化推荐
export function delRecommendation(userId) {
  return request({
    url: '/books/recommendation/' + userId,
    method: 'delete'
  })
}
