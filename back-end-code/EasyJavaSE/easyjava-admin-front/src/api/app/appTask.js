import request from '@/utils/request'

/**
* 新增
*/
export function addAppTask(params) {
  return request({
    url: '/appTask/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteAppTask(param) {
  return request({
    url: '/appTask/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateAppTask(params) {
  return request({
    url: '/appTask/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getAppTaskTableData(params) {
  return request({
    url: '/appTask/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getAppTaskList() {
  return request({
    url: '/appTask/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/appTask/getDataById/' + params,
    method: 'get',
  })
}

