<!--
  柱状图公用组件
-->
<template>
  <div :id="divId" :style="style"></div>
</template>

<script>
import Util from '@/utils/util';
import echarts from 'echarts'

export default {
  name: "MyBarChart",
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
        trigger: "item",
        formatter: "{a} <br/>{b} : {c}万元"
      },
      xAxis: {
        type: "category",
        data: this.dataValue.xAxis,
        // data: ["衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子"],
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
          show: true
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
          type: 'line',
          data: this.dataValue.seriesData
        },
        {
          name: this.dataValue.title,
          data: this.dataValue.seriesData,
          type: "bar",
          symbol: "triangle",
          symbolSize: 20,
          lineStyle: {
            normal: {
              color: "green",
              width: 4,
              type: "dashed"
            }
          },
          markPoint: {
            data: [
              { type: 'max', name: '最大值' },
              { type: 'min', name: '最小值' }
            ],
          },
          markLine: {
            data: [{ type: 'average', name: '平均值' }]
          },
          barWidth: 30,
          itemStyle: {
            normal: {
              barBorderRadius: 30
            }
          },
        }
      ]
    };
    this.myChart.setOption(option);
  },
};
</script>
