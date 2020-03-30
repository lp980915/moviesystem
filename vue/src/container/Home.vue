<template>
      <el-container class="home-container">
        <audio :src="mus" class="media-audio" loop autoplay ref="MusicPlay" id="mus"></audio>
        <!--头部区域-->
        <el-header>
          <div>
            <img src="../assets/qianxun.png" class="circleImg">
            <span @click="goWelcome()" class="point">后台管理系统</span>
          </div>

          <div class="music">
            <el-tooltip content="上一首" placement="bottom" transition="el-zoom-in-center">
            <i class="el-icon-arrow-left point" @click="lastMusic"></i>
            </el-tooltip>
            <el-tooltip :content="musName" placement="bottom" transition="el-zoom-in-center">
            <i class="el-icon-video-play point" @click="musicPlay" v-show="musicAction"></i>
            </el-tooltip>
            <el-tooltip :content="musName" placement="bottom" transition="el-zoom-in-center">
            <i class="el-icon-video-pause point" @click="musicPause" v-show="!musicAction"></i>
            </el-tooltip>
            <el-tooltip content="下一首" placement="bottom" transition="el-zoom-in-center">
            <i class="el-icon-arrow-right point" @click="nextMusic"></i>
            </el-tooltip>
          </div>
          <LogoutBtn></LogoutBtn>
        </el-header>
        <!--主体区域-->
        <el-container>
          <!--左侧侧边栏-->
          <el-aside :width="iscollapse?'64px':'200px'" >
            <div class="toggle-button" @click="toggleCollapse"><></div>
            <!-- 侧边栏菜单区域-->
              <el-menu
                :collapse-transition="false"
                :collapse="iscollapse"
                :unique-opened="true"
                background-color="#333744"
                text-color="#fff"
                active-text-color="#409EFF"
                :default-active="activeIndex"
                router><!--:router=true表示开启路由模式，可以简写为router-->
                <!--一级菜单-->
                <el-submenu index="1">
                  <!--一级菜单模板区-->
                  <template slot="title">
                    <i class="el-icon-user"></i>
                    <span>用户管理</span>
                  </template>
                  <!--二级菜单-->
                  <el-menu-item index="/users" @click="setIndex('/users')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>用户列表</span>
                    </template>
                  </el-menu-item>
                  <el-menu-item index="/upPass" @click="setIndex('/upPass')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>修改密码</span>
                    </template>
                  </el-menu-item>
                </el-submenu>

                <el-submenu index="2">
                  <!--一级菜单模板区-->
                  <template slot="title">
                    <i class="el-icon-video-play"></i>
                    <span>电影管理</span>
                  </template>
                  <!--二级菜单-->
                  <el-menu-item index="/movies" @click="setIndex('/movies')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>电影列表</span>
                    </template>
                  </el-menu-item>
                  <el-menu-item index="/movieSort" @click="setIndex('/movieSort')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>电影分类</span>
                    </template>
                  </el-menu-item>
                  <el-menu-item index="/actors" @click="setIndex('/actors')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>演职员列表</span>
                    </template>
                  </el-menu-item>
                </el-submenu>

                <el-submenu index="3">
                  <!--一级菜单模板区-->
                  <template slot="title">
                    <i class="el-icon-chat-dot-round"></i>
                    <span>评论管理</span>
                  </template>
                  <!--二级菜单-->
                  <el-menu-item index="/comments" @click="setIndex('/comments')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>评论列表</span>
                    </template>
                  </el-menu-item>
                </el-submenu>

                <el-submenu index="4">
                  <!--一级菜单模板区-->
                  <template slot="title">
                    <i class="el-icon-s-data"></i>
                    <span>数据统计</span>
                  </template>
                  <!--二级菜单-->
                  <el-menu-item index="/reports" @click="setIndex('/reports')">
                    <!-- 二级菜单模板区-->
                    <template slot="title">
                      <i class="el-icon-menu"></i>
                      <span>数据报表</span>
                    </template>
                  </el-menu-item>
                </el-submenu>
              </el-menu>
          </el-aside>
          <!--右侧主体内容-->
          <el-main>
            <router-view></router-view>     <!--显示welcome欢迎界面 -->

          </el-main>
        </el-container>
      </el-container>
</template>

<script>
   import LogoutBtn from "../components/logout/LogoutBtn";
    export default {
        name: "Home",
        components:{LogoutBtn},
        data(){
            return{
                mus: require('../assets/music/Ennio Morricone - Playing Love.mp3'),  //使用的音频
                musicAction:false,
                musName:'Ennio Morricone - Playing Love',
                iscollapse:false,
                activeIndex:''
            }
        },

        methods:{

            //暂停音乐
            musicPause() {
                this.$refs.MusicPlay.pause();
                this.musicAction = true;
            },

            //播放音乐
            musicPlay() {
                this.$refs.MusicPlay.play();
                this.musicAction = false
            },
            lastMusic:function(){
           if(this.mus===require('../assets/music/Ennio Morricone - Playing Love.mp3')){
               this.mus=require('../assets/music/Ennio Morricone - A Mozart Reincarnated.mp3')
           }else if(this.mus===require('../assets/music/Ennio Morricone - A Mozart Reincarnated.mp3')){
               this.mus=require('../assets/music/Kan R. Gao - For River - Piano (Johnnys Version).mp3')
           }else {
               this.mus=require('../assets/music/Ennio Morricone - Playing Love.mp3')
           }
           this.musicAction=false
            },
            nextMusic:function(){
                if(this.mus===require('../assets/music/Ennio Morricone - Playing Love.mp3')){
                    this.mus=require('../assets/music/Kan R. Gao - For River - Piano (Johnnys Version).mp3')
                }else if(this.mus===require('../assets/music/Kan R. Gao - For River - Piano (Johnnys Version).mp3')){
                    this.mus=require('../assets/music/Ennio Morricone - A Mozart Reincarnated.mp3')
                }else {
                    this.mus=require('../assets/music/Ennio Morricone - Playing Love.mp3')
                }
                this.musicAction=false
            },

            //切换菜单折叠效果
            toggleCollapse:function () {
            this.iscollapse=!this.iscollapse
            },
            goWelcome:function () {
                this.$router.push('/welcome');
            },
            setIndex:function (index) {
                sessionStorage.setItem('index',index);
            }
        },

        updated() {
            if(this.mus===require('../assets/music/Ennio Morricone - Playing Love.mp3')){
                this.musName='Ennio Morricone - Playing Love'
            }else if(this.mus===require('../assets/music/Kan R. Gao - For River - Piano (Johnnys Version).mp3')){
                this.musName='Kan R. Gao - For River - Piano (Johnnys Version)'
            }else{
                this.musName='Ennio Morricone - A Mozart Reincarnated'
            }
        },
        mounted() {
            this.activeIndex=sessionStorage.getItem('index')
        }


    }
</script>

<style lang="less" scoped>
  .home-container{
  height: 100%;
  }
 .el-header{
  background-color: #373d41;
   display: flex;
   justify-content: space-between;  //flex布局左右贴标对齐
   padding-left: 0;  //让header的左侧贴边
   align-items: center;  //让按钮居中
   color: #fff;
   font-size: 20px;
   >div{
     display: flex;    //让header的文字也纵向居中
     align-items: center;
     span{
       margin-left: 15px;  //让文字与图标间隔15px
     }
   }
 }
  .el-aside{
    background-color: #333744;
    .el-menu{
      border-right: none;   //去掉边框线
    }
  }
  .el-main{
    background-color: #EAEDF1;
  }
  .circleImg{
    border-radius:50%;
    width: 6.3%;
  }
  .music{
    margin-left: 500px;
    font-size: 20px;
    span{
      font-size: 15px;
    }
  }
  .point{
    cursor:pointer;
  }
  .toggle-button{
   background-color: #4A5064;
    font-size: 20px;
    line-height: 30px;
    color: #fff;
    text-align: center;
    cursor: pointer;
  }


</style>
