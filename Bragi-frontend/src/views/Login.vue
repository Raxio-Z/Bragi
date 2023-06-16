<template>
  <div>
    <div class="top-area">
      <div class="logo-img">
        <img src="../assets/logo.png" />
      </div>
      <div class="title">
        Bragi
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
          <div class="login-text">
            登 录
          </div>

          <el-divider class="login-divider"></el-divider>

          <el-form :model="formData" @keyup.enter.native="handleLogin">
            <el-form-item prop="username" :rules="formRules.username">
              <el-input maxlength="100" class="box-input" v-model="formData.username" placeholder="用户名/邮箱" />
            </el-form-item>
            <el-form-item prop="password" :rules="formRules.password">
              <el-input class="box-input" type="password" v-model="formData.password" placeholder="密码" />
            </el-form-item>

            <div class="box-btn">
              <el-button @click="handleLogin" type="primary" round class="login-btn">登录</el-button>
            </div>

            <div style="display: flex;margin-top: -15px">
              <div class="login-prompt-text">
                <span @click="toSignup" class="login-a">注册账号</span>
              </div>
              <div class="login-prompt-text">
                <span @click="toRetrievepwd" class="login-a">找回密码</span>
              </div>
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
  name: "login",
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
          { pattern: /^[A-Za-z0-9]+$|^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/, message: "参数非法" },
        ],
        password: [
          { required: true, message: "不能为空" },
        ],
      },
    };
  },

  methods: {
    handleLogin() {
      let flag = "";
      const emailapattern = /^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/;
      let email = false
      if (emailapattern.test(this.formData.username)) {
        flag = "emaillogin"
        email = true
      }
      const namepattern = /^[a-zA-Z0-9]+$/;
      if (email == false && namepattern.test(this.formData.username)) {
        flag = "usernamelogin";
      }
      let user = {
        username: this.formData.username,
        password: this.formData.password,
        flag: flag
      };

      axios.post(global.HOST_URL + "/user/login", user).then((res) => {
        res = res.data;
        if (res.code === 0) {
          console.log(res.data);
          this.$notify({
            type: "success",
            message: "登录成功",
            duration: 1000,
          });
          this.$store.commit("setLocalInfo", res.data);
          setTimeout(() => {
            this.$router.push("/");
          }, 700);
        } else {
          this.$notify({
            type: "warning",
            message: "用户名或密码错误",
            duration: 2000,
          });
        }
      });
    },

    toSignup() {
      this.$router.push({ name: 'signup' });
    },
    toRetrievepwd() {
      this.$router.push({ name: "retrievepwd" });
    }
  },
  mounted() {
    document.title = this.$store.getters.getWebsiteTitle + " - 登录";
  },
};
</script>
<style>
.top-area {
  width: 1000px;
  height: 50px;
  margin: 20px auto;
}

.title {
  float: left;
  margin-left: 10px;
  line-height: 50px;
  font-size: 2rem;
  font-weight: bold;
}

.box-left-img {
  width: 100%;
  height: 450px;
  overflow: hidden;
}

.box-left-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.box-btn {
  /* float: ; */
  margin-bottom: 10px;
  margin-top: 10px;
}

.box-card {
  max-height: 500px;
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

.logo-img {
  float: left;
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
  margin-bottom: 20px;
  text-align: center;
  font-size: 30px;
  font-weight: normal;
}

.login-btn {
  display: block;
  margin: 30px auto;
  width: 200px;
}

.login-divider {
  width: 20%;
  margin: 10px auto 30px;
}

.login-prompt-text {
  margin: 30px auto;
  text-align: center;
}

.login-a {
  color: rgb(160, 160, 160);
  text-decoration: underline;
  cursor: pointer;
}
</style>