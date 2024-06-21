<template>
  <el-row :gutter="10" class="panel-group">
    <el-col :xs="24" :sm="24" :lg="6" class="card-panel-col" v-for="(list,i) in lists" :key="i">
      <div class="card-panel" :style="{background:list.color}">
        <div class="card-panel-description">
          <div class="card-panel-text">
             {{list.name}}
          </div>
          <count-to :start-val="list.startVal" :end-val="list.endVal" :duration="list.duration" class="card-panel-num" />
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to' //数字滚动插件

export default {
  data(){
    return {
    }
  },
  computed:{
    lists(){
      return [
            {startVal:0,endVal:236521,duration:4000,name:'服务学员总数量',color:'#2388c3'},
            {startVal:0,endVal:32102362,duration:4000,name:'去年总营收(元)',color:'#f60'},
            {startVal:0,endVal:52301158,duration:4000,name:'去年总营收(元)',color:'#c4bb10'},
            {startVal:0,endVal:5685214,duration:4000,name:'去年总利润(元)',color:'#184aa3'},
            // {startVal:0,endVal:8888,duration:4000,name:'销售量4',color:'#184aa3'},
            // {startVal:0,endVal:8888,duration:4000,name:'销售量3',color:'#c4bb10'},
            // {startVal:0,endVal:8888,duration:4000,name:'销售量2',color:'#f60'},
            // {startVal:0,endVal:8888,duration:4000,name:'销售量1',color:'#2388c3'},
        ]
    }
  },
  components:{
    CountTo
  }
}
</script>

