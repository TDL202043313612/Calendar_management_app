import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'
const userIdKey = 'easyObjectId'

export function getUserId() {
  return localStorage.getItem(userIdKey);
}

export function setUserId(userId) {
  return localStorage.setItem(userIdKey, userId);
}

export function getToken() {
  // return Cookies.get(TokenKey)
  return localStorage.getItem(TokenKey);
}

export function setToken(token) {
  // return Cookies.set(TokenKey, token)
  return localStorage.setItem(TokenKey, token);
}

export function removeToken() {
  // return Cookies.remove(TokenKey)
  return localStorage.clear();
}
