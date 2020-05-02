<template>
  <div class="login_container">
    <el-row>
      <el-col :span="16"><div class="grid-content bg-purple">
        <el-carousel  height="1038px" >
          <el-carousel-item v-for="(img,index) in imgList" :key="index">
            <img :src="img.url">
            <h3>{{img.index}}</h3>
          </el-carousel-item>
        </el-carousel>
      </div></el-col>
      <el-col :span="8"><div class="grid-content bg-purple"></div>
        <img src="../../assets/logo.png" class="login_logo">
      <div class="login_box">
        <div class="systemtitle">后台管理系统</div>
      <login-form></login-form>
      </div>
      </el-col>
    </el-row>


  </div>
</template>

<script>
  import LoginForm from "./LoginForm";
    export default {
        components:{LoginForm},
        name: "Login",
        data(){
            return{
                imgList:[
                    {url:require('../../assets/images/three.png')},
                    {url:require('../../assets/images/two.png')},
                    {url:require('../../assets/images/one.png')}
                ]
            }
        }

    }
</script>

<style scoped lang="less">
  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
  .login_container{
    background-color: #d0d0d0;
    height: 100%;
  }

  .login_box{
    background-color: #ffffff;
    width: 350px;
    height: 450px;
    border-radius: 3px;  //圆角边框3px
    position: absolute;  //绝对定位
    left: 73%;
    top: 30%;

  }
  .login_logo{
    position: absolute;  //绝对定位
    left: 77%;
    top: 8%;
  }
  .systemtitle{
    position: relative;
    padding-left: 8%;
    font-size: 20px;
    padding-top: 30px;
  }
.el-row{
  background-color: #d0d0d0;
}

</style>
