<template>
  <!-- 中国地图图片 -->
  <div class="app-container">
    <div>日程管理-后台系统</div>
<!--    <img src="@/assets/images/bigmap.jpg" class="responsive-image"/>-->
  </div>
</template>

<script>
export default {
  name: "Index4",
  data() {
    return {
      // 版本号
      version: "3.8.2",
    };
  },
  methods: {
  },
};
</script>

<style scoped lang="scss">
.responsive-image {
  max-width: 100%; /* 图片最大宽度为父容器宽度的100% */
  height: auto; /* 高度自适应，保持宽高比 */
}
</style>

