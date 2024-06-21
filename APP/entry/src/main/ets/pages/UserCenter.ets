/*
 * Desc: 我的页面
 */
import router from '@ohos.router';
import CommonConstants from '../common/CommonConstants';
import { HttpGet } from '../common/utils/HttpTools';
import Logger from '../common/utils/Logger';
import { myTools } from '../common/utils/MyTools';
import { tokenUtils } from '../common/utils/TokenUtils';

@Entry
@Component
export struct UserCenter {
  @State userId: number = tokenUtils.getUserId();
  @State userInfo: any = {}
  @State durationTime: number = 100
  @State translateY: number = 0
  @State text: string = '下拉即可刷新'
  @State loading: boolean = false

  aboutToAppear() {
    this.loadData();
  }

  //加载页面数据
  loadData() {
    Logger.debug('this.userId===' + this.userId);
    HttpGet('/communityUser/getUserInfoById/' + this.userId).then(response => {
      Logger.debug('response===' + JSON.stringify(response))
      if (response != -1) {
        //业务处理
        this.userInfo = response;
      }
    })
  }

  @Builder
  MyRow(title: string, img: Resource) {
    Column() {
      Row() {
        Row() {
          Image(img)
            .width(28)
            .height(28)
            .objectFit(ImageFit.Cover)
          Text(title).fontSize(17).margin({ left: 10 })
        }

        Image("https://photo-tupige.oss-cn-beijing.aliyuncs.com/wechat/arrow_forward.jpg")
          .width(28)
          .height(28)
      }
      .width('100%')
      .backgroundColor('#ffffff')
      .padding(10)
      .justifyContent(FlexAlign.SpaceBetween)
      .onClick(async () => {
        if (title == '退出系统') {
          AlertDialog.show({
            title: "请确认：", //弹窗标题
            message: '确定退出系统吗？', // 提示消息
            autoCancel: false, // 点击遮罩层即阴影，是否关闭弹窗。true是点击遮罩层弹窗关闭。
            alignment: DialogAlignment.Center, //弹窗位置
            offset: { dx: 0, dy: 0 }, // 相对弹窗位置即DialogAlignment.Bottom 在x轴和y轴偏移
            primaryButton: {
              value: "确定",
              fontColor: Color.Red,
              action: () => {
                tokenUtils.clearAppStorage();
                router.replaceUrl({
                  url: 'pages/Login',
                })
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
        else {
          myTools.alertMsg(CommonConstants.UN_DO_TIPS)
        }
      })

      Divider()
        .strokeWidth(1)
        .color("#eee")
        .padding({ left: 50, right: 0 })
    }
    .backgroundColor('#ffffff')
  }

  ease(originValue: number = 0) {
    const space = 60
    const damp = 0.3
    if (originValue > space) {
      return space + (originValue - space) * damp
    }
    return originValue
  }

  build() {
    Stack({ alignContent: Alignment.Top }) {
      //刷新信息提示语
      Row() {
        if (this.loading) {
          LoadingProgress()
            .width(40)
            .aspectRatio(1)
        }
        Text(this.text)
          .fontColor('#ffff0000')
          .fontSize(20)
          .fontWeight(FontWeight.Bolder)
      }
      .height(100)

      //页面正文部分
      Column() {
        Row() {
          Column() {
            Row() {
              Image(this.userInfo.userImg)
                .width(80)
                .height(80)
                .borderRadius(10) //圆角效果
                .margin({ left: 27 }) //左边距
            }
          }

          Column() {
            Row() {
              Text(this.userInfo.nickName).fontSize(25).fontWeight(90).margin({ left: 10 })
            }

            Row() {
              Text("登录账号：" + this.userInfo.loginName)
                .fontSize(14)
                .fontColor('#ff565656')
                .margin({ left: 10 })
            }
            .margin({ top: 8 })
          }
          .alignItems(HorizontalAlign.Start) //子元素在水平方向左对齐
          .margin({ left: 12 })
        }
        .width('100%')
        .height('180vp')
        .backgroundColor("#ffffff")

        Divider() //分隔器组件
          .strokeWidth(10) //分割线宽度,默认值1
          .color($r('app.color.page_bg')) //分割线颜色

        Divider()
          .strokeWidth(10)
          .color($r('app.color.page_bg'))


        this.MyRow('退出系统', $r('app.media.exitSystem'));

      }
      .translate({ y: this.translateY })
      //gesture：绑定下拉手势，实现下拉刷新获取后台数据
      .gesture(
        //PanGesture：用于触发拖动手势事件，滑动的最小距离为5vp时拖动手势识别成功
        PanGesture()
          //Pan手势移动过程中回调
          .onActionUpdate((event: GestureEvent) => {
            this.translateY = this.ease(event.offsetY)
            if (this.translateY > 100) {
              this.text = '释放立即刷新'
            }
          })
            //Pan手势识别成功，手指抬起后触发回调
          .onActionEnd((event: GestureEvent) => {
            if (this.translateY > 100) {
              this.loading = true
              this.text = '正在刷新中...'
              animateTo({ duration: this.durationTime }, () => {
                this.translateY = 100
              })
              // 加载数据
              setTimeout(() => {
                this.loading = false
                this.text = ''
                animateTo({ duration: this.durationTime, onFinish: () => this.text = '下拉即可刷新' }, () => {
                  this.translateY = 0
                })
                //发请求
                this.loadData();
                myTools.alertMsg('刷新成功！')
              }, 1100)
            } else {
              animateTo({ duration: this.durationTime }, () => {
                this.translateY = 0
              })
            }
          })
      )
    }
    .width('100%')
    .height('100%')
    .backgroundColor($r('app.color.page_bg'))
  }
}