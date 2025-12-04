import request from '@/utils/request'

// 获取推荐书籍
export function getRecommendations(userId) {
    return request({
        url: `/recommend/${userId}`,
        method: 'get'
    })
}

// 提交评分
export function submitRating(data) {
    return request({
        url: '/recommend/rate',
        method: 'post',
        data
    })
}