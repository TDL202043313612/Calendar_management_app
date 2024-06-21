<template>
  <!-- APP用户管理 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="85px">

      <el-form-item label="关键词" prop="keyword">
        <el-input
            v-model="queryParams.keyword"
            placeholder="登录账号/昵称/手机号"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="提交时间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['communityUser:pagelist']" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>

    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['communityUser:save']"
        >新增</el-button>
      </el-col>
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
              v-hasPermi="['communityUser:deleteBatch']"
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
              v-hasPermi="['communityUser:exportExcelFile']"
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

      <el-table-column type="index" label="序号" width="50" fixed="left">
      </el-table-column>

      <el-table-column prop="loginName" label="登录账号" :show-overflow-tooltip="true" width="100" fixed="left">
      </el-table-column>

      <el-table-column prop="loginPass" label="登录密码" :show-overflow-tooltip="true" width="120">
      </el-table-column>

      <el-table-column prop="nickName" label="昵称" :show-overflow-tooltip="true" width="100">
      </el-table-column>

      <el-table-column prop="userImg" label="用户头像" :show-overflow-tooltip="true" width="130">
        <template slot-scope="scope">
          <ImagePreviewBase64 v-show="scope.row.userImg != null && scope.row.userImg != ''" width="70" :height="70" :src="scope.row.userImg"></ImagePreviewBase64>
        </template>
      </el-table-column>

      <el-table-column prop="userSex" label="性别" :show-overflow-tooltip="true" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.userSex == 0" type="success">男</el-tag>
          <el-tag v-if="scope.row.userSex == 1" type="danger">女</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="userPhone" label="手机号" :show-overflow-tooltip="true" width="110">
      </el-table-column>

      <el-table-column prop="userMoney" label="账户余额" :show-overflow-tooltip="true" width="130">
      </el-table-column>

      <el-table-column prop="userStatus" label="状态" :show-overflow-tooltip="true" width="100">
        <template slot-scope="scope">
          <el-alert v-if="scope.row.userStatus == 1" center :closable=false title="正常" type="success" effect="dark">
          </el-alert>
          <el-alert v-if="scope.row.userStatus == 2" center :closable=false title="限制登录" type="error" effect="dark">
          </el-alert>
        </template>
      </el-table-column>

      <el-table-column prop="userSchool" label="毕业院校" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="userMajor" label="专业" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="userBrief" label="个人简介" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="lastLoginTime" label="最后一次登录时间" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="registerTime" label="注册时间" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="userError" label="限制登录原因" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="createUserName" label="提交人" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column prop="createTime" label="提交时间" :show-overflow-tooltip="true" width="160">
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['communityUser:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['communityUser:deleteBatch']"
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

          <el-form-item label="用户状态" prop="userStatus">
            <el-radio-group v-model="addForm.userStatus" border>
              <el-radio-button :label=1>正常</el-radio-button>
              <el-radio-button :label=2>限制登录</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="限制登录原因" prop="userError">
            <el-input v-model="addForm.userError" placeholder="限制登录原因" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="性别" prop="userSex">
            <el-radio-group v-model="addForm.userSex" border>
              <el-radio-button :label=0>男</el-radio-button>
              <el-radio-button :label=1>女</el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="登录账号" prop="loginName">
            <el-input v-model="addForm.loginName" placeholder="登录账号" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="登录密码" prop="loginPass">
            <el-input v-model="addForm.loginPass" placeholder="登录密码" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="昵称" prop="nickName">
            <el-input v-model="addForm.nickName" placeholder="昵称" clearable auto-complete="off" maxlength="20" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="用户手机号" prop="userPhone">
            <el-input v-model="addForm.userPhone" placeholder="用户手机号" clearable auto-complete="off" maxlength="11" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="账户余额" prop="userMoney">
            <el-input-number v-model="addForm.userMoney" :min="0" :max="99999999" :step="1" :precision="2" style="width: 100%"></el-input-number>
          </el-form-item>

          <el-form-item label="最后一次登录时间" prop="lastLoginTime">
            <el-date-picker v-model="addForm.lastLoginTime" placeholder="请选择" type="datetime" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
          </el-form-item>

          <el-form-item label="注册时间" prop="registerTime">
            <el-date-picker v-model="addForm.registerTime" placeholder="请选择" type="datetime" style="width: 100%" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
          </el-form-item>

          <el-form-item label="毕业院校" prop="userSchool">
            <el-input v-model="addForm.userSchool" placeholder="毕业院校" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="专业" prop="userMajor">
            <el-input v-model="addForm.userMajor" placeholder="专业" clearable auto-complete="off" maxlength="50" show-word-limit style="width: 100%;"></el-input>
          </el-form-item>

          <el-form-item label="个人简介" prop="userBrief">
            <el-input v-model="addForm.userBrief" type="textarea" :rows="4" placeholder="请输入备注" maxlength="100" show-word-limit></el-input>
          </el-form-item>

          <el-form-item label="用户头像" prop="userImg">
            <ImageUpload v-model="addForm.userImg"
                         uploadUrl="/file/uploadImageCommon"
                         :limit=1
                         :fileSize=5
                         :isShowTip=true
                         :fileType='["jpg", "png", "jpeg"]'></ImageUpload>
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
  getCommunityUserTableData,
  deleteCommunityUser,
  addCommunityUser,
  updateCommunityUser,
  getCommunityUserList,
  getDataById,
} from "@/api/community/appUser";

export default {
  name: "CommunityUser",
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
        loginName: [{ required: true, message: '登录账号不能为空', trigger: 'blur' }],
        loginPass: [{ required: true, message: '登录密码不能为空', trigger: 'blur' }],
        nickName: [{ required: true, message: '昵称不能为空', trigger: 'blur' }],
      },
    };
  },

  methods: {

    /** 导出Excel */
    handleExportExcel() {
      this.queryParams = {
        "ids": this.ids,
      }
      this.download('appUser/exportExcelFile', {
        ...this.queryParams
      }, 'export_excel_' + this.getCurrentTimeString() + '.xlsx')
    },

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      getCommunityUserTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
      this.addForm = {
        userSex: 0,
        userStatus: 1,
      }
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
              updateCommunityUser(this.addForm).then(response => {
                this.open = false;
                this.getList();
              });
            } else {
              addCommunityUser(this.addForm).then(response => {
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
        return deleteCommunityUser(param);
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
