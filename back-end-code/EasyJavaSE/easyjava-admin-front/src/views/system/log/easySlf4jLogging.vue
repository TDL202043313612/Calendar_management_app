<template>
  <!-- Slf4j日志 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="85px">

      <el-form-item label="全局跟踪ID" prop="trackId">
        <el-input
            v-model="queryParams.trackId"
            placeholder="请输入全局跟踪ID"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="类名" prop="className">
        <el-input
            v-model="queryParams.className"
            placeholder="请输入类名"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="日志级别" prop="logLevel">
          <el-select v-model="queryParams.logLevel" filterable clearable placeholder="请选择日志级别" style="width: 100%;">
              <el-option label="INFO" value="INFO"></el-option>
              <el-option label="ERROR" value="ERROR"></el-option>
              <el-option label="WARN" value="WARN"></el-option>
              <el-option label="DEBUG" value="DEBUG"></el-option>
          </el-select>
      </el-form-item>

      <el-form-item label="日志时间">
        <el-date-picker
            v-model="dateRange"
            style="width: 340px"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetimerange"
            :picker-options="this.pickerOptions"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['easySlf4jLogging:pagelist']" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>

    <el-row :gutter="10" class="mb8">
<!--      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['easySlf4jLogging:save']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['easySlf4jLogging:deleteBatch']"
        >删除</el-button>
      </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--列表-->
    <el-table :height="tableMaxHeight" :cell-style="rowStyle" stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column type="index" width="50" label="序号" fixed="left">
      </el-table-column>

      <el-table-column prop="trackId" width="270" label="全局跟踪ID">
      </el-table-column>

      <el-table-column prop="logLevel" :show-overflow-tooltip="true" width="100" label="日志级别">
          <template slot-scope="scope">
              <el-tag v-if="scope.row.logLevel == 'DEBUG'">DEBUG</el-tag>
              <el-tag v-if="scope.row.logLevel == 'WARN'" type="info">WARN</el-tag>
              <el-tag v-if="scope.row.logLevel == 'INFO'" type="success">INFO</el-tag>
              <el-tag v-if="scope.row.logLevel == 'ERROR'" type="danger">ERROR</el-tag>
          </template>
      </el-table-column>

      <el-table-column prop="logTime" :show-overflow-tooltip="true" width="160" label="日志时间">
      </el-table-column>

      <el-table-column prop="logClass" width="500" label="日志类路径">
      </el-table-column>

      <el-table-column prop="logThread" width="160" label="日志线程">
      </el-table-column>

      <el-table-column prop="logContent" width="800" label="日志内容">
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="130">
        <template slot-scope="scope">
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['easySlf4jLogging:update']"
          >修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['easySlf4jLogging:deleteBatch']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--分页-->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      :page-sizes="[5, 10, 50, 100, 500]"
      @pagination="getList"
    />

    <!-- [添加/修改]弹框界面 -->
    <el-drawer
      :title="addForm.id ? '修改' : '新增'"
      :visible.sync="open"
      direction="rtl"
      size="50%"
    >
      <div style="width: 90%;margin-left: 50px;">

        <!-- 表单头部的提示信息 -->
        <!--
        <el-card class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix">
            <span>提示：</span>
          </div>
          <div>
            <p>这里是提示信息</p>
          </div>
        </el-card>
        -->

        <!-- 表单 -->
        <el-form ref="addFormRef" :model="addForm" :rules="rules" label-width="150px">

          <el-form-item label="日志级别" prop="logLevel">
            <el-input v-model="addForm.logLevel" placeholder="日志级别" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志内容" prop="logContent">
            <el-input v-model="addForm.logContent" placeholder="日志内容" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志时间" prop="logTime">
            <el-input v-model="addForm.logTime" placeholder="日志时间" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志方法" prop="logMethod">
            <el-input v-model="addForm.logMethod" placeholder="日志方法" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志类路径" prop="logClass">
            <el-input v-model="addForm.logClass" placeholder="日志类路径" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志行数" prop="logLine">
            <el-input v-model="addForm.logLine" placeholder="日志行数" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="日志线程" prop="logThread">
            <el-input v-model="addForm.logThread" placeholder="日志线程" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center;margin-bottom: 50px;margin-top: 20px;">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </div>
    </el-drawer>

    </div>
</template>

<script>
  import {
    getEasySlf4jLoggingTableData,
    deleteEasySlf4jLogging,
    addEasySlf4jLogging,
    updateEasySlf4jLogging,
    getEasySlf4jLoggingList
  } from "@/api/system/easySlf4jLogging";

  export default {
    name: "EasySlf4jLogging",
    data() {
    return {
      // 遮罩层
      loading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 参数表格数据
      tableDataList: [],
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        keyword: undefined,
      },
      // 表单参数
      addForm: {},
      // 表单校验
      rules: {
        id: [{ required: true, message: '主键ID不能为空', trigger: 'blur' }],
        logLevel: [{ required: true, message: '日志级别不能为空', trigger: 'blur' }],
        logContent: [{ required: true, message: '日志内容不能为空', trigger: 'blur' }],
        logTime: [{ required: true, message: '日志时间不能为空', trigger: 'blur' }],
        logMethod: [{ required: true, message: '日志方法不能为空', trigger: 'blur' }],
        logClass: [{ required: true, message: '日志类路径不能为空', trigger: 'blur' }],
        logLine: [{ required: true, message: '日志行数不能为空', trigger: 'blur' }],
        logThread: [{ required: true, message: '日志线程不能为空', trigger: 'blur' }],
      },
    };
  },

  methods: {

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      getEasySlf4jLoggingTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.tableDataList = response.data.rows;
        this.total = response.data.total;
        this.loading = false;
      }
      );
    },

    /** 点击 搜索 按钮 */
    handleQuery() {
      this.queryParams.current = 1;
      this.getList();
    },

    /** 点击 重置 按钮 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 点击 新增 按钮 */
    handleAdd() {
      this.addForm = Object.assign({}, {});
      this.open = true;
    },

    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);//将数组里面的ID取出来形成ids集合
      this.single = selection.length!=1;
      this.multiple = !selection.length;
    },

    /** 取消多选框选中的记录 */
    cancelSelectionChange() {
      this.$refs.objectTable.clearSelection();
    },

    /** 点击 修改 按钮 */
    handleUpdate(row) {
      this.addForm = Object.assign({}, row);
      this.open = true;
    },

    /** 提交按钮 */
    submitForm: function() {
      this.$refs["addFormRef"].validate(valid => {
          if (valid) {
            if (this.addForm.id != undefined) {
              updateEasySlf4jLogging(this.addForm).then(response => {
                this.open = false;
                this.getList();
              });
            } else {
              addEasySlf4jLogging(this.addForm).then(response => {
                this.open = false;
                this.getList();
              });
            }
          }
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      if(row.id){
        this.cancelSelectionChange();//取消复选框
        this.ids.push(row.id);
      }
      const tempIds = this.ids;
      let tishi = '';
      if(tempIds.length == 0){
        tishi = '您确定要删除这条数据吗';
      }else{
        tishi = '您确定要删除这【' + tempIds.length + '】条数据吗？';
      }
      this.$modal.confirm(tishi).then(function() {
        let param = {
            "ids": tempIds,
        }
        return deleteEasySlf4jLogging(param);
      }).then(() => {
        this.getList();
      }).catch((e) => {
        console.log('发生异常：');
        console.log(e);
      });
    },

    //设置行样式
    rowStyle({ row, column, rowIndex, columnIndex }){
      /*
      if(column.label == '填表人'){
        return {
          background: '#eff1e3', //设置背景颜色
          color: '#fd0000' //设置文字颜色
        }
      }
      if(row.backgroundColor == 1){
        return {
          background: '#eff1e3' //设置背景颜色
        }
      }
      */
    },

  },

  computed: {
    tableMaxHeight() {
      return window.innerHeight - 270 + 'px'
    }
  },

  created() {
    this.getList();
  },
};
</script>

<style lang="scss">
</style>
