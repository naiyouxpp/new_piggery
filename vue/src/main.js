import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import '@/assets/css/global.css'
import '@/assets/css/iconfont/iconfont.css'
import request from "@/utils/request.is";

Vue.config.productionTip = false
Vue.use(ElementUI,{ size :'small'});

Vue.prototype.$request=request/*设置全局的request，方便写请求*/
Vue.prototype.$baseUrl=process.env.VUE_APP_BASEURL/*设置一个全局的baseUrl，当是在本地运行的时候使用的就是.env.development,当项目在服务器运行的时候使用的就是.env.production中的地址*/

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
