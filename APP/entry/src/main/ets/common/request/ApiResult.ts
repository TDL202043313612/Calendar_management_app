/*
 * Desc: 统一接口请求结果
 */
export class ApiResult {
  code : string //响应码
  message ?: string //响应信息
  data ?: any //响应数据
}