import { RequestMethod, RequestOptions } from './RequestOptions';
import { httpCore } from './HttpCore';
import { ApiResult } from './ApiResult';
/*
 * Desc: 对外请求工具包
 */
export class HttpUtil {
  private static mInstance: HttpUtil;

  // 防止实例化
  private constructor() {
  }

  static getInstance(): HttpUtil {
    if (!HttpUtil.mInstance) {
      HttpUtil.mInstance = new HttpUtil();
    }
    return HttpUtil.mInstance;
  }

  request (option: RequestOptions): Promise<ApiResult> {
    return httpCore.request(option);
  }

  /**
   * 封装Post网络请求
   * @param option
   * @returns
   */
  Post(option: RequestOptions){
    if(option != null){
      option.method = RequestMethod.POST
    }
    return this.request(option);
  }

  /**
   * 封装Get网络请求
   * @param option
   * @returns
   */
  Get(option: RequestOptions){
    if(option != null){
      option.method = RequestMethod.GET
    }
    return this.request(option);
  }

}

export const httpUtil = HttpUtil.getInstance();