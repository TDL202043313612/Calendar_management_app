import request from '@/utils/request'

/**
* 查询数据（分页）
*/
export function getEasyResourceLinkTableData(params) {
  return request({
    url: '/easyResourceLink/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getEasyResourceLinkList() {
  return request({
    url: '/easyResourceLink/list',
    method: 'get',
  })
}

/**
* 批量删除
*/
export function deleteEasyResourceLink(ids) {
  return request({
    url: '/easyResourceLink/batchDelete',
    method: 'post',
    data: ids,
  })
};


/**
* 新增数据
*/
export function addEasyResourceLink(params) {
  return request({
    url: '/easyResourceLink/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateEasyResourceLink(params) {
  return request({
    url: '/easyResourceLink/update',
    method: 'post',
    data: params
  })
};