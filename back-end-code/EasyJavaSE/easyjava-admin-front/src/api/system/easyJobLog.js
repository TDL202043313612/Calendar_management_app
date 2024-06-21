import request from '@/utils/request'

/**
* 列表数据
*/
export function getTableData(params) {
  return request({
    url: '/easyJobLog/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 批量删除
*/
export function deleteObject(ids) {
  return request({
    url: '/easyJobLog/batchDelete',
    method: 'post',
    data: ids,
  })
};


/**
* 新增数据
*/
export function addObject(params) {
  return request({
    url: '/easyJobLog/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateObject(params) {
  return request({
    url: '/easyJobLog/update',
    method: 'post',
    data: params
  })
};
