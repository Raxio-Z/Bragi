import axios from "axios";
// import nprogress from "nprogress";
// import "nprogress/nprogress.css"
import {Message} from 'element-ui';
import router from '../router'
// import HOST_URL from '../global'

const requests = axios.create({
    baseURL: "http://localhost:8090/api",
    timeout: 500,
});
requests.interceptors.request.use((config) => {
    //nprogress.start();
    return config;
});
requests.interceptors.response.use((res) => {
    // nprogress.done();
    //console.log(res);
    return res.data;

}, (error) => {
    //console.log("error error.response",error.response)
    //console.log("error",error)
    // nprogress.done();
    if (error.response.status) {
        if (error.response.data.fault) {
            Message.error(error.response.data.fault);
        } else if (error.response.data.msg) {
            Message.error(error.response.data.msg);
        }
        switch (error.response.status) {
            case 400:
                // Message.error("400输入不合法");
                break;
            case 401:
                // Message.error("身份有误，请重新登录")
                //可以拿到历史记录栈，清空栈
                // router.replace('/')
                break;
            case 404:
                // Message.error("404NotFound")
                break;
            case 403:
                // Message.error("403权限不足!");
                router.push("/login");
                break;
            case 405:
                //     Message.error("405未登录!");
                //     router.push("/login");
                break;
            case 409:
                // Message.error("409冲突");
                break;
            default:
                Message.error("未捕获错误");
        }
        return Promise.reject(error);
    }
});
export default requests;
