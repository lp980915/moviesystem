<template>
    <Layout>
        <Header>
            <Row>
            <Menu mode="horizontal" theme="light"  @on-select="navPath" :active-name="activeName" :style="'background:'+color" ref="menu">
                <Col :xs="24" :sm="24" :md="24" :lg="12">
                <MenuItem name="1">
                    <Icon type="ios-desktop" />
                    首页
                </MenuItem>
                <MenuItem name="2">
                    <Icon type="md-list-box" />
                    电影榜单
                </MenuItem>
                <MenuItem name="3">
                    <Icon type="ios-switch" />
                    电影分类
                </MenuItem>
                    <MenuItem name="5">
                        <Icon type="ios-chatbubbles" />
                        电影话题
                    </MenuItem>
                <Submenu name="4" >
                    <template slot="title">
                        <Icon type="ios-person" />
                        个人中心
                    </template>
                        <MenuItem name="4-1">查看个人信息</MenuItem>
                        <MenuItem name="4-2">修改个人信息</MenuItem>
                </Submenu>
                </Col>
                <Col :xs="1" :sm="1" :md="1" :lg="1">
                    <ColorPicker v-model="color" alpha  recommend @on-change="colorChanged" />
                </Col>
                <Col :xs="0" :sm="0" :md="0" :lg="8">
                <span class="homeTitle"><Icon type="ios-film"/>电影交流平台</span>
                </Col>
                <Col :xs="23" :sm="18" :md="12" :lg="8">
                <div class="search">
                <Input search placeholder="站内搜索" v-model="searchValue" @on-search="homeSearch()"/>
                </div>
                <div class="userimg">
                    <Poptip placement="bottom-end" trigger="hover" padding="15px 50px">
                        <div slot="title">
                            <div>{{username}}</div>
                            <div style="margin-top: 10px;font-size: 12px">{{user.autograph}}</div>
                        </div>
                        <div slot="content">
                            <div>
                           <span class="friend" @click="toLikely">
                               关注
                           </span>
                            <span class="friend" @click="toFans">
                               粉丝
                           </span>
                            <span class="friend" @click="toFriend">
                               好友
                           </span>
                            </div>
                            <div>
                                <strong>
                                <span class="friendNum1">
                               {{friendData.myLikely.length}}
                           </span>
                                <span class="friendNum2">
                               {{friendData.myFans.length}}
                           </span>
                                <span class="friendNum3">
                               {{friendData.myFriend.length}}
                           </span>
                                </strong>
                            </div>
                            <div >
                                <!--action="http://60.205.222.92:8088/api/customer/uploadUserImg"-->
                                <Upload action="http://localhost:8082/api/customer/uploadUserImg"
                                      :headers="token" :data="user"
                                        :on-success="handleSuccess"
                                        :format="['jpg','jpeg','png']"
                                        :max-size="2048"
                                        :on-format-error="handleFormatError"
                                        :on-exceeded-size="handleMaxSize"
                                        :on-error="handleError"
                                :show-upload-list="false">
                                    <Button icon="ios-cloud-upload-outline" class="uploadBtn">上传头像</Button>
                                </Upload>
                            </div>
                        </div>
                        <Avatar :src="userimg" size="large" />
                    </Poptip>
                </div>
                <Tooltip content="注销" placement="bottom" theme="light" style="right: 1%">
                    <Button size="large" type="error" shape="circle" icon="md-close" @click="logout"></Button>
                </Tooltip>
                </Col>
            </Menu>
            </Row>
        </Header>
        <Content :style="'background:'+color" >

            <router-view/>
            <BackTop></BackTop>
        </Content>
        <Footer :style="'background:'+color+';padding-left:70%;height:700px'">
            <div style="font-size: 27px;font-family: Times New Roman;font-size: 27px;line-height:100px;margin-top: 150px">Designed by LJC</div>
            <div @click="goBlog" style="cursor: pointer;font-size: 27px;font-family:STXingkai;line-height:100px">
                <Icon type="ios-link-outline" />我的博客</div>
            <div @click="goGithub" style="cursor: pointer;font-size: 27px;font-family: Times New Roman;line-height:100px"><Icon type="ios-link-outline" />  Github</div>
            <div @click="goGitee" style="cursor: pointer;font-size: 27px;font-family:STXingkai;line-height:100px"><Icon type="ios-link-outline" />  码云</div>
        </Footer>
    </Layout>
</template>
<script>

    export default {
        name: "home",
        data(){
            return{
                searchValue:'',
                activeName:sessionStorage.getItem('activeName'),
                username:JSON.parse(localStorage.getItem('loginUser')).username,
                user:JSON.parse(localStorage.getItem('loginUser')),
                userimg:JSON.parse(localStorage.getItem('loginUser')).userimg,
                friendData:{
                    myLikely:[],
                    myFans:[],
                    myFriend:[]
                },
                token:{token:localStorage.getItem('token')},
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            goGithub(){
                window.open("https://github.com/lp980915")
            },
            goGitee(){
                window.open("https://gitee.com/lp980915")
            },
            goBlog(){
              window.open("https://www.ljcblog.top")
            },
            colorChanged:function(color){
                sessionStorage.setItem('color',color);
                window.location.reload();
            },
            logout:function () {
                localStorage.clear();
                sessionStorage.clear();
                this.$router.push('/login');
                this.$Message.success('成功注销!')
            },

            homeSearch:function () {
              sessionStorage.setItem('searchValue',this.searchValue);
              if(sessionStorage.getItem('searchValue')!==null&&sessionStorage.getItem('searchValue')!==''){
                  this.axios.get('/customer/search?moviename='+sessionStorage.getItem('searchValue')+'&actorname='+sessionStorage.getItem('searchValue'),
                      {headers:{'token':localStorage.getItem('token')}})
                      .then(res=>{
                          console.log(res)
                          if(res.data.data.actorList.length===0&&res.data.data.movieList.length===0){
                              this.$Message.warning('未找到相关内容');
                          }else {
                              sessionStorage.removeItem('actorList');
                              sessionStorage.removeItem('movieList');
                              if(res.data.data.actorList.length!==0){
                              sessionStorage.setItem('actorList',JSON.stringify(res.data.data.actorList));
                          }if(res.data.data.movieList.length!==0){
                              sessionStorage.setItem('movieList',JSON.stringify(res.data.data.movieList));
                          }
                              if(this.$route.path!=='/search'){
                                  this.$router.push('/search');
                              }else {
                                  window.location.reload()
                              }
                          }
                      }).catch(err=>{
                      console.log(err)
                  });
              }else{
                  this.$Message.warning('请输入您需要搜索的信息');
              }
            },
            navPath:function (name) {
                if(name==='1'){
                    this.$router.push('/welcome')
                }
                else if(name==='2'){
                    this.$router.push('/allMovie')
                }
                else if(name==='3'){
                    this.$router.push('/sortMovie')
                }
                else if(name==='4-1'){
                        this.$router.push('/userInfo');
                }
                else if(name==='4-2'){
                    this.$router.push('/upUserInfo')
                }
                else if(name==='5'){
                    this.$router.push('/topic')
                }
               sessionStorage.setItem('activeName',name)
            },
            toLikely:function(){
                sessionStorage.setItem('activeName','4-1');
                this.$router.push({path:'/userInfo',query:{tabValue:'4'}});
            },
            toFans:function(){
                sessionStorage.setItem('activeName','4-1');
                this.$router.push({path:'/userInfo',query:{tabValue:'5'}});
            },
            toFriend:function () {
                sessionStorage.setItem('activeName','4-1');
                this.$router.push({path:'/userInfo',query:{tabValue:'6'}});
            },
            handleSuccess:function (response, file, fileList) {
                if(response.data.result){
                    this.$Message.success('上传成功');
                }else {
                    this.$Message.error('上传失败');
                }
              let newUser=JSON.stringify(response.data.newUser);
                localStorage.setItem('loginUser',newUser)
                window.location.reload()
            },
            handleFormatError (file) {
                this.$Message.warning('请上传JPEG/JPG/PNG格式图片');
            },
            handleMaxSize (file) {
                this.$Message.warning('文件大小不能超过2m');
            },
            handleError(){
                this.$Message.warning('上传失败');
            },
            getFriend:function () {
                this.$axios.get('/customer/getFriend?userid='+JSON.parse(localStorage.getItem('loginUser')).userid)
                    .then(res=>{
                        console.log(res)
                        this.friendData=res.data.data;
                        localStorage.setItem('friendData',JSON.stringify(res.data.data))
                    }).catch(err=>{
                    console.log(err)
                })
            }
        },
        updated() {
            this.activeName=sessionStorage.getItem('activeName');
            this.$nextTick(()=>{
                this.$refs.menu.updateActiveName();
            })
        },
        created(){
           this.getFriend();
        },


    }
</script>

<style lang="less" scoped>

.ivu-tooltip{
   position: fixed;
    right: 5%;
}
.userimg{
    position:fixed;
    right: 80px;
}
    .ivu-layout{
        height: 100%;
    }
    .ivu-layout-header{
        background-color: #f5f7f9;
    }
    .ivu-menu{
        position: fixed;
        left: 0;
        right: 0;
    }
    .homeTitle{
        position: fixed;
        right: 400px;
        font-size: 20px;
    }
    .point{
        cursor:pointer;
    }

    .search{
        position: fixed;
        width: 200px;
        right: 160px;
    }
    .friend{
        margin-right: 15px;
        font-size: 13px;
    }
    span.friend:hover{
        color: #3399ff;
        cursor: pointer;
    }
    .friendNum1{
        margin-right: 32px;
        font-size: 13px;
    }
    .friendNum2{
    margin-right: 28px;
    font-size: 13px;
   }
     .friendNum3{
    margin-right: 15px;
    font-size: 13px;
   }
    .ivu-poptip{
        text-align: center;
    }
    .uploadBtn{
        margin-top: 20px;
    }
    .ivu-layout-content{
        background-color: #f5f7f9;
    }

</style>