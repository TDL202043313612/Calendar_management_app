import request from '@/utils/request'

/**
* 列表数据
*/
export function getTableData(params) {
  return request({
    url: '/easyJob/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 批量删除
*/
export function deleteObject(ids) {
  return request({
    url: '/easyJob/batchDelete',
    method: 'post',
    data: ids,
  })
};


/**
* 新增数据
*/
export function addObject(params) {
  return request({
    url: '/easyJob/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateObject(params) {
  return request({
    url: '/easyJob/update',
    method: 'post',
    data: params
  })
};
