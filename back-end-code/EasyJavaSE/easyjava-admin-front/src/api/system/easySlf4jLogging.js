import request from '@/utils/request'

/**
* 查询数据（分页）
*/
export function getEasySlf4jLoggingTableData(params) {
  return request({
    url: '/easySlf4jLogging/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getEasySlf4jLoggingList() {
  return request({
    url: '/easySlf4jLogging/list',
    method: 'get',
  })
}

/**
* 批量删除
*/
export function deleteEasySlf4jLogging(param) {
  return request({
    url: '/easySlf4jLogging/batchDelete',
    method: 'post',
    data: param,
  })
};


/**
* 新增数据
*/
export function addEasySlf4jLogging(params) {
  return request({
    url: '/easySlf4jLogging/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateEasySlf4jLogging(params) {
  return request({
    url: '/easySlf4jLogging/update',
    method: 'post',
    data: params
  })
};