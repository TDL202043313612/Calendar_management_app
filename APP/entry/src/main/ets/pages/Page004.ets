import { HttpPost } from '../common/utils/HttpTools'
import Logger from '../common/utils/Logger'
import { LoadingDialog } from '../dialog/LoadingDialog'
/**
 * Desc:
 */
@Entry
@Component
export struct Page004 {
  @State keyword: string = ''
  @State resultStr: string = ''
  controller: SearchController = new SearchController() //搜索框

  //加载框
  private loadingDialog: CustomDialogController = new CustomDialogController({
    builder: LoadingDialog(),
    autoCancel: false,
    customStyle: true
  })

  build() {
    Column() {

      //显示答案区域

      List() {
        ListItem() {
          Column() {
            if (this.resultStr != '') {
              Text("回答：")
                .fontColor('#ffff0000')
                .fontSize(25)
              Text(this.resultStr)
            }else{
              Text("请输入问题吧...")
                .fontColor('#ffff0000')
                .fontSize(25)
            }
          }
          .alignItems(HorizontalAlign.Start)
          .layoutWeight(1)
          .padding(15)
          .width('100%')
        }
      }
      .layoutWeight(1)

      //搜索
      Search({ value: this.keyword, placeholder: '请输入关键字进行提问...', controller: this.controller })
        .searchButton('提问')
        .width('95%')
        .height(40)
        .backgroundColor('#ffffff')
        .placeholderColor(Color.Grey)
        .placeholderFont({ size: 14, weight: 400 })
        .textFont({ size: 14, weight: 400 })
        .onSubmit((value: string) => {
          //点击搜索按钮后，value就是用户输入的值
          this.keyword = value
          this.loadingDialog.open();
          HttpPost("/communityUser/chat", { keyword: this.keyword }).then(response => {
            if (response != -1) {
              Logger.debug('response>>>>>>>' + response)
              this.resultStr = response
              this.loadingDialog.close();
            }
          })
        })


    }
    .justifyContent(FlexAlign.SpaceBetween)
    .backgroundColor('#f5f5f5')
    .width('100%')
    .height('100%')
  }
}