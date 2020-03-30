import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/container/Home";
import NotFound from "@/components/NotFound";
import Login from "@/components/login/Login";
import ViewUI from 'view-design';
import '@/plugins/axios'
import AllMovie from "@/components/allMovie/AllMovie";
import Welcome from "@/components/welcome/Welcome";
import SortMovie from "@/components/sortMovie/SortMovie";
import UpUserInfo from "@/components/user/UpUserInfo";
import UserInfo from "@/components/user/UserInfo";
import MovieInfo from "@/components/movieInfo/MovieInfo";
import Search from "@/components/search/Search";
import ActorInfo from "@/components/actor/ActorInfo";
import Topic from "@/components/topic/Topic";
import TopicContent from "@/components/topic/TopicContent";

Vue.use(ViewUI);

Vue.use(VueRouter)

const routes = [
  {
    path:'/home',
    name:Home,
    component:Home,
    children:[
      {
        path: '/welcome',
        name:Welcome,
        component: Welcome
      },
      {
        path:'/allMovie',
        name:AllMovie,
        component:AllMovie
      },
      {
        path:'/sortMovie',
        name:SortMovie,
        component:SortMovie
      },
      {
        path:'/upUserInfo',
        name:UpUserInfo,
        component:UpUserInfo
      },
      {
        path:'/userInfo',
        name:UserInfo,
        component:UserInfo
      },
      {
        path:'/movieInfo',
        name:MovieInfo,
        component:MovieInfo
      },
      {
        path:'/search',
        name:Search,
        component:Search
      },
      {
        path:'/actorInfo',
        name:ActorInfo,
        component:ActorInfo
      },
      {
        path:'/topic',
        name:Topic,
        component:Topic
      },
      {
        path:'/topicContent',
        name:TopicContent,
        component:TopicContent
      }
    ]
  },
  {
    path: '*',
    component: NotFound
  },
  {
    path:'/login',
    name:Login,
    component:Login
  },
  {
    path:'/',
    redirect:'/login'
  }

]

const router = new VueRouter({
  routes
})
//这是路由守卫
router.beforeEach((to, from, next) => {   //to代表即将跳转的url，from表示从哪个url过来的，next表示放行
  if (to.path === '/login') {    //若要跳转的页面是登录界面
    next();     //直接跳转
  }
  else if (to.path !== '/login'){   //若要跳转的页面是个人界面
    let token = localStorage.getItem('token');    //获取本地存储的token值
    if (token===null||token===''){    //若token为空则验证不成功，跳转到登录页面
      next('/login');
      ViewUI.Message.warning('请登录')
    }
    else{           //不为空则验证成功
      next();
    }
  }
  else{
    next();
  }
});
router.afterEach((to,form) => {
});

export default router
