<template>
  <!-- 我的消息 -->
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="12" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>系统消息</span>
          </div>
          <div>
            <!-- 时间线 -->
            <el-timeline style="height:100%">
              <el-timeline-item
                v-for="(msg, index) in xiaoxiList"
                :key="index"
                :color="msg.msgStatus == '1' ? '#0bbd87' : '#c1bfbf'"
                :timestamp="msg.createTime">
                <a class="aStyle" @click="showMessageDetail1(msg)">
                  {{msg.title}}
                </a>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>系统公告</span>
          </div>
          <div>
            <!-- 时间线 -->
            <el-timeline style="height:100%">
              <el-timeline-item
                v-for="(msg, index) in gonggaoList"
                :key="index"
                :color="'#c1bfbf'"
                :timestamp="msg.createTime">
                <a class="aStyle" @click="showMessageDetail2(msg)">
                  {{msg.msgTitle}}
                </a>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      title="系统消息详情"
      :visible.sync="dialog1"
      width="40%">
      <!--   显示消息详情   -->
      <el-form label-position="right" label-width="100px" :model="messageForm1">
        <el-form-item label="发布人：">
          {{messageForm1.createUserName}}
        </el-form-item>
        <el-form-item label="发送时间：">
          {{messageForm1.createTime}}
        </el-form-item>
        <el-form-item label="消息标题：">
          {{messageForm1.title}}
        </el-form-item>
        <el-form-item label="消息内容：">
          {{messageForm1.msgContent}}
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="sure1">确定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="系统公告详情"
      :visible.sync="dialog2"
      width="40%">
      <!--   显示消息详情   -->
      <el-form label-position="right" label-width="100px" :model="messageForm2">
        <el-form-item label="发布人：">
          {{messageForm2.publishName}}
        </el-form-item>
        <el-form-item label="发送时间：">
          {{messageForm2.createTime}}
        </el-form-item>
        <el-form-item label="消息标题：">
          {{messageForm2.msgTitle}}
        </el-form-item>
        <el-form-item label="消息内容：">
          {{messageForm2.msgContent}}
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialog2 = false">确定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { getAllMessage, updateMessageStatus } from "@/api/system/easyMessage";

export default {
  name: "myMessage",
  data() {
    return {
      xiaoxiList:[],
      gonggaoList:[],
      dialog1: false, //系统消息弹框是否显示
      dialog2: false, //系统公告弹框是否显示
      messageForm1: [],
      messageForm2: [],
    };
  },

  created() {
    this.getAllMsg();
  },

  methods: {
    sure1(){
      this.getAllMsg();
      this.dialog1 = false;
    },

    getAllMsg(){
      //查询登录人的消息总数量
      getAllMessage().then(response => {
        this.xiaoxiList = response.data.xiaoxi;
        this.gonggaoList = response.data.gonggao;
      });
    },

    //改变系统消息状态
    updateMessageStatus(id){
      updateMessageStatus(id).then(response => {
        this.getAllMsg();
      });
    },

    //查看系统消息详情
    showMessageDetail1(obj){
      this.dialog1 = true;
      this.messageForm1 = obj;
      this.updateMessageStatus(obj.id);
    },

    //查看系统公告详情
    showMessageDetail2(obj){
      this.dialog2 = true;
      this.messageForm2 = obj;
    },
  }
};
</script>

<style>
.el-timeline{
  margin-left: -40px;
}

.el-card__header{
  background-color: #edcb75;
}

.clearfix{
  color: #000000;
  font-weight: bold;
  font-size: 22px;
  font-family: "Microsoft YaHei";
}

.el-timeline-item__content{
  font-size: 16px;
}

.aStyle{
  text-decoration: underline;
}
</style>
