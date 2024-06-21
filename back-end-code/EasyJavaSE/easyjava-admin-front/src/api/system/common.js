import request from '@/utils/request'

// 生成就业喜报图片，供前端预览
export function previewGoodsNews(query) {
  return request({
    url: '/file/previewGoodsNews',
    method: 'post',
    data: query
  })
}
