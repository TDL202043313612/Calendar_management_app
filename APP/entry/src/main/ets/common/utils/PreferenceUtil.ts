import preferences from '@ohos.data.preferences';
import Logger from './Logger';

/**
 * Desc: Preferences 工具类
 */
class PreferenceUtil{
  private prefMap: Map<string, preferences.Preferences> = new Map()

  // 加载preferences
  async loadPreference(context, name: string){
    try {
      let pref = await preferences.getPreferences(context, name)
      this.prefMap.set(name, pref)
      Logger.info(`加载Preferences[${name}]成功！`)
    }
    catch (e){
      Logger.error(`加载Preferences[${name}]失败！`)
    }
  }

  /**
   * 存入数据到首选项
   * @param name:Preferences名称
   * @param key:字符串类型
   * @param value:值的存储数据类型包括数字型、字符型、布尔型以及这3种类型的数组类型
   */
  async putPreferenceValue(name: string, key: string, value: preferences.ValueType){
    if (!this.prefMap.has(name)) {
      Logger.error(`Preferences[${name}]尚未初始化！`)
      return
    }
    try {
      let pref = this.prefMap.get(name)
      // 写入数据
      await pref.put(key, value)
      // 刷盘
      await pref.flush()
      Logger.debug(`保存Preferences[${name}.${key} = ${value}]成功！`)
    } catch (e) {
      Logger.error(`保存Preferences[${name}.${key} = ${value}]失败！`, JSON.stringify(e))
    }
  }

  /**
   * 从首选项中获取数据
   * @param name:Preferences名称
   * @param key:字符串类型
   * @param defaultValue:默认值,值的存储数据类型包括数字型、字符型、布尔型以及这3种类型的数组类型
   * @returns
   */
  async getPreferenceValue(name: string, key: string, defaultValue: preferences.ValueType){
    if (!this.prefMap.has(name)) {
      Logger.error(`Preferences[${name}]尚未初始化！`)
      return
    }
    try {
      let pref = this.prefMap.get(name)
      // 读数据
      let value = await pref.get(key, defaultValue)
      Logger.info(`读取Preferences[${name}.${key} = ${value}]成功！`)
      return value
    } catch (e) {
      Logger.error(`读取Preferences[${name}.${key}]失败！`, JSON.stringify(e))
    }
  }

  /**
   * 从首选项中删除某个key
   * @param name:Preferences名称
   * @param key:key值,字符串类型
   */
  async deletePreferenceValue(name: string, key: string){
    if (!this.prefMap.has(name)) {
      Logger.error(`Preferences[${name}]尚未初始化！`)
      return
    }
    try {
      let pref = this.prefMap.get(name)
      // 删除数据
      let value = await pref.delete(key)
      Logger.info(`删除Preferences[${name}.${key}]成功！`)
    } catch (e) {
      Logger.error(`删除Preferences[${name}.${key}]失败！`, JSON.stringify(e))
    }
  }

  /**
   * 清除此Preferences实例中的所有存储
   * @param name:Preferences名称
   */
  clearPreferenceValue(name: string){
    if (!this.prefMap.has(name)) {
      Logger.error(`Preferences[${name}]尚未初始化！`)
      return
    }
    try {
      let pref = this.prefMap.get(name)
      // 清除数据
      pref.clear()
      Logger.info(`清除Preferences[${name}]成功！`)
    } catch (e) {
      Logger.error(`清除Preferences[${name}]失败！`, JSON.stringify(e))
    }
  }
}

const preferenceUtil = new PreferenceUtil()

export default preferenceUtil as PreferenceUtil