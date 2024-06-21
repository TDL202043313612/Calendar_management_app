<!--
  饼状图公用组件
-->
<template>
  <div :id="divId" :style="style"></div>
</template>

<script>
  import Util from '@/utils/util';
  import echarts from 'echarts'

export default {
  name: "myPieChart",
  // 自定义echarts的宽高
  props: {
    height: {
      type: String,
      default: "350px",
    },
    width: {
      type: String,
      default: "560px",
    },
    dataValue: {
      type: Object,
      default: null,
    },
    divId: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      uuid: null, //自定义uuid
      myChart: null,
    };
  },
  // style的计算属性，当宽高改变的时候，style发生相应改变
  computed: {
    style() {
      return {
        height: this.height,
        width: this.width,
      };
    },
  },

  methods: {

    //数据初始化
    dataInit(){
      this.myChart = echarts.init(document.getElementById(this.divId));
      //option参数
      let option = {
        title: Object.assign({}, Util.defaultEchartsOpt.title, {text: this.dataValue.title}),
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c}({d}%)"
        },
        color: ["#af84cb", "#3acaa9", "#ebcc6f", "#67c4ed", "rgba(32, 254, 255, 0.5)"],
        series: [
          {
            name: this.dataValue.title,
            type: "pie",
            radius: "80%", //值越大，饼状图直径越大
            center: ["33%", "50%"],
            data: this.dataValue.seriesData,
            // roseType: "area",//有层次感的饼状图
            label: {
              show: true,
              position: 'inside', // 通过设置位置为 inside，在饼状图的扇形区域内显示文字
              formatter: '{b}:{c}' //'{b}:{c} ({d}%)' 可以自定义显示格式，{b} 表示数据项名称，{c} 表示数据项值，{d} 表示百分比
              // normal: {
                // position: 'outseide', // 在内部显示，outseide 是在外部显示
                // show: true,
                // formatter: '{b}({d}%)', // 设置标签内容格式，{b} 表示名称，{d} 表示占比
                // textStyle: {
                //   color: "#fd0000"
                // }
              // }
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '18',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              normal: {
                lineStyle: {
                  color: "#888"
                },
                smooth: 0.2,
                length: 10,
                length2: 20
              }
            },
            itemStyle: {
              normal: {
                shadowBlur: 200,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            },
            animationType: "scale",
            animationEasing: "elasticOut",
            animationDelay: function(idx) {
              return Math.random() * 200;
            }
          }
        ]
      };
      this.myChart.setOption(option);
      //设置高亮轮播
      this.handleChartLoop(option, this.myChart)
    },


    // 饼图自动轮播
    handleChartLoop(option, myChart) {
      if (!myChart) {
        return
      }
      let currentIndex = -1 // 当前高亮图形在饼图数据中的下标
      let changePieInterval = setInterval(selectPie, 1200) // 设置自动切换高亮图形的定时器

      // 取消所有高亮并高亮当前图形
      function highlightPie() {
        // 遍历饼图数据，取消所有图形的高亮效果
        for (var idx in option.series[0].data) {
          myChart.dispatchAction({
            type: 'downplay',
            seriesIndex: 0,
            dataIndex: idx
          })
        }
        // 高亮当前图形
        myChart.dispatchAction({
          type: 'highlight',
          seriesIndex: 0,
          dataIndex: currentIndex
        })
      }

      // 用户鼠标悬浮到某一图形时，停止自动切换并高亮鼠标悬浮的图形
      myChart.on('mouseover', (params) => {
        clearInterval(changePieInterval)
        currentIndex = params.dataIndex
        highlightPie()
      })

      // 用户鼠标移出时，重新开始自动切换
      myChart.on('mouseout', (params) => {
        if (changePieInterval) {
          clearInterval(changePieInterval)
        }
        changePieInterval = setInterval(selectPie, 1500)
      })

      // 高亮效果切换到下一个图形
      function selectPie() {
        var dataLen = option.series[0].data.length
        currentIndex = (currentIndex + 1) % dataLen
        highlightPie()
      }
    },


  },

  mounted() {
    this.dataInit();
  },
};
</script>
