/**
 * Desc: 新增联系人
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
import { LinkmanItem } from '../entity/LinkmanItem'
import { linkmanApi } from '../api/LinkmanApi'

@Entry
@Component
struct addLinkman {
  @State message: string = 'Hello World'
  @State topTitle: string = router.getParams()?.['topTitle']
  @State linkmanItem: LinkmanItem = new LinkmanItem();

  onPageShow() {
    this.linkmanItem = new LinkmanItem();
  }

  build() {
    Column() {
      Header({ title: this.topTitle, showBack: true })

      Column({ space: 8 }) {

        Row() {
          MyInput({
            title: '联系人姓名',
            defaultValue: this.linkmanItem.manName,
            change: (val) => {
              this.linkmanItem.manName = val
            }
          })
        }

        Row() {
          MyInput({
            title: '联系人手机',
            defaultValue: this.linkmanItem.manPhone,
            change: (val) => {
              this.linkmanItem.manPhone = val
            }
          })
        }

        Row() {
          MySelect({
            title: '是否置顶',
            defaultValue: '请选择',
            selectData: [
              { value: '置顶' },
              { value: '不置顶' },
            ],
            onSelectHandler: (index, value) => {
              this.linkmanItem.manTop = value
            }
          })
        }

        //按钮
        Row() {

          MyButton({
            title: '保存',
            onClickHandler: async () => {
              //发请求保存数据到数据库
              await linkmanApi.addObj(this.linkmanItem)
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