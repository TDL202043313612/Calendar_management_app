import { PieCharts, PieChartsData, PieChartsOptions, PieChartsLabelStyle } from '../common/PieCharts'
import router from '@ohos.router'
import { MoneyItem } from '../entity/MoneyItem'
import ArrayList from '@ohos.util.ArrayList'
import Logger from '../common/utils/Logger'

@Extend(Button)
function setButtonStyle(selected: boolean) {
  .type(ButtonType.Normal)
  .backgroundColor(selected ? Color.Black : '#F5F5F5')
  .fontColor(selected ? Color.White : Color.Black)
  .fontSize(14)
  .padding(0)
  .width(60)
  .height(36)
  .borderRadius(18)
}

class BillModel implements PieChartsData {
  type: number = 0 // 0收入/1支出
  amount: number = 0 // 金额
  category: string = '购物'

  getValue(): number {
    return this.amount
  }
}

@Entry
@Component
struct Index {
  @State billType: number = 0
  @State options: PieChartsOptions = new PieChartsOptions()
  @State moneyList: Array<MoneyItem> = router.getParams()?.['moneyList']

  aboutToAppear(): void {
    this.loadData()
  }

  loadData() {
    // const expense_categories = ['购物', '出行', '餐饮', '医疗', '美容', '娱乐', '教育', '房租']
    // const income_categories = ['工资', '投资', '奖金', '红包', '租金', '礼金', '兼职', '广告']
    const expense_categories = [" "]
    const income_categories = [" "]
    const expense_amount = []
    const income_amount = []
    // let expense_categories = new ArrayList();
    // expense_categories.insert("1",1);
    // let expense_categories:Array<String>=[];
    // let income_categories:Array<String>=[];
    //mock data
    let data: BillModel[] = []

    let income_index=0;
    let expense_index=0;

    for (let i = 0; i < this.moneyList.length; i++) {

      if ('收入' == this.moneyList[i].moneyStatus) {
        income_categories[income_index] = this.moneyList[i].moneyDesc;
        income_amount[income_index] = this.moneyList[i].moneyNum;
        income_index++;
        Logger.error('expense_index :' + expense_index)
        Logger.error('income_index :' + income_index)
      } else {
        expense_categories[expense_index] = this.moneyList[i].moneyDesc;
        expense_amount[expense_index] = this.moneyList[i].moneyNum;
        expense_index++;

        Logger.error('expense_index :' + expense_index)
        Logger.error('income_index :' + income_index)
      }
    }



    // ForEach(this.moneyList, (item: MoneyItem, index) => {
    //   if ('收入' == item.moneyStatus) {
    //     // income_categories[index] = item.moneyDesc;
    //     // income_amount[index] = item.moneyNum;
    //     // income_categories.add("item.moneyDesc");
    //   }
    //   else if ('支出' == item.moneyStatus) {
    //     expense_categories.insert("1",1);
    //     // expense_amount[index] = item.moneyNum;
    //   }
    // })
    const categories = this.billType == 0 ? expense_categories : income_categories
    const amount = this.billType == 0 ? expense_amount : income_amount
    Logger.error('categories :' + categories.length)

    for (let i = 0; i < categories.length; i++) {
      let item = new BillModel()
      item.type = this.billType
      // @ts-ignore
      item.amount = amount[i]
      // item.amount = Math.random()*100
      item.category = categories[i]
      data.push(item)
    }
    // 根据价格排序
    data = data.sort((a, b) => b.amount - a.amount)

    let options = new PieChartsOptions()
    options.animate = true
    options.duration = 300
    options.data = data
    options.radius = 60
    options.innerRadius = 30
    options.labelFn = (data: PieChartsData) => {
      return (data as BillModel).category
    }
    options.labelStyleFn = (data: PieChartsData, index: number) => {
      let colors = [Color.Green, Color.Blue, Color.Orange, Color.Grey, '#FFCC00', Color.Pink, '#800080', '#ff6633', '#ffcc66', Color.Red]
      let style = new PieChartsLabelStyle()
      style.fontColor = colors[index]
      style.fontSize = 12
      return style
    }
    options.colorFn = (data: PieChartsData, index: number) => {
      let colors = [Color.Green, Color.Blue, Color.Orange, Color.Grey, '#FFCC00', Color.Pink, '#800080', '#ff6633', '#ffcc66', Color.Red]
      return colors[index]
    }
    this.options = options
  }

  build() {
    Navigation() {
      Column() {
        PieCharts({
          options: this.options
        })

        Row() {
          Button('支出')
            .setButtonStyle(this.billType == 0)
            .onClick(() => {
              this.billType = 0
              this.loadData()
            })
          Blank()
            .width(12)
          Button('收入')
            .setButtonStyle(this.billType != 0)
            .onClick(() => {
              this.billType = 1
              this.loadData()
            })
        }
      }
      .size({ width: '100%', height: "50%" })
    }
    .title('收支分析')
    .titleMode(NavigationTitleMode.Mini)
  }
}