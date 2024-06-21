import request from '@/utils/request'

/**
* 新增
*/
export function addAppMemo(params) {
  return request({
    url: '/appMemo/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteAppMemo(param) {
  return request({
    url: '/appMemo/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateAppMemo(params) {
  return request({
    url: '/appMemo/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getAppMemoTableData(params) {
  return request({
    url: '/appMemo/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getAppMemoList() {
  return request({
    url: '/appMemo/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/appMemo/getDataById/' + params,
    method: 'get',
  })
}

