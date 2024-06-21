import http from '@ohos.net.http';
import Logger from '../common/utils/Logger';
import cityModel from '../entity/CityModel';
import CityBean from '../entity/CityBean';
import DialogUtils from '../dialog/DialogUtils';
import { myTools } from '../common/utils/MyTools';
/**
 * Desc: 天气预报页面
 */

interface Forecast {
  date: string,
  high: string,
  low: string,
  ymd: string,
  week: string,
  sunrise: string,
  sunset: string,
  aqi: number,
  fx: string,
  fl: string,
  type: string,
  notice: string
}

interface Data {
  shidu: string,
  pm25: number,
  pm10: number,
  quality: string,
  wendu: string,
  ganmao: string,
  forecast: Array<Forecast>
}

interface CityInfo {
  city: string,
  citykey: string,
  parent: string,
  updateTime: string
}

interface NowWeatherBean {
  status: string,
  message: string,
  date: string,
  time: string,
  cityInfo: CityInfo,
  data: Data
}

@Entry
@Component
export struct Page003 {
  @State code: string = '101010100'
  @State city_name: string = '北京'
  @State nowWeather: NowWeatherBean = {} as NowWeatherBean;
  @State cityList: Array<CityBean> = cityModel.getCityList()

  async aboutToAppear() {
    this.loadData();
  }

  //加载天气数据
  async loadData() {
    // 每一个httpRequest对应一个HTTP请求任务，不可复用
    let httpRequest = http.createHttp();
    // 用于订阅HTTP响应头，此接口会比request请求先返回。可以根据业务需要订阅此消息
    // 从API 8开始，使用on('headersReceive', Callback)替代on('headerReceive', AsyncCallback)。 8+
    httpRequest.on('headersReceive', (header) => {
      console.error('header: ' + JSON.stringify(header));
    });
    httpRequest.request(
      // 填写HTTP请求的URL地址，可以带参数也可以不带参数。URL地址需要开发者自定义。请求的参数可以在extraData中指定
      "http://t.weather.itboy.net/api/weather/city/" + this.code,
      {
        method: http.RequestMethod.GET, // 可选，默认为http.RequestMethod.GET
        expectDataType: http.HttpDataType.STRING, // 可选，指定返回数据的类型
      }, (err, data) => {
      if (!err) {
        Logger.debug('data.result>>>>>' + JSON.stringify(data.result))
        this.nowWeather = JSON.parse(data.result as string);
        Logger.debug('this.nowWeather>>>>>' + this.nowWeather.time)
      } else {
        // 取消订阅HTTP响应头事件
        httpRequest.off('headersReceive');
        // 当该请求使用完毕时，调用destroy方法主动销毁
        httpRequest.destroy();
      }
    }
    );
  }

  //选择切换城市
  showCityDialog() {
    let namse: Array<string> = []
    let idss = []
    this.cityList.forEach(item => {
      namse.push(item.city_name)
      idss.push(item.city_code)
    })
    //弹窗选择天气
    DialogUtils.textPickerDialog(namse, res => {
      this.city_name = res;
      let index = 0;
      for (let i = 0; i < namse.length; i++) {
        if (res == namse[i]) {
          index = i;
          break
        }
      }
      //保留当前城市信息
      this.code = idss[index];
      // myTools.alertMsg('code=' + this.code)
      if(this.code){
        //重新加载城市天气数据
        this.loadData();
      }else{
        myTools.alertMsg('暂无该城市天气数据！')
      }
    })

  }

  //获取天气图标
  getWeatherIcon(type: string): string {
    if (type.indexOf("雨") >= 0) {
      return "/images/yu.png"
    } else if (type.indexOf("雪") >= 0) {
      return "/images/xue.png"
    } else if (type.indexOf("阴") >= 0) {
      return "/images/yin.png"
    } else if (type.indexOf("晴") >= 0) {
      return "/images/qing.png"
    } else if (type.indexOf("霜") >= 0) {
      return "/images/shuang.png"
    } else if (type.indexOf("雾") >= 0) {
      return "/images/wu.png"
    } else if (type.indexOf("雷") >= 0) {
      return "/images/lei.png"
    } else if (type.indexOf("云") >= 0) {
      return "/images/yun.png"
    } else if (type.indexOf("风") >= 0) {
      return "/images/feng.png"
    } else if (type.indexOf("冰") >= 0) {
      return "/images/bing.png"
    } else {
      return "/images/default.png"
    }
  }

  build() {
    Column() {
      Column() {
        Text("天气预报")
          .fontSize(30)
          .width("100%")
          .textAlign(TextAlign.Start)
          .fontColor(Color.White)
          .margin({
            top: "5%",
            left: 30
          })


        Row() {
          Image($r("app.media.icon_address"))
            .width(25)
            .height(25)
            .objectFit(ImageFit.Fill)
            .onClick(e => {
              //切换城市
              this.showCityDialog()
            })

          Text(this.city_name)
            .fontSize(20)
            .width("30%")
            .height("100%")
            .textAlign(TextAlign.Start)
            .fontColor(Color.White)
            .margin({
              left: 5,
            })
            .onClick(e => {
              //切换城市
              this.showCityDialog()
            })
          Text("更新时间：" + (this.nowWeather ? this.nowWeather.time : ""))
            .fontSize(12)
            .width("100%")
            .height("100%")
            .textAlign(TextAlign.Start)
            .fontColor(Color.White)
            .padding({
              right: 10
            })
            .margin({
              left: 5,
              right: 20
            })
        }
        .width("90%")
        .height("15%")
        .margin({
          top: 20
        })

        Column() {

          Row() {
            Image(this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.getWeatherIcon(this.nowWeather.data.forecast[0].type) : "/images/default.png")
              .width("20%")
              .height("100%")
              .margin({ top: 20 })

            Text((this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.nowWeather.data.forecast[0].type : ""))
              .width("30%")
              .height("100%")
              .fontSize(40)
              .align(Alignment.Center)
              .textAlign(TextAlign.Center)
              .fontColor(Color.White)
              .borderRadius(10)
              .margin({ left: 10, top: 20 })
          }.align(Alignment.Center)
          .height("30%")

          Row() {
            Image($r("app.media.richu")).width(30).height(30).margin({ left: 10, right: 10 })
            Text("日出：" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.nowWeather.data.forecast[0].sunrise : ""))
              .width("32%")
              .height(50)
              .fontSize(16)
              .fontColor(Color.White)
              .borderRadius(10)

            Image($r("app.media.riluo")).width(30).height(30).margin({ left: 15, right: 10 })
            Text("日落：" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.nowWeather.data.forecast[0].sunset : ""))
              .width("32%")
              .height(50)
              .fontSize(16)
              .borderRadius(10)
              .fontColor(Color.White)

          }.width("100%")
          .margin({
            left: 20,
            top: 10
          })

          Row() {
            Image($r("app.media.code")).width(30).height(30).margin({ left: 10, right: 10 })
            Text("" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.nowWeather.data.forecast[0].low : ""))
              .width("32%")
              .height(50)
              .fontSize(16)
              .borderRadius(10)
              .fontColor(Color.White)
            Image($r("app.media.hot")).width(30).height(30).margin({ left: 15, right: 10 })
            Text("" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ?
            this.nowWeather.data.forecast[0].high : ""))
              .width("32%")
              .height(50)
              .fontSize(16)
              .borderRadius(10)
              .fontColor(Color.White)
          }.width("100%")
          .margin({
            left: 20,
            top: 5
          })

          Text("" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ? this.nowWeather.data.forecast[0].notice : ""))
            .fontColor("#000")
            .width("100%")
            .margin({
              top: 8
            })
            .textAlign(TextAlign.Center)
            .align(Alignment.End)
            .fontSize(24)
            .fontColor(Color.White)
        }
        .backgroundColor('#80c127')
        .width("90%")
        .height("100%")
        .border({
          radius: 12
        })

      }
      .width("100%")
      .height("35%")
      .backgroundColor("#3697F2")
      .backgroundImageSize(ImageSize.Cover)


      Column() {
        Row() {
          Text("今日天气")
            .fontColor("#000")
            .width("30%")
            .fontSize(20)

        }.width("100%")
        .height(45)

        Row({ space: 10 }) {

          Text("湿度：" + (this.nowWeather && this.nowWeather.data ? this.nowWeather.data.shidu : "") + "\n\n"
          + "温度：" + (this.nowWeather && this.nowWeather.data ? this.nowWeather.data.wendu : ""))
            .width("50%")
            .height("12%")
            .padding({ left: 20
            })
            .fontSize(16)
            .fontColor(Color.White)
            .borderRadius(10)
            .backgroundColor("#53C4F5")

          Text("PM25：" + (this.nowWeather && this.nowWeather.data ? this.nowWeather.data.pm25 : "") + "\n\n"
          + "PM10：" + (this.nowWeather && this.nowWeather.data ? this.nowWeather.data.pm10 : ""))
            .width("50%")
            .height("12%")
            .padding({ left: 20
            })
            .fontSize(16)
            .fontColor(Color.White)
            .borderRadius(10)
            .backgroundColor("#02BFBF")

        }
        .width("92%")
        .margin({
          top: 20
        })


        Row({ space: 10 }) {

          Text("空气质量：" + (this.nowWeather && this.nowWeather.data ? this.nowWeather.data.quality : ""))
            .width("50%")
            .height("12%")
            .padding({ left: 20
            })
            .fontSize(16)
            .fontColor(Color.White)
            .borderRadius(10)
            .backgroundColor("#B4B4FF")

          Text("风力：" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ? this.nowWeather.data.forecast[0].fl : "") + "\n\n"
          + "风向：" + (this.nowWeather && this.nowWeather.data && this.nowWeather.data.forecast[0] ? this.nowWeather.data.forecast[0].fx : ""))
            .width("50%")
            .height("12%")
            .padding({ left: 20, right: 20
            })
            .fontSize(16)
            .fontColor(Color.White)
            .borderRadius(10)
            .backgroundColor("#EB8A59")

        }
        .width("92%")
        .margin({
          top: 20
        })


        Text(this.nowWeather && this.nowWeather.data ? this.nowWeather.data.ganmao : "")
          .width("95%")
          .height("12%")
          .fontSize(16)
          .fontColor(Color.White)
          .borderRadius(10)
          .textAlign(TextAlign.Center)
          .backgroundColor("#80c127")
          .margin({
            top: 20
          })
      }
      .margin({
        left: 10,
        right: 10,
        top: "40%"
      })

    }
    .width('100%')
    .height('100%')
  }
}