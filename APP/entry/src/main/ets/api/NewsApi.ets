import CommonConstants from '../common/CommonConstants';
import { httpUtil } from '../common/request/HttpUtil';
import { RequestOptions } from '../common/request/RequestOptions';
import Logger from '../common/utils/Logger';
import { NewsItem } from '../entity/NewsItem';


export class NewsApi {

  /**
   * 查询所有数据-聚合数据
   */
  async getNewsApi(item: NewsItem) : Promise<any> {
    Logger.debug('getAllDataList >>>>>>>>>>>>>>>>>>>>item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appNews/getNewsApi",
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
   * 查询所有数据
   */
  async getAllDataList(item: NewsItem) : Promise<any> {
    Logger.debug('getAllDataList >>>>>>>>>>>>>>>>>>>>item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appNews/getNewsList",
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
   * 查询详情数据
   */
  async getById(id: number) : Promise<any> {
    Logger.debug('getAllDataList >>>>>>>>>>>>>>>>>>>>id=' + id)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appNews/getNewsById/" + id,
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
   * 查询详情数据-聚合数据
   */
  async getApiId(id: string) : Promise<any> {
    Logger.debug('getAllDataList >>>>>>>>>>>>>>>>>>>>id=' + id)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appNews/getApiId/" + id,
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
export const newsApi = new NewsApi();


