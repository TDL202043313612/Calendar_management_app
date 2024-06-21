/**
 * Desc: 新闻详情页
 */
import router from '@ohos.router'
import { newsApi } from '../api/NewsApi'
import { Header } from '../common/Header'
import Logger from '../common/utils/Logger'

@Entry
@Component
struct NewsDetails {
  @State newsId: string = router.getParams()?.['newsId']
  @State newsItem: any = {};
  @State newsContent: string = '';

  async onPageShow() {
    Logger.debug('this.newsId=' + this.newsId)
    await newsApi.getApiId(this.newsId).then(res => {
      Logger.debug('新闻详情=' + JSON.stringify(res))
      this.newsItem = res.detail
      this.newsContent = res.content
      Logger.debug('this.newsContent=' + this.newsContent)
    })
  }

  build() {
    Column() {
      //头部
      Header({ title: '新闻详情', showBack: true })

      List(){
        ListItem(){
          Column() {

            //内容
            Row(){
              RichText(this.newsContent)
            }
            .padding(5)
            .width('100%')
          }
        }
      }

    }
    .width('100%')
    .height('100%')
  }
}