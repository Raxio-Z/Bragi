<template>
    <div>
        <div class="top-area">
            <div class="logo-img">
                <img src="../assets/logo.png" />
            </div>
            <div class="title">Bragi</div>
        </div>
        <el-card class="box-card" style="margin-top: 70px">
            <el-row :gutter="30">
                <el-col :span="12">
                    <div class="box-left-img">
                        <img src="../assets/homepage_picture.jpg" />
                    </div>
                </el-col>

                <el-col :span="12">
                    <div class="find-password-text">找回密码</div>

                    <el-divider class="find-password-divider"></el-divider>

                    <el-form ref="Form" :model="formData" @keyup.enter.native="handleLogin" :rules="formRules">
                        <el-form-item prop="password" :rules="formRules.password">
                            <el-input class="box-input" type="password" v-model="formData.password" placeholder="新密码" />
                        </el-form-item>
                        <el-form-item prop="confirm_password" :rules="formRules.confirm_password">
                            <el-input class="box-input" type="password" v-model="formData.confirm_password"
                                placeholder="确认密码" />
                        </el-form-item>
                        <el-form-item ref="emailnumber" prop="emailnumber" :rules="formRules.emailnumber">
                            <el-input maxlength="30" class="box-input" v-model="formData.emailnumber" placeholder="邮箱" />
                        </el-form-item>
                        <div class="find-pwd-verify-area">
                            <el-form-item prop="code" :rules="formRules.codenumber" style="width:70%; margin-top:2px;">
                                <el-input v-model="formData.code" maxlength="6" placeholder="请输入邮箱验证码" />
                            </el-form-item>
                            <el-button class="find-pwd-email-btn" :loading="codeLoading" :disabled="isDisable" size="small"
                                round @click="sendMsg()">邮箱验证
                            </el-button>
                        </div>
                        <div>
                            <span class="status">{{ statusMsg }}</span>
                        </div>
                        <div class="box-btn">
                            <el-button @click="handleRetrieve" type="primary" round
                                class="find-pwd-confirm-btn">确认</el-button>
                        </div>

                        <div class="find-pwd-back-login-prompt-text">
                            <span @click="toSignin" class="find-pwd-back-login-a">返回登录</span>
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
    name: "retrievepwd",
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
                emailnumber: null,
                code: null,
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
                        this.pwdsame = false;
                        if (value === '') {
                            callback(new Error('请再次输入密码'))
                        } else if (value !== this.formData.password) {
                            callback(new Error('两次输入密码不一致'))
                        } else {
                            this.pwdsame = true;
                            callback()
                        }
                    },
                    trigger: 'blur'
                }],
                codenumber: [
                    { required: true, message: "请输验证码" }
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
                password: [
                    { required: true, message: "不能为空" },
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
            statusMsg: "",
            phoneDis: false,
            emailDis: false,
            pwdsame: false
        };
    },

    methods: {
        handleRetrieve() {
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
            }
            else if (!this.pwdsame) {
                this.$notify({
                    type: "warning",
                    message: "两次输入密码不同",
                    duration: 1000,
                });
                Pass = false;
            }
            else if (!emailapattern.test(this.formData.emailnumber)) {
                this.$notify({
                    type: "warning",
                    message: "请输入正确的邮箱",
                    duration: 1000,
                });
                Pass = false;
            }
            if (Pass) {
                let user = {
                    emailnumber: this.formData.emailnumber,
                    password: this.formData.password,
                    code: this.formData.code,
                };
                let url = global.HOST_URL + "/user/confirm_retrieve";
                axios.post(url, user).then((res) => {
                    res = res.data;
                    if (res.code === 0) {
                        this.$notify({
                            type: "success",
                            message: "修改密码成功",
                        });
                        this.$router.push({ name: "login" });
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
            this.$router.push({ name: "login" });
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
                let url = global.HOST_URL + "/user/retrievepwd";
                let params = { emailNo: this.formData.emailnumber };
                console.log(params);
                axios
                    .post(url, params)
                    .then((res) => {
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
                            this.$message({
                                showClose: true,
                                message: res.msg,
                                type: "warning",
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
        console.log("进入这个页面了")
        document.title = this.$store.getters.getWebsiteTitle + " - 找回密码";
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
    max-height: 500px;
    max-width: 800px;
    margin: 0 auto;
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

.find-password-text {
    margin-top: 10px;
    margin-bottom: 15px;
    text-align: center;
    font-size: 30px;
    font-weight: normal;
}

.find-pwd-confirm-btn {
    display: block;
    margin: 10px auto;
    width: 200px;
}

.find-password-divider {
    width: 20%;
    margin: 10px auto 30px;
}

.find-pwd-back-login-prompt-text {
    margin: 20px auto 0;
    text-align: center;
}

.find-pwd-verify-area {
    display: flex;
    padding: 0;
    height: 60px;
}

.find-pwd-email-btn {
    margin-top: 2px;
    height: 40px;
    margin-left: 4%;
    width: 25%;
}

.find-pwd-back-login-a {
    color: rgb(160, 160, 160);
    text-decoration: underline;
    cursor: pointer;
}
</style>