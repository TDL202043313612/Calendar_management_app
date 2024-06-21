/**
 * Desc: 新增账目
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

@Entry
@Component
struct AddTask {
  @State message: string = 'Hello World'
  @State topTitle: string = router.getParams()?.['topTitle']
  @State moneyItem: MoneyItem = new MoneyItem();

  onPageShow() {
    this.moneyItem = new MoneyItem();
  }

  build() {
    Column() {
      Header({ title: this.topTitle, showBack: true })

      Column({ space: 8 }) {

        Row() {
          MyInput({
            title: '账目描述',
            defaultValue: this.moneyItem.moneyDesc,
            change: (val) => {
              this.moneyItem.moneyDesc = val
            }
          })
        }

        Row() {
          MyInput({
            title: '账目金额',
            defaultValue: this.moneyItem.moneyNum,
            change: (val) => {
              this.moneyItem.moneyNum = val
            }
          })
        }

        Row() {
          MySelect({
            title: '账目类型',
            defaultValue: '请选择',
            selectData: [
              { value: '支出' },
              { value: '收入' },
            ],
            onSelectHandler: (index, value) => {
              this.moneyItem.moneyStatus = value
            }
          })
        }

        //按钮
        Row() {

          MyButton({
            title: '保存',
            onClickHandler: async () => {
              //发请求保存数据到数据库
              await moneyApi.addObj(this.moneyItem)
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