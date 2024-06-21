import request from '@/utils/request'

/**
* 新增
*/
export function addAppLinkman(params) {
  return request({
    url: '/appLinkman/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteAppLinkman(param) {
  return request({
    url: '/appLinkman/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateAppLinkman(params) {
  return request({
    url: '/appLinkman/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getAppLinkmanTableData(params) {
  return request({
    url: '/appLinkman/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getAppLinkmanList() {
  return request({
    url: '/appLinkman/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/appLinkman/getDataById/' + params,
    method: 'get',
  })
}

