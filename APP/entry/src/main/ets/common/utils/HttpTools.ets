import http from '@ohos.net.http'
import CommonConstants from '../CommonConstants'
import Logger from './Logger'
import { myTools } from './myTools'
import { tokenUtils } from './TokenUtils'

/**
 * 后台服务器IP地址(如果是本地调试，请填写局域网IP地址)
 */
// const IP = CommonConstants.REQUEST_HOST
// const IP = "192.168.151.230"
const  IP = "10.21.75.132"
//后台服务器端口
const PORT = CommonConstants.REQUEST_PORT

//连接超时时间(单位：毫秒)
let connectTimeout = 5000

//读取超时间(单位：毫秒)
let readTimeout = 90000

//Content-Type
let contentType = 'application/json'

//client_type
let clientType = 'HarmonyOS'

//请求前缀路径地址
const BASE_URL = 'http://' + IP + ':' + PORT;


/**
 * 发送Get请求
 * @param url
 * @returns
 */
export function HttpGet(url: string) {
  url = BASE_URL + url
  Logger.debug('发送Get请求URL=' + url)
  let httpRequest = http.createHttp();
  var header = {
    'Content-Type': contentType,
    'client_type': clientType,
    "token": tokenUtils.getToken()
  }
  let responseResult = httpRequest.request(url, {
    method: http.RequestMethod.GET,
    connectTimeout: connectTimeout,
    readTimeout: readTimeout,
    header: header
  });
  let serverData: {
    code: string,
    data: any,
    message: string,
    showMessage: boolean,
  } = { code: '0000', data: '', message: '', showMessage: false };

  // 处理数据，并返回
  return responseResult.then((value) => {
    if (value.responseCode === 200) {
      // 获取返回数据
      let result = `${value.result}`;
      let resultJson = JSON.parse(result);
      Logger.debug('Get请求返回结果==============' + JSON.stringify(resultJson))
      serverData.data = resultJson.data;
      serverData.code = resultJson.code;
      serverData.message = resultJson.message;
      serverData.showMessage = resultJson.showMessage;
      if (serverData.showMessage) {
        myTools.alertMsg(serverData.message)
      }
      if ('0000' == serverData.code) {
        return serverData.data;
      } else {
        myTools.alertMsg(serverData.message)
        return -1;
      }
    }
  }).catch((err) => {
    Logger.error('http request error:' + JSON.stringify(err))
    myTools.alertMsg('网络异常：' + JSON.stringify(err))
  })
}


/**
 * 发送Post请求
 * @param url
 * @param data
 * @returns
 */
export function HttpPost(url: string, data: any) {
  url = BASE_URL + url
  Logger.debug('发送Post请求URL=' + url + ',请求参数data=' + JSON.stringify(data))
  let httpRequest = http.createHttp();
  var header = {
    'Content-Type': contentType,
    'client_type': clientType,
    "token": tokenUtils.getToken()
  }
  let responseResult = httpRequest.request(url, {
    method: http.RequestMethod.POST,
    connectTimeout: connectTimeout,
    readTimeout: readTimeout,
    extraData: data,
    header: header
  });
  let serverData: {
    code: string,
    data: any,
    message: string,
    showMessage: boolean,
  } = { code: '0000', data: '', message: '', showMessage: false };

  // 处理数据，并返回
  return responseResult.then((value) => {
    if (value.responseCode === 200) {
      // 获取返回数据
      let result = `${value.result}`;
      let resultJson = JSON.parse(result);
      Logger.debug('Post请求返回结果==============' + JSON.stringify(resultJson))
      serverData.data = resultJson.data;
      serverData.code = resultJson.code;
      serverData.message = resultJson.message;
      serverData.showMessage = resultJson.showMessage;
      if (serverData.showMessage) {
        myTools.alertMsg(serverData.message)
      }
      if ('0000' == serverData.code) {
        return serverData.data;
      } else {
        myTools.alertMsg(serverData.message)
        return -1;
      }
    }
  }).catch((err) => {
    Logger.error('http request error:' + JSON.stringify(err))
    myTools.alertMsg('网络异常：' + JSON.stringify(err))
  })
}