/**
 * Desc: 新增任务
 */
import { Header } from '../common/Header'
import router from '@ohos.router'
import MyInput from '../component/MyInput'
import { TaskItem } from '../entity/TaskItem'
import MyTextArea from '../component/MyTextArea'
import MySelect from '../component/MySelect'
import MyButton from '../component/MyButton'
import { taskApi } from '../api/TaskApi'

@Entry
@Component
struct AddTask {
  @State message: string = 'Hello World'
  @State taskId: number = router.getParams()?.['taskId']
  @State topTitle: string = router.getParams()?.['topTitle']
  @State taskItem: TaskItem = new TaskItem();
  @State buttonStatus: boolean = true //按钮是否可点击(true:可点击;false:不可点击)

  onPageShow() {
    if(this.topTitle == '任务详情'){
      this.buttonStatus = false
    }
    this.loadTaskInfo();
  }

  async loadTaskInfo() {
    this.taskItem = new TaskItem();
    if (this.taskId) {
      await taskApi.getTaskById(this.taskId).then(res => {
        this.taskItem = res;
      })
    }
  }

  build() {
    Column() {
      Header({ title: this.topTitle, showBack: true })

      Column({ space: 8 }) {

        Row() {
          MyInput({
            title: '任务标题',
            defaultValue: this.taskItem.taskTitle,
            change: (val) => {
              this.taskItem.taskTitle = val
            }
          })
        }

        Row() {
          MyTextArea({
            title: '文档内容',
            defaultValue: this.taskItem.taskContent,
            change: (val) => {
              this.taskItem.taskContent = val
            }
          })
        }

        Row() {
          MySelect({
            title: '任务状态',
            defaultValue: this.taskItem.taskStatus ? this.taskItem.taskStatus : '请选择',
            selectData: [
              { value: '待办' },
              { value: '已办' },
            ],
            onSelectHandler: (index, value) => {
              this.taskItem.taskStatus = value
            }
          })
        }

        //按钮
        Row() {

          MyButton({
            title: '保存',
            enabledStatus: this.buttonStatus,
            onClickHandler: async () => {
              //发请求保存数据到数据库
              await taskApi.addTask(this.taskItem)
            }
          })
        }

      }
      // .backgroundColor('#123123')
      .width('100%')
      .height('94%')

    }
    .width('100%')
    .height('100%')
  }
}