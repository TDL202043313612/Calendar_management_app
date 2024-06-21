<template>
    <!-- 操作日志 -->
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
                <el-button type="primary" icon="el-icon-search" size="mini" v-hasPermi="['easyOperLog:pagelist']" @click="handleQuery">搜索</el-button>
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
                        v-hasPermi="['easyOperLog:deleteBatch']"
                >删除</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <!--列表-->
        <el-table stripe border v-loading="loading" :height="tableMaxHeight" :data="tableDataList" @selection-change="handleSelectionChange" ref="objectTable">
            <el-table-column type="selection" width="55" align="center" />

            <el-table-column prop="title" :show-overflow-tooltip="true" width="300" label="操作描述" fixed="left">
            </el-table-column>

            <el-table-column prop="createUserName" :show-overflow-tooltip="true" width="100" label="操作人">
            </el-table-column>

            <el-table-column prop="businessType" :show-overflow-tooltip="true" width="100" label="操作类型">
              <template slot-scope="scope">
                <!--业务类型（0其它 1新增 2修改 3删除 4查询）-->
                <el-tag v-if="scope.row.businessType == 0">其他</el-tag>
                <el-tag v-if="scope.row.businessType == 1" type="success">新增</el-tag>
                <el-tag v-if="scope.row.businessType == 2" type="info">修改</el-tag>
                <el-tag v-if="scope.row.businessType == 3" type="danger">删除</el-tag>
                <el-tag v-if="scope.row.businessType == 4" type="warning">查询</el-tag>
                <el-tag v-if="scope.row.businessType == 6" type="info">导出</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="requestMethod" :show-overflow-tooltip="true" width="100" label="请求方式">
            </el-table-column>

            <el-table-column prop="operatorType" :show-overflow-tooltip="true" width="100" label="用户类型">
              <template slot-scope="scope">
                <!--操作类别（0其它 1后台用户 2门户用户）-->
                <el-tag v-if="scope.row.operatorType == 0">其他</el-tag>
                <el-tag v-if="scope.row.operatorType == 1" type="success">后台用户</el-tag>
                <el-tag v-if="scope.row.operatorType == 2" type="danger">门户用户</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="operTime" :show-overflow-tooltip="true" width="160" label="操作时间" sortable>
            </el-table-column>

            <el-table-column prop="operUrl" :show-overflow-tooltip="true" width="210" label="请求URL">
            </el-table-column>

            <el-table-column prop="operIp" :show-overflow-tooltip="true" width="150" label="IP地址">
            </el-table-column>

            <el-table-column prop="operLocation" :show-overflow-tooltip="true" width="150" label="操作地点">
            </el-table-column>

            <el-table-column prop="method" :show-overflow-tooltip="true" width="500" label="操作方法">
            </el-table-column>

            <el-table-column prop="operParam" :show-overflow-tooltip="true" width="360" label="请求参数">
            </el-table-column>

            <el-table-column prop="jsonResult" :show-overflow-tooltip="true" width="460" label="返回参数">
            </el-table-column>

            <el-table-column prop="status" :show-overflow-tooltip="true" width="90" label="操作状态">
              <template slot-scope="scope">
                <!--操作状态（0正常 1异常）-->
                <el-tag v-if="scope.row.status == 0" type="success">正常</el-tag>
                <el-tag v-if="scope.row.status == 1" type="danger">异常</el-tag>
              </template>
            </el-table-column>

            <el-table-column prop="errorMsg" :show-overflow-tooltip="true" width="150" label="错误消息">
            </el-table-column>

            <el-table-column prop="takeTime" :show-overflow-tooltip="true" width="110" label="执行耗时(毫秒)">
            </el-table-column>

            <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120" fixed="right">
                <template slot-scope="scope">
                    <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleDelete(scope.row)"
                            v-hasPermi="['easyOperLog:deleteBatch']"
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

                <el-form-item label="模块标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入模块标题" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="业务类型（0其它 1新增 2修改 3删除 4查询）" prop="businessType">
                    <el-input v-model="form.businessType" placeholder="请输入业务类型（0其它 1新增 2修改 3删除 4查询）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="方法名称" prop="method">
                    <el-input v-model="form.method" placeholder="请输入方法名称" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="请求方式" prop="requestMethod">
                    <el-input v-model="form.requestMethod" placeholder="请输入请求方式" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作类别（0其它 1后台用户 2手机端用户）" prop="operatorType">
                    <el-input v-model="form.operatorType" placeholder="请输入操作类别（0其它 1后台用户 2手机端用户）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作人ID" prop="operUserId">
                    <el-input v-model="form.operUserId" placeholder="请输入操作人ID" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="部门名称" prop="deptName">
                    <el-input v-model="form.deptName" placeholder="请输入部门名称" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="请求URL" prop="operUrl">
                    <el-input v-model="form.operUrl" placeholder="请输入请求URL" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="主机地址" prop="operIp">
                    <el-input v-model="form.operIp" placeholder="请输入主机地址" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作地点" prop="operLocation">
                    <el-input v-model="form.operLocation" placeholder="请输入操作地点" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="请求参数" prop="operParam">
                    <el-input v-model="form.operParam" placeholder="请输入请求参数" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="返回参数" prop="jsonResult">
                    <el-input v-model="form.jsonResult" placeholder="请输入返回参数" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作状态（0正常 1异常）" prop="status">
                    <el-input v-model="form.status" placeholder="请输入操作状态（0正常 1异常）" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="错误消息" prop="errorMsg">
                    <el-input v-model="form.errorMsg" placeholder="请输入错误消息" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="操作时间" prop="operTime">
                    <el-input v-model="form.operTime" placeholder="请输入操作时间" auto-complete="off" maxlength="50" show-word-limit></el-input>
                </el-form-item>

                <el-form-item label="方法执行耗时" prop="takeTime">
                    <el-input v-model="form.takeTime" placeholder="请输入方法执行耗时" auto-complete="off" maxlength="50" show-word-limit></el-input>
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
    import { getTableData, deleteObject, addObject, updateObject} from "@/api/system/easyOperLog";

    export default {
        name: "EasyOperLog",
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
                    id: [{ required: true, message: '日志主键不能为空', trigger: 'blur' }],
                    title: [{ required: true, message: '模块标题不能为空', trigger: 'blur' }],
                    businessType: [{ required: true, message: '业务类型（0其它 1新增 2修改 3删除 4查询）不能为空', trigger: 'blur' }],
                    method: [{ required: true, message: '方法名称不能为空', trigger: 'blur' }],
                    requestMethod: [{ required: true, message: '请求方式不能为空', trigger: 'blur' }],
                    operatorType: [{ required: true, message: '操作类别（0其它 1后台用户 2手机端用户）不能为空', trigger: 'blur' }],
                    operUserId: [{ required: true, message: '操作人ID不能为空', trigger: 'blur' }],
                    deptName: [{ required: true, message: '部门名称不能为空', trigger: 'blur' }],
                    operUrl: [{ required: true, message: '请求URL不能为空', trigger: 'blur' }],
                    operIp: [{ required: true, message: '主机地址不能为空', trigger: 'blur' }],
                    operLocation: [{ required: true, message: '操作地点不能为空', trigger: 'blur' }],
                    operParam: [{ required: true, message: '请求参数不能为空', trigger: 'blur' }],
                    jsonResult: [{ required: true, message: '返回参数不能为空', trigger: 'blur' }],
                    status: [{ required: true, message: '操作状态（0正常 1异常）不能为空', trigger: 'blur' }],
                    errorMsg: [{ required: true, message: '错误消息不能为空', trigger: 'blur' }],
                    operTime: [{ required: true, message: '操作时间不能为空', trigger: 'blur' }],
                    takeTime: [{ required: true, message: '方法执行耗时不能为空', trigger: 'blur' }],
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
                    title: undefined,
                    businessType: undefined,
                    method: undefined,
                    requestMethod: undefined,
                    operatorType: undefined,
                    operUserId: undefined,
                    deptName: undefined,
                    operUrl: undefined,
                    operIp: undefined,
                    operLocation: undefined,
                    operParam: undefined,
                    jsonResult: undefined,
                    status: undefined,
                    errorMsg: undefined,
                    operTime: undefined,
                    takeTime: undefined,
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
