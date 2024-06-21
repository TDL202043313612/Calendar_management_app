
import CityBean from '../weather/CityBean';
/**
 * 城市实体类
 * */
export class CityModel {
  getCityList(): Array<CityBean> {
    let tabItems: Array<CityBean> = [
      {
        "districtId": "110100",
        "cityName": "北京",
      },
      {
        "districtId": "420100",
        "cityName": "武汉",
      },
      {
        "districtId": "420500",
        "cityName": "宜昌",
      },
      {
        "districtId": "430100",
        "cityName": "长沙",
      },
      {
        "districtId": "340100",
        "cityName": "合肥",
      },
      {
        "districtId": "429004",
        "cityName": "仙桃",
      },
      {
        "districtId": "440300",
        "cityName": "深圳",
      },
      {
        "districtId": "310100",
        "cityName": "上海",
      },
      {
        "districtId": "320100",
        "cityName": "南京",
      },
      {
        "districtId": "440100",
        "cityName": "广州",
      },
      {
        "districtId": "330100",
        "cityName": "杭州",
      },
      {
        "districtId": "450300",
        "cityName": "桂林",
      },
      {
        "districtId": "460321",
        "cityName": "西沙群岛",
      },
      {
        "districtId": "510100",
        "cityName": "成都",
      },
      {
        "districtId": "520100",
        "cityName": "贵阳",
      },
      {
        "districtId": "530100",
        "cityName": "昆明",
      },
      {
        "districtId": "610100",
        "cityName": "西安",
      },
      {
        "districtId": "620100",
        "cityName": "兰州",
      },
      {
        "districtId": "630100",
        "cityName": "西宁",
      },
      {
        "districtId": "640100",
        "cityName": "银川",
      },
      {
        "districtId": "360100",
        "cityName": "南昌",
      },
      {
        "districtId": "650100",
        "cityName": "乌鲁木齐",
      },
    ];
    return tabItems;
  }
}


let cityModel = new CityModel();

export default cityModel ;