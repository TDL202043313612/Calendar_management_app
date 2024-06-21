<template>
  <el-dialog title="数据导入操作" :visible.sync="open" width="400px" append-to-body>
    <el-upload
      ref="upload"
      :limit="1"
      accept=".xlsx, .xls"
      :headers="upload.headers"
      :action="upload.url"
      :on-progress="handleFileUploadProgress"
      :on-success="handleFileSuccess"
      :auto-upload="false"
      drag
    >
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip text-center" slot="tip">
        <span>仅允许导入xls、xlsx格式文件。</span>
        <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">
          下载模板
        </el-link>
      </div>
    </el-upload>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitFileForm">确 定</el-button>
      <el-button @click="open = false">取 消</el-button>
    </div>
  </el-dialog>
</template>

<script>
  import {getToken} from "@/utils/auth";

  export default {
    name: "fileimport",
    props: {
      // 值
      value: [String, Object, Array],
      uploadUrl: {
        type: String
      },
      downloadUrl: {
        type: String
      },
      open: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        upload: {
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + this.uploadUrl
        }
      }
    },
    methods: {
      /** 下载模板操作 */
      importTemplate() {
        this.download(this.downloadUrl, {}, `user_template_${new Date().getTime()}.xlsx`)
      },
      // 文件上传中处理
      handleFileUploadProgress(event, file, fileList) {
        this.upload.isUploading = true;
      },
      // 文件上传成功处理
      handleFileSuccess(response, file, fileList) {
        this.open = false;
        this.upload.isUploading = false;
        this.$refs.upload.clearFiles();
        this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.message + "</div>", "导入结果", {dangerouslyUseHTMLString: true});
      },
      submitFileForm() {
        this.$refs.upload.submit();
        this.open = false
      }
    }
  }
</script>

<style scoped>

</style>
