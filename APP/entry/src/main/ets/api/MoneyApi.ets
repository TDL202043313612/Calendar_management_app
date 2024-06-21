import CommonConstants from '../common/CommonConstants';
import { httpUtil } from '../common/request/HttpUtil';
import { RequestOptions } from '../common/request/RequestOptions';
import Logger from '../common/utils/Logger';
import { MoneyItem } from '../entity/MoneyItem';

export class MoneyApi {

  /**
   * 保存
   */
  async addObj(item: MoneyItem) : Promise<any> {
    Logger.debug('addObj >>>>>>>>>>>>>>>>>>>> item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appMoney/addMoney",
      extraData: item
    };
    const promise = new Promise<any>((resolve, reject) => {
      httpUtil.Post(options).then((response) => {
        //返回后台对象中的data字段
        resolve(response.data);
      }).catch((error) => {
        reject(error);
      });
    });
    return promise;
  }

  /**
   * 删除
   */
  async deleteObj(id: number) : Promise<any> {
    Logger.debug('deleteObj >>>>>>>>>>>>>>>>>>>> id=' + id)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appMoney/deleteMoney/" + id,
      extraData: {}
    };
    const promise = new Promise<any>((resolve, reject) => {
      httpUtil.Get(options).then((response) => {
        //返回后台对象中的data字段
        resolve(response.data);
      }).catch((error) => {
        reject(error);
      });
    });
    return promise;
  }

  /**
   * 查询所有数据
   */
  async getAllDataList() : Promise<any> {
    Logger.debug('getAllDataList >>>>>>>>>>>>>>>>>>>>')
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appMoney/getAllMoneyData",
      extraData: {}
    };
    const promise = new Promise<any>((resolve, reject) => {
      httpUtil.Get(options).then((response) => {
        //返回后台对象中的data字段
        resolve(response.data);
      }).catch((error) => {
        reject(error);
      });
    });
    return promise;
  }

}
export const moneyApi = new MoneyApi();


