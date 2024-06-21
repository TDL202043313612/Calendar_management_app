import request from '@/utils/request'

/**
* 新增
*/
export function addAppMoney(params) {
  return request({
    url: '/appMoney/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteAppMoney(param) {
  return request({
    url: '/appMoney/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateAppMoney(params) {
  return request({
    url: '/appMoney/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getAppMoneyTableData(params) {
  return request({
    url: '/appMoney/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getAppMoneyList() {
  return request({
    url: '/appMoney/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/appMoney/getDataById/' + params,
    method: 'get',
  })
}

