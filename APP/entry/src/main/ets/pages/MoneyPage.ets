import { Header } from '../common/Header'
import router from '@ohos.router'
import { taskApi } from '../api/TaskApi'
import Logger from '../common/utils/Logger'
import { TaskItem } from '../entity/TaskItem'
import { moneyApi } from '../api/MoneyApi'
import { MoneyItem } from '../entity/MoneyItem'


@Entry
@Component
struct MoneyPage {
  @State message: string = 'Hello World'
  @State moneyItem: MoneyItem = new MoneyItem();
  @State moneyList: Array<MoneyItem> = []
  @State income:number = 10;
  @State outcome:number = 20;


  onPageShow() {
    this.loadDataInfo();
  }

  async loadDataInfo() {
    await moneyApi.getAllDataList().then(res => {
      this.moneyList = res
      // ForEach(this.moneyList, (item: MoneyItem, index) => {
      //   if ('收入' == item.moneyStatus) {
      //
      //   }else {
      //
      //   }
      // })

    })
  }

  @Builder
  MyText(docType: string, colorStr: string) {
    Text(docType)
      .fontSize(12)
      .margin({ left: 10, top: 9 })
      .backgroundColor(colorStr)
      .fontColor('#ffffffff')
      .border({
        width: { left: '8px', right: '8px', top: '8px', bottom: '8px' },
        color: { left: colorStr, right: colorStr, top: colorStr, bottom: colorStr },
      })
  }

  build() {
    Column() {

      Stack() {
        Header({ title: '财务管理', showBack: true })

        Image($r('app.media.add'))
          .height(30)
          .offset({ x: '42%' })
          .onClick(() => {
            router.pushUrl({
              url: 'pages/addMoney',
              params: {
                topTitle: '新增账目',
              },
            }, router.RouterMode.Single)
          })

        Image($r('app.media.icon_chart'))
          .height(30)
          .offset({ x: '32%' })
          .onClick(() => {
            router.pushUrl({
              url: 'pages/MoneyChart',
              // params: {
              //   topTitle: '新增账目',
              // },

              params: {
                // topTitle : '财务可视化',
                // income : this.income,
                // outcome : this.outcome
                moneyList : this.moneyList
              },
            }, router.RouterMode.Single)
          })
      }

      List() {
        ForEach(this.moneyList, (item: MoneyItem, index) => {
          ListItem() {
            Column() {
              //标题+时间
              Text(item.moneyDesc)
                .fontColor('#111111')
                .fontSize(24)
                .maxLines(1) //最多显示一行
                .textOverflow({ overflow: TextOverflow.Ellipsis }) //超过一行就显示省略号
              Row() {
                Text(item.createTime)
                  .fontColor('#ff858383')
                  .fontSize(16)
                  .margin({ top: 9 })
                if ('收入' == item.moneyStatus) {
                  this.MyText(item.moneyStatus + ',金额：' + item.moneyNum, '#5e85f4')
                }
                else if ('支出' == item.moneyStatus) {
                  this.MyText(item.moneyStatus + ',金额：' + item.moneyNum, '#ff6700')
                }
              }
              .justifyContent(FlexAlign.Start)
            }
            .margin({ top: 8 })
            .padding({ top: 10, left: 2 })
            .justifyContent(FlexAlign.Start)
            .alignItems(HorizontalAlign.Start)
            .borderRadius(10)
            .border({ width: 2, color: '#ffd2cfd2' })
            // .backgroundColor('#fff3f09d')
            .height(90)
            .width('97%')
            .bindMenu([
              {
                value: '删除',
                action: () => {
                  AlertDialog.show({
                    title: "请确认：", //弹窗标题
                    message: '确定删除该数据吗？', // 提示消息
                    autoCancel: false, // 点击遮罩层即阴影，是否关闭弹窗。true是点击遮罩层弹窗关闭。
                    alignment: DialogAlignment.Center, //弹窗位置
                    offset: { dx: 0, dy: 0 }, // 相对弹窗位置即DialogAlignment.Bottom 在x轴和y轴偏移
                    primaryButton: {
                      value: "确定",
                      fontColor: Color.Red,
                      action: async () => {
                        await moneyApi.deleteObj(item.id)
                        this.loadDataInfo();
                      }
                    },
                    secondaryButton: {
                      value: "取消",
                      action: () => {
                        Logger.debug('点击了取消按钮')
                      }
                    }
                  })
                }
              },
            ])
          }
        })
      }
      .alignListItem(ListItemAlign.Center)
      // .backgroundColor('#111111')
      .height('94%')

    }
    .width('100%')
    .height('100%')
  }
}