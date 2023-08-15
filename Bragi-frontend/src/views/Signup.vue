<template>
    <div>
        <div class="top-area">
            <div class="logo-img">
                <img src="../assets/logo.png"/>
            </div>
            <div class="title">Bragi</div>
        </div>
        <el-card class="box-card" style="margin-top: 70px">
            <el-row :gutter="30">
                <el-col :span="12">
                    <div class="box-left-img">
                        <img src="../assets/homepage_picture.jpg"/>
                    </div>
                </el-col>

                <el-col :span="12">
                    <div class="signup-text">注 册</div>

                    <el-form ref="Form" :model="formData" @keyup.enter.native="handleLogin" :rules="formRules">
                        <el-form-item prop="username" :rules="formRules.username">
                            <el-input maxlength="10" class="box-input" v-model="formData.username"
                                      placeholder="用户名"/>
                        </el-form-item>
                        <el-form-item prop="password" :rules="formRules.password">
                            <el-input class="box-input" type="password" v-model="formData.password" placeholder="密码"/>
                        </el-form-item>
                        <el-form-item prop="confirm_password" :rules="formRules.confirm_password">
                            <el-input class="box-input" type="password" v-model="formData.confirm_password"
                                      placeholder="确认密码"/>
                        </el-form-item>
                        <el-form-item ref="emailnumber" prop="emailnumber" :rules="formRules.emailnumber">
                            <el-input maxlength="30" class="box-input" v-model="formData.emailnumber"
                                      placeholder="邮箱"/>
                        </el-form-item>
                        <div class="verify-area">
                            <el-form-item prop="code" :rules="formRules.codenumber" style="width:70%; margin-top:2px;">
                                <el-input v-model="formData.code" maxlength="6" placeholder="请输入邮箱验证码"/>
                            </el-form-item>
                            <el-button class="email-verify-btn" :loading="codeLoading" :disabled="isDisable"
                                       size="small" round
                                       @click="sendMsg()">邮箱验证
                            </el-button>
                        </div>
                        <div>
                            <span class="status">{{ statusMsg }}</span>
                        </div>
                        <div class="box-btn">
                            <el-button @click="handleRegister" type="primary" round class="signup-btn">注册</el-button>
                        </div>

                        <div class="back-login-prompt-text">
                            <span @click="toSignin" class="back-login-a">返回登录</span>
                        </div>
                    </el-form>
                </el-col>
            </el-row>
        </el-card>
    </div>
</template>
<script>
// import axios from "axios";
import global from "../global";
import {reqUserEmailSignUp, reqUserPostRegister} from "@/api";

export default {
    name: "signup",
    data() {
        return {
            config: {
                headers: {
                    token: this.$store.getters.getToken,
                    username: this.$store.getters.getUsername,
                },
            },
            pageTitle: this.$store.getters.getWebsiteTitle,
            formData: {
                // phonenumber: null,
                emailnumber: null,
                code: null,
                username: null,
                password: null,
                confirm_password: null
            },
            formRules: {
                // phonenumber:[
                //     {required:true,trigger:'blur',validator:validatePhone},
                // ],
                confirm_password: [{
                    required: true,
                    message: '确认密码',
                    trigger: 'blur'
                }, {
                    validator: (rule, value, callback) => {
                        this.pwdSame = false;
                        if (value === '') {
                            callback(new Error('请再次输入密码'))
                        } else if (value !== this.formData.password) {
                            callback(new Error('两次输入密码不一致'))
                        } else {
                            this.pwdSame = true;
                            callback()
                        }
                    },
                    trigger: 'blur'
                }],
                codenumber: [
                    {required: true, message: "请输验证码"}
                ],
                emailnumber: [
                    {
                        required: true,
                        trigger: ["blur"],
                        message: "不能为空"
                        // validator: validateEmail.bind(this),
                    },
                    {
                        pattern: /^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/,
                        message: "请输入正确的邮箱"
                    }
                ],
                username: [
                    {required: true, message: "不能为空"},
                    {pattern: /^[A-Za-z0-9]+$/, message: "参数非法"},
                ],
                password: [
                    {required: true, message: "不能为空"},
                    // { pattern: /^[A-Za-z0-9!@#$^]+$/, message: "参数非法" },
                    {
                        pattern:
                            /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\s).{6,}/,
                        message:
                            "密码不少于6位，包含大小写字母、数字、特殊符号(@#$%^&+=_!)",
                    },
                ],
            },
            codeLoading: false,
            isDisable: false,
            statusMsg: "",
            phoneInvalid: true,
            phoneDis: false,
            emailDis: false,
            pwdSame: false
        };
    },

    methods: {
        handleRegister() {
            let Pass = true;
            const emailapattern = /^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/;
            const passwordpattern = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!])(?!.*\s).{6,}/;
            if (!passwordpattern.test(this.formData.password)) {
                this.$notify({
                    type: "warning",
                    message: "请输入正确的密码",
                    duration: 1000,
                });
                Pass = false;
            } else if (!this.pwdSame) {
                this.$notify({
                    type: "warning",
                    message: "两次输入密码不同",
                    duration: 1000,
                });
                Pass = false;
            } else if (!emailapattern.test(this.formData.emailnumber)) {
                this.$notify({
                    type: "warning",
                    message: "请输入正确的邮箱",
                    duration: 1000,
                });
                Pass = false;
            }
            if (Pass) {
                let user = {
                    // phonenumber:this.formData.phonenumber,
                    emailnumber: this.formData.emailnumber,
                    username: this.formData.username,
                    password: this.formData.password,
                    code: this.formData.code,
                };
                let url = global.HOST_URL + "/user/register";
                reqUserPostRegister(user).then((res) => {
                    // axios.post(url, user).then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.$notify({
                            type: "success",
                            message: "注册成功",
                        });
                        this.$router.push({name: "login"});
                    } else {
                        this.$notify({
                            type: "warning",
                            message: res.msg,
                            duration: 700,
                        });
                    }
                });
            }
        },
        toSignin() {
            this.$router.push({name: "login"});
        },

        sendMsg() {
            let Pass = true;
            let timeRid;
            if (timeRid) {
                return false;
            }
            this.statusMsg = "";

            const emailapattern = /^([a-zA-Z\d][\w-]{2,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/;
            if (!emailapattern.test(this.formData.emailnumber)) {
                this.$notify({
                    type: "warning",
                    message: "请输入正确的邮箱",
                    duration: 1000,
                });
                Pass = false;
            }

            if (Pass) {
                this.codeLoading = true;
                this.statusMsg = "验证码发送中...";
                let url = global.HOST_URL + "/user/emailsignup";
                let params = {emailNo: this.formData.emailnumber};
                console.log(params);
                reqUserEmailSignUp(params).then((res) => {
                    // axios
                    //   .post(url, params)
                    //   .then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.$message({
                            showClose: true,
                            message: "发送成功，验证码有效期5分钟",
                            type: "success",
                        });
                        let count = 60;
                        this.formData.code = "";
                        this.isDisable = true;
                        this.codeLoading = false
                        this.statusMsg = "验证码已发送,60秒后重新发送";
                        timeRid = setInterval(() => {
                            count -= 1;
                            this.statusMsg = "验证码已发送," + count + "秒后重新发送";
                            if (count <= 0) {
                                clearInterval(timeRid);
                                this.isDisable = false;
                                this.statusMsg = "";
                            }
                        }, 1000);
                    } else {
                        this.$notify({
                            type: "warning",
                            message: res.msg,
                            duration: 700
                        });
                        this.isDisable = false;
                        this.statusMsg = "";
                        this.codeLoading = false;
                    }
                })
                    .catch((err) => {
                        console.log(err);
                        this.isDisable = false;
                        this.statusMsg = "";
                        this.codeLoading = false;
                        console.log(err.data);
                    });
            }
        },
    },
    mounted() {
        document.title = this.$store.getters.getWebsiteTitle + " - 注册";
    },
};
</script>
<style>
.status {
    font-size: 14px;
    color: red;
}

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
    margin-bottom: 10px;
    margin-top: 10px;
}

.box-card {
    max-height: 600px;
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

.signup-text {
    margin-top: 0px;
    margin-bottom: 15px;
    text-align: center;
    font-size: 25px;
    font-weight: normal;
}

/* 验证码区域样式 */
.verify-area {
    display: flex;
    padding: 0;
    height: 60px;
}

.email-verify-btn {
    margin-top: 2px;
    height: 40px;
    margin-left: 4%;
    width: 25%;
}

.signup-btn {
    display: block;
    margin: 10px auto;
    width: 200px;
}

.back-login-prompt-text {
    margin: 15px auto 0;
    text-align: center;
}

.back-login-a {
    color: rgb(160, 160, 160);
    text-decoration: underline;
    cursor: pointer;
}
</style>
