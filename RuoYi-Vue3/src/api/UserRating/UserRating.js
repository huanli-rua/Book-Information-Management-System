import request from '@/utils/request'

// 查询评分列表
export function listUserRating(query) {
  return request({
    url: '/UserRating/UserRating/list',
    method: 'get',
    params: query
  })
}

// 查询评分详细
export function getUserRating(userId) {
  return request({
    url: '/UserRating/UserRating/' + userId,
    method: 'get'
  })
}

// 新增评分
export function addUserRating(data) {
  return request({
    url: '/UserRating/UserRating',
    method: 'post',
    data: data
  })
}

// 修改评分
export function updateUserRating(data) {
  return request({
    url: '/UserRating/UserRating',
    method: 'put',
    data: data
  })
}

// 删除评分
export function delUserRating(userId) {
  return request({
    url: '/UserRating/UserRating/' + userId,
    method: 'delete'
  })
}
