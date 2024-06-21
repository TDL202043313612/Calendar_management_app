/**
 * Desc: 新增备忘录
 */
import { Header } from '../common/Header'
import router from '@ohos.router'
import MyInput from '../component/MyInput'
import { TaskItem } from '../entity/TaskItem'
import MyTextArea from '../component/MyTextArea'
import MySelect from '../component/MySelect'
import MyButton from '../component/MyButton'
import { taskApi } from '../api/TaskApi'
import { MoneyItem } from '../entity/MoneyItem'
import { moneyApi } from '../api/MoneyApi'
import { MemoItem } from '../entity/MemoItem'
import { memoApi } from '../api/MemoApi'

@Entry
@Component
struct addMemo {
  @State message: string = 'Hello World'
  @State topTitle: string = router.getParams()?.['topTitle']
  @State memoId: number = router.getParams()?.['memoId']
  @State memoItem: MemoItem = new MemoItem();

  async onPageShow() {
    this.memoItem = new MemoItem();
    if(this.memoId != -1){
      //查询详情
      await memoApi.getDataById(this.memoId).then(res => {
        this.memoItem = res
      })
    }
  }

  build() {
    Column() {
      //头部
      Header({ title: this.topTitle, showBack: true })

      Column({ space: 8 }) {

        Row() {
          MyInput({
            title: '备忘录标题',
            defaultValue: this.memoItem.memoTitle,
            change: (val) => {
              this.memoItem.memoTitle = val
            }
          })
        }

        Row() {
          MyTextArea({
            title: '备忘录内容',
            defaultValue: this.memoItem.memoContent,
            change: (val) => {
              this.memoItem.memoContent = val
            }
          })
        }

        //按钮
        Row() {

          MyButton({
            title: '保存',
            onClickHandler: async () => {
              //发请求保存数据到数据库
              await memoApi.addObj(this.memoItem)
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