import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 获取额外信息
export function getExtraInfo() {
  return request({
    url: '/easyUser/getExtraInfo',
    method: 'get',
  })
}

// 获取部门用户下拉框选择树
export function getDeptUserTreeSelect() {
  return request({
    url: '/easyUser/deptUserTree',
    method: 'get',
  })
}

// 获取部门用户树数据
export function getDeptUserTree() {
  return request({
    url: '/easyUser/getDeptUserTree',
    method: 'get',
  })
}

// 查询用户列表
export function listUser(query) {
  return request({
    url: '/easyUser/pagelist',
    method: 'post',
    data: query
  })
}

// 查询用户详细
export function getUser(userId) {
  return request({
    url: '/easyUser/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增用户
export function addUser(data) {
  return request({
    url: '/easyUser/save',
    method: 'post',
    data: data
  })
}

// 修改用户
export function updateUser(data) {
  return request({
    url: '/easyUser/update',
    method: 'post',
    data: data
  })
}

// 删除用户
export function delUser(userId) {
  return request({
    url: '/easyUser/' + userId,
    method: 'delete'
  })
}

// 用户密码重置
export function resetUserPwd(id, password) {
  const data = {
    id,
    password
  }
  return request({
    url: '/easyUser/resetPwd',
    method: 'post',
    data: data
  })
}

// 用户状态修改
export function changeUserStatus(id, status) {
  const data = {
    id,
    status
  }
  return request({
    url: '/easyUser/changeStatus',
    method: 'post',
    data: data
  })
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/easyUser/getUserProfile',
    method: 'get'
  })
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/easyUser/updateUserProfile',
    method: 'post',
    data: data
  })
}

// 用户密码重置
export function updateUserPwd(data) {
  return request({
    url: '/easyUser/updateUserPwd',
    method: 'post',
    data: data
  })
}

// 用户头像上传
export function uploadAvatar(data) {
  return request({
    url: '/easyUser/updateUserAvatar',
    method: 'post',
    data: data
  })
}

// 查询授权角色
export function getAuthRole(userId) {
  return request({
    url: '/easyUser/authRole/' + userId,
    method: 'get'
  })
}

// 保存授权角色
export function updateAuthRole(data) {
  return request({
    url: '/easyUser/authRole',
    method: 'put',
    params: data
  })
}
