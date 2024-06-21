import request from '@/utils/request'

/**
 * 获取部门树形结构数据（用于选择上级）
 */
export function getDeptTreeData(param) {
  return request({
    url: '/easyDept/getDeptTreeData',
    method: 'post',
    data: param
  })
};

/**
 * 获取部门和人员的树形数据
 */
export function getDeptUserTree(param) {
    return request({
        url: '/easyUser/getDeptUserTree',
        method: 'get',
        data: param
    })
};

/**
 * 处理文件下载
 * 张总专用
 */
export function download(param) {
    return request({
        url: '/file/download',
        method: 'post',
        data: param
    })
};

/**
 * 上传单个头像文件接口
 */
export function uploadSingleImage(param) {
    return request({
        url: '/file/uploadSingleImage',
        method: 'post',
        data: param
    })
};

/**
 * 删除OSS中的某个文件-接口
 */
export function deleteFile(param) {
    return request({
        url: '/file/deleteFile',
        method: 'post',
        data: param
    })
};
