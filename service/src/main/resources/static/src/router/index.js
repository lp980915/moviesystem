import Vue from 'vue'
import Router from 'vue-router'  //1.导入路由
import LearnRouter from "../components/notused/LearnRouter"; //@代表根目录
import Fruit from "../components/notused/Fruit";
import Apple from "../components/notused/Apple";
import Banana from "../components/notused/Banana";
import NotFound from "../components/NotFound";
import CustomerTable from "../components/notused/customerTable/CustomerTable";
import Home from "../container/Home";
import Login from "../components/login/Login";
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import welcome from "../components/welcome/welcome";
import Users from "../components/user/Users";
import {Notification } from 'element-ui';
import UpPass from "../components/user/UpPass";
import Movies from "../components/movie/Movies";
import addMovie from "../components/movie/addMovie";
import MovieSort from "../components/movie/MovieSort";
import Reports from "../components/report/Reports";
import Actors from "../components/movie/Actors";
import Comments from "../components/commment/Comments";

Vue.use(Router)    //2.use路由




const router=new Router({    //3.创建路由
  linkActiveClass:"active",  //为了给路由高亮显示,只需设置active的样式
  linkExactActiveClass:"currentActive", //设置精准匹配。如果选择子级元素，父级不会有反应
  routes: [

    {
      path: '/learn/:id',   //冒号形式动态接参数(动态路由)
      name: 'LearnRouter',
      component: LearnRouter
    },
    {
      path:'/fruit',
      name:'Fruit',
      component:Fruit,
      children:[
        {
        path:"Apple",
        component:Apple
      },
        {
          path: "Banana",
          component: Banana
        }
      ]
    },
    {
      path:'/table',
      name:'CustomerTable',
      component:CustomerTable

    },
    /*{
      path:'/',
      redirect:'/fruit'     //这是一个重定向，表示到项目根路径会自动跳转至/fruit
    },*/
    {
      path:'*',
      component:NotFound     //表示访问未定义路径会跳转到404组件
    },
    {
      path:'/home',
      name:'Home',
      component:Home,
      redirect:'welcome',        //登录主页显示home的子路由welcome
      children:[
        {path:'/welcome', component:welcome},
        {
          path:'/users',
          name:Users,
          component:Users
        },
        {
          path:'/upPass',
          name:UpPass,
          component:UpPass
        },
        {
          path:'/movies',
          name:Movies,
          component:Movies,
        },
        {
          path:'/movies/addMovie',
          name:addMovie,
          component:addMovie
        },
        {
          path:'/movieSort',
          name:MovieSort,
          component:MovieSort
        },
        {
          path:'/reports',
          name:Reports,
          component:Reports
        },
        {
          path:'/actors',
          name:Actors,
          component:Actors
        },
        {
          path:'/comments',
          name:Comments,
          component:Comments
        }

      ]
    },

    {
      path:'/login',
      name:'Login',
      component:Login
    },
    {
      path:'/',
      redirect:'/login'
    }

  ]
});
//这是路由守卫
router.beforeEach((to, from, next) => {   //to代表即将跳转的url，from表示从哪个url过来的，next表示放行
  if (to.path === '/login') {    //若要跳转的页面是登录界面
    NProgress.start();
    next();     //直接跳转
  }
  else if (to.path !== '/login'){   //若要跳转的页面是个人界面
    let token = localStorage.getItem('token');    //获取本地存储的token值
    if (token===null||token===''){    //若token为空则验证不成功，跳转到登录页面
      next('/login');
      Notification.warning('请登录')
    }
    else{           //不为空则验证成功
      NProgress.start();
      next();
    }
  }
  else{
    NProgress.start();
    next();
  }
});

router.afterEach(() => {
  NProgress.done();
});



export default router;
