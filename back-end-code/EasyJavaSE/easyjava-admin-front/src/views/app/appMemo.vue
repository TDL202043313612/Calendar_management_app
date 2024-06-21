<template>
  <!-- 备忘录管理 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="85px">

      <el-form-item label="关键词" prop="keyword">
        <el-input
            v-model="queryParams.keyword"
            placeholder="请输入标题"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="创建时间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['appMemo:pagelist']" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['appMemo:save']"
        >新增</el-button>
      </el-col>-->
      <el-col :span="1.5">
        <el-tooltip :disabled="!multiple" content="请先勾选需要删除的记录！" placement="top">
          <span>
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['appMemo:deleteBatch']"
            >删除{{ids.length > 0 ? '：' + ids.length : ''}}</el-button>
          </span>
        </el-tooltip>
      </el-col>
      <el-col :span="1.5">
        <el-tooltip :disabled="!multiple" content="请先勾选需要导出的记录！" placement="top">
          <span>
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              :disabled="multiple"
              @click="handleExportExcel"
              v-hasPermi="['appMemo:exportExcelFile']"
            >导出{{ids.length > 0 ? '：' + ids.length : ''}}</el-button>
          </span>
        </el-tooltip>
      </el-col>
    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--
    横条提示
    <el-alert type="success" :closable="true" style="margin-bottom: 10px;">
      这里是横条提示
    </el-alert>
    -->

    <!--列表-->
    <el-table :height="tableMaxHeight" :cell-style="rowStyle" stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column type="index" label="序号" width="50">
      </el-table-column>

      <el-table-column prop="memoTitle" label="备忘录标题" :show-overflow-tooltip="true" width="300">
      </el-table-column>

      <el-table-column prop="memoContent" label="备忘录内容" :show-overflow-tooltip="true" width="600">
      </el-table-column>

      <el-table-column prop="createUserName" label="创建人" :show-overflow-tooltip="true" width="200">
      </el-table-column>

      <el-table-column prop="createTime" label="创建时间" :show-overflow-tooltip="true" width="180">
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120" fixed="right">
        <template slot-scope="scope">
<!--          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['appMemo:update']"
          >修改</el-button>-->
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['appMemo:deleteBatch']"
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
      :page-sizes="pageSizeArray"
      @pagination="getList"
    />

    <!-- [添加/修改]弹框界面 -->
    <el-drawer
      :title="addForm.id ? '修改数据' : '新增数据'"
      :visible.sync="open"
      :close-on-click-modal="false"
      size="60%"
    >
      <div class="bobo_dialog_div">

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

          <el-form-item label="提交人ID" prop="createUserId">
            <el-input v-model="addForm.createUserId" placeholder="提交人ID" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="提交人" prop="createUserName">
            <el-input v-model="addForm.createUserName" placeholder="提交人" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="提交时间" prop="createTime">
            <el-input v-model="addForm.createTime" placeholder="提交时间" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="更新人ID" prop="updateUserId">
            <el-input v-model="addForm.updateUserId" placeholder="更新人ID" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="更新人姓名" prop="updateUserName">
            <el-input v-model="addForm.updateUserName" placeholder="更新人姓名" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="更新时间" prop="updateTime">
            <el-input v-model="addForm.updateTime" placeholder="更新时间" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="备忘录标题" prop="memoTitle">
            <el-input v-model="addForm.memoTitle" placeholder="备忘录标题" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="备忘录内容" prop="memoContent">
            <el-input v-model="addForm.memoContent" placeholder="备忘录内容" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

        </el-form>
      </div>
      <div style="text-align: center;margin-top: 10px;">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="open = false">取 消</el-button>
      </div>
    </el-drawer>

    </div>
</template>

<script>

import {
  getAppMemoTableData,
  deleteAppMemo,
  addAppMemo,
  updateAppMemo,
  getAppMemoList,
  getDataById,
} from "@/api/app/appMemo";

export default {
  name: "AppMemo",
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
        createUserId: [{ required: true, message: '提交人ID不能为空', trigger: 'blur' }],
        createUserName: [{ required: true, message: '提交人不能为空', trigger: 'blur' }],
        createTime: [{ required: true, message: '提交时间不能为空', trigger: 'blur' }],
        updateUserId: [{ required: true, message: '更新人ID不能为空', trigger: 'blur' }],
        updateUserName: [{ required: true, message: '更新人姓名不能为空', trigger: 'blur' }],
        updateTime: [{ required: true, message: '更新时间不能为空', trigger: 'blur' }],
        memoTitle: [{ required: true, message: '备忘录标题不能为空', trigger: 'blur' }],
        memoContent: [{ required: true, message: '备忘录内容不能为空', trigger: 'blur' }],
      },
    };
  },

  methods: {

    /** 导出Excel */
    handleExportExcel() {
      this.queryParams = {
        "ids": this.ids,
      }
      this.download('appMemo/exportExcelFile', {
        ...this.queryParams
      }, 'export_excel_' + this.getCurrentTimeString() + '.xlsx')
    },

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      getAppMemoTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
              updateAppMemo(this.addForm).then(response => {
                this.open = false;
                this.getList();
              });
            } else {
              addAppMemo(this.addForm).then(response => {
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
        return deleteAppMemo(param);
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
