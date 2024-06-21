import request from '@/utils/request'

/**
* 新增
*/
export function addAppNews(params) {
  return request({
    url: '/appNews/save',
    method: 'post',
    data: params
  })
}

/**
* 删除
*/
export function deleteAppNews(param) {
  return request({
    url: '/appNews/batchDelete',
    method: 'post',
    data: param,
  })
}

/**
* 修改
*/
export function updateAppNews(params) {
  return request({
    url: '/appNews/update',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（分页）
*/
export function getAppNewsTableData(params) {
  return request({
    url: '/appNews/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getAppNewsList() {
  return request({
    url: '/appNews/list',
    method: 'get',
  })
}

/**
* 根据主键ID获取详情
*/
export function getDataById(params) {
  return request({
    url: '/appNews/getDataById/' + params,
    method: 'get',
  })
}

