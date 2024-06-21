import request from '@/utils/request'

// 查询菜单列表
export function listMenu(query) {
  return request({
    url: '/easyMenu/list',
    method: 'post',
    data: query
  })
}

// 查询菜单详细
export function getMenu(menuId) {
  return request({
    url: '/easyMenu/' + menuId,
    method: 'get'
  })
}

// 查询菜单下拉树结构
export function treeMenuSelect() {
  return request({
    url: '/easyMenu/treeselect',
    method: 'get'
  })
}

// 根据角色ID查询菜单下拉树结构
export function roleMenuTreeselect(roleId) {
  return request({
    url: '/easyMenu/roleMenuTreeselect/' + roleId,
    method: 'get'
  })
}

// 新增菜单
export function addMenu(data) {
  return request({
    url: '/easyMenu/save',
    method: 'post',
    data: data
  })
}

// 修改菜单
export function updateMenu(data) {
  return request({
    url: '/easyMenu/update',
    method: 'post',
    data: data
  })
}

// 删除菜单
export function delMenu(menuId) {
  return request({
    url: '/easyMenu/' + menuId,
    method: 'delete'
  })
}
