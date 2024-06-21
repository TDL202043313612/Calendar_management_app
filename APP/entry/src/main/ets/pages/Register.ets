/*
 * Time: 2024-03-25 09:33:45
 * Desc: 注册页面
 */
import { HttpPost } from '../common/utils/HttpTools';
import Logger from '../common/utils/Logger';
import { myTools } from '../common/utils/MyTools';
import router from '@ohos.router';

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


@Entry
@Component
struct Register {
  @State account: string = ''; // 登录账号
  @State password: string = ''; // 登录密码

  // 构建图片按钮的函数
  @Builder
  imageButton(src: Resource) {
    Button({ type: ButtonType.Circle, stateEffect: true }) {
      Image(src)
    }
    .height('48vp') // 图片按钮高度
    .width('48vp') // 图片按钮宽度
    .backgroundColor('#F1F3F5') // 背景颜色
  }


  // 构建页面布局的函数
  build() {
    Column() {
      Image($r('app.media.icon_login')) // Logo图片
        .width('78vh') // Logo宽度
        .height('78vh') // Logo高度
        .margin({ top: '100vp', bottom: '8vp' }) // Logo边距

      Text('注  册') // 注册页面标题
        .fontSize('24fp') // 标题字体大小
        .fontWeight(FontWeight.Medium) // 标题字体粗细
        .fontColor('#182431') // 标题字体颜色

      Text('注册账号以使用更多服务') // 副标题文本
        .fontSize('16fp') // 字体大小
        .fontColor('#99182431') // 字体颜色
        .margin({ bottom: '30vp', top: '8vp' }) // 边距

      // 账号输入框
      TextInput({ placeholder: '请输入登录账号' })
        .maxLength(20) // 最大长度
        .type(InputType.Number) // 输入类型为数字
        .inputStyle() // 应用自定义样式
        .onChange((value: string) => {
          this.account = value; // 更新账号状态
        })
      Line().lineStyle() // 应用自定义Line样式

      // 短信验证码输入框
      TextInput({ placeholder: '请输入登录密码' })
        .maxLength(20) // 最大长度
        .type(InputType.Number) // 输入类型为密码
        .inputStyle() // 应用自定义样式
        .onChange((value: string) => {
          this.password = value; // 更新密码状态
        })
      Line().lineStyle() // 应用自定义Line样式

      // 注册按钮
      Button('注册', { type: ButtonType.Capsule })
        .width('90%') // 按钮宽度
        .height('40vp') // 按钮高度
        .fontSize('16fp') // 字体大小
        .fontWeight(FontWeight.Medium) // 字体粗细
        .backgroundColor('#007DFF') // 背景颜色
        .margin({ top: '87vp', bottom: '12vp' }) // 边距
        .onClick(() => {
          if (this.account == '') {
            myTools.alertMsg('请输入登录账号！')
            return;
          } else if (this.password == '') {
            myTools.alertMsg('请输入登录密码！')
            return;
          }
          //发请求注册账号
          HttpPost('/app/login/register', {
            userPhone: this.account,
            loginPass: this.password
          }).then(response => {
            Logger.debug('response===' + JSON.stringify(response))
            if (response != -1) {
              //业务处理
              this.account = '';
              this.password = '';
            }
          })

        })

      // 已有账号，去登录-文本
      Text('已有账号，去登录')
        .fontColor('#007DFF') // 字体颜色
        .fontSize('16fp') // 字体大小
        .fontWeight(FontWeight.Medium) // 字体粗细
        .onClick(() => {
          router.replaceUrl({
            url: 'pages/Login',
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