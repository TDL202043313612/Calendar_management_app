import { Header } from '../common/Header'
import router from '@ohos.router'
import { taskApi } from '../api/TaskApi'
import Logger from '../common/utils/Logger'
import { TaskItem } from '../entity/TaskItem'
import { moneyApi } from '../api/MoneyApi'
import { MoneyItem } from '../entity/MoneyItem'
import { MemoItem } from '../entity/MemoItem'
import { memoApi } from '../api/MemoApi'

/**
 * Desc: 备忘录页面
 */
@Entry
@Component
struct MemoPage {
  @State message: string = 'Hello World'
  @State memoItem: MemoItem = new MemoItem();
  @State memoList: Array<MemoItem> = []

  onPageShow() {
    this.loadDataInfo();
  }

  async loadDataInfo() {
    await memoApi.getAllDataList().then(res => {
      this.memoList = res
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
        Header({ title: '备忘录管理', showBack: true })

        Image($r('app.media.add'))
          .height(30)
          .offset({ x: '42%' })
          .onClick(() => {
            router.pushUrl({
              url: 'pages/addMemo',
              params: {
                topTitle: '新增备忘录',
                memoId: '-1',
              },
            }, router.RouterMode.Single)
          })

      }

      List() {
        ForEach(this.memoList, (item: MemoItem, index) => {
          ListItem() {
            Column() {
              //标题+时间
              Text(item.memoTitle)
                .fontColor('#111111')
                .fontSize(24)
                .maxLines(1) //最多显示一行
                .textOverflow({ overflow: TextOverflow.Ellipsis }) //超过一行就显示省略号
              Text(item.createTime)
                .fontColor('#ff858383')
                .fontSize(16)
                .margin({ top: 9 })
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
                        await memoApi.deleteObj(item.id)
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
              {
                value: '详情',
                action: () => {
                  router.pushUrl({
                    url: 'pages/addMemo',
                    params: {
                      memoId: item.id,
                      topTitle: '备忘录详情',
                    },
                  }, router.RouterMode.Single)
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