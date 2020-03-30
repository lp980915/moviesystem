import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: localStorage.getItem('token') ? localStorage.getItem('token') : ''
  },
  mutations: {
    setToken (state,token) {
      state.token =token.token;
      localStorage.setItem("token",token.token);     //存储token,采用localStorage方式存储token，只要不删除token，他就会一直存在，而sessionStorage方式在用户关闭网页时就会清空数据
    },
    delToken (state) {
      state.token = '';
      localStorage.removeItem("token");    //删除token
    },
  }
})
