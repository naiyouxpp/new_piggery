import axios from "axios";
import router from "@/router";

const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL, //后端接口地址
    timeout: 30000 //30s内后端必须返回数据不然就超时
})


request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8'

    let user = JSON.parse(localStorage.getItem("honey-user" )||'{}');
        config.headers['token'] = user.token //设置请求头
        config.headers['X-Current-User-Id'] = user.id //设置请求头

    return config
}, error => {
    console.log('request error: ' + error) // for error debug
    return Promise.reject(error)
})



request.interceptors.response.use(response => {
    let res = response.data
    //  res.code

    if(typeof res === 'string'){
        res == res ? JSON.parse(res) : res
    }

    if(res.code  ===  401){
        router.push('/login')
    }

    return res
}, error => {
    console.log('response error: ' + error) // for error debug
    return Promise.reject(error)
})

export default request
