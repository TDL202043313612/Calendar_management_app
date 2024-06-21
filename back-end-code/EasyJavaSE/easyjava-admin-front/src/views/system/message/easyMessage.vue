<template>
    <!--系统消息-->
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
            <el-form-item label="发布时间">
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
                <el-button type="primary" icon="el-icon-search" v-hasPermi="['easyMessage:pagelist']" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-s-promotion"
                        size="mini"
                        @click="sendMsg"
                        v-hasPermi="['socket:pushMessageToAllUser']"
                >广播实时消息</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="primary"
                        plain
                        icon="el-icon-plus"
                        size="mini"
                        @click="handleAdd"
                        v-hasPermi="['easyMessage:add']"
                >发布消息</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['easyMessage:deleteBatch']"
                >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--列表-->
        <el-table :height="tableMaxHeight" stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
            <el-table-column type="selection" width="55" align="center" />

            <el-table-column prop="receiveUserName" :show-overflow-tooltip="true" fixed="left" width="150" label="接收人">
            </el-table-column>

            <el-table-column prop="msgStatus" :show-overflow-tooltip="true" width="150" label="消息状态">
              <template slot-scope="scope">
                <!--消息状态（0已读 1未读）-->
                <el-tag v-if="scope.row.msgStatus == '0'" type="success">已读</el-tag>
                <el-tag v-if="scope.row.msgStatus == '1'" type="danger">未读</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="title" width="200" label="消息标题">
            </el-table-column>

            <el-table-column prop="msgContent" width="300" label="消息内容">
            </el-table-column>

            <el-table-column prop="createUserName" :show-overflow-tooltip="true" label="发布人" width="130">
            </el-table-column>

            <el-table-column prop="createTime" :show-overflow-tooltip="true" label="发布时间" width="160" sortable>
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="100">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['easyMessage:deleteBatch']"
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

        <el-dialog width="30%" :visible.sync="userSelectVisible" title="选择用户" append-to-body>
          <selectUser v-if="userSelectVisible" ref="selectUser" @listen-checked="listenBackValue" />
        </el-dialog>

        <!-- [添加/修改]弹框界面 -->
      <el-drawer
        :title="form.id ? '修改' : '新增'"
        :visible.sync="open"
        direction="rtl"
        size="50%"
      >
        <div style="width: 90%;margin-left: 50px;">
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">

                <el-form-item label="接收人" prop="receiveUserName">
                    <el-input v-model="form.receiveUserName" @focus="openDialog" placeholder="请选择接收人" auto-complete="off"></el-input>
                </el-form-item>

                <el-form-item label="消息标题" prop="title">
                  <el-input v-model="form.title" placeholder="消息标题" auto-complete="off" maxlength="100" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="消息内容" prop="msgContent">
                  <el-input
                    type="textarea"
                    :rows="4"
                    auto-complete="off"
                    placeholder="请输入消息内容"
                    maxlength="200" show-word-limit
                    v-model="form.msgContent">
                  </el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer" style="text-align: center;margin-bottom: 50px;">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </div>
      </el-drawer>

        <!--群发实时消息弹框-->
        <!--
        close-on-click-modal：是否可以通过点击 modal 关闭 Dialog
        -->
        <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
            <el-form :model="addForm" label-width="80px" ref="addForm">

                <el-form-item label="消息内容" prop="content">
                    <el-input
                        type="textarea"
                        :rows="5"
                        v-model="addForm.content">
                    </el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click.native="publishMessage">提交</el-button>
            </div>
        </el-dialog>

    </div>
</template>

<script>
    import selectUser from '../../common/selectUser.vue';
    import { getTableData, deleteObject, addObject, updateObject} from "@/api/system/easyMessage";

    import {pushMessageToAllUser} from "@/api/monitor/online";
    export default {
        name: "EasyMessage",
        components: {
          //部门人员选择
          selectUser,
        },
        data() {
            return {
                // 遮罩层
                loading: true,
                dialogVisible: false,
              userSelectVisible: false,//部门用户树
              id: '',//部门用户树已选择的ID集合
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
                addForm: {},
                // 查询参数
                queryParams: {
                    current: 1,
                    size: 10,
                    keyword: undefined,
                },
                // 表单参数
                form: {},
                // 表单校验
                rules: {
                    msgContent: [{ required: true, message: '消息内容不能为空', trigger: 'blur' }],
                },
                dialogTitle: '',
            };
        },
      computed: {
        tableMaxHeight() {
          return window.innerHeight - 270 + 'px'
        }
      },

        created() {
            this.getList();
        },

        methods: {

            //群发消息
            publishMessage() {
                //请求后台接口
                pushMessageToAllUser(this.addForm).then(response => {
                    this.dialogVisible = false;
                    }
                );
            },

            //群发消息弹框
            sendMsg() {
                this.dialogTitle = "群发消息";
                this.dialogVisible = true;//显示编辑框
            },

          openDialog() {
            this.userSelectVisible = true
            this.$nextTick(() => {
              this.$refs.selectUser.dataInitialization(1, this.form.receiveUserIds);
            })
          },

          // 用来接收弹出页面（选择用户组件）回传的值
          listenBackValue(data) {
            this.form.receiveUserIds = data.ids;
            this.form.receiveUserName = data.names;
            this.userSelectVisible = false;
          },

            /** 查询参数列表 */
            getList() {
                this.loading = true;
                getTableData(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
                  this.tableDataList = response.data.rows;
                  this.total = response.data.total;
                  this.loading = false;
                    }
                );
            },
            // 取消按钮
            cancel() {
                this.open = false;
                this.reset();
            },
            // 表单重置
            reset() {
                this.form = {
                    id: undefined,
                    remark: undefined,
                    receiveUserId: undefined,
                    msgStatus: undefined,
                    msgContent: undefined,
                };
                this.resetForm("form");
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.queryParams.current = 1;
                this.getList();
            },
            /** 重置按钮操作 */
            resetQuery() {
                this.dateRange = [];
                this.resetForm("queryForm");
                this.handleQuery();
            },
            /** 新增按钮操作 */
            handleAdd() {
                this.reset();
                this.open = true;
            },
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id);
                this.single = selection.length!=1;
                this.multiple = !selection.length;
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                this.open = true;
                this.form = Object.assign({}, row);
            },
            /** 提交按钮 */
            submitForm: function() {
                this.$refs["form"].validate(valid => {
                    if (valid) {
                        if (this.form.id != undefined) {
                            updateObject(this.form).then(response => {
                                this.open = false;
                                this.getList();
                            });
                        } else {
                            addObject(this.form).then(response => {
                                this.open = false;
                                this.getList();
                            });
                        }
                    }
                });
            },

            // 取消多选框选中的记录
            cancelSelectionChange() {
              this.$refs.objectTable.clearSelection();
            },

            /** 删除按钮操作 */
            handleDelete(row) {
                if(row.id){
                    this.cancelSelectionChange();
                    this.ids.push(row.id);
                }
                const tempIds = this.ids;
                this.$modal.confirm('您确定要删除这【' + tempIds.length + '】条数据吗？').then(function() {
                    let param = {
                        "ids": tempIds,
                    }
                    return deleteObject(param);
                }).then(() => {
                    this.getList();
                }).catch(() => {});
            },

        }
    };
</script>
