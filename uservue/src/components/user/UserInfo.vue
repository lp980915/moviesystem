<template>
    <div>
        <Row>
            <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 24, offset: 0 }"  :md="{ span: 6, offset: 1 }" :lg="{ span: 6, offset: 1 }">
                <Card  :style="'background:'+color+';width:90%;margin-top:40px'" >
                    <Spin fix v-if="spinShow"></Spin>
                    <Avatar :src="user.userimg" size="60" style="margin-left: 42%"/>
                    <h2 style="text-align: center;margin-top:10px">{{user.username}}</h2>
                    <div style="text-align: center;margin-top:10px">{{user.autograph}}</div>
                    <div style="text-align: center;margin-top: 15px" v-show="existOther">
                        <Tooltip content="关注" placement="left" theme="light">
                    <Button @click="addLikely" v-show="!existLike" style="margin-right: 20px" shape="circle" type="info" icon="md-add"></Button>
                        </Tooltip>
                        <Tooltip content="取消关注" placement="left" theme="light">
                            <Button @click="removeLikely" v-show="existLike" style="margin-right: 20px"  shape="circle" type="error" icon="md-close"></Button>
                        </Tooltip>
                        <Tooltip content="留言" placement="right" theme="light">
                    <Button shape="circle" type="success" icon="md-brush" @click="existFriend"></Button>
                        </Tooltip>
                    </div>

                    <Modal v-model="messageModal" :mask-closable="false" draggable scrollable>
                        <p slot="header" style="text-align:center">
                            <Icon type="md-brush" size="13"></Icon>
                            <span style="font-size: 12px">  给{{user.username}}的留言</span>
                        </p>
                        <div style="text-align:center">
                            <Input  v-model="messageValue" type="textarea" :autosize="{minRows: 5,maxRows: 10}"  placeholder="请填写留言..."
                                   maxlength="400" />
                        </div>
                        <div slot="footer">
                            <Button size="small" type="primary" @click="submitMessage" :disabled="messageValue===''" style="font-size: 12px">提交</Button>
                            <Button size="small" @click="messageModal=false" style="font-size: 12px">取消</Button>
                        </div>
                    </Modal>

                    <div style="margin-top:30px;margin-left:10%"><Icon type="ios-person-outline" size="25"/>  {{user.realname}}</div>
                    <div style="margin-top:10px;margin-left:10%"> <Icon type="ios-call-outline" size="25"/>  {{user.number}}</div>
                    <div style="margin-top:10px;margin-left:10%"><Icon type="ios-mail-outline" size="25"/>  {{user.email}}</div>
                    <Divider/>
                        <div>
                           <span class="friend1" @click="tabValue='4'">
                               关注
                           </span>
                            <span class="friend" @click="tabValue='5'">
                               粉丝
                           </span>
                            <span class="friend" @click="tabValue='6'">
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
                </Card>
            </Col>
            <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 24, offset: 0 }"  :md="{ span: 16, offset: 0 }" :lg="{ span: 16,offset: 0  }">
                <Card :style="'background:'+color+';width:100%;margin-top:40px'" >
                    <Spin fix v-if="spinShow"></Spin>
                    <Tabs :value="tabValue" @on-click="tabChange">
                        <TabPane :label="'评论'+' ('+commentList.length+')'" icon="ios-create" name="1">
                            <List item-layout="vertical">
                                <ListItem v-for="(comment,index) in commentList" :key="index" v-show="index<commentShowNum">
                                    <ListItemMeta slot="header"/>
                                    <slot name="header">
                                        <div class="ivu-list-item-meta">
                                            <div class="ivu-list-item-content">
                                                <div class="ivu-list-item-meta-description" >
                                                    <Icon type="md-time" /> <Time :time="comment.date" />
                                                </div></div></div></slot>
                                    <div v-html="comment.comment"></div>
                                    <template slot="action">
                                        <li>
                                            <Rate class="commentRate" allow-half v-model="comment.score" disabled>
                                            </Rate>
                                        </li>
                                        <li>
                                            <Icon size="15" type="ios-thumbs-up" class="activeLikeValue"  @click="addLikeValue(comment.commentid)"/>
                                            <span>{{comment.likevalue}}</span>
                                        </li>
                                        <li v-if="existDeleteComment">
                                            <Poptip :transfer="true"
                                                    confirm
                                                    title="确定删除这条评论吗?"
                                                    @on-ok="deleteComment(comment.commentid,comment.score,comment.movieid)">
                                          <span class="liClass"> <Icon type="ios-trash" />删除</span>
                                            </Poptip>
                                        </li>
                                    </template>
                                    <template slot="extra">
                                    <Tooltip :content="comment.moviename" placement="left" theme="light">
                                        <img :src="comment.movieimg" class="clickClass" @click="toMovieInfo(comment.movieid)" style="height: 100px;">
                                    </Tooltip>
                                    </template>
                                </ListItem>
                            </List>
                            <div>
                                <Button style="margin-top: 30px" type="primary" v-show="commentShowNum<commentList.length" @click="commentShowMore" :loading="loading" long>加载更多</Button>
                                <Button style="margin-top: 30px" v-show="commentShowNum>=commentList.length" disabled long>莫得了</Button>
                            </div>
                        </TabPane>
                        <TabPane :label="'收藏'+' ('+collectMovieList.length+')'" icon="ios-heart"  name="2">
                            <Row>
                                    <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 12, offset: 0 }" :md="{ span: 12, offset: 0 }" :lg="{ span: 5, offset: 1 }"
                                         v-for="(movie,index) in collectMovieList" :key="index"  v-show="index<movieShowNum" style="margin-top: 20px">
                                        <Card :style="'background:'+color" >
                                        <img @click="toMovieInfo(movie.movieid)" class=" clickClass" :src="movie.movieimg" height="300px" width="100%"  alt="movie.moviename"/>
                                        <Rate class="Rate" disabled show-text v-model="movie.moviescore/2"><span style="color: #f5a623">{{movie.moviescore}}</span></Rate>
                                        <div @click="toMovieInfo(movie.movieid)" class="moviename clickClass">{{movie.moviename}}</div>
                                            <div class="collectDate">收藏于 <Time :time="movie.collectdate" /></div>
                                        </Card>
                                    </Col>
                                    <div>
                                        <Button style="margin-top: 30px" type="primary" v-show="movieShowNum<collectMovieList.length" @click="movieShowMore" :loading="loading1" long>加载更多</Button>
                                        <Button style="margin-top: 30px" v-show="movieShowNum>=collectMovieList.length" disabled long>莫得了</Button>
                                    </div>
                            </Row>
                        </TabPane>
                        <TabPane :label="'留言'+' ('+messageList.length+')'" icon="md-chatboxes" name="3">
                            <Row>
                                <List>
                                    <li class="ivu-list-item" style="width:100%" v-for="(message,index) in messageList" :key="index" v-show="index<messageShowNum">
                                        <div class="ivu-list-item-meta-avatar"  @click="toUserInfo(message.myuserid)">
                                            <Avatar :src="message.userimg" style="cursor: pointer"></Avatar>
                                        </div>
                                        <div class="ivu-list-item-meta">
                                            <div class="ivu-list-item-content">
                                                <div class="ivu-list-item-meta-title" @click="toUserInfo(message.myuserid)">
                                                    {{message.username}}
                                                </div>
                                                <div class="ivu-list-item-meta-description" style="color: #515a6e">
                                                    {{message.message}}
                                                </div>
                                            </div>
                                        </div>
                                        <ul class="ivu-list-item-action">
                                            <li><Icon type="md-time" /> <Time :time="message.messagedate" /></li>
                                            <li v-if="message.myuserid===loginUser.userid||user.userid===loginUser.userid">
                                                <Poptip :transfer="true"
                                                        confirm
                                                        title="确定删除这条留言吗?"
                                                        @on-ok="deleteMessage(message.myuserid,message.otheruserid,message.messagedate)">
                                                    <span class="liClass">删除</span>
                                                </Poptip></li>
                                        </ul>
                                    </li>
                                </List>
                                <div>
                                    <Button style="margin-top: 30px" type="primary" v-show="messageShowNum<messageList.length" @click="messageShowMore" :loading="loading2" long>加载更多</Button>
                                    <Button style="margin-top: 30px" v-show="messageShowNum>=messageList.length" disabled long>莫得了</Button>
                                </div>
                            </Row>
                        </TabPane>
                        <TabPane :label="'话题'+' ('+topicList.length+')'" icon="ios-chatbubbles" name="7">
                            <Row>
                                <List item-layout="vertical">
                                    <ListItem v-for="(topic,index) in topicList" :key="index"  v-show="index<topicShowNum">
                                        <ListItemMeta slot="header"/>
                                        <slot name="header">
                                            <div class="ivu-list-item-meta">
                                                <div class="ivu-list-item-meta-avatar clickClass" @click="toUserInfo(topic.userid)">
                                                    <Avatar class="activeLikeValue " :src="topic.userimg" ></Avatar>
                                                </div>
                                                <div class="ivu-list-item-content">
                                                    <div class="ivu-list-item-meta-title" @click="toUserInfo(topic.userid)">
                                                        {{topic.username}}
                                                    </div>
                                                    <div class="ivu-list-item-meta-description" >
                                                        <Icon type="md-time" /> <Time :time="topic.topicdate" />
                                                    </div></div></div></slot>
                                        <div class="clickClass" @click="toTopicContent(topic)">
                                            <div style="text-align: center;font-size: 20px">{{topic.title}}</div>
                                            <div style="font-size:13px;margin-top: 10px;text-align: center">{{topic.describe}}</div>
                                        </div>
                                        <template slot="extra">
                                            <Tooltip :content="topic.moviename" placement="right" theme="light">
                                                <img :src="topic.movieimg" class="clickClass" style="height: 120px;margin-top:50%" @click="toMovieInfo(topic.movieid)">
                                            </Tooltip>
                                        </template>
                                    </ListItem>
                                </List>
                                <div>
                                    <Button style="margin-top: 30px" type="primary" v-show="topicShowNum<topicList.length" @click="topicShowMore" :loading="loading3" long>加载更多</Button>
                                    <Button style="margin-top: 30px" v-show="topicShowNum>=topicList.length" disabled long>莫得了</Button>
                                </div>
                            </Row>
                        </TabPane>
                        <TabPane :label="'关注'+' ('+friendData.myLikely.length+')'" icon="ios-people" name="4">
                            <Row>
                            <Col :xs="8" :sm="8" :md="6" :lg="3"
                                    v-for="(user,index) in friendData.myLikely" :key="index">
                            <Card  :style="'background:'+color+';width:100px;text-align: center'" >
                                <div @click="toUserInfo(user.userid)" >
                                    <Avatar class="clickClass" :src="user.userimg" size="50"></Avatar>
                                </div>
                                <div class="clickClass" @click="toUserInfo(user.userid)">{{user.username}}</div>
                            </Card>
                            </Col>
                            </Row>
                        </TabPane>
                        <TabPane :label="'粉丝'+' ('+friendData.myFans.length+')'" icon="md-people" name="5">
                            <Row>
                                <Col :xs="8" :sm="8" :md="6" :lg="3"
                                     v-for="(user,index) in friendData.myFans" :key="index">
                                    <Card :style="'background:'+color+';width:100px;text-align: center'">
                                        <div @click="toUserInfo(user.userid)" >
                                            <Avatar class="clickClass" :src="user.userimg" size="50"></Avatar>
                                        </div>
                                        <div class="clickClass" @click="toUserInfo(user.userid)">{{user.username}}</div>
                                    </Card>
                                </Col>
                            </Row>
                        </TabPane>
                        <TabPane :label="'好友'+' ('+friendData.myFriend.length+')'" icon="ios-person" name="6">
                            <Row>
                                <Col :xs="8" :sm="8" :md="6" :lg="3"
                                     v-for="(user,index) in friendData.myFriend" :key="index">
                                    <Card :style="'background:'+color+';width:100px;text-align: center'">
                                        <div @click="toUserInfo(user.userid)" >
                                            <Avatar class="clickClass" :src="user.userimg" size="50"></Avatar>
                                        </div>
                                        <div class="clickClass" @click="toUserInfo(user.userid)">{{user.username}}</div>
                                    </Card>
                                </Col>
                            </Row>
                        </TabPane>
                    </Tabs>
                </Card>
            </Col>
        </Row>
    </div>
</template>

<script>
    export default {
        name: "UserInfo",
        data(){
            return{
                user:{},
                loginUser:JSON.parse(localStorage.getItem('loginUser')),
                friendData:{
                    myLikely:[],
                    myFans:[],
                    myFriend:[]
                },
                commentList:[],
                collectMovieList:[],
                messageList:[],
                topicList:[],
                tabValue:'1',
                existOther:false,
                existLike:false,
                existDeleteComment:false,
                commentShowNum:4,
                movieShowNum:4,
                messageShowNum:7,
                topicShowNum:2,
                messageModal:false,
                messageValue:'',
                spinShow:true,
                loading:false,
                loading1:false,
                loading2:false,
                loading3:false,
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            deleteMessage:function(myuserid,otheruserid,messagedate){
                this.axios.get('/customer/deleteMessage?myuserid='+myuserid+'&otheruserid='+otheruserid+'&messagedate='+messagedate)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('删除成功!');
                            this.getUserInfo();
                        }else {
                            this.$Message.error('删除失败!');
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            deleteComment:function(commentid,score,movieid){
                console.log(commentid,score,movieid);
           this.axios.get('movie/deleteComment?commentid='+commentid+'&score='+score+'&movieid='+movieid)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('删除成功!');
                            this.getUserInfo();
                        }else {
                            this.$Message.error('删除失败!');
                        }
                    }).catch(err=>{
                        console.log(err)
                })
            },
            commentShowMore(){
                this.loading=true;
                setTimeout(()=>{
                    this.commentShowNum =this.commentShowNum+3;
                    this.loading=false;
                },500);
            },
            movieShowMore(){
                this.loading1=true;
                setTimeout(()=>{
                    this.movieShowNum =this.movieShowNum+3;
                    this.loading1=false;
                },500);
            },
            messageShowMore(){
                this.loading2=true;
                setTimeout(()=>{
                    this.messageShowNum =this.messageShowNum+3;
                    this.loading2=false;
                },500);
            },
            topicShowMore(){
                this.loading3=true;
                setTimeout(()=>{
                    this.topicShowNum =this.topicShowNum+2;
                    this.loading3=false;
                },500);
            },
            getUserInfo:function () {
                if(this.$route.query.tabValue!==null){
                    this.tabValue=this.$route.query.tabValue
                }
                else {
                    this.tabValue='1'
                }
                let userid;
                if(this.$route.query.userid!==undefined&&this.$route.query.userid!==JSON.parse(localStorage.getItem('loginUser')).userid){
                    userid=this.$route.query.userid;
                    this.existOther=true;
                    this.existDeleteComment=false;
                }else{
                    this.existOther=false;
                    userid=JSON.parse(localStorage.getItem('loginUser')).userid
                    this.existDeleteComment=true;
                }
                this.axios.get('/customer/getUserInfo?userid='+userid)
                    .then(res=>{
                        console.log(res);
                        this.user=res.data.data.user;
                        this.friendData=res.data.data.friendInfo;
                        let fansNameArr=[];
                        for(let fans of this.friendData.myFans){
                            fansNameArr.push(fans.userid)
                        }
                        if(this.$route.query.userid!==undefined&&fansNameArr.indexOf(JSON.parse(localStorage.getItem('loginUser')).userid)>-1){
                            this.existLike=true
                        }
                        this.commentList=res.data.data.commentList;
                        this.collectMovieList=res.data.data.collectMovieList;
                        this.messageList=res.data.data.messageList;
                        this.topicList=res.data.data.topicList;
                        this.spinShow=false
                    }).catch(err=>{
                    console.log(err)
                })
            },
            addLikeValue:function (id) {
                let commentid=this.$qs.stringify({commentid:id})
                this.$axios.post('/customer/addCommentLike',commentid)
                    .then(res=>{
                        if(res.data.data){
                            this.getUserInfo();
                        }else {
                            this.$Message.error("点赞失败")
                        }
                    }).catch(err=>{
                    console.log(err)
                })
            },
            tabChange:function (name) {
                if(name==='1'){
                    this.movieShowNum=4;
                    this.messageShowNum=7;
                    this.topicShowNum=2;
                }if(name==='2'){
                    this.commentShowNum=4;
                    this.messageShowNum=7;
                    this.topicShowNum=2;
                }if(name==='3'){
                    this.movieShowNum=4;
                    this.commentShowNum=4;
                    this.topicShowNum=2;
                }if(name==='7'){
                    this.movieShowNum=4;
                    this.commentShowNum=4;
                    this.messageShowNum=7;
                }
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
            toUserInfo:function (userid) {
                if(userid!==JSON.parse(localStorage.getItem('loginUser')).userid){
                    this.$router.push({path:'/userInfo',query:{userid:userid}});
                }else {
                    this.$router.push('/userInfo');
                }
            },
            toTopicContent:function (topic) {
                this.$router.push({path:'/topicContent',query:{topic:JSON.stringify(topic)}})
            },
            submitMessage:function () {
                let message=this.$qs.stringify({
                    message:this.messageValue,
                    myuserid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    otheruserid:this.user.userid
                });
                this.axios.post('/customer/addMessage',message)
                    .then(res=>{
                        console.log(res);
                        this.$Message.success('成功留言!');
                        this.messageModal=false;
                        this.messageValue='';
                        this.getUserInfo();
                    }).catch(err=>{
                        console.log(err);
                })
            },
            existFriend:function () {
                let frienduserid=[];
                for(let friend of this.friendData.myFriend){
                    frienduserid.push(friend.userid);
                }
                if(frienduserid.indexOf(JSON.parse(localStorage.getItem('loginUser')).userid)>-1){
                    this.messageModal=true;
                }else {
                    this.$Message.info('互相关注成为好友才可以留言哦');
                }
            },
            addLikely:function () {
                this.axios.get('customer/addLikely?userid='+JSON.parse(localStorage.getItem('loginUser')).userid+'&likelyUserid='+this.user.userid
                )
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('关注成功!');
                            this.getUserInfo();
                        }else {
                            this.$Message.error('关注失败!');
                        }
                    }).catch(err=>{
                        console.log(err)
                })
            },
            removeLikely:function () {
                this.axios.get('customer/removeLikely?userid='+JSON.parse(localStorage.getItem('loginUser')).userid+'&likelyUserid='+this.user.userid
                    )
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('成功取消关注!');
                            this.existLike=false;
                        }else {
                            this.$Message.error('取消关注失败!');
                        }
                    }).catch(err=>{
                    console.log(err)
                })
            },

        },
        created(){
            this.getUserInfo();
        },
        watch: {   //监听路由变化
            "$route" (to, from) {
                this.getUserInfo();
            },
        }
    }
</script>

<style lang="less" scoped>
    .friend{
        margin-left: 25%;
        font-size: 13px;
    }
    .friend1{
        margin-left:10%;
        font-size: 13px;
    }
    span.friend:hover{
          color: #3399ff;
          cursor: pointer;
      }
    span.friend1:hover{
        color: #3399ff;
        cursor: pointer;
    }
    .friendNum1{
        margin-left:12%;
        font-size: 13px;
    }
    .friendNum2{
        margin-left: 30%;
        font-size: 13px;
    }
    .friendNum3{
        margin-left: 31%;
        font-size: 13px;
    }
    .activeLikeValue{
        cursor: pointer;
    }
    i.activeLikeValue:hover{
        color: red;
    }
    .Rate{
        font-size: 20px;
    }
    .moviename{
        text-align: center;
        font-size: 13px;
    }
    .clickClass{
        cursor: pointer;
    }
    div.clickClass:hover{
        color: #3399ff;
    }
    span.liClass:hover{
        color: red;
    }
    .collectDate{
        text-align: center;
        margin-top: 5px;
        font-size: 12px;
        color:#aaa;
    }
</style>