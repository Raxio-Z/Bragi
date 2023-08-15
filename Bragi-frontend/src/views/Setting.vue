<template>
  <div>
    <el-tabs @tab-click="tabClick" type="border-card" tab-position="top">
      <el-tab-pane label="🖼️ 编辑器设置" mname="editorConfigTab">
        <editor-setting ref="editorSetting"></editor-setting>
      </el-tab-pane>

      <el-tab-pane label="📑 公开笔记管理">
        <public-note-manage></public-note-manage>
      </el-tab-pane>

      <el-tab-pane label="🔑 修改密码">
        <el-form label-width="110px" :model="formData" @keyup.enter.native="handleChangePassword" :rules="formRules">
          <el-form-item class="noselect" prop="oldPassword" label="旧密码" :rules="formRules.oldPassword">
            <el-input v-model="formData.oldPassword" type="password" style="max-width: 500px; margin-right: 20px"
            ></el-input>
          </el-form-item>
          <el-form-item class="noselect" prop="newPassword" label="新密码" :rules="formRules.newPassword">
            <el-input v-model="formData.newPassword" type="password" style="max-width: 500px; margin-right: 20px"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="handleChangePassword()">提交</el-button>
          </el-form-item>
        </el-form>

      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
// import axios from 'axios'
import global from '../global'
import PublicNoteManage from '@/components/PublicNoteManage'
import EditorSetting from '@/components/EditorSetting'
import {reqUserGetRemoteUrl, reqUserPostChangePassword, reqUserPostValidate} from "@/api";

export default {
  name: "admin",
  components: {
    // FileManage,
    PublicNoteManage,
    EditorSetting
  },
  data() {
    return {
      gitActive: 0,
      genSshKey: false,
      sshkey: "",
      remoteRepoUrl: null,
      formData: {
          oldPassword: null,
          newPassword: null,
      },
      formRules:{
          oldPassword: {
              required: true,
              message: '原密码',
          },
        newPassword:[{ required: true, message: "不能为空" },
            // { pattern: /^[A-Za-z0-9!@#$^]+$/, message: "参数非法" },
            {
                pattern:
                    /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\s).{6,}/,
                message:
                    "密码不少于6位，包含大小写字母、数字、特殊符号(@#$%^&+=_!)",
                trigger: 'blur'
            }]
      },
      config: {
        headers: {
          token: this.$store.getters.getToken,
          username: this.$store.getters.getUsername
        }
      }
    };
  },
  methods: {
    notifyLoadEditorConfig() {
      console.log("fdsfsdfsdfsf")
      this.$refs.editorSetting.loadEditorConfig();
    },
    tabClick(target) {
      if (target.$attrs.mname === 'editorConfigTab') {
        this.notifyLoadEditorConfig();
      }
    },
    handleChangePassword() {
      let url = global.HOST_URL + "/user/changePass";
      let request = {
        password: this.formData.oldPassword,
        newPassword: this.formData.newPassword
      };
        const passwordpattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\s).{6,}/;
        if(!passwordpattern.test(this.formData.newPassword)){
            this.$notify({
                type: "warning",
                message: "请输入正确的新密码",
                duration: 1000,
            });
        }else{
          reqUserPostChangePassword(request, this.config).then(res => {
          // axios.post(url, request, this.config).then(
          //   res => {
              res = res.data;
              if (res.code === 0) {
                this.$notify({
                  type: 'success',
                  message: '修改密码成功'
                });
                this.formData.newPassword = null,
                    this.formData.oldPassword = null
              } else {
                this.$notify({
                  type: 'warning',
                  message: res.msg
                });
              }
            }
          )
        }
    }
  },
  mounted() {
    document.title = this.$store.getters.getWebsiteTitle + " - 设置"
    if (!this.config.headers.token) {
      this.$notify({
        type: 'warning',
        message: '需要登录'
      });
      setTimeout(() => {
        this.$router.push('/login');
      }, 700);
      return;
    }
    let url = global.HOST_URL + "/user/validate";
    // console.log(this.config)
    reqUserPostValidate(null,this.config).then(res => {
    // axios.post(url, null, this.config).then(
    //     res => {
          res = res.data;
          if (res.code === 0) {
            return;
          }
          this.$store.commit('setLocalInfo', {});
          this.$notify({
            type: 'warning',
            message: '需要登录',
            duration: 1500
          });
          setTimeout(() => {
            this.$router.push('/login');
          }, 700);

        }
    )
    // TODO user info
    let getRemoteRepoUrl = global.HOST_URL + "/user/remote";
    reqUserGetRemoteUrl().then( res => {
    // axios.get(getRemoteRepoUrl, this.config).then(
    //     res => {
          res = res.data;
          if (res.code === 0) {
            this.remoteRepoUrl = res.data;
          }
        }
    )

  }
};
</script>
<style>
.el-tabs__item {

  -webkit-touch-callout: none; /* iOS Safari */
  -webkit-user-select: none; /* Chrome/Safari/Opera */
  -khtml-user-select: none; /* Konqueror */
  -moz-user-select: none; /* Firefox */
  -ms-user-select: none; /* Internet Explorer/Edge */
  user-select: none; /* Non-prefixed version, currentlynot supported by any browser */
}
</style>

