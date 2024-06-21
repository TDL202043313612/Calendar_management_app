/**
 * Desc: 新闻管理
 */
import Logger from '../common/utils/Logger'
import { NewsItem } from '../entity/NewsItem';
import router from '@ohos.router';
import { newsApi } from '../api/NewsApi';
import { Header } from '../common/Header';

@Entry
@Component
export struct NewsPage {
  @State message: string = 'Hello World'
  @State newsList: Array<any> = [];
  @State newsItem: NewsItem = new NewsItem();

  @State fontColor: string = '#182431'
  @State selectedFontColor: string = '#007DFF'
  @State currentIndex: number = 0
  private controller: TabsController = new TabsController()

  @Builder TabBuilder(index: number, name: string, newsType: string) {
    Column() {
      Text(name)
        .fontColor(this.currentIndex === index ? this.selectedFontColor : this.fontColor)
        .fontSize(16)
        .fontWeight(this.currentIndex === index ? 500 : 400)
        .lineHeight(22)
        .margin({ top: 17, bottom: 7 })
      Divider()
        .strokeWidth(2)
        .color('#007DFF')
        .opacity(this.currentIndex === index ? 1 : 0)
    }
    .width('100%')
    .onClick(() => {
      this.newsItem.newsType = newsType
      this.getAllData();
    })
  }

  aboutToAppear() {
    this.newsItem.newsType = 'guonei'
    this.getAllData();
  }

  @Builder
  newList(){
    //列表(可上下滑动)
    List() {
      ForEach(this.newsList, (item: any, index) => {
        ListItem() {
          Column() {
            Row() {
              Image(item.thumbnail_pic_s)
                .width(110)
                .height(110)
                .margin({ left: 10 })
                .borderRadius(12)
              Column({ space: 5 }) {
                //名称
                Text(item.title)
                  .width('60%')
                  .fontSize(17)
                  .fontWeight(FontWeight.Bold)
                  .maxLines(2) //最多显示2行
                  .textOverflow({ overflow: TextOverflow.Ellipsis }) //超过2行就显示省略号
                //作者
                Text('作者：' + item.author_name)
                  .fontSize(13)
                  .fontColor('#787878')
                Row() {
                  //时间
                  Text(item.date)
                    .fontSize(13)
                    .fontColor('#787878')
                  //分类
                  Text(item.category)
                    .margin({ left: 18 })
                    .backgroundColor('#5e85f4')
                    .fontColor('#ffffffff')
                    .fontSize(13)
                    .border({
                      width: { left: '8px', right: '8px', top: '8px', bottom: '8px' },
                      color: { left: '#5e85f4', right: '#5e85f4', top: '#5e85f4', bottom: '#5e85f4' },
                    })
                }
                .width('60%')
              }
              .alignItems(HorizontalAlign.Start)
              .margin({ left: 20 })
            }
            .onClick(() => {
              //跳转到指定路径，并传递参数
              router.pushUrl({
                url: 'pages/NewsDetails',
                params: {
                  newsId: item.uniquekey
                },
              },
                router.RouterMode.Single)
            })
            .width('100%')
            .justifyContent(FlexAlign.Start)
          }
          .margin({ bottom: 10 })
          .borderRadius(8)
          .backgroundColor('#ffffffff') //行背景色
          .width('96%')
          .height(120)
          .justifyContent(FlexAlign.Center) //垂直居中
        }
        .margin({ left: 10 })
      })
    }
    .scrollBar(BarState.Auto) //设置滚动条状态(默认值：BarState.Off)
    .edgeEffect(EdgeEffect.Spring) // 滑动到边缘无效果
    // .layoutWeight(1)
  }

  async getAllData(){
    await newsApi.getNewsApi(this.newsItem).then(res => {
      Logger.debug('res=' + JSON.stringify(res))
      this.newsList =  res;
    })
  }

  build() {
    Column() {
      Header({ title: '新闻', showBack: true })

      Tabs({ barPosition: BarPosition.Start, controller: this.controller }) {
        TabContent() {
          this.newList();
        }.tabBar(this.TabBuilder(0, '国内', 'guonei'))

        TabContent() {
          this.newList();
        }.tabBar(this.TabBuilder(1, '国际', 'guoji'))

        TabContent() {
          this.newList();
        }.tabBar(this.TabBuilder(2, '娱乐', 'yule'))

        TabContent() {
          this.newList();
        }.tabBar(this.TabBuilder(3, '体育', 'tiyu'))

        TabContent() {
          this.newList();
        }.tabBar(this.TabBuilder(4, '军事', 'junshi'))
      }
      .vertical(false)
      .barMode(BarMode.Fixed)
      .barWidth(360)
      .barHeight(56)
      .animationDuration(400)
      .onChange((index: number) => {
        this.currentIndex = index
      })
      .width('100%') //360
      .layoutWeight(1)
      .backgroundColor('#F1F3F5')

    }
    .backgroundColor('#fff1efef')
    .width('100%')
    .height('100%')
  }
}