<template>
  <div class="echartsHomePage">

    <el-row :gutter="20">

      <el-col :xs="24" :sm="8" :md="8" :lg="5">
        <div class="bg-purple bg-white">

          <el-card :class="'transparent-card'" class="box-card">
            <div slot="header" :class="'card-title'" class="clearfix">
              <span>动态栏</span>
            </div>
            <div :class="'card-content'"class="text item">
              <!--  内容   -->
              <div class="page-example3">
                <vue-seamless-scroll :data="newsList" :class-option="defaultOption" >
                  <div class="ul-scoll">
                    <div v-for="(item, index) in newsList" :key='index'>
                      <span class="title">{{item.date}}：</span>
                      <span class="date">{{item.text}}</span>
                    </div>
                  </div>
                </vue-seamless-scroll>
              </div>
            </div>
          </el-card>

          <el-card :class="'transparent-card'" class="box-card">
            <div slot="header" :class="'card-title'" class="clearfix">
              <span>报表总体完成情况</span>
            </div>
            <div>
              <MyPieChart :data-value="pieDataValue" :div-id="divId4" width="450px" height="300px"></MyPieChart>
            </div>
          </el-card>

        </div>
      </el-col>

      <el-col :xs="24" :sm="8" :md="8" :lg="14">
        <div class="bg-purple bg-white">
          <!--  中间报表图  -->
          <div id="chart" style="height: 700px;width: 100%;">
          </div>
          <div class="subTitle">
            武汉服务部
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="8" :md="8" :lg="5">
        <div class="bg-purple bg-white">

          <el-card :class="'transparent-card'" class="box-card">
            <div slot="header" :class="'card-title'" class="clearfix">
              <span>通知栏</span>
            </div>
            <div :class="'card-content'"class="text item">
              <!--  内容   -->
              <div class="page-example3">
                <vue-seamless-scroll :data="noticeDataList" :class-option="defaultOption" >
                  <div class="ul-scoll">
                    <div v-for="(item, index) in noticeDataList" :key='index'>
                      <span class="title">{{item.date}}：</span>
                      <span class="date">{{item.text}}</span>
                    </div>
                  </div>
                </vue-seamless-scroll>
              </div>
            </div>
          </el-card>

          <el-card :class="'transparent-card'" class="box-card">
            <div slot="header" :class="'card-title'" class="clearfix">
              <span>报表进度(后五家)</span>
            </div>
            <div :class="'card-content'" class="text item">

              <div class="div2099">
                <div class="div1001">
                  【江汉油田】
                </div>
                <div>
                  <el-progress :text-inside="true" :stroke-width="24" :percentage="48.32"></el-progress>
                </div>
              </div>

              <div class="div2099">
                <div class="div1001">
                  【湖北油田】
                </div>
                <div>
                  <el-progress :text-inside="true" :stroke-width="24" :percentage="39.21" status="success"></el-progress>
                </div>
              </div>

              <div class="div2099">
                <div class="div1001">
                  【贵州石油】
                </div>
                <div>
                  <el-progress :text-inside="true" :stroke-width="24" :percentage="27.22" status="warning"></el-progress>
                </div>
              </div>

              <div class="div2099">
                <div class="div1001">
                  【重庆石油】
                </div>
                <div>
                  <el-progress :text-inside="true" :stroke-width="24" :percentage="22.91" :color="customColor1"></el-progress>
                </div>
              </div>

              <div class="div2099">
                <div class="div1001">
                  【天津悦泰】
                </div>
                <div>
                  <el-progress :text-inside="true" :stroke-width="24" :percentage="19.22" status="exception"></el-progress>
                </div>
              </div>


            </div>
          </el-card>




        </div>
      </el-col>

    </el-row>

  </div>
</template>

<script src="../../../plugins/echarts/echarts-my.js"></script>

<script>
import echarts from "echarts";
import vueSeamlessScroll from 'vue-seamless-scroll'
import Util from "@/utils/util";
import MyPieChart from '../../dashboard/echarts/pieChart'

export default {
  components: {
    vueSeamlessScroll,
    MyPieChart,
  },
  // 监听属性 类似于data概念
  computed: {

    /** 滚动参数设置 */
    defaultOption () {
      return {
        step: 0.4, // 数值越大速度滚动越快
        limitMoveNum: 2, // 开始无缝滚动的数据量 this.dataList.length
        hoverStop: true, // 是否开启鼠标悬停stop
        direction: 1, // 0向下 1向上 2向左 3向右
        openWatch: true, // 开启数据实时监控刷新dom
        singleHeight: 0, // 单步运动停止的高度(默认值0是无缝不停止的滚动) direction => 0/1
        singleWidth: 0, // 单步运动停止的宽度(默认值0是无缝不停止的滚动) direction => 2/3
        waitTime: 1000 // 单步运动停止的时间(默认值1000ms)
      }
    }
  },
  data() {
    return {
      customColor1: '#c091df', //重庆石油，进度条颜色

      //中间数据
      nodesM: [{
        name: '报表中心'
      }],
      //上下图标数据
      nodes: [],

      //通知栏数据
      noticeDataList: [
        {
          title: '武汉',
          date: '2023-08-29',
          text: '8月起BX报表新增校验公式；',
        },
        {
          title: '武汉',
          date: '2023-08-28',
          text: '8月报表上报时间为9月2日12点前；',
        },
        {
          title: '武汉',
          date: '2023-08-20',
          text: '开展平台测试工作；',
        },
      ],

      //动态栏数据
      newsList: [
        // 新闻列表数据
        {
          title: '武汉',
          date: '2023-08-28 20:12:25',
          text: '湖北石油C1节点完成；',
        },
        {
          title: '桂林',
          date: '2023-08-28 19:30:25',
          text: '安徽石油X5节点完成，备注：清账工作已完成，请新入账同事及时沟通责任人；',
        },
        {
          title: '深圳',
          date: '2023-08-28 16:18:09',
          text: '江汉油田Z4节点完成；备注：科研经费已结转，请注意新入账科研订单；',
        },
        {
          title: '南宁',
          date: '2023-08-28 15:55:23',
          text: '江汉油田Z5节点完成; 备注：物料账结转完毕，已关闭物料账期；',
        },
        {
          title: '南宁',
          date: '2023-08-28 12:16:48',
          text: '安徽石油C2节点完成；',
        },
        {
          title: '南宁',
          date: '2023-08-28 10:12:23',
          text: '吉林石油X3节点完成；备注：坏账计提完毕，进行所得税调整；',
        },
        {
          title: '南宁',
          date: '2023-08-28 09:43:11',
          text: '河南石油Q2节点完成；备注：完成所有业务提报；',
        },
        {
          title: '南宁',
          date: '2023-08-28 08:18:55',
          text: '安徽石油Q5节点完成；备注：资金监管自动清账已关闭；',
        },
      ],

      divId4: Util.getRandomString(8),
      divId5: Util.getRandomString(8),
      pieDataValue:{
        title: '',
        seriesData: [
          {value: 3, name:'已完成'},
          {value: 11, name:'审核中'},
          {value: 6, name:'进行中'},
        ],
      },


    };
  },
  created() {
  },
  mounted() {
  },

  methods: {

    init(){
      /** echarts相关代码开始 */
      let charts = {
        nodes: [],
        links: [],
        linesData: []
      };
      var x = 0;
      var y = 1;
      let dataMap = new Map();
      var aValue = [];
      const image002 = require('@/assets/images/echarts/2.png');
      const image003 = require('@/assets/images/echarts/3.png');
      for (var j = 0; j < this.nodes.length; j++) {
        if (j % 2 == 0) { //偶数
          aValue = [x, y];
        } else {
          aValue = [x, y - 2];
          x += 2;
        }
        var node = {
          name: this.nodes[j].name,
          value: aValue,
          symbolSize: 40,
          // symbol: 'image://images/' + nodes[j].img,
          // 小节点图标
          //../../../assets/images/echarts/2.png
          symbol: 'image://' + image002,
          itemStyle: {
            normal: {
              color: '#12b5d0',
              fontSize: 12,
              fontWeight: 'normal'
            }
          }
        };
        dataMap.set(this.nodes[j].name, aValue);
        charts.nodes.push(node);
      }
      //中间数据显示
      for (var k = 0; k < this.nodesM.length; k++) {
        var node = {
          name: this.nodesM[k].name,
          value: [x / 2 + k - 1, y - 1],
          symbolSize: 100,
          // symbol: 'image://images/' + nodesM[k].img
          //中间图标
          symbol: 'image://' + image003,
        };
        dataMap.set(this.nodesM[k].name, [x / 2 + k - 1, y - 1]);
        charts.nodes.push(node)
      }

      //画线
      let labelName = '';
      for (var i = 0; i < this.nodes.length; i++) {
        //通过传输状态state 显示传输文字提示 0：暂停传输；1：传输中；2：上报完成
        if (this.nodes[i].state === '1') {
          labelName = '';//数据传输中
        } else if (this.nodes[i].state === '0') {
          labelName = '暂停传输';
        } else if (this.nodes[i].state === '2') {
          labelName = '上报完成';
        }
        var link = {
          source: this.nodes[i].name,
          target: this.nodesM[0].name,
          label: {
            normal: {
              show: true,
              formatter: labelName,
              //暂停传输的文字颜色
              color: '#ed46a2',
              fontSize: 14,
              fontWeight: 'normal'
            }
          },
          lineStyle: {
            normal: {
              //暂停传输的线条颜色
              color: '#ed46a2',
              width: .5
            }
          }
        };
        charts.links.push(link);

        //判断传输状态1 传输动效改变线条颜色
        if (this.nodes[i].state === '1') {
          link.lineStyle.normal.color = '#fffb00';//正常传输线条颜色
          link.label.normal.color = '#fffb00';//正常传输文字颜色
          var lines = [{
            coord: dataMap.get(this.nodes[i].name)
          }, {
            coord: dataMap.get(this.nodesM[0].name)
          }];
          charts.linesData.push(lines)
        }
      }

      let option = {
        title: {
          text: '月结报表数据分析平台',
          /*subtext: '武汉服务部', //副标题
          subtextStyle: {   //副标题样式
            fontSize: 20,   // 设置字体大小为14px
            color: '#ffffff'   // 设置字体颜色为灰色
          },*/
          left: 'center',//标题居中
          top: 10,//距离顶部间距
          textStyle: {
            color: '#ffffff',
            fontStyle: 'normal',//风格（可选斜体italic）
            fontWeight: 'bolder',
            fontSize: 30,
          },
        },
        grid: {
          top: 110, // 设置图表距离容器顶部的距离
        },
        //设置背景颜色
        // backgroundColor: "#0e1735",
        xAxis: {
          show: false,
          type: 'value'
        },
        yAxis: {
          show: false,
          type: 'value'
        },
        series: [{
          type: 'graph',
          layout: 'none',
          coordinateSystem: 'cartesian2d',
          symbolSize: 50,
          label: {
            normal: {
              show: true,
              position: 'bottom',
              color: '#fffb00',
              fontSize: 15,
            }
          },
          lineStyle: {
            normal: {
              shadowColor: 'none',
              opacity: 1, //尾迹线条透明度
              curveness: .1 //尾迹线条曲直度
            }

          },
          edgeSymbolSize: 8,
          data: charts.nodes,
          links: charts.links,
          itemStyle: {
            normal: {
              label: {
                show: true,
                formatter: function (item) {
                  return item.data.name
                }
              }
            }
          }
        }, {
          name: 'A',
          type: 'lines',
          coordinateSystem: 'cartesian2d',
          effect: {
            show: true,
            period: 2, //箭头指向速度，值越小速度越快
            trailLength: 0.02,
            symbol: 'pin',
            color: '#ffaa5f',
            symbolSize: 15 //点点的大小，值越大点越大

          },
          lineStyle: {
            color: '#fff',
            curveness: .1 //尾迹线条曲直度
          },
          data: charts.linesData
        }]
      };


      /*采集拓扑图 chart*/
      var dom = document.getElementById("chart");
      var myChartNM = echarts.init(dom, 'purple-passion');
      myChartNM.setOption(option);
      window.onresize = myChartNM.resize;

      //添加点击事件
      myChartNM.on('click', function (params) {
        // 弹窗打印数据的名称
        if (params.dataType == "node") {
          //跳转到指定页面，显示流程详情
          window.open('/showDraw?name=' + params.name);
        } else if (params.dataType == "edge") {
          alert("from：" + params.data.source + "=====to:" + params.data.target);
        }
      });
    },
  },
};
</script>
<style lang='scss' scoped>

.echartsHomePage {
  /* 设置背景图片 */
  background-image: url('~@/assets/images/echarts/Homebodybg.png');
  background-repeat: no-repeat;//将图片样式不重复
  background-size: 100% 100%;  //设置图片大小
  position: fixed; /* 充满全屏 */
  height: 100%; //设置div高度
  width: 100%; //设置div宽度
}

/*设置卡片的颜色*/
.transparent-card {
  //background-color: transparent;
  background-color: #06396d; /* 设置卡片背景颜色为灰色 */
  margin-top: 20px;
  margin-left: 5px;
  width: 82%;
  height: 380px;
  border: 1px solid #00a8ff; /* 设置边框为红色 */
}

/*设置卡片标题的样式*/
.card-title {
  color: #fffb00; /* 设置标题字体颜色为红色 */
}

/*设置卡片内容的样式*/
.card-content {
  color: #ffffff; /* 设置内容字体颜色为蓝色 */
}


.title{
  font-size: 17px;
  color: #ffffff;
}





/*滚动样式*/
.page-example3{
  height: 100%;
  overflow: hidden;
  .ul-scoll{
    div{
      margin: 6px;
      padding: 15px;
      background: #07325e;
    }
  }
}





.div2099{
  .div1001{
    margin-top: 13px;
  }
}




.subTitle{
  position: fixed;
  left: 53%;
  font-size: 24px;
  font-weight: bolder;
  transform: translateX(-50%);
  bottom: 80px;
  color: #d2d814;
}
</style>
