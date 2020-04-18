import Vue from 'vue'
import './plugins/axios'
import App from './App.vue'
import router from './router'
import qs from 'qs';
import './plugins/iview.js'
import './assets/css/global.css'
import  {Comment,Avatar,List} from 'ant-design-vue';
import SlideVerify from 'vue-monoplasty-slide-verify';

Vue.use(SlideVerify);

Vue.config.productionTip = false
Vue.prototype.$qs = qs;
Vue.use(Comment);
Vue.use(Avatar);
Vue.use(List);

new Vue({
  router,
  render: function (h) { return h(App) }
}).$mount('#app')
