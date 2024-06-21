import Vue from 'vue'

import Cookies from 'js-cookie'

import Element from 'element-ui'
import './assets/styles/element-variables.scss'

import '@/assets/styles/index.scss' // global css
import '@/assets/styles/ruoyi.scss' // ruoyi css
import App from './App'
import store from './store'
import router from './router'
import directive from './directive' // directive
import plugins from './plugins' // plugins
import {download} from '@/utils/request'

import './assets/icons' // icon
import './permission' // permission control

import {Notification} from "element-ui";
import {MessageBox} from "element-ui";

import {getDicts} from "@/api/system/dict/data";
import {getConfigKey} from "@/api/system/config";
import {parseTime, resetForm, addDateRange, selectDictLabel, selectDictLabels, handleTree} from "@/utils/ruoyi";
// 分页组件
import Pagination from "@/components/Pagination";
// 自定义表格工具组件
import RightToolbar from "@/components/RightToolbar"
// 富文本组件
import Editor from "@/components/Editor"
// 文件上传组件
import FileUpload from "@/components/FileUpload"
// 文件导入组件
import FileImport from '@/components/FileImport'
// 图片上传组件
import ImageUpload from "@/components/ImageUpload"
// 图片预览组件
import ImagePreview from "@/components/ImagePreview"
// 图片预览组件
import ImagePreviewBase64 from "@/components/ImagePreviewBase64"
// 字典标签组件
import DictTag from '@/components/DictTag'
// 头部标签组件
import VueMeta from 'vue-meta'
// 图片新窗口打开组件
import PhotoOpen from "@/components/PhotoOpen"
// 字典数据组件
import DictData from '@/components/DictData'

import {webSocketInit, webSocketDisconnect} from "@/plugins/websocket";

//自定义的全局js文件
import {
  getCurrentTimeString,
} from "@/plugins/global";

import {getDeptUserTreeSelect, getExtraInfo} from "@/api/system/user";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

import SuperFlow from 'vue-super-flow'
import 'vue-super-flow/lib/index.css'

webSocketInit();

import echarts from "echarts"
Vue.prototype.$echarts = echarts;


//引入less
import less from 'less';
//使用less
Vue.use(less);


/** 时间区间选择 **/
Vue.prototype.pickerOptions = {
  shortcuts: [{
    text: '最近一周',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '最近一个月',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '最近三个月',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
      picker.$emit('pick', [start, end]);
    }
  }, {
    text: '最近一年',
    onClick(picker) {
      const end = new Date();
      const start = new Date();
      start.setTime(start.getTime() - 3600 * 1000 * 24 * 365);
      picker.$emit('pick', [start, end]);
    }
  }]
},

  /** 获取额外信息 */
  getExtraInfo().then(response => {
    Vue.prototype.extra = response.data;
  });

  /** 查询部门人员下拉树结构 */
  getDeptUserTreeSelect().then(response => {
    Vue.prototype.deptUserTreeData = response.data;
  });

// 全局方法挂载
Vue.prototype.getDicts = getDicts
Vue.prototype.getConfigKey = getConfigKey
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.selectDictLabel = selectDictLabel
Vue.prototype.selectDictLabels = selectDictLabels
Vue.prototype.download = download
Vue.prototype.webSocketInit = webSocketInit
Vue.prototype.webSocketDisconnect = webSocketDisconnect
Vue.prototype.Notification = Notification
Vue.prototype.MessageBox = MessageBox
Vue.prototype.handleTree = handleTree
Vue.prototype.PhotoOpen = PhotoOpen

Vue.prototype.pageSizeArray = [10, 50, 100, 300, 500] //所有页面的页码大小集合

//全局js方法
Vue.prototype.getCurrentTimeString = getCurrentTimeString


// 全局组件挂载
Vue.component('DictTag', DictTag)
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)
Vue.component('Editor', Editor)
Vue.component('FileUpload', FileUpload)
Vue.component('FileImport', FileImport)
Vue.component('ImageUpload', ImageUpload)
Vue.component('ImagePreview', ImagePreview)
Vue.component('ImagePreviewBase64', ImagePreviewBase64)
Vue.component('Treeselect', Treeselect)

Vue.use(directive)
Vue.use(plugins)
Vue.use(VueMeta)
Vue.use(SuperFlow)
DictData.install()

Vue.use(Element, {
  size: Cookies.get('size') || 'medium' // set element-ui default size
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
