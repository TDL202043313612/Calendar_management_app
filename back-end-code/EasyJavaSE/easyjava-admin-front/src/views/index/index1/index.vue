<template>
  <!-- 网站首页页面-显示地图版本 -->
  <div class="content">
    <div class="content-row">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="24" :md="24" :lg="24">
          <div class="bg-purple bg-white">
            <panelProup />
          </div>
        </el-col>
        <!--<el-col :xs="24" :sm="8" :md="8" :lg="8" >
          <div class="bg-purple bg-white">
            <line-chart :chart-data="lineChartData" />
          </div>
        </el-col>-->
      </el-row>
    </div>

    <div class="content-row">
      <el-row :gutter="20">
        <el-col :span="20">
            <Map />
        </el-col>
      </el-row>
    </div>

    <div class="content-row">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8" :md="8" :lg="8">
          <div class="bg-purple bg-white">
            <MyPieChart :data-value="pieDataValue" :div-id="divId4"></MyPieChart>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8">
          <div class="bg-purple bg-white">
            <MyPieChart :data-value="pieDataValue" :div-id="divId5"></MyPieChart>
          </div>
        </el-col>
        <el-col :xs="24" :sm="8" :md="8" :lg="8">
          <div class="bg-purple bg-white">
            <MyPieChart :data-value="pieDataValue" :div-id="divId2"></MyPieChart>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="content-row">
      <el-row :gutter="20">
        <el-col :xs="24" :sm="8" :md="8" :lg="8" >
          <div class="bg-purple bg-white">
            <MyBarChart :data-value="barDataValue" :div-id="divId1" :width="tempWidth" :height="tempHeight"></MyBarChart>
          </div>
        </el-col>
        <el-col :xs="24" :sm="16" :md="16" :lg="16">
          <div class="bg-purple bg-white">
            <MyLineChart :data-value="lineDataValue" :div-id="divId3" :width="tempWidth" :height="tempHeight"></MyLineChart>
          </div>
        </el-col>
      </el-row>
    </div>

  </div>
</template>

<script>
  import Util from '@/utils/util';
  import panelProup from '../../dashboard/panel/panelProup'
  import BarChart from '../../dashboard/BarChart'
  import Map from '../../dashboard/Map'
  import LineChart from '../../dashboard/LineChart'
  import PieChart from '../../dashboard/PieChart'
  import RaddarChart from '../../dashboard/RaddarChart'
  import MyBarChart from '../../dashboard/echarts/barChart'
  import MyLineChart from '../../dashboard/echarts/lineChart'
  import MyPieChart from '../../dashboard/echarts/pieChart'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  data(){
    return {
      barDataValue:{
        title: '昨日产品销售额',
        xAxis: ["iphone12", "小米19", "vivo", "华为", "电磁炉", "电冰箱"],
        seriesData: [89, 45, 67, 37, 78, 23],
      },
      divId4: Util.getRandomString(8),
      divId5: Util.getRandomString(8),

      divId2: Util.getRandomString(8),
      pieDataValue:{
        title: '用户地区分布图',
        seriesData: [
          {value:10, name:'北京'},
          {value:5, name:'上海'},
          {value:15, name:'广州'},
          {value:25, name:'福建'},
          {value:20, name:'厦门'},
          {value:35, name:'武汉'},
          {value:30, name:'青岛'},
          {value:40, name:'南京'}
        ],
      },

      divId3: Util.getRandomString(8),
      lineDataValue:{
        title: '上周用户活跃度',
        xData: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        yData: [456, 985, 456, 904, 569, 109, 499],
      },

      tempWidth: '100%',
      tempHeight: '300px',
      divId1: Util.getRandomString(8),
      lineChartData: lineChartData.newVisitis
    }
  },
  methods:{
  },
  components:{
    BarChart,
    panelProup,
    Map,
    LineChart,
    PieChart,
    RaddarChart,
    MyBarChart,
    MyLineChart,
    MyPieChart,
  }
}
</script>


