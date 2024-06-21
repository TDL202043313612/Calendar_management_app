/**
 * Desc: 下拉框-自定义组件
 */
@Component
export default struct MySelect {
  title: string // 标题
  selectData: Array<any> //下拉选项集合数据
  onSelectHandler = (index: number, value: string) => {} //选择事件
  @Prop defaultValue: string //默认选中值

  build() {
    Row() {
      Text(this.title)
        .width(110)
        .fontSize(16)
        .fontWeight(500)
        .fontColor('#ff000000')
        .margin({ left: 13 })
      Select(this.selectData)
        .onSelect((index: number, value: string) => {
          this.onSelectHandler(index, value)
        })
        .width(240)
        .value(this.defaultValue)
        .margin({ left: -26 })
        .border({ width: 1, color: '#e2e0e1' })
        .align(Alignment.Center)
        .borderRadius(8)
    }
    .width('100%')
  }
}