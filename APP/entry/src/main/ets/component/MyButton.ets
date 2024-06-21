/**
 * Desc: 按钮-自定义组件
 */
import CommonConstants from '../common/CommonConstants'
@Component
export default struct MyButton {
  title: string // 按钮文字
  bgColor?: string = CommonConstants.DEFAULT_BUTTON_BG_COLOR // 按钮背景色
  widthNumber?: number = 100 // 按钮宽度
  heightNumber?: number = 40 // 按钮高度
  enabledStatus?: boolean = true //按钮是否可点击(true:可点击;false:不可点击)
  fontSize?: number = 14 // 文字大小
  //onClick事件
  onClickHandler = () => {
  }

  build() {
    Row() {
      Column() {
        Button(this.title, { type: ButtonType.Normal, stateEffect: true })
          .fontSize(this.fontSize)
          .borderRadius(10)
          .enabled(this.enabledStatus)
          .width(this.widthNumber)
          .height(this.heightNumber)
          .backgroundColor(this.bgColor)
          .onClick(() => {
            //点击事件
            this.onClickHandler()
          })
      }
    }
  }
}