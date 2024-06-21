<template>
  <!--公告信息管理-->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="关键词" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入关键词"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="时间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['easySystemMsg:pagelist']" @click="handleQuery">搜索</el-button>
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
          v-hasPermi="['easySystemMsg:save']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['easySystemMsg:deleteBatch']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--列表-->
    <el-table :height="tableMaxHeight" stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
      <el-table-column type="selection" width="55" align="center" />

      <el-table-column prop="msgTitle" width="300" label="标题">
      </el-table-column>

      <el-table-column prop="msgContent" width="500" label="内容">
      </el-table-column>

      <el-table-column prop="msgLevel" :show-overflow-tooltip="true" width="150" label="级别">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.msgLevel == 1" type="danger">非常紧急</el-tag>
          <el-tag v-if="scope.row.msgLevel == 2" type="warning">紧急</el-tag>
          <el-tag v-if="scope.row.msgLevel == 3" type="success">一般</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="publishName" :show-overflow-tooltip="true" width="150" label="发布人">
      </el-table-column>

      <el-table-column prop="createTime" :show-overflow-tooltip="true" width="160" label="发布时间">
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="130">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['easySystemMsg:update']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['easySystemMsg:deleteBatch']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      :page-sizes="[5, 10, 50, 100]"
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
        <el-form ref="addFormRef" :model="addForm" :rules="rules" label-width="130px">

          <el-form-item label="级别" prop="msgLevel">
            <el-radio-group v-model="addForm.msgLevel">
              <el-radio :label="1">非常紧急</el-radio>
              <el-radio :label="2">紧急</el-radio>
              <el-radio :label="3">一般</el-radio>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="标题" prop="msgTitle">
            <el-input v-model="addForm.msgTitle" placeholder="公告标题" auto-complete="off" maxlength="50" show-word-limit></el-input>
          </el-form-item>

          <el-form-item label="内容" prop="msgContent">
            <el-input
              type="textarea"
              :rows="4"
              auto-complete="off"
              placeholder="请输入公告内容"
              maxlength="100" show-word-limit
              v-model="addForm.msgContent">
            </el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center;margin-bottom: 50px;">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="open = false">取 消</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { getEasySystemMsgTableData, deleteEasySystemMsg, addEasySystemMsg, updateEasySystemMsg, getEasySystemMsgList} from "@/api/system/easySystemMsg";

export default {
  name: "EasySystemMsg",
  data() {
    return {
      // 遮罩层
      loading: true,
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
        msgTitle: [{ required: true, message: '公告标题不能为空', trigger: 'blur' }],
        msgContent: [{ required: true, message: '公告内容不能为空', trigger: 'blur' }],
        msgLevel: [{ required: true, message: '公告级别必选', trigger: 'blur' }],
      },
    };
  },

  methods: {

    /** 查询参数列表 */
    getList() {
      this.loading = true;
      getEasySystemMsgTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
            updateEasySystemMsg(this.addForm).then(response => {
              this.open = false;
              this.getList();
            });
          } else {
            addEasySystemMsg(this.addForm).then(response => {
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
        return deleteEasySystemMsg(param);
      }).then(() => {
        this.getList();
      }).catch(() => {});
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

<style>
</style>
