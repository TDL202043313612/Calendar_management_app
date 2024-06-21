import request from '@/utils/request'

/**
* 新增
*/
export function addCommunityUser(params) {
  return request({
    url: '/communityUser/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteCommunityUser(param) {
  return request({
    url: '/communityUser/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateCommunityUser(params) {
  return request({
    url: '/communityUser/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getCommunityUserTableData(params) {
  return request({
    url: '/communityUser/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getCommunityUserList() {
  return request({
    url: '/communityUser/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/communityUser/getDataById/' + params,
    method: 'get',
  })
}

