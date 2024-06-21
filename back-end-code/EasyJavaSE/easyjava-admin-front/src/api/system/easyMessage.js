import request from '@/utils/request'

/**
* 列表数据
*/
export function getTableData(params) {
  return request({
    url: '/easyMessage/pagelist',
    method: 'post',
    data: params
  })
}

/**
* 批量删除
*/
export function deleteObject(ids) {
  return request({
    url: '/easyMessage/batchDelete',
    method: 'post',
    data: ids,
  })
};


/**
* 新增数据
*/
export function addObject(params) {
  return request({
    url: '/easyMessage/save',
    method: 'post',
    data: params
  })
};

/**
* 修改数据
*/
export function updateObject(params) {
  return request({
    url: '/easyMessage/update',
    method: 'post',
    data: params
  })
};

/**
 * 获取系统消息数量（包含消息+通知+公告的总数量）
 */
export function getMsgCount() {
  return request({
    url: '/easyMessage/getMsgCount',
    method: 'get',
  })
};

/**
 * 获取所有消息集合信息
 */
export function getAllMessage() {
  return request({
    url: '/easyMessage/getAllMessage',
    method: 'get',
  })
};

/**
 * 修改系统消息状态
 */
export function updateMessageStatus(param) {
  return request({
    url: '/easyMessage/updateMessageStatus/' + param,
    method: 'get',
  })
};
