/**
 * Desc: 加载框
 */

@Preview
@CustomDialog
export struct LoadingDialog {
  @State rotateAngle: number = 0
  controller: CustomDialogController//弹窗控制器
  private rotateDuration: any //计时器

  aboutToAppear() {
    this.startAnimation()
  }

  aboutToDisappear() {
    this.endAnimation()
  }

  startAnimation() {
    this.rotateDuration = setInterval(() => {
      this.rotateAngle += 15
    }, 30)
  }

  endAnimation() {
    this.rotateAngle = 0
    clearInterval(this.rotateDuration)
  }

  build() {
    Stack() {
      Column() {
        Image($r('app.media.icon_loading_dialog'))
          .width('40vp')
          .height('40vp')
          .rotate({ angle: this.rotateAngle })

        Text('处理中，请稍后...')
          .fontSize('13fp')
          .margin({ top: '8vp' })
      }
      .justifyContent(FlexAlign.Center)
      .height('90vp')
      .width('150vp')
      .backgroundColor('#FFFFFF')
      .border({ radius: '6vp' })
    }
  }
}