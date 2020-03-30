<template>
    <div>这是水果
      <router-view></router-view>
      <router-link to="/fruit/apple">苹果</router-link>
      <router-link to="/fruit/banana">香蕉</router-link>
      <h1 v-show="hello">hello</h1>
      <h1>路径是{{getUrl}}</h1>
      <el-button type="success" plain @click="getTable">查看表格</el-button>
      <button @click="getMessage">getMessage</button>
    </div>

</template>

<script>
    export default {
        name: "Fruit",
        data(){
            return{
                hello:false
            }
        },
        computed:{
            getUrl(){
               return this.$route.path;
            }

        },
        methods:{
            getTable:function () {
                this.$router.push('/table');
            },
            getMessage:function(){
                this.$axios.get('/getMessage')
                    .then(res=>{
                        console.log(res)
                    }).catch(err=>{
                    console.log(err)
                })
            },
        },
        //页面初始化函数，在初始化时被调用
       created() {
          this.hello=!this.hello;
       }
    }
</script>

<style lang="less" scoped>

</style>
