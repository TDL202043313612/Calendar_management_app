/**
 * Desc: 任务实体类
 */
export class TaskItem {
  id: number;
  createTime: string;
  taskTitle: string;
  taskContent: string;
  taskStatus: string; //任务状态(待办;已办)
}