<template>
  <!--
   本组件功能：
   在列表中展示图片或文件连接，可供点击
   如果是图片，就新开窗口展示图片
   否则就下载
   -->
  <el-link type="primary" @click="showPhotoDetailHandle(src)">{{text}}{{index}}</el-link>
</template>

<script>
export default {
  name: "PhotoOpen",
  props: {
    src: {
      type: String,
      default: ""
    },
    text: {
      type: String,
      default: "查看详情"
    },
    index: {
      type: Number | String,
    },
  },
  methods: {
    showPhotoDetailHandle(url){
      console.log('PhotoOpen---------url=' + url);
      var lastDotIndex = url.lastIndexOf(".");
      if (lastDotIndex !== -1) {
        let fileType = url.substr(lastDotIndex + 1);
        console.log('PhotoOpen---------fileType=' + fileType);
        let imagesType = ['png', 'jpg', 'jpeg']
        if(imagesType.indexOf(fileType) !== -1){
          //是图片格式，就在新窗口打开图片
          const image_window = window.open(url, "_blank")
          //图片在新页面打开后，高度会自动占浏览器的90%，宽度等比例缩放，完美展示
          image_window.document.write(`<!DOCTYPE html><html><body style='height: 100%;margin: 0;padding: 0;'><img style="height: 90vh;width: auto;max-width: 100%;display: block;margin: 0 auto;" src='${url}'/></body></html>`);
        }
        else{
          //不是图片格式就下载文件
          let link = document.createElement("a");
          link.href = url;
          link.download = ""; // 可以设置下载文件的默认名称
          link.style.display = "none";
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);
        }
      }

    },
  },
};
</script>

<style lang="scss" scoped>
</style>
