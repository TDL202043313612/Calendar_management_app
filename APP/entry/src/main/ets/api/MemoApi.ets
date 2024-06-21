import CommonConstants from '../common/CommonConstants';
import { httpUtil } from '../common/request/HttpUtil';
import { RequestOptions } from '../common/request/RequestOptions';
import Logger from '../common/utils/Logger';
import { MemoItem } from '../entity/MemoItem';
import { MoneyItem } from '../entity/MoneyItem';


export class MemoApi {

  /**
   * 保存
   */
  async addObj(item: MemoItem) : Promise<any> {
    Logger.debug('addObj >>>>>>>>>>>>>>>>>>>> item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appMemo/addMemo",
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
      url: CommonConstants.REQUEST_PREFIX + "/appMemo/deleteMemoById/" + id,
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
      url: CommonConstants.REQUEST_PREFIX + "/appMemo/getAllMemoList",
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
   * 查询详情
   */
  async getDataById(itemId: number) : Promise<any> {
    Logger.debug('getDataById >>>>>>>>>>>>>>>>>>>>')
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appMemo/getMemoById/" + itemId,
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
export const memoApi = new MemoApi();


