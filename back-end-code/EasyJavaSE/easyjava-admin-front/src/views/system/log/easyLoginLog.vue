<template>
    <!-- 登录日志 -->
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
                <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['easyLoginLog:pagelist']" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                        v-hasPermi="['easyLoginLog:deleteBatch']"
                >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--列表-->
        <el-table stripe border v-loading="loading" :height="tableMaxHeight" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
            <el-table-column type="selection" width="55" align="center" />

            <el-table-column prop="userName" :show-overflow-tooltip="true" label="登录人" width="160">
            </el-table-column>

            <el-table-column prop="ipaddr" :show-overflow-tooltip="true" width="150" label="登录IP地址">
            </el-table-column>

            <el-table-column prop="loginLocation" :show-overflow-tooltip="true" width="150" label="登录地点">
            </el-table-column>

            <el-table-column prop="browser" :show-overflow-tooltip="true" width="150" label="浏览器类型">
            </el-table-column>

            <el-table-column prop="os" :show-overflow-tooltip="true" width="150" label="操作系统">
            </el-table-column>

            <el-table-column prop="msg" :show-overflow-tooltip="true" width="150" label="描述">
            </el-table-column>

            <el-table-column prop="loginTime" :show-overflow-tooltip="true" width="160" label="登录时间" sortable>
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['easyLoginLog:deleteBatch']"
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
        <el-dialog :title="form.id ? '修改' : '新增'" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="130px">

                <el-form-item label="用户ID" prop="userId">
                    <el-input v-model="form.userId" placeholder="请输入用户ID" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="登录IP地址" prop="ipaddr">
                    <el-input v-model="form.ipaddr" placeholder="请输入登录IP地址" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="登录地点" prop="loginLocation">
                    <el-input v-model="form.loginLocation" placeholder="请输入登录地点" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="浏览器类型" prop="browser">
                    <el-input v-model="form.browser" placeholder="请输入浏览器类型" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作系统" prop="os">
                    <el-input v-model="form.os" placeholder="请输入操作系统" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="提示消息" prop="msg">
                    <el-input v-model="form.msg" placeholder="请输入提示消息" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="访问时间" prop="loginTime">
                    <el-input v-model="form.loginTime" placeholder="请输入访问时间" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import { getTableData, deleteObject, addObject, updateObject} from "@/api/system/easyLoginLog";

    export default {
        name: "EasyLoginLog",
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
                form: {},
                // 表单校验
                rules: {
                    id: [{ required: true, message: '访问ID不能为空', trigger: 'blur' }],
                    userId: [{ required: true, message: '用户ID不能为空', trigger: 'blur' }],
                    ipaddr: [{ required: true, message: '登录IP地址不能为空', trigger: 'blur' }],
                    loginLocation: [{ required: true, message: '登录地点不能为空', trigger: 'blur' }],
                    browser: [{ required: true, message: '浏览器类型不能为空', trigger: 'blur' }],
                    os: [{ required: true, message: '操作系统不能为空', trigger: 'blur' }],
                    msg: [{ required: true, message: '提示消息不能为空', trigger: 'blur' }],
                    loginTime: [{ required: true, message: '访问时间不能为空', trigger: 'blur' }],
                },
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
                    userId: undefined,
                    ipaddr: undefined,
                    loginLocation: undefined,
                    browser: undefined,
                    os: undefined,
                    msg: undefined,
                    loginTime: undefined,
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
            // 多选框选中数据
            handleSelectionChange(selection) {
                this.ids = selection.map(item => item.id);//将数组里面的ID取出来形成ids集合
                this.single = selection.length!=1;
                this.multiple = !selection.length;
            },
            // 取消多选框选中的记录
            cancelSelectionChange() {
                this.$refs.objectTable.clearSelection();
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

            /** 删除按钮操作 */
            handleDelete(row) {
                if(row.id){
                    this.cancelSelectionChange();//取消复选框
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
