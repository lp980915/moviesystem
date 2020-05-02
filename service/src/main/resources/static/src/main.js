// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import axios from 'axios'
import qs from 'qs'
import elementui from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/css/global.css'       //导入全局样式表
import store from "./store/store";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import App from './App'
import router from './router'  //5.导入写好的路由
import SlideVerify from 'vue-monoplasty-slide-verify';

Vue.use(SlideVerify);
NProgress.configure({showSpinner:false})

Vue.config.productionTip = false;

Vue.prototype.$axios = axios;
axios.defaults.baseURL = '/api';   //设置请求默认根路径
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8'
Vue.prototype.$qs = qs;
Vue.use(elementui);

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,    //5.注入写好的路由
  store:store,
  components: { App },
  template: '<App/>'
});


//请求拦截器
axios.interceptors.request.use(config => {
//判断是否存在token，如果存在将每个页面的header都添加token
  NProgress.start();
  if(store.state.token){
    config.headers.common['token']=store.state.token   //使用vuex发送token
  }
 /* const token=localStorage.getItem('token')
  token?config.headers.token=token:null;*/

  return config;
}, error => {
// 请求错误
  return Promise.reject(error);
});

//response拦截器
axios.interceptors.response.use(
  response => {
    NProgress.done();
    return response;
  },

  error => {  //默认除了2XX之外都为错误
    if(error.response){
      switch(error.response.status){
        case 401:
          this.$store.commit('delToken');   //vuex方式删除token
          //localStorage.removeItem('token');
          router.replace({ //跳转到登录页面
            path: '/login',
            query: { redirect: router.currentRoute.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
          });
      }
    }
    return Promise.reject(error.response);
  }
);

