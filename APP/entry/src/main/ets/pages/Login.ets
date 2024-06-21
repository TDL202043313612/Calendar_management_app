/*
 * Desc: 登录页面
 */

// TextInput组件的自定义样式扩展
@Extend(TextInput) function inputStyle() {
  .placeholderColor('#99182431') // 占位符颜色
  .height('45vp') // 输入框高度
  .fontSize('18fp') // 字体大小
  .backgroundColor('#F1F3F5') // 背景颜色
  .width('100%') // 宽度为父组件的100%
  .padding({ left: 0 }) // 左侧填充
  .margin({ top: '12vp' }) // 上方边距
}

// Line组件的自定义样式扩展
@Extend(Line) function lineStyle() {
  .width('100%') // 宽度为父组件的100%
  .height('1vp') // 高度
  .backgroundColor('#33182431') // 背景颜色
}

import router from '@ohos.router';
import CommonConstants from '../common/CommonConstants';
import { HttpPost } from '../common/utils/HttpTools';
import Logger from '../common/utils/Logger';
import { myTools } from '../common/utils/MyTools';
import { tokenUtils } from '../common/utils/TokenUtils';
import { LoadingDialog } from '../dialog/LoadingDialog';

@Entry
@Component
struct Login {
  @State account: string = '17769443613'; // 登录账号
  @State password: string = '123456'; // 登录密码
  //加载框
  private loadingDialog: CustomDialogController = new CustomDialogController({
    builder: LoadingDialog(),
    autoCancel: false,
    customStyle: true
  })

  login(): void {
    Logger.debug('开始登录！')
    if (this.account == '') {
      //提示框
      myTools.alertMsg('登录账号不能为空！')
    }
    else if (this.password == '') {
      //提示框
      myTools.alertMsg('登录密码不能为空！')
    } else {
      //显示加载框
      this.loadingDialog.open()

      //发请求进行登录操作
      HttpPost("/app/login/login", { loginName: this.account, loginPass: this.password }).then(response => {
        if (response != -1) {
          Logger.debug('登录接口返回数据=' + JSON.stringify(response))
          //登录成功，token存入缓存
          tokenUtils.setToken(response.token)
          tokenUtils.setUserId(response.userId)
          // 页面跳转,replaceUrl:目标页面会替换当前页，并销毁当前页。
          router.replaceUrl({
            url: 'pages/Home',
          }, router.RouterMode.Single)
        }
        //隐藏加载框
        this.loadingDialog.close()
      })

    }
  }

  // 构建图片按钮的函数
  @Builder
  imageButton(src: Resource) {
    Button({ type: ButtonType.Circle, stateEffect: true }) {
      Image(src)
    }
    .height('48vp') // 图片按钮高度
    .width('48vp') // 图片按钮宽度
    .backgroundColor('#F1F3F5') // 背景颜色
    .onClick(() => {
      myTools.alertMsg(CommonConstants.UN_DO_TIPS)
    })
  }

  // 构建页面布局的函数
  build() {
    Column() {
      Image($r('app.media.icon_login')) // Logo图片
        .width('78vh') // Logo宽度
        .height('78vh') // Logo高度
        .margin({ top: '100vp', bottom: '15vp' }) // Logo边距

      Text('登  录') // 登录页面标题
        .fontSize('24fp') // 标题字体大小
        .fontWeight(FontWeight.Medium) // 标题字体粗细
        .fontColor('#182431') // 标题字体颜色

      Text('登录账号以使用更多服务') // “了解更多”文本
        .fontSize('16fp') // 字体大小
        .fontColor('#99182431') // 字体颜色
        .margin({ bottom: '40vp', top: '8vp' }) // 边距

      // 账号输入框
      TextInput({ text: this.account, placeholder: '请输入登录账号' })
        .maxLength(20) // 最大长度
        .type(InputType.Number) // 输入类型为数字
        .inputStyle() // 应用自定义样式
        .onChange((value: string) => {
          this.account = value; // 更新账号状态
        })
      Line().lineStyle() // 应用自定义Line样式

      // 密码输入框
      TextInput({ text: this.password, placeholder: '请输入登录密码' })
        .maxLength(20) // 最大长度
        .type(InputType.Password) // 输入类型为密码
        .inputStyle() // 应用自定义样式
        .onChange((value: string) => {
          this.password = value; // 更新密码状态
        })
      Line().lineStyle() // 应用自定义Line样式

      // 登录按钮
      Button('登录', { type: ButtonType.Capsule })
        .width('90%') // 按钮宽度
        .height('40vp') // 按钮高度
        .fontSize('16fp') // 字体大小
        .fontWeight(FontWeight.Medium) // 字体粗细
        .backgroundColor('#007DFF') // 背景颜色
        .margin({ top: '87vp', bottom: '12vp' }) // 边距
        .onClick(() => {
          // 登录操作
          this.login()
        })




      // 注册账号文本
      Text('注册账号')
        .fontColor('#007DFF') // 字体颜色
        .fontSize('16fp') // 字体大小
        .fontWeight(FontWeight.Medium) // 字体粗细
        .onClick(() => {
          router.replaceUrl({
            url: 'pages/Register',
          }, router.RouterMode.Single)
        })

    }
    .backgroundColor($r('app.color.page_bg'))
    .height('100%') // 高度为父组件的100%
    .width('100%') // 宽度为父组件的100%
    .padding({
      left: '12vp', // 左右填充
      right: '12vp',
      bottom: '24vp' // 底部填充
    })

  }
}