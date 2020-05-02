<template>
<div>
  <h3>Welcome,{{user.username}}</h3>
  <span>当前时间为:</span>
  <span>{{hours}}</span>:
  <span>{{minutes}}</span>:
  <span>{{seconds}}</span>
  <el-calendar v-model="value">
  </el-calendar>
</div>
</template>

<script>
    export default {
        name: "welcome",
        data(){
            return{
                value: new Date(),
                hours: '', // 设定小时，存在这个变量
                minutes: '', // 设定分，存在这个变量
                seconds: '', // 设定秒，存在这个变量
                user:JSON.parse(localStorage.getItem("user"))    //localStorage只能存储字符串类型，需要使用parse进行格式转换
            }
        },
        created() {
            let target = new Date().getTime(); //获取目标时间戳
            setInterval(() => {
                let date = new Date();
                let hours = date.getHours();
                let minutes = date.getMinutes();
                let seconds = date.getSeconds();
                this.hours = hours;
                this.minutes = minutes;
                this.seconds = seconds;
            }, 1000);
        }
    }
</script>

<style lang="less" scoped>

</style>
