<template>
  <section>
    <!--工具条-->
    <div class="app-container">
      <el-form :inline="true">
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['socket:getOnlineUserIdList']" @click="getOnlineUserIdList">搜索</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!--列表-->
    <!--
    border：是否带有纵向边框
    stripe：是否为斑马纹 table
    highlight-current-row：是否要高亮当前行
    @selection-change：当选择项发生变化时会触发该事件
    @row-click：当某一行被点击时会触发该事件
    -->
    <el-table size="small" :height="tableMaxHeight" border stripe highlight-current-row ref="multipleTable" @row-click="rowClick" :cell-style="cellStyle" :data="tableDatas"
              @selection-change="handleSelectionChange"
              style="width: 100%;margin-left: 10px;">
      <!--多选框-->
      <el-table-column align="center" type="selection" width="55">
      </el-table-column>
      <!--索引值,为什么不用id,id不序号-->
      <el-table-column type="index" label="序号" width="55" fixed="left">
      </el-table-column>
      <el-table-column prop="userName" label="登录账号" width="180">
      </el-table-column>
      <el-table-column prop="nickName" label="姓名" width="180">
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="180">
      </el-table-column>
      <el-table-column prop="email" label="邮箱" width="180">
      </el-table-column>
      <el-table-column prop="loginIp" label="登录IP" width="180">
      </el-table-column>
      <el-table-column prop="loginDate" label="登录时间" width="180">
      </el-table-column>
      <el-table-column align="center" label="操作" fixed="right" min-width="80">
        <template scope="scope">
          <el-button type="danger" size="mini" v-hasPermi="['socket:forceLogout']" @click="forceLogout(scope.row)">下线</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--工具条-->
    <el-col :span="24" class="toolbar">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-sizes="[5, 10, 50, 100, 500, 1000]"
          :page-size="size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalSize"
          style="float:right;">
      </el-pagination>
    </el-col>

  </section>
</template>

<script>
import {getOnlineUserIdList, forceLogout} from "@/api/monitor/online";

export default {
  data() {
    return {
      dialogTitle: "新增",
      row: "",
      sels: [], //存储多选记录的ID集合
      addFormVisible: false,
      addForm: {
        content: ''
      },
      totalSize: 0,//总记录条数
      current: 1,//当前页,要传递到后台的
      size: 10, //每页显示多少条
      tableDatas: [] //当前页数据
    }
  },
  methods: {
    //根据状态不同显示不同颜色字体
    cellStyle(row, column, rowIndex, columnIndex) {
      if (row.row.status == '1' && row.column.label === "状态") {
        return 'color:red'
      }
    },
    //表格多条数据选中事件
    handleSelectionChange(val) {
      /**
       * val中存放的是所有已选择的行数据集合，这里需要先清空，再赋值，否则会出现重复数据
       */
      this.sels = [];
      if (val != null && val.length > 0) {
        for (var i = 0; i < val.length; i++) {
          //往sels数组中存值
          this.sels.push(val[i].id);
        }
      }
    },
    //强制用户下线
    forceLogout(row) {
      this.$confirm('确定将用户： [' + row.nickName + '] 强制下线吗？', '请确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //请求后台接口
        forceLogout(row.userId).then(res => {
        }).catch(function(error){
          //请求失败，处理业务逻辑
          console.log(error);
        });
      }).catch(() => {
        console.log("用户已取消操作");
      });
    },
    //选择第几页时触发
    handleCurrentChange(curentPage) {
      this.current = curentPage;
      this.getOnlineUserIdList();
    },
    //选择每页显示记录条数
    handleSizeChange(curentPage) {
      this.size = curentPage
      this.getOnlineUserIdList();
    },
    //获取列表数据
    getOnlineUserIdList() {
      let param = {
        "current": this.current,
        "size": this.size,
      };
      //请求后台接口
      getOnlineUserIdList(param).then(res => {
        this.totalSize = res.data.total;
        this.tableDatas = res.data.rows;
      }).catch(function(error){
        //请求失败，处理业务逻辑
        console.log(error);
      });
    },
    //单击行事件
    rowClick(row) {
    },
  },
  computed: {
    tableMaxHeight() {
      return window.innerHeight - 270 + 'px'
    }
  },
  //钩子函数
  mounted() {
    this.getOnlineUserIdList();
    this.webSocketInit();
  }
}
</script>
<style scoped>
</style>
