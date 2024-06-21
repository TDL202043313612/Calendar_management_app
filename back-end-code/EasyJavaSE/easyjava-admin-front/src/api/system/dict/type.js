import request from '@/utils/request'

// 查询字典类型列表
export function listType(query) {
  return request({
    url: '/easyDictType/pagelist',
    method: 'post',
    data: query
  })
}

// 查询字典类型详细
export function getType(dictId) {
  return request({
    url: '/easyDictType/' + dictId,
    method: 'get'
  })
}

// 新增字典类型
export function addType(data) {
  return request({
    url: '/easyDictType/save',
    method: 'post',
    data: data
  })
}

// 修改字典类型
export function updateType(data) {
  return request({
    url: '/easyDictType/update',
    method: 'post',
    data: data
  })
}

// 删除字典类型
export function delType(dictIds) {
  return request({
    url: '/easyDictType/batchDelete',
    method: 'post',
    data: dictIds,
  })
}

// 刷新字典缓存
export function refreshCache() {
  return request({
    url: '/easyDictType/refreshCache',
    method: 'get'
  })
}

// 获取字典选择框列表
export function optionselect() {
  return request({
    url: '/easyDictType/optionselect',
    method: 'get'
  })
}
