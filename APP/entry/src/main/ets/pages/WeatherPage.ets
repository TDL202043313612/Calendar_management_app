import { Header } from '../common/Header'
import { HttpGet } from '../common/utils/HttpTools';
import Logger from '../common/utils/Logger';
import CityBean from '../entity/weather/CityBean';
import cityModel from '../entity/weather/CityModel';

//当前天气信息
class NowWeather {
  text: string; //天气现象,如中雨
  temp: number; //温度（℃）
  feels_like: number; //体感温度(℃)
  rh: number; //相对湿度(%)
  wind_class: string; //风力等级,0级
  wind_dir: string; //风向描述,西南风
  uptime: string; //数据更新时间，北京时间
}

//当前城市位置
class Location {
  id: string; //区县规划编码
  country: string; //国家
  province: string; //省份
  city: string; //城市
  name: string; //名称
}

//未来天气信息
class ForecastItem {
  text_day: string; //白天天气现象
  text_night: string; //晚上天气现象
  high: number; //最高温度
  low: number; //最低温度
  wc_day: string; //白天风力
  wd_day: string; //白天风向
  wc_night: string; //晚上风力
  wd_night: string; //晚上风向
  date: string; //日期,年月日,如2024-05-30
  week: string; //星期数,如星期四
}

// Text组件的蓝色文本样式
@Extend(Text) function myTextStyle() {
  .fontColor('#ffffff') // 字体颜色
  .fontWeight(FontWeight.Bolder)
  .fontSize(18)
  .width('50%')
}

@Entry
@Component
export struct WeatherPage {
  @State districtId: string = '420100'
  @State cityName: string = '武汉'
  @State cityList: Array<CityBean> = cityModel.getCityList()
  @State selectData: Array<any> = [];
  @State now: NowWeather = new NowWeather();
  @State location: Location = null
  @State forecasts: Array<ForecastItem> = []
  @State bgColor: Array<string> = ['#ec5c37', '#3064bb', '#ff8018f8', '#ffb808d6', '#12a7a3']

  aboutToAppear() {
    this.getData();

    //封装城市下拉框
    for (let index = 0; index < this.cityList.length; index++) {
      const element: CityBean = this.cityList[index];
      this.selectData.push(
        element.cityName
      )
    }
  }

  getData() {
    HttpGet('/communityUser/getWeather/' + this.districtId).then(response => {
      Logger.debug('getWeather >>>' + JSON.stringify(response))
      if (response != -1) {
        //业务处理
        this.now = response.now;
        this.location = response.location;
        this.forecasts = response.forecasts;
        Logger.debug('now >>>' + JSON.stringify(this.now))
        Logger.debug('location >>>' + JSON.stringify(this.location))
        Logger.debug('forecasts >>>' + JSON.stringify(this.forecasts))
      }
    })
  }

  @Builder
  MyRow(leftText: string, rightText: string) {
    Row() {
      Text(leftText)
        .fontColor('#ffffff') // 字体颜色
        .fontWeight(FontWeight.Bolder)
        .fontSize(14)
      Text(rightText)
        .fontColor('#ffffff') // 字体颜色
        .fontWeight(FontWeight.Bolder)
        .fontSize(14)
    }
    .justifyContent(FlexAlign.SpaceBetween)
    .width('100%')
  }

  build() {
    Column() {

      Header({ title: '天气信息' })

      Row() {
        Text('今日天气：')
          .fontSize(20)

        Text(this.cityName)
          .fontSize(20)
          .onClick(() => {
            TextPickerDialog.show({
              range: this.selectData,
              selected: 0,
              onAccept: (result: TextPickerResult) => {
                for (let index = 0; index < this.cityList.length; index++) {
                  const element: CityBean = this.cityList[index];
                  if (element.cityName == result.value) {
                    this.districtId = element.districtId;
                    this.cityName = result.value;
                    break;
                  }
                }
                this.getData();
              },
              onCancel: () => {
              }
            });
          })

        // Select(this.selectData)
        //   .onSelect((index: number, value: string) => {
        //     for (let index = 0; index < this.cityList.length; index++) {
        //       const element: CityBean = this.cityList[index];
        //       if(element.cityName == value){
        //         this.districtId = element.districtId
        //         break;
        //       }
        //     }
        //     Logger.debug('this.districtId=' + this.districtId)
        //     this.getData();
        //   })
        //   .value(this.cityName)
        //   .border({ width: 1, color: '#e2e0e1' })
        //   .borderRadius(8)
      }
      .justifyContent(FlexAlign.SpaceBetween)
      .padding({ top: 15, bottom: 10 })
      .width('95%')

      //今日天气
      Column({ space: 10 }) {

        Row() {
          Text("天气：" + this.now.text)
            .myTextStyle()
          Text("温度：" + this.now.temp + "℃")
            .myTextStyle()
        }
        .justifyContent(FlexAlign.SpaceBetween)
        .width('100%')

        Row() {
          Text("体感温度：" + this.now.feels_like + "℃")
            .myTextStyle()
          Text("相对湿度：" + this.now.rh + "%")
            .myTextStyle()
        }
        .justifyContent(FlexAlign.SpaceBetween)
        .width('100%')

        Row() {
          Text("风力等级：" + this.now.wind_class)
            .myTextStyle()
          Text("风向：" + this.now.wind_dir)
            .myTextStyle()
        }
        .justifyContent(FlexAlign.SpaceBetween)
        .width('100%')

        Row() {
          Text("更新时间：" + this.now.uptime)
            .fontSize(12)
            .fontColor('#ffffff')
        }
        .justifyContent(FlexAlign.Center)
        .width('100%')

      }
      .padding(15)
      .alignItems(HorizontalAlign.Start)
      .justifyContent(FlexAlign.Start)
      .borderRadius(10)
      .backgroundColor('#ff13a7a2')
      .width('95%')


      Row() {
        Text('未来5日天气：')
          .fontSize(20)
      }
      .padding({ top: 15, bottom: 10 })
      .width('95%')

      //未来五天天气信息
      List({ space: 5 }) {
        ForEach(this.forecasts, (item: ForecastItem, index) => {
          ListItem() {
            Column({ space: 10 }) {
              this.MyRow("日期：" + item.date, item.week);
              this.MyRow("白天：" + item.text_day, "晚上：" + item.text_night);
              this.MyRow("最高温度：" + item.high + "℃", "最低温度：" + item.low + "℃");
              this.MyRow("白天风力：" + item.wc_day, "白天风向：" + item.wd_day);
              this.MyRow("晚上风力：" + item.wc_night, "晚上风向：" + item.wd_night);
            }
            .padding(15)
            .borderRadius(10)
            .backgroundColor(this.bgColor[index])
            .width('95%')
          }
        })
      }
      .layoutWeight(1)
      .alignListItem(ListItemAlign.Center)

    }
    .width('100%')
    .height('100%')
  }
}