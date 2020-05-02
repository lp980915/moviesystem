<template>
  <div>


  <img src="../../assets/logo.png">
  <router-view/>  <!-- 4.给出路由显示位置-->
  <ul>
    <!--通过:to和name动态路由并传递参数 -->
    <router-link to="/learn/123" tag="li"  class="ul1">learn</router-link>
    <router-link to="/fruit"  class="ul1">fruit</router-link>
  </ul>
  <!--<button @click="toHello">tohello(click)</button>-->
  <button @click="getReq()">getRequest</button>
  <button @click="postReq()">postRequest</button>
  </div>
</template>

<script>

    export default {
        name: "Home",
        data(){
            return{
                obj:{
                    id:123,
                    name:"ljc",
                    sex:"男",
                    tell:"123-456",
                    addr:"陕西"
                },
                ljc:'liujunchi',
                arr:[],


            }
        },
        methods:{

            getReq:function () {
                this.$axios.get('/getList?current='+this.page.current+"&size="+this.page.size)
                    .then(res=>{
                        console.log(res);
                        this.arr=res.data.data.records;
                        this.page.total=res.data.data.total;

                    }).catch(err=>{
                    alert(err);
                })
            },
            postReq:function () {
                let customer=this.$qs.stringify({
                    id: this.obj.id,
                    name: this.obj.name,
                    sex: this.obj.sex,
                    tell:this.obj.tell,
                    addr:this.obj.addr,
                });

                this.$axios.post('/getList',customer,
                    {headers:{
                            "Content-Type": "application/x-www-form-urlencoded;charset=utf-8"  //头部信息
                        }}).then(res=>{
                    this.arr=res.data.records;
                    this.page.total=res.data.total;
                    console.log(this.arr);
                }).catch(err=>{
                    alert(err);
                })

            },

            //编程式导航
            toHello:function () {
                //this.$router.push("/hello");
                //this.$router.push({name:'HelloWorld',params:{id:12345,name:this.ljc}});
                //this.$router.push({path:"/hello"});
                //this.$router.replace("/hello");   //使用replace的区别是浏览器不会增加这条history访问记录，不能返回到上一层(表示替换)
                //this.$router.go(-1);  //go里为负几就回退到几个页面以前
            }
        }
    }
</script>

<style lang="less" scoped>

</style>
