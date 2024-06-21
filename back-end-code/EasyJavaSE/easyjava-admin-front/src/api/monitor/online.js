import request from '@/utils/request'

// 查询在线用户列表
export function getOnlineUserIdList(params) {
  return request({
    url: '/socket/getOnlineUserIdList',
    method: 'post',
    data: params
  })
}

// 群发系统实时消息
export function pushMessageToAllUser(params) {
  return request({
    url: '/socket/pushMessageToAllUser',
    method: 'post',
    data: params
  })
}

// 强退用户
export function forceLogout(userId) {
  return request({
    url: '/socket/forceLogout/' + userId,
    method: 'get'
  })
}
