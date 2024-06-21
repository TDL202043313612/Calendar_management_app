<template>
  <div>
    <!-- 准备容器 -->
    <div ref="chart" style="width:100%;height:800px"></div>
  </div>
</template>

<script>
// import * as echarts from 'echarts/core' //引入echarts5
import echarts from 'echarts'
import 'echarts/theme/macarons'  //图表主题
import chinaMap from '@/assets/json/china.json'  //引入地图文件

export default {
  data(){
    return {
        mapData:[]
    }
  },
  mounted(){
      this.getData(); //发送请求
  },
  methods:{
      init(){
          //初始化
          let myChart = echarts.init(this.$refs.chart);
          echarts.registerMap('china', chinaMap); // 注册可用的地图

          let option = {
                title:{
                    text:'XXX机构学员-全国分布图',  //标题
                    left:'53%',  //居中
                    top:'1%',   //上间距
                    textAlign: 'center',
                    textStyle: {
                      color: '#333333',
                      fontWeight: 'bold',
                      fontSize: 32,
                    },
                },
                tooltip:{   //提示信息  地图 : {a}（系列名称），{b}（区域名称），{c}（合并数值）, {d}（无）
                   tigger: 'item' ,
                   formatter: function (params) {
                     if(params.value){
                       return '城市名称：' + params.name + '<br/>学员数量：' + params.value + '人';
                     }else{
                       return '城市名称：' + params.name + '<br/>学员数量：0人';
                     }
                    }
                 },
                series: [{
                  layoutCenter: ['53%', '42%'], //地图位置偏移量
                  layoutSize: 700, //地图大小
                  // layoutSize: 850, //地图大小
                   type: 'map',
                   mapType:'china',
                   label:{     //文字显示
                       show: true,   //是否显示标签。
                       color: '#000', //文字颜色
                       fontSize: 10     //文字大小
                   },
                  itemStyle: {
                    areaColor: '#fff' //区域的背景颜色
                  },
                   data: this.mapData,  //动态数据处理
                   emphasis: {   //高亮状态下的多边形和标签样式。
                           //控制地图滑过后的颜色
                           label:{
                           color: '#fff', //移入后显示颜色
                            fontSize: 12,
                           },
                           itemStyle:{ //移入后颜色及边框
                               areaColor: '#1bc1ad',   //区域颜色
                               borderColor: '#ccc'   //边框颜色
                           },
                       },
                   }
               ],
                toolbox: { // 可下载地图
                  show: true,
                  orient: 'vertical',
                  left: 'right',
                  top: '10%',
                  feature: {
                    dataView: { // 数据视图
                      readOnly: false
                    },
                    restore: {}, // 刷新
                    saveAsImage: {} // 保存图片
                  }
                },
                visualMap:[{   //数据分段
                    left: "90%",
                    top: "10%",
                   type: 'piecewise', // 定义为分段型 visualMap
                   show: true,
                    splitNumber: 6, // 分成几条
                   pieces: [
                       {
                         min: 5000
                       },// 不指定 max，表示 max 为无限大（Infinity）。
                       {min: 600, max: 5000},
                       {min: 300, max: 599},
                       {min: 100, max: 299},
                       {min: 10, max: 99},
                       {min: 1, max: 9},
                   ],
                   inRange: {   //定义 在选中范围中 的视觉元素。
                       color: [
                       '#bfb519',
                       '#b3ffff',
                       '#c0f478',
                       '#1bfd03',
                       '#f3bbff',
                       '#fea78a'], //颜色可以在实时地图上取
                   },
                   itemWidth: 10,       //显示框的宽高
                   itemHeight: 10,
                }],
            };
          //设置渲染
          myChart.setOption(option);
      },
      //获取地图数据
      getData() {
        this.mapData = [
          {"name":"北京","value":135},
          {"name":"上海","value":369},
          {"name":"黑龙江","value":39},
          {"name":"新疆","value":491},
          {"name":"湖北","value":3458},
          {"name":"湖南","value":1025},
          {"name":"四川","value":169},
          {"name":"西藏","value":8},
          {"name":"内蒙古","value":7},
          {"name":"青海","value":77},
          {"name":"山东","value":88},
          {"name":"台湾","value":189},
          {"name":"海南","value":1069},
          {"name":"南海诸岛","value":2},
          ];
        this.init();
      }
  }
}
</script>


