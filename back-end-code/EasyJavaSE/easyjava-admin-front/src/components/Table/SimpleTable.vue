<template>
  <div class="page-table">
    <el-table :data="list" style="width: 100%">
      <el-table-column
        v-for="(item, index) in column"
        :key="index"
        :label="item.label"
        :prop="item.prop"
      >
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
  export default {
    props: {
      list: {
        type: Array,
        default: () => {},
      },
      column: {
        type: Array,
        default: () => {},
      },
    },
  };
</script>

<style scoped lang="scss">
</style>
