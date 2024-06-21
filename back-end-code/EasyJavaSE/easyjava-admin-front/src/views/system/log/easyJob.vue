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
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
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
                >新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="success"
                        plain
                        icon="el-icon-edit"
                        size="mini"
                        :disabled="single"
                        @click="handleUpdate"
                >修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button
                        type="danger"
                        plain
                        icon="el-icon-delete"
                        size="mini"
                        :disabled="multiple"
                        @click="handleDelete"
                >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--列表-->
        <el-table stripe border v-loading="loading" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
            <el-table-column type="selection" width="55" align="center" />

            <el-table-column type="index" width="70" label="序号" fixed="left">
            </el-table-column>


            <el-table-column prop="jobName" :show-overflow-tooltip="true" width="150" label="任务名称">
            </el-table-column>

            <el-table-column prop="jobGroup" :show-overflow-tooltip="true" width="150" label="任务组名">
            </el-table-column>

            <el-table-column prop="invokeTarget" :show-overflow-tooltip="true" width="150" label="调用目标字符串">
            </el-table-column>

            <el-table-column prop="cronExpression" :show-overflow-tooltip="true" width="150" label="cron执行表达式">
            </el-table-column>

            <el-table-column prop="misfirePolicy" :show-overflow-tooltip="true" width="150" label="计划执行错误策略（1立即执行 2执行一次 3放弃执行）">
            </el-table-column>

            <el-table-column prop="concurrent" :show-overflow-tooltip="true" width="150" label="是否并发执行（0允许 1禁止）">
            </el-table-column>

            <el-table-column prop="status" :show-overflow-tooltip="true" width="150" label="状态（0正常 1暂停）">
            </el-table-column>

            <el-table-column prop="createUserId" :show-overflow-tooltip="true" width="150" label="创建者ID">
            </el-table-column>

            <el-table-column prop="createTime" :show-overflow-tooltip="true" width="150" label="创建时间">
            </el-table-column>

            <el-table-column prop="updateUserId" :show-overflow-tooltip="true" width="150" label="更新者">
            </el-table-column>

            <el-table-column prop="updateTime" :show-overflow-tooltip="true" width="150" label="更新时间">
            </el-table-column>

            <el-table-column prop="remark" :show-overflow-tooltip="true" width="150" label="备注信息">
            </el-table-column>

            <el-table-column prop="createUserName" :show-overflow-tooltip="true" label="创建人" width="90">
            </el-table-column>

            <el-table-column prop="createTime" :show-overflow-tooltip="true" label="创建时间" width="150">
            </el-table-column>

            <el-table-column prop="updateUserName" :show-overflow-tooltip="true" label="更新人" width="90">
            </el-table-column>

            <el-table-column prop="updateTime" :show-overflow-tooltip="true" label="更新时间" width="150">
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleUpdate(scope.row)"
                    >修改</el-button>
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
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

                <el-form-item label="任务名称" prop="jobName">
                    <el-input v-model="form.jobName" placeholder="请输入任务名称" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="任务组名" prop="jobGroup">
                    <el-input v-model="form.jobGroup" placeholder="请输入任务组名" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="调用目标字符串" prop="invokeTarget">
                    <el-input v-model="form.invokeTarget" placeholder="请输入调用目标字符串" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="cron执行表达式" prop="cronExpression">
                    <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="计划执行错误策略（1立即执行 2执行一次 3放弃执行）" prop="misfirePolicy">
                    <el-input v-model="form.misfirePolicy" placeholder="请输入计划执行错误策略（1立即执行 2执行一次 3放弃执行）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="是否并发执行（0允许 1禁止）" prop="concurrent">
                    <el-input v-model="form.concurrent" placeholder="请输入是否并发执行（0允许 1禁止）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="状态（0正常 1暂停）" prop="status">
                    <el-input v-model="form.status" placeholder="请输入状态（0正常 1暂停）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="创建者ID" prop="createUserId">
                    <el-input v-model="form.createUserId" placeholder="请输入创建者ID" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="创建时间" prop="createTime">
                    <el-input v-model="form.createTime" placeholder="请输入创建时间" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="更新者" prop="updateUserId">
                    <el-input v-model="form.updateUserId" placeholder="请输入更新者" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="更新时间" prop="updateTime">
                    <el-input v-model="form.updateTime" placeholder="请输入更新时间" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="备注信息" prop="remark">
                    <el-input v-model="form.remark" placeholder="请输入备注信息" auto-complete="off" maxlength="50" show-word-limit></el-input>
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
    import { getTableData, deleteObject, addObject, updateObject} from "@/api/system/easyJob";

    export default {
        name: "EasyJob",
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
                    jobId: [{ required: true, message: '主键ID不能为空', trigger: 'blur' }],
                    jobName: [{ required: true, message: '任务名称不能为空', trigger: 'blur' }],
                    jobGroup: [{ required: true, message: '任务组名不能为空', trigger: 'blur' }],
                    invokeTarget: [{ required: true, message: '调用目标字符串不能为空', trigger: 'blur' }],
                    cronExpression: [{ required: true, message: 'cron执行表达式不能为空', trigger: 'blur' }],
                    misfirePolicy: [{ required: true, message: '计划执行错误策略（1立即执行 2执行一次 3放弃执行）不能为空', trigger: 'blur' }],
                    concurrent: [{ required: true, message: '是否并发执行（0允许 1禁止）不能为空', trigger: 'blur' }],
                    status: [{ required: true, message: '状态（0正常 1暂停）不能为空', trigger: 'blur' }],
                    createUserId: [{ required: true, message: '创建者ID不能为空', trigger: 'blur' }],
                    createTime: [{ required: true, message: '创建时间不能为空', trigger: 'blur' }],
                    updateUserId: [{ required: true, message: '更新者不能为空', trigger: 'blur' }],
                    updateTime: [{ required: true, message: '更新时间不能为空', trigger: 'blur' }],
                    remark: [{ required: true, message: '备注信息不能为空', trigger: 'blur' }],
                },
            };
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
                    jobId: undefined,
                    jobName: undefined,
                    jobGroup: undefined,
                    invokeTarget: undefined,
                    cronExpression: undefined,
                    misfirePolicy: undefined,
                    concurrent: undefined,
                    status: undefined,
                    createUserId: undefined,
                    createTime: undefined,
                    updateUserId: undefined,
                    updateTime: undefined,
                    remark: undefined,
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
                this.ids = selection.map(item => item.id);//将数组里面的ID取出来形成ids集合
                this.single = selection.length!=1;
                this.multiple = !selection.length;
            },
            // 取消多选框选中的记录
            cancelSelectionChange() {
                this.$refs.objectTable.clearSelection();
            },
            /** 修改按钮操作 */
            handleUpdate(row) {
                this.reset();
                const id = row.id || this.ids;
                getConfig(id).then(response => {
                    this.form = response.data;
                    this.open = true;
                });
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
