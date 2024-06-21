/**
 * Desc: 发表信息弹框组件
 */
import { myTools } from '../common/utils/MyTools'
import MyTextArea from '../component/MyTextArea'

@Preview
@CustomDialog
export struct AddCommontDialog {

  controller: CustomDialogController //弹窗控制器
  @Link commontContent: string
  cancel: () => void
  confirm: (thisObj: any) => void
  @Link thisObj: any

  build() {
    Column() {
      Text('添加留言')
        .fontColor('#333333')
        .fontSize('25fp')
        .fontWeight(FontWeight.Bold)
        .margin({ top: '30vp' })

      MyTextArea({
        title: "留言内容：",
        placeholder: "请输入留言内容",
        maxLength: 300,
        defaultValue: this.commontContent,
        change: (val) => {
          this.commontContent = val
        }
      })
        .margin({ top: 10 })

      //确认/取消按钮
      Flex({ justifyContent: FlexAlign.SpaceAround }) {
        Button('取消')
          .onClick(() => {
            this.controller.close()
            this.cancel()
          }).backgroundColor(0xffffff).fontColor(Color.Black)
        Button('确认')
          .onClick(() => {
            if (this.commontContent == '') {
              myTools.alertMsg('请输入评论内容！')
              return;
            }
            this.controller.close()
            this.confirm(this.thisObj)
          }).backgroundColor(0xffffff).fontColor(Color.Red)
      }.margin({ bottom: 10, top: 15 })

    }
    .width('328vp')
    .borderRadius('10vp')
    .backgroundColor($r('app.color.page_bg')) //每一页内容区域背景颜色
  }
}