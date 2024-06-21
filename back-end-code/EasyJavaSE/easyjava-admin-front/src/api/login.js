import request from '@/utils/request'

// 刷新权限
export function refreshPermission(data) {
  return request({
    url: '/login/refreshPermission',
    headers: {
      isToken: true
    },
    method: 'post',
    data: data
  })
}

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/login/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/easyUser/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/login/loginOut',
    method: 'get'
  })
}

// 获取验证码
export function getCodeImg(param) {
  return request({
    url: '/captcha/getImageEasyCode/' + param,
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}
