<!--
  折线图公用组件
-->
<template>
  <div :id="divId" :style="style"></div>
</template>

<script>
  import Util from '@/utils/util';
  import echarts from 'echarts'

export default {
  name: "MyLineChart",
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
  mounted() {
    this.myChart = echarts.init(document.getElementById(this.divId));

    const option = {
      title: Object.assign({}, Util.defaultEchartsOpt.title, {text: this.dataValue.title}),
      tooltip: {
        trigger: "axis",
        formatter: "{a} <br/>{b} : {c}人"
      },
      xAxis: {
        type: "category",
        data: this.dataValue.xData,
        boundaryGap: false,
        splitLine: {
          show: false,
          interval: 'auto',
          lineStyle: {
            color: ['#D4DFF5']
          }
        },
        axisLine: {
          lineStyle: {
            color: '#999'
          }
        },
        axisLabel: {
          margin: 10,
          textStyle: {
            fontSize: 12
          }
        }
      },
      yAxis: {
        type: "value",
        splitLine: {
          lineStyle: {
            color: ['#D4DFF5']
          }
        },
        axisTick: {
          show: false
        },
        axisLine: {
          lineStyle: {
            color: '#999'
          }
        },
        axisLabel: {
          margin: 10,
          textStyle: {
            fontSize: 12
          }
        }
      },
      series: [
        {
          name: this.dataValue.title,
          data: this.dataValue.yData,
          type: "line",
          smooth: true,
          showSymbol: false,
          symbol: 'circle',
          symbolSize: 6,
          areaStyle: {
            normal: {
              color: echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                offset: 0,
                color: 'rgba(199, 237, 250, 0.5)'
              }, {
                offset: 1,
                color: 'rgba(199, 237, 250, 0.2)'
              }], false)
            }
          },
          itemStyle: {
            normal: {
              color: 'rgba(32, 254, 255, 0.5)'
            }
          },
          lineStyle: {
            normal: {
              width: 2
            }
          }
        }
      ]
    };
    this.myChart.setOption(option);
  },
};
</script>
