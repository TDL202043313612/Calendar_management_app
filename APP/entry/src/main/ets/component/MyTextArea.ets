/*
 * Desc: 多行输入框-自定义组件
 */
@Component
export default struct MyTextArea {
  @State isFocus: boolean = false
  title: string // 标题
  icon?: Resource
  placeholder: string
  enabledStatus?: boolean = true //输入框是否可编辑(true:可编辑;false:不可编辑)
  maxLength?: number = 50
  inputType: InputType = InputType.Normal
  @Prop defaultValue: string
  change = (val: string) => {
  }

  build() {
    Column() {

      Flex({ alignItems: ItemAlign.Center }) {
        Text(this.title)
          .textAlign(TextAlign.Start)
          .width(110)
          .fontSize(16)
          .fontWeight(500)
          .fontColor('#ff000000')

        if (this.icon) {
          Image(this.icon)
            .width(30)
            .height(30)
            .margin({ right: 5 })
        }
        TextArea({
          placeholder: this.placeholder,
          text: this.defaultValue
        })
          .enabled(this.enabledStatus)
          .onFocus(() => {
            this.isFocus = true
          })
          .onBlur(() => {
            this.isFocus = false
          })
          .onChange((val: string) => {
            this.change(val)
          })
          .borderRadius(10)
          .placeholderColor(`rgba(0,40,132,0.6)`)
          .backgroundColor('#ffe2e0e0')
          .height(90)
      }
      .height(90)
      .padding({ left: 10, right: 10 })
      .borderRadius(10)
      .border({ width: 1, color: '#ffffff' }) //输入框边框颜色
    }
    .width('99%')
  }
}