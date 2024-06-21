import CommonConstants from '../common/CommonConstants';
import { httpUtil } from '../common/request/HttpUtil';
import { RequestOptions } from '../common/request/RequestOptions';
import Logger from '../common/utils/Logger';
import { TaskItem } from '../entity/TaskItem';


export class TaskApi {

  /**
   * 修改任务状态(待办/已办)
   */
  async updateTaskStatus(taskId: number) : Promise<any> {
    Logger.debug('修改任务状态(待办/已办) >>>>>>>>>>>>>>>>>>>> taskId=' + taskId)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/updateTaskStatus/" + taskId,
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
   * 保存
   */
  async addTask(item: TaskItem) : Promise<any> {
    Logger.debug('保存TaskItem >>>>>>>>>>>>>>>>>>>> item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/addTask",
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
   * 编辑
   */
  async updateTask(item: TaskItem) : Promise<any> {
    Logger.debug('编辑TaskItem >>>>>>>>>>>>>>>>>>>> item=' + JSON.stringify(item))
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/updateTask",
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
   * 根据ID查询任务详情
   */
  async getTaskById(taskId: number) : Promise<any> {
    Logger.debug('根据ID查询任务详情 >>>>>>>>>>>>>>>>>>>> taskId=' + taskId)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/getTaskById/" + taskId,
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
   * 删除日常任务
   */
  async deleteTask(taskId: number) : Promise<any> {
    Logger.debug('删除日常任务 >>>>>>>>>>>>>>>>>>>> taskId=' + taskId)
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/deleteTask/" + taskId,
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
   * 查询所有日常任务数据
   */
  async getAllTaskList() : Promise<any> {
    Logger.debug('查询所有日常任务数据 >>>>>>>>>>>>>>>>>>>>')
    let options: RequestOptions = {
      url: CommonConstants.REQUEST_PREFIX + "/appTask/getAllTaskList",
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
export const taskApi = new TaskApi();


