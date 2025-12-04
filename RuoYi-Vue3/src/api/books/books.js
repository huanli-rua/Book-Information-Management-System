import request from '@/utils/request'

// 查询书籍信息列表
export function listBooks(query) {
  return request({
    url: '/books/books/list',
    method: 'get',
    params: query
  })
}

// 查询书籍信息详细
export function getBooks(bookIndex) {
  return request({
    url: '/books/books/' + bookIndex,
    method: 'get'
  })
}

// 新增书籍信息
export function addBooks(data) {
  return request({
    url: '/books/books',
    method: 'post',
    data: data
  })
}

// 修改书籍信息
export function updateBooks(data) {
  return request({
    url: '/books/books',
    method: 'put',
    data: data
  })
}

// 删除书籍信息
export function delBooks(bookIndex) {
  return request({
    url: '/books/books/' + bookIndex,
    method: 'delete'
  })
}

export function getBookDetail(bookIndex) {
  return request({
    url: `/books/books/detail/${bookIndex}`,
    method: 'get'
  })
}

export function submitRating(data) {
  return request({
    url: '/books/books/submitRating',
    method: 'post',
    headers: {
      'Content-Type': 'application/json' // 明确设置请求头
    },
    data: data
  })
}

export function getAllComments(bookIndex) {
  return request({
    url: `/books/books/allComments/${bookIndex}`,
    method: 'get'
  })
}