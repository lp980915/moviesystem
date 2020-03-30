"use strict";

import Vue from 'vue';
import axios from "axios";
import ViewUI from 'view-design';
Vue.use(ViewUI);
import router from "@/router";

// Full config:  https://github.com/axios/axios#request-config
axios.defaults.baseURL ='/api';
axios.defaults.headers.common['token'] = localStorage.getItem('token');
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;charset=utf-8';

let config = {
  // baseURL: process.env.baseURL || process.env.apiUrl || ""
  // timeout: 60 * 1000, // Timeout
  // withCredentials: true, // Check cross-site Access-Control
};

const _axios = axios.create(config);

_axios.interceptors.request.use(
  function(config) {
    // Do something before request is sent
    ViewUI.LoadingBar.config({
      height: 5,
    });
    ViewUI.LoadingBar.start();
    return config;
  },
  function(error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add a response interceptor
_axios.interceptors.response.use(
  function(response) {
    // Do something with response data
    ViewUI.LoadingBar.finish();
    return response;
  },
  function(error) {
    // Do something with response error
    ViewUI.LoadingBar.error();
    if(error.response.status===500&&error.response.data.message!=='401'){
      ViewUI.Message.error('服务器端错误')
    }
    if(error.response.status===400){
      ViewUI.Message.error('请求参数类型错误')
    }
    if(error.response.data.message==='401'){
      router.push('/login')
      localStorage.clear()
      sessionStorage.clear()
      ViewUI.Message.warning('登录超时')
    }
    if(error.response.status===403){
      ViewUI.Message.error('服务器拒绝请求')
    }
    if(error.response.status===404){
      ViewUI.Message.error('错误的请求路径')
    }
    if(error.response.status===405){
      ViewUI.Message.error('不能使用该请求方法')
    }
    if(error.response.status===408){
      ViewUI.Message.error('请求超时')
    }
    return Promise.reject(error);
  }
);

Plugin.install = function(Vue, options) {
  Vue.axios = _axios;
  window.axios = _axios;
  Object.defineProperties(Vue.prototype, {
    axios: {
      get() {
        return _axios;
      }
    },
    $axios: {
      get() {
        return _axios;
      }
    },
  });
};

Vue.use(Plugin)

export default Plugin;
