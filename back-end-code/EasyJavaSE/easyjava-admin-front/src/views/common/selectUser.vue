<template>
  <div>
    <div id="divTree">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-input
            placeholder="输入关键字进行过滤"
            :clearable=true
            v-model="filterText">
          </el-input>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="info"
            plain
            icon="el-icon-sort"
            @click="toggleExpandAll"
          >展开/折叠
          </el-button>
        </el-col>
      </el-row>

      <el-tree
          v-if="refreshUserTree"
          ref="tree"
          :data="data"
          show-checkbox
          node-key="id"
          row-key="id"
          :default-expand-all="isExpandAll"
          :default-checked-keys="checkedData"
          highlight-current
          :filter-node-method="filterNode"
          :props="defaultProps"
      >
      <!--重点：给节点添加图标-->
        <span slot-scope="{ node, data }" class="slot-t-node">
       <template>
         <i
           :class="{
             'el-icon-folder': !node.expanded,       // 节点收缩时的图标
             'el-icon-folder-opened': node.expanded, // 节点展开时的图标
             'el-icon-user-solid': data.type == 1   // data.type是后端配合提供的识别字段，最后一级
           }"
           style="color: #409eff;"
         />
         <span>{{ node.label }}</span>
       </template>
     </span>
      </el-tree>
    </div>
    <div id="divButtons">
      <el-button type="primary" @click="getCheckedNodes">确定</el-button>
      <el-button @click="resetChecked">重置</el-button>
    </div>
  </div>
</template>

<script>
import {getDeptUserTree} from "@/api/system/user";

export default {
  name: 'SelectUser',
  data() {
    return {
      filterText: '',
      // 是否展开，默认全部折叠
      isExpandAll: false,
      refreshUserTree: true, //v-if设置这个属性，重新渲染
      checkedData: [],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    //根据关键字过滤节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.isExpandAll = !this.isExpandAll;
      // tree为Tree组件的ref值，isExpandAll 为true或false
      this.$nextTick(() => {
        for(var i=0;i<this.$refs.tree.store._getAllNodes().length;i++){
          this.$refs.tree.store._getAllNodes()[i].expanded=this.isExpandAll;
        }
      });

    },
    //初始化默认选中
    dataInitialization(stype, ids) {
      //请求后台接口
      getDeptUserTree().then(res => {
        let result = res.data;
        this.data = result;
        // 根据主页面传来的id值，进行默认选中设置
        if (ids) {
          let dataIntArr = [];
          ids.forEach(function(data, index, arr) {
            dataIntArr.push(+data);
          })
          this.checkedData = dataIntArr;
        }
      }).catch(function(error){
        //请求失败，处理业务逻辑
        console.log(error);
      });
    },
    //确定选择
    getCheckedNodes() {
      this.checkChange(this.$refs.tree.getCheckedNodes())
    },
    checkChange(List) {
      let ids = '';
      let names = '';
      List.forEach(function(item) {
        //对选中的数据进行过滤，部门的数据过滤掉，type=1（员工）的数据进行id和name的json重构，传给主页面
        if (item.type === 1) {
          ids += item.id + ','
          names += item.label + ','
        }
      })
      if (ids.length > 0) {
        ids = ids.slice(0, ids.length - 1)
        names = names.slice(0, names.length - 1)
      }
      const returnValue = {
        ids: [],
        names: '',
      }
      returnValue.ids = ids.split(",");//选择的用户ID封装成集合返回
      returnValue.names = names;//选择的用户名称封装成字符串返回
      this.$emit('listen-checked', returnValue)
    },
    //重置按钮
    resetChecked() {
      this.$refs.tree.setCheckedKeys([])
    }
  }
}
</script>

<style scoped>
#divTree {
  height:500px;
  width:100%;
  z-index:5;
  top:0;
  padding-bottom: 10px;
}
#divButtons {
  width:100%;
  position:absolute;
  z-index:10;
  bottom:30px;
  text-align: right;
  right: 25px;
}
</style>
