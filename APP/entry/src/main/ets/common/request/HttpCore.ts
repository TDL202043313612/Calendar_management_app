import http from '@ohos.net.http';
import { RequestOptions } from './RequestOptions';
import { ApiResult } from './ApiResult';
import Logger from '../utils/Logger';
import { tokenUtils } from '../utils/TokenUtils';
import { myTools } from '../utils/MyTools';
/**
 * Http请求器
 */
export class HttpCore {

  /**
   * 发送请求
   * @param requestOption
   * @returns Promise
   */
  request(requestOptions: RequestOptions): Promise<ApiResult> {
    let p = new Promise<ApiResult>(async (resolve, reject) => {
      Logger.debug("request url=" + requestOptions.url)
      // 每一个httpRequest对应一个HTTP请求任务，不可复用
      let httpRequest = http.createHttp();
      let token : string = await getToken();
      let promise = httpRequest.request(
        requestOptions.url, //请求地址
        {
        method: requestOptions.method, //请求类型
        connectTimeout: 5000, //连接超时时间(单位：毫秒)
        readTimeout: 10000, //读取超时间(单位：毫秒)
        header:{
          'Content-Type': 'application/json',
          'token': token,
          'client_type': 'HarmonyOS'
        },
        extraData: requestOptions.extraData
      });
      promise.then((data) => {
        let resultObj = JSON.parse(data.result.toString());
        Logger.debug('后台响应结果=' + JSON.stringify(resultObj))
        //先校验http请求的响应码，不等于200的都是有问题的
        if(data.responseCode == 200){
          //响应码为200
          //校验后台返回数据的状态码，如果不是0000，这里就拦截，提示错误信息，如果是0000，则返回页面做处理
          if(resultObj.code != '0000'){
            myTools.alertMsg(resultObj.message)
          }else{
            if(resultObj.showMessage){
              myTools.alertMsg(resultObj.message)
            }
            resolve(resultObj);
          }
        }
        else {
          Logger.debug('后台响应状态码非200=' + JSON.stringify(resultObj))
          myTools.alertMsg('请求异常，请稍后再试！')
        }
      }).catch((err) => {
        //这里做全局异常统一处理  根据Http状态码做出处理
        Logger.error('http request error:' + JSON.stringify(err))
        myTools.alertMsg('网络异常：' + JSON.stringify(err))
        reject(err);
      });
      httpRequest.destroy();//销毁HttpRequest对象
    })
    return p;
  }
}

/**
 * 获取 Token 值
 * @returns
 */
async function getToken(): Promise<string> {
  return new Promise<string>(async (resolve, reject) => {
    try {
      const token = tokenUtils.getToken() + ""
      if (typeof token === 'string') {
        resolve(token);
      } else {
        myTools.alertMsg('后台异常，请稍后再试！')
        Logger.error("Invalid Token")
        reject(new Error('Invalid Token'));
      }
    } catch (err) {
      reject(err);
    }
  });

}

export const httpCore = new HttpCore();