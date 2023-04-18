<template>
  <div>
    <div class="top-area">
      <div class="logo-img">
        <img src="../assets/logo.png" />
      </div>
      <div class="title">
        {{ pageTitle }}
      </div>
    </div>
    <el-card class="box-card" style="margin-top: 70px">
      <el-row :gutter="30">
        <el-col :span="12">
          <div class="box-left-img">
            <img src="../assets/homepage_picture.jpg" />
          </div>
        </el-col>


        <el-col :span="12">
          <div>
            <div class="login-text">
              注册
            </div>
          </div>

          <el-divider class="login-divider"></el-divider>

          <el-form :model="formData" @keyup.enter.native="handleLogin">
            <el-form-item prop="username" :rules="formRules.username">
              <el-input
                  maxlength="10"
                  class="box-input"
                  v-model="formData.username"
                  placeholder="用户名"
              />
            </el-form-item>
            <el-form-item prop="password" :rules="formRules.password">
              <el-input
                  class="box-input"
                  type="password"
                  v-model="formData.password"
                  placeholder="密码"
              />
            </el-form-item>

            <div class="box-btn">
              <el-button @click="handleRegister" type="primary" round class="signup-btn">注册</el-button>
            </div>

            <div class="box-prompt-text">
              <span @click="toSignin" class="signup-a">返回登录</span>
            </div>

          </el-form>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import axios from "axios";
import global from "../global";

export default {
  name: "signup",
  data() {
    return {
      pageTitle: this.$store.getters.getWebsiteTitle,
      formData: {
        username: null,
        password: null,
      },
      formRules: {
        username: [
          { required: true, message: "不能为空" },
          { pattern: /^[A-Za-z0-9]+$/, message: "参数非法" },
        ],
        password: [
          { required: true, message: "不能为空" },
          { pattern: /^[A-Za-z0-9!@#$^]+$/, message: "参数非法" },
        ],
      },
    };
  },

  methods: {

    handleRegister() {
      let user = {
        username: this.formData.username,
        password: this.formData.password,
      };
      let url = global.HOST_URL + "/user/register";
      axios.post(url, user).then((res) => {
        res = res.data;
        if (res.code === 0) {
          this.$notify({
            type: "success",
            message: "注册成功",
          });
          this.$router.push({name:'login'});
        } else {
          this.$notify({
            type: "warning",
            message: res.msg,
            duration: 700,
          });
        }
      });
    },
    toSignin(){
      this.$router.push({name:'login'});
    }

  },
  mounted() {
    document.title = this.$store.getters.getWebsiteTitle + " - 注册";
  },
};
</script>
<style>

.top-area {
  top:0px;
  width:1000px;
  height: 50px ;
  margin:0 auto;
}

.title{
  float:left;
  margin-left: 10px;
  line-height:50px;
}

.box-left-img {
  width:100%;
  height: 450px;
  overflow:hidden;
}

.box-left-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.box-btn {
  margin-bottom: 10px;
  margin-top: 10px;
}

.box-card {
  max-height:500px;
  max-width: 800px;
  margin: 0 auto;
}

.box-img {
  margin: 40px auto 20px;
  width: 80px;
  height: 80px;
}

.box-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.logo-img{
  float:left;
  width: 50px;
  height: 50px;
}

.logo-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.markideaname {
  min-width: 200px;
  margin: 10px;
  font-size: 40px;
}


.login-text {
  margin-top: 30px;
  margin-bottom:20px;
  text-align: center;
  font-size: 30px;
  font-weight:normal;
}

.signup-btn {
  display: block;
  margin: 30px auto;
  width:200px;
}

.login-divider{
  width:20%;
  margin:10px auto 30px;
}

.box-prompt-text{
  margin:30px auto;
  text-align: center;
}

</style>