import CommonConstants from '../CommonConstants';
import Logger from './Logger';
import { appStorageUtils } from './AppStorageUtils';

/*
 * Desc: 存取 Token 的工具类
 */
export class TokenUtils {

  /**
   * 保存 token 到缓存中
   * @param token
   */
  setToken(token: string) {
    appStorageUtils.setData(CommonConstants.LOGIN_TOKEN, token)
  }

  setAdminFlag(adminFlag: number) {
    appStorageUtils.setData('adminFlag', adminFlag)
  }

  getAdminFlag(): number {
    return Number(appStorageUtils.getData('adminFlag'));
  }

  /**
   * 从缓存中获取token
   */
  getToken(): string {
    let token = appStorageUtils.getData(CommonConstants.LOGIN_TOKEN);
    token = token ? token : ''
    Logger.debug('AppStorage取出token=' + token)
    return String(token)
  }

  /**
   * 保存 userId 到缓存中
   * @param token
   */
  setUserId(userId: number) {
    appStorageUtils.setData(CommonConstants.LOGIN_USER_ID, userId)
  }

  /**
   * 从缓存中获取userId
   */
  getUserId(): number {
    let userId = appStorageUtils.getData(CommonConstants.LOGIN_USER_ID);
    Logger.debug('AppStorage取出userId=' + userId)
    return Number(userId)
  }

  /**
   * 清空缓存中所有数据
   */
  clearAppStorage() {
    Logger.debug('清空 AppStorage--------开始')
    appStorageUtils.clearAppStorage();
    Logger.debug('清空 AppStorage--------结束')
  }
}

export const tokenUtils = new TokenUtils();