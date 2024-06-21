<template>
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
      <el-form-item label="时间区间">
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
        <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['easyResourceLink:pagelist']" @click="handleQuery">搜索</el-button>
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
            v-hasPermi="['easyResourceLink:save']"
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
            v-hasPermi="['easyResourceLink:deleteBatch']"
        >删除</el-button>
      </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!--列表-->
    <el-table :height="tableMaxHeight" stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
      <el-table-column type="selection" width="55" align="center" />

        <el-table-column prop="resName" :show-overflow-tooltip="true" width="160" label="资源名称">
        </el-table-column>

        <el-table-column prop="resLink" :show-overflow-tooltip="true" width="160" label="资源链接地址">
        </el-table-column>

        <el-table-column prop="resIntro" :show-overflow-tooltip="true" width="160" label="资源简介描述">
        </el-table-column>

        <el-table-column prop="status" :show-overflow-tooltip="true" width="160" label="资源状态">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status == 0" type="success">正常</el-tag>
            <el-tag v-if="scope.row.status == 1" type="danger">停用</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="clickCount" :show-overflow-tooltip="true" width="160" label="点击量">
        </el-table-column>

        <el-table-column prop="createUserName" :show-overflow-tooltip="true" width="160" label="创建人姓名">
        </el-table-column>

        <el-table-column prop="createTime" :show-overflow-tooltip="true" width="160" label="创建时间">
        </el-table-column>

        <el-table-column prop="updateUserName" :show-overflow-tooltip="true" width="160" label="更新人姓名">
        </el-table-column>

        <el-table-column prop="updateTime" :show-overflow-tooltip="true" width="160" label="更新时间">
        </el-table-column>

        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" min-width="130">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                            v-hasPermi="['easyResourceLink:update']"
                    >修改</el-button>
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['easyResourceLink:deleteBatch']"
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
      <el-form ref="addFormRef" :model="addForm" :rules="rules" label-width="150px">

        <el-form-item label="资源名称" prop="resName">
          <el-input v-model="addForm.resName" placeholder="资源名称" auto-complete="off" maxlength="80" show-word-limit style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="资源链接地址" prop="resLink">
          <el-input v-model="addForm.resLink" placeholder="资源链接地址" auto-complete="off" maxlength="255" show-word-limit style="width: 100%;"></el-input>
        </el-form-item>

        <el-form-item label="资源简介描述" prop="resIntro">
          <el-input
            type="textarea"
            :rows="3"
            placeholder="请输入资源简介描述"
            maxlength="240" show-word-limit
            v-model="addForm.resIntro">
          </el-input>
        </el-form-item>

        <el-form-item label="资源状态" prop="status">
          <el-radio-group v-model="addForm.status">
            <el-radio :label="0">正常</el-radio>
            <el-radio :label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="资源图片" prop="photoPath">
          <ImageUpload v-model="addForm.photoPath"
                       uploadUrl="/file/uploadImageCommon"
                       :limit=1
                       :fileSize=2
                       :isShowTip=true
                       :fileType='["jpg", "png"]'></ImageUpload>
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
  import { getEasyResourceLinkTableData, deleteEasyResourceLink, addEasyResourceLink, updateEasyResourceLink, getEasyResourceLinkList} from "@/api/system/easyResourceLink";

  export default {
    name: "EasyResourceLink",
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
        resName: [{ required: true, message: '资源名称不能为空', trigger: 'blur' }],
        resIntro: [{ required: true, message: '资源简介描述不能为空', trigger: 'blur' }],
        resLink: [{ required: true, message: '资源链接地址不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '资源状态（0：正常；1：停用）不能为空', trigger: 'blur' }],
      },
    };
  },

  methods: {
      /** 查询参数列表 */
      getList() {
        this.loading = true;
        getEasyResourceLinkTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
                    updateEasyResourceLink(this.addForm).then(response => {
                        this.open = false;
                        this.getList();
                    });
                } else {
                    addEasyResourceLink(this.addForm).then(response => {
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
            return deleteEasyResourceLink(param);
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
