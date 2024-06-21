/*
 * Desc: APP首页
 */

import router from '@ohos.router'

class RouterInfo {
  //页面路径
  url: string
  //页面名称
  title: string
  //构造函数
  constructor(url: string, title: string) {
    this.url = url
    this.title = title
  }
}

@Entry
@Component
export struct Index {
  @State message: string = ''
  @State imageHeight: number = 80

  //创建数组，数组中的元素类型是：RouterInfo
  private routers: Array<RouterInfo> = [
    new RouterInfo('pages/TaskPage', '日程管理'),
    new RouterInfo('pages/MoneyPage', '财务管理'),
    // new RouterInfo('pages/NewsPage', '新闻订阅'),
    new RouterInfo('pages/MemoPage', '备忘录信息'),
    new RouterInfo('pages/LinkmanPage', '联系人信息'),
  ]

  build() {
    Column() {
      Text(this.message)
        .fontSize(50)
        .fontWeight(FontWeight.Bold)
        .height(80)

      //第一行
      Row() {
        Column() {
          Row(){
            Image($r('app.media.m_001'))
              .height(this.imageHeight)
              .onClick(() => {
                router.pushUrl({
                  url: 'pages/TaskPage',
                }, router.RouterMode.Single)
              })
            Text(' 日程管理')
              .margin({ top: 10 })
              .fontWeight(FontWeight.Bolder)
          }
        }

        // Column() {
        //   Image($r('app.media.m_002'))
        //     .height(this.imageHeight)
        //     .onClick(() => {
        //       router.pushUrl({
        //         url: 'pages/MoneyPage',
        //       }, router.RouterMode.Single)
        //     })
        //   Text('财务管理')
        //     .margin({ top: 10 })
        //     .fontWeight(FontWeight.Bolder)
        // }
      }
      .justifyContent(FlexAlign.SpaceEvenly)
      .height(150)
      // .backgroundColor('#fff1a2a2')
      .width('100%')

      //第二行
      Row() {
        // Column() {
        //   Image($r('app.media.m_001'))
        //     .height(this.imageHeight)
        //     .onClick(() => {
        //       router.pushUrl({
        //         url: 'pages/TaskPage',
        //       }, router.RouterMode.Single)
        //     })
        //   Text('日程管理')
        //     .margin({ top: 10 })
        //     .fontWeight(FontWeight.Bolder)
        // }

        Column() {
          Row(){
            Image($r('app.media.m_002'))
              .height(this.imageHeight)
              .onClick(() => {
                router.pushUrl({
                  url: 'pages/MoneyPage',
                }, router.RouterMode.Single)
              })
            Text('  财务管理')
              .margin({ top: 10 })
              .fontWeight(FontWeight.Bolder)
          }
        }

      }
      .justifyContent(FlexAlign.SpaceEvenly)
      .height(150)
      // .backgroundColor('#fff1a2a2')
      .width('100%')




      //第三行
      Row() {

        Column() {
              Row(){
                Image($r('app.media.m_005'))
                  .height(this.imageHeight)
                  .onClick(() => {
                    router.pushUrl({
                      url: 'pages/LinkmanPage',
                    }, router.RouterMode.Single)
                  })
                Text(' 联系人信息')
                  .margin({ top: 10 })
                  .fontWeight(FontWeight.Bolder)
              }
            }


        // Column() {
        //   Image($r('app.media.m_004'))
        //     .height(this.imageHeight)
        //     .onClick(() => {
        //       router.pushUrl({
        //         url: 'pages/MemoPage',
        //       }, router.RouterMode.Single)
        //     })
        //   Text('备忘录信息')
        //     .margin({ top: 10 })
        //     .fontWeight(FontWeight.Bolder)
        // }

      }
      .justifyContent(FlexAlign.SpaceEvenly)
      .height(150)
      // .backgroundColor('#fff1a2a2')
      .width('100%')

      //第四行
      Row() {

        // Column() {
        //   Image($r('app.media.m_005'))
        //     .height(this.imageHeight)
        //     .onClick(() => {
        //       router.pushUrl({
        //         url: 'pages/LinkmanPage',
        //       }, router.RouterMode.Single)
        //     })
        //   Text('联系人信息')
        //     .margin({ top: 10 })
        //     .fontWeight(FontWeight.Bolder)
        // }


        Column() {
          Row(){
            Image($r('app.media.m_004'))
              .height(this.imageHeight)
              .onClick(() => {
                router.pushUrl({
                  url: 'pages/MemoPage',
                }, router.RouterMode.Single)
              })
            Text(' 备忘录信息')
              .margin({ top: 10 })
              .fontWeight(FontWeight.Bolder)
          }
        }

      }
      .justifyContent(FlexAlign.SpaceEvenly)
      .height(150)
      // .backgroundColor('#fff1a2a2')
      .width('100%')

      //第三行
      // Row() {
      //   Column() {
      //     Image($r('app.media.m_005'))
      //       .height(this.imageHeight)
      //       .onClick(() => {
      //         router.pushUrl({
      //           url: 'pages/LinkmanPage',
      //         }, router.RouterMode.Single)
      //       })
      //     Text('联系人信息')
      //       .margin({ top: 10 })
      //       .fontWeight(FontWeight.Bolder)
      //   }
      // }
      // .justifyContent(FlexAlign.SpaceEvenly)
      // .height(150)
      // .backgroundColor('#fff1a2a2')
      // .width('100%')

    }
    .height('100%')
    .width('100%')
  }

  @Builder
  RouterItem(r: RouterInfo, index: number) {
    Row() {
      Text(index + '、')
        .fontSize(20)
        .fontColor(Color.White)
      Blank()
      Text(r.title)
        .fontSize(20)
        .fontColor(Color.White)
    }
    .width('100%')
    .padding(12)
    .backgroundColor('#38f')
    .borderRadius(13)
    .height(60)
    .onClick(() => {
      //路由跳转
      router.pushUrl({
        url: r.url,
        params: { index: index }
      })
    })
  }
}