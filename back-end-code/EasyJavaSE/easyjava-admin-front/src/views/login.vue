<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
      <h3 class="title">日程管理后台管理系统</h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          maxlength="20"
          placeholder="请输入登录账号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          maxlength="20"
          placeholder="请输入登录密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaOnOff">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="请输入右侧图片验证码内容"
          maxlength="5"
          style="width: 67%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <!--<el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="danger"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <!--<div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">学员注册</router-link>
        </div>-->
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2020-{{currentYear}}
        <a style="text-decoration: underline" @click="linkMe = true">bobo</a>
        All Rights Reserved.</span>
    </div>

    <el-dialog
      title="联系开发人员"
      :visible.sync="linkMe"
      width="30%">
      <div style="text-align: center;">
        <span style="color:#ff4949;">
           扫码下方二维码，联系作者
        </span>
      </div>
      <div style="text-align: center;">
        <img src="../assets/images/wjb_weixin.jpg" style="width: 230px;height: 230px;">
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="linkMe = false">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { createUuid } from '@/utils/index'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin",
        rememberMe: true,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      // 验证码开关
      captchaOnOff: true,
      linkMe: false,
      // 注册开关
      register: true,
      currentYear: '',
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();

    const timeOne = new Date()
    this.currentYear = timeOne.getFullYear();
  },
  methods: {

    getCode() {
      this.getCookie();
      let imageCodeKey = localStorage.getItem("loginImageCodeKey");
      if (!imageCodeKey || imageCodeKey === '' || imageCodeKey == null) {
        //第一次进入页面
        imageCodeKey = createUuid();
        localStorage.setItem("loginImageCodeKey", imageCodeKey);
      }
      this.loginForm.uuid = imageCodeKey;
      getCodeImg(imageCodeKey).then(res => {
        if (res.data.captchaOnOff == "0") {
          this.captchaOnOff = true;
          this.codeUrl = res.data.img;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe');
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
            Cookies.set('loginImageCodeKey', this.loginForm.uuid, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
            Cookies.remove('loginImageCodeKey');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.loading = false;
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            this.getCode();
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
$whiteColor: #fff;
$greyColor: #d0d0d0;
$padding: 20px;

.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  /*设置背景图片*/
  background-image: url("../assets/images/bg_login_3.jpg");
  background-size: cover;
}

.title {
  font-family: Arial;
  margin: 0px auto 30px auto;
  text-align: center;
  font-size: 32px;
  color: #2b2626;
  font-weight: bold;
  letter-spacing: 2px;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 510px;
  position: relative;
  left: 26%;
  /*内周边距*/
  padding: 92px 70px 76px 56px;
  /* 添加卡片阴影 */
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.3);
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 30%;
  height: 38px;
  float: right;
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  position: fixed;
  bottom: 20px;
  width: 100%;
  text-align: center;
  color: #555252;
  font-family: Arial;
  font-size: 15px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}
</style>
