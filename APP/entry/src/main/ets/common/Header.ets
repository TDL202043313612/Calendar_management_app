
import router from '@ohos.router'

@Component
export struct Header {
  private title: ResourceStr //标题信息
  private showBack?: boolean = false //是否显示返回按钮
  private sureDialog?: boolean = false //是否确认弹框提示

  build() {
    Row() {
      //层叠布局
      Stack() {
        //标题行
        Row() {
          Text(this.title)
            .fontColor('#ff000000')
            .fontSize(24)
            .fontWeight(FontWeight.Bold)
        }
        .justifyContent(FlexAlign.Center)
        .width('100%')

        //返回按钮
        if (this.showBack) {
          Image($r('app.media.back'))
            .offset({ x: '-46%', y: 0 })
            .height(28)
            .tabIndex(100)
            .onClick(() => {
              if (this.sureDialog) {
                //返回弹框确认
                router.showAlertBeforeBackPage({
                  message: '确定返回上一页吗？'
                })
              }
              //返回上一页
              router.back()
            })
        }
      }
    }
    .backgroundColor('#ffd2cfd2') //header头部背景颜色
    .width('100%')
    .height(45)
  }
}