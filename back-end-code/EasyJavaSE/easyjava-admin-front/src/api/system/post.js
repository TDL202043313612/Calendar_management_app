import request from '@/utils/request'

// 查询岗位列表
export function listPost(query) {
  return request({
    url: '/easyPost/pagelist',
    method: 'post',
    data: query
  })
}

// 查询岗位详细
export function getPost(postId) {
  return request({
    url: '/easyPost/' + postId,
    method: 'get'
  })
}

// 新增岗位
export function addPost(data) {
  return request({
    url: '/easyPost/save',
    method: 'post',
    data: data
  })
}

// 修改岗位
export function updatePost(data) {
  return request({
    url: '/easyPost/update',
    method: 'post',
    data: data
  })
}

// 删除岗位
export function delPost(postIds) {
  return request({
    url: '/easyPost/batchDelete',
    method: 'post',
    data: postIds
  })
}
