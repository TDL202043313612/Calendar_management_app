import request from '@/utils/request'

/**
* 查询数据（分页）
*/
export function getEasySystemMsgTableData(params) {
  return request({
    url: '/easySystemMsg/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 查询数据（不分页）
*/
export function getEasySystemMsgList() {
  return request({
    url: '/easySystemMsg/list',
    method: 'get',
  })
}

/**
* 批量删除
*/
export function deleteEasySystemMsg(ids) {
  return request({
    url: '/easySystemMsg/batchDelete',
    method: 'post',
    data: ids,
  })
};


/**
* 新增数据
*/
export function addEasySystemMsg(params) {
  return request({
    url: '/easySystemMsg/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateEasySystemMsg(params) {
  return request({
    url: '/easySystemMsg/update',
    method: 'post',
    data: params
  })
};