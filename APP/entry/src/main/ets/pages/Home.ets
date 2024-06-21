
import { Index } from './Index'
import { NewsPage } from './NewsPage'
import { Page002 } from './Page002'
import { Page003 } from './Page003'
import { Page004 } from './Page004'
import { WeatherPage } from './WeatherPage'
@Entry
@Component
export struct Home {
  @State message: string = 'Hello World'
  @State
  currentName: string = 'page01'
  @State tabsData: Array<any> = [
    {
      title: '信息管理',
      name: 'page01',
      icon: $r("app.media.home1")
    },
    {
      title: '新闻订阅',
      name: 'page02',
      icon: $r("app.media.new1002")
    },
    {
      title: '天气预报',
      name: 'page03',
      icon: $r("app.media.tianqiyubao")
    },
    // {
    //   title: '智能对话',
    //   name: 'page04',
    //   icon: $r("app.media.gpt")
    // },
  ]

  // tabBar组件
  @Builder
  getTabBar(item: any) {
    Column() {
      Row() {
        Image(item.icon)
          .fillColor(item.name === this.currentName ? Color.White : '#ff000000')
          .width(item.name === this.currentName ? 18 : 22)
          .aspectRatio(1)
      }
      .width(22)
      .aspectRatio(1)
      .borderRadius(22)
      .backgroundColor(item.name === this.currentName ? '#f5a101' : Color.Transparent)
      .justifyContent(FlexAlign.Center)

      Text(item.title)
        .fontSize(15)
        .fontWeight(FontWeight.Bolder)
        .margin({ top: 5 })
        .fontColor(item.name === this.currentName ? '#f5a101' : '#ff000000')
    }
    .width('100%')
    .alignItems(HorizontalAlign.Center)
    .justifyContent(FlexAlign.SpaceBetween)
  }

  build() {
    Row() {
      Column() {
        Tabs({ barPosition: BarPosition.End }) {
          ForEach(this.tabsData, (item: any) => {
            TabContent() {
              if (item.name === 'page01') {
                Index()
              } else if (item.name === 'page02') {
                NewsPage()
              } else if (item.name === 'page03') {
                WeatherPage()
              } else if (item.name === 'page04') {
                Page004()
              }
            }
            .tabBar(this.getTabBar(item))
            .backgroundColor($r('app.color.page_bg')) //每一页内容区域背景颜色
          })
        }
        .barHeight(60)
        .onChange(index => {
          this.currentName = this.tabsData[index].name
        })
      }
      .width('100%')
    }
    .height('100%')
  }
}