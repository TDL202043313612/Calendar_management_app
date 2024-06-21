
import Logger from './Logger';
/**
 * Desc: 存取 AppStorage 的工具类
 */
export class AppStorageUtils {

  /**
   * 保存数据到 AppStorage 中
   * @param data
   */
  setData(key: string, data: any) {
    Logger.debug(`保存数据到 AppStorage:[${key} = ${data}]`)
    AppStorage.SetOrCreate(key, data)
  }

  /**
   * 从 AppStorage 中获取data
   */
  getData(key: string): string {
    let data = AppStorage.Get(key);
    Logger.debug(`从AppStorage中获取数据:[${key} = ${data}]`)
    return String(data)
  }

  /**
   * 清空 AppStorage 中所有数据
   */
  clearAppStorage() {
    Logger.debug('清空 AppStorage 所有数据')
    AppStorage.Clear()
  }

}

export const appStorageUtils = new AppStorageUtils();