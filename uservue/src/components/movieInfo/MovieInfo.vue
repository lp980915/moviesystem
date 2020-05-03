<template>
    <div>
    <Card :bordered="false"  :style="'background:'+color">
        <Spin fix v-if="spinShow"></Spin>
        <Row>
            <Col class=titleClass :xs="{span:24,offset:0}" :sm="{span:18,offset:1}" :md="{span:12,offset:1}" :lg="{span:8,offset:2}">
                <h1 class="movieTitle">{{movie.moviename}}<span style="font-size: 20px;color: #aaaaaa">  ({{moviecountry}})</span></h1>
                <img :src="movie.movieimg" height="350px" width="250px" :alt="movie.moviename">
                <div class="smallTitleLayout">上映时间:{{movie.date}}</div>
                <div class="smallTitleLayout">
                <Tag color="blue" :fade="true" v-for="(sort,index) in moviesort" :key="index">{{sort}}</Tag>
                </div>
                <div class="collectBox">
                    <Icon type="ios-heart" size="20" class="activeClick" :color="collectIconColor" @click="collectMovie"/>
                    <span style="font-size: 13px" :style="collectColor">{{collectText}}</span>
                    <span style="font-size: 10px;margin-left: 10px">({{movie.movielike}}人收藏了这部电影)</span>
                </div>
            </Col>
            <Col :xs="{span:24,offset:0}" :sm="{span:18,offset:1}" :md="{span:12,offset:1}" :lg="{span:8,offset:5}" class="rightDiv">
                <div>({{movie.scoreperson}}人参与了评价)</div>
                <Rate show-text allow-half v-model="movie.moviescore/2" disabled>
                    <span style="font-size: 35px">{{ movie.moviescore }}</span>
                </Rate>
                <div>五星:<Progress :percent="Number.parseFloat(fivestar)"  :stroke-color="['#108ee9', '#87d068']" style="width: 300px"/><span style="font-size: 8px">({{movie.fivestar}}人)</span></div>
                <div>四星:<Progress :percent="Number.parseFloat(fourstar)"  :stroke-color="['#108ee9', '#87d068']" style="width: 300px"/><span style="font-size: 8px">({{movie.fourstar}}人)</span></div>
                <div>三星:<Progress :percent="Number.parseFloat(threestar)"  :stroke-color="['#108ee9', '#87d068']" style="width: 300px"/><span style="font-size: 8px">({{movie.threestar}}人)</span></div>
                <div>二星:<Progress :percent="Number.parseFloat(twostar)" :stroke-color="['#108ee9', '#87d068']" style="width: 300px"/><span style="font-size: 8px">({{movie.twostar}}人)</span></div>
                <div>一星:<Progress :percent="Number.parseFloat(onestar)"  :stroke-color="['#108ee9', '#87d068']" style="width: 300px"/><span style="font-size: 8px">({{movie.onestar}}人)</span></div>
                <Button type="success" icon="ios-brush" size="small" class="commentBtn" @click="commentModal=true">写影评</Button>
                <Button type="info" icon="ios-chatbubbles" size="small" class="commentBtn" style="margin-left: 10px" @click="addTopicModal=true">发起话题</Button>
                <Modal
                        width="70%"
                        :mask-closable="false"
                        v-model="commentModal">
                    <p slot="header" style="text-align:center">
                        <Icon type="ios-brush"></Icon>
                        <span>写影评</span>
                    </p>
                    <Form  :model="commentForm" ref="commentForm">
                        <FormItem prop="addMovieScore">
                            <div  style="margin:10px 40%;font-size:13px ">
                                <span>评分:</span><Rate v-model="commentForm.addMovieScore"/>
                            </div>
                        </FormItem>
                        <FormItem prop="addMovieComment">
                            <quill-editor
                                    v-model="commentForm.addMovieComment"
                                    ref="commentRef">
                            </quill-editor>
                        </FormItem>
                    </Form>
                    <div slot="footer">
                        <Button type="primary" @click="submitComment" :disabled="commentForm.addMovieComment===''">提交</Button>
                        <Button   @click="commentModal=false" >取消</Button>
                    </div>
                </Modal>

                <Modal
                        v-model="addTopicModal"
                        :mask-closable="false"
                        width="50%">
                    <p slot="header" style="text-align:center">
                        <Icon type="ios-chatbubbles" ></Icon>
                        <span>发起一个话题</span>
                    </p>
                    <div style="text-align:center">
                        <Form label-position="left">
                            <FormItem label="标题:" :label-width="50">
                            <Input  v-model="title"  placeholder="请填写标题..."
                                    maxlength="50" />
                            </FormItem>
                            <FormItem label="描述:" :label-width="50">
                        <Input  v-model="describe" type="textarea" :autosize="{minRows: 5,maxRows: 10}"  placeholder="请填写话题描述..."
                                maxlength="400" />
                            </FormItem>
                        </Form>
                    </div>
                    <div slot="footer">
                        <Button type="primary" @click="submitTopic" :disabled="title===''||describe===''">提交</Button>
                        <Button @click="addTopicModal=false" >取消</Button>
                    </div>
                </Modal>

            </Col>

        </Row>
        <Card  :style="'background:'+color" class="introCard">
            <p slot="title">{{movie.moviename}}的简介:</p>
            <p>{{movie.movieintro}}</p>
        </Card>
        <Row>
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2">
            </Col>
            <Col :xs="24" :sm="16" :md="13" :lg="13"><span class="smallTitle">{{movie.moviename}}的演职员:</span><Divider /></Col>
        </Row>
        <Row>
            <Col class="titleClass":xs="0" :sm="0" :md="1" :lg="2"></Col>
            <Col :xs="24" :sm="12" :md="8" :lg="4" v-for="(actor,index) in movieactor" :key="index">
                <img :src="actor.actorimg" height="250px" width="170px" class="clickClass" @click="toActorInfo(actor.actorid)">
                <div class="actorname clickClass" @click="toActorInfo(actor.actorid)">{{actor.actorname}}</div>
                <div class="position">{{actor.position}}</div>
           </Col>
        </Row>
        <Row class="smallTitleLayout" v-show="commentList.length>0">
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2"></Col>
            <Col :xs="24" :sm="16" :md="13" :lg="13"><span class="smallTitle">{{movie.moviename}}的影评:</span><Divider /></Col>
        </Row>
        <Row class="commentRow" v-for="(comment,index) in commentList" :key="index" v-show="commentList.length>0">
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2" ></Col>
           <Col :xs="24" :sm="16" :md="13" :lg="13">
               <span @click="toUserInfo(comment.userid)"><Avatar :src="comment.userimg" class="clickClass" /></span>
               <span class="clickClass commentNameLayout" @click="toUserInfo(comment.userid)">{{comment.username}}</span>
               <Rate class="commentRate" allow-half v-model="comment.score" disabled>
               </Rate>
               <span class="dateClass"> <Icon type="md-time" /> <Time :time="comment.date" /></span>
               <div class="likevalueClass"><Icon size="15" type="ios-thumbs-up-outline" class="activeLikeValue" @click="addLikeValue(comment.commentid)"/><span>{{comment.likevalue}}</span></div>
                <div class="comment" v-html="comment.comment"></div>
                <Divider /></Col>
        </Row>
        <Row class="commentRow" v-show="commentList.length>0">
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2" ></Col>
            <Col :xs="24" :sm="16" :md="13" :lg="13">
                <Page style="margin-top: 15px" show-total :total="this.pageForm.total"
                      size="small" show-elevator show-sizer :page-size="5"
                      :page-size-opts="[2, 5, 10, 20]"
                      @on-change="handleCurrentChange"
                      @on-page-size-change="handleSizeChange"/>
            </Col>
        </Row>


        <Row v-show="topicList.length>0" style="margin-top:80px">
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2"></Col>
            <Col :xs="24" :sm="16" :md="13" :lg="13"><span class="smallTitle">{{movie.moviename}}的话题:</span><Divider /></Col>
        </Row>
        <Row v-show="topicList.length>0">
            <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2" ></Col>
            <Col :xs="24" :sm="16" :md="13" :lg="13">
                <Card  :style="'background:'+color">
                    <List item-layout="vertical">
                        <ListItem v-for="(topic,index) in topicList" :key="index">
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
                                    <img :src="topic.movieimg" style="height: 120px;margin-top:50%">
                                </Tooltip>
                            </template>
                        </ListItem>
                    </List>
                </Card>
            </Col>
        </Row>
    </Card>
    </div>
</template>

<script>
    import { quillEditor } from 'vue-quill-editor' // 调用富文本编辑器
    import 'quill/dist/quill.snow.css' // 富文本编辑器外部引用样式  三种样式三选一引入即可
    export default {
        name: "MovieInfo",
        components:{quillEditor},
        data(){
            return{
                movie:{},
                moviecountry:'',
                commentList:[],
                moviesort:[],
                movieactor:[],
                onestar:'',
                twostar:'',
                threestar:'',
                fourstar:'',
                fivestar:'',
                collectIconColor:'',
                collectColor:'',
                collectText:'收藏',
                commentModal:false,
                commentForm:{
                    addMovieScore:1,
                    addMovieComment:''
                },
                pageForm:{
                    current:1,
                    size:5,
                    total:0
                },
                spinShow:true,
                addTopicModal:false,
                title:'',
                describe:'',
                topicList:[],
                color:sessionStorage.getItem('color')
            }
        },
        created:function () {
            this.getMovieInfo();
        },
        methods:{
            toTopicContent:function (topic) {
                this.$router.push({path:'/topicContent',query:{topic:JSON.stringify(topic)}})
            },
            submitTopic:function(){
                let topic=this.$qs.stringify({
                    userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    movieid:this.movie.movieid,
                    title:this.title,
                    describe:this.describe
                })
                this.axios.post('/customer/addTopic',topic)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('成功发起话题!');
                            this.addTopicModal=false;
                            this.title='';
                            this.describe='';
                            this.getMovieInfo();
                        }else {
                            this.$Message.error('发起话题失败!');
                        }
                    }).catch(err=>{
                        console.log(err)
                })
            },
            submitComment:function(){
                let comment=this.$qs.stringify({
                    userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    movieid:this.movie.movieid,
                    comment:this.commentForm.addMovieComment,
                    score:this.commentForm.addMovieScore,
                });
                this.axios.post('/customer/addComment',comment)
                    .then(res=>{
                        console.log(res)
                        if(res.data.data){
                            this.$Message.success('评论成功');
                            this.commentModal=false;
                            this.getMovieInfo()
                            this.$refs['commentRef'].resetFields();
                        }else{
                            this.$Message.error('评论失败')
                        }
                    }).catch(err=>{
                        console.log(err)
                })
            },
            collectMovie:function () {
                if(this.collectIconColor!=='red'){
                    let data=this.$qs.stringify({
                        'userid':JSON.parse(localStorage.getItem('loginUser')).userid,
                        'movieid':this.movie.movieid
                    });
                    this.axios.post('/customer/collectMovie',data)
                        .then(res=>{
                            console.log(res)
                            if(res.data.data){
                                this.collectIconColor='red';
                                this.collectColor='color:red';
                                this.collectText='已收藏';
                                this.getMovieInfo();
                            }else {
                                this.$Message.error('收藏失败');
                            }
                        }).catch(err=>{
                        console.log(err)
                    })
                }else{
                    let data=this.$qs.stringify({
                        'userid':JSON.parse(localStorage.getItem('loginUser')).userid,
                        'movieid':this.movie.movieid
                    });
                    this.axios.post('/customer/removeCollect',data)
                        .then(res=>{
                            console.log(res);
                            if(res.data.data){
                                this.collectIconColor='';
                                this.collectColor='';
                                this.collectText='收藏';
                                this.getMovieInfo();
                            }else {
                                this.$Message.error('取消收藏失败');
                            }
                        }).catch(err=>{
                        console.log(err)
                    })
                }

            },
            getMovieInfo:function () {
                this.axios.get('/customer/getMovieInfoById?movieid='+this.$route.query.movieid+'&userid='+JSON.parse(localStorage.getItem('loginUser')).userid
                    +'&current='+this.pageForm.current+'&size='+this.pageForm.size)
                    .then(res=>{
                        console.log(res);
                        this.movie=res.data.data.movie;
                        this.moviecountry=res.data.data.moviecountry;
                        this.commentList=res.data.data.commentList.records;
                        this.pageForm.total=res.data.data.commentList.total;
                        this.moviesort=res.data.data.moviesort;
                        this.movieactor=res.data.data.movieactor;
                        this.topicList=res.data.data.topicList;
                        if(res.data.data.existCollect){
                            this.collectIconColor='red';
                            this.collectColor='color:red';
                            this.collectText='已收藏';
                        }
                        this.onestar=((Number.parseInt(this.movie.onestar)/(Number.parseInt(this.movie.onestar)+Number.parseInt(this.movie.twostar)+Number.parseInt(this.movie.threestar)+Number.parseInt(this.movie.fourstar)+Number.parseInt(this.movie.fivestar)))*100).toFixed(1);
                        this.twostar=((Number.parseInt(this.movie.twostar)/(Number.parseInt(this.movie.onestar)+Number.parseInt(this.movie.twostar)+Number.parseInt(this.movie.threestar)+Number.parseInt(this.movie.fourstar)+Number.parseInt(this.movie.fivestar)))*100).toFixed(1);
                        this.threestar=((Number.parseInt(this.movie.threestar)/(Number.parseInt(this.movie.onestar)+Number.parseInt(this.movie.twostar)+Number.parseInt(this.movie.threestar)+Number.parseInt(this.movie.fourstar)+Number.parseInt(this.movie.fivestar)))*100).toFixed(1);
                        this.fourstar=((Number.parseInt(this.movie.fourstar)/(Number.parseInt(this.movie.onestar)+Number.parseInt(this.movie.twostar)+Number.parseInt(this.movie.threestar)+Number.parseInt(this.movie.fourstar)+Number.parseInt(this.movie.fivestar)))*100).toFixed(1);
                        this.fivestar=((Number.parseInt(this.movie.fivestar)/(Number.parseInt(this.movie.onestar)+Number.parseInt(this.movie.twostar)+Number.parseInt(this.movie.threestar)+Number.parseInt(this.movie.fourstar)+Number.parseInt(this.movie.fivestar)))*100).toFixed(1);
                        this.spinShow=false
                    }).catch(err=>{
                    console.log(err)
                });
            },
            addLikeValue:function (id) {
                let commentid=this.$qs.stringify({commentid:id});
                this.$axios.post('/customer/addCommentLike',commentid)
                    .then(res=>{
                        if(res.data.data){
                            this.getMovieInfo()
                        }else {
                            this.$Message.error("点赞失败")
                        }
                    }).catch(err=>{
                    console.log(err)
                })
            },
            toActorInfo:function (actorid) {
                this.$router.push({path:'/actorInfo',query:{actorid:actorid}})
            },
            toUserInfo:function (userid) {
                this.$router.push({path:'/userInfo',query:{userid:userid}})
            },
            handleCurrentChange:function (current) {
                this.pageForm.current=current;
                this.getMovieInfo()
            },
            handleSizeChange:function (size) {
                this.pageForm.size=size;
                this.getMovieInfo()
            },
        }
    }
</script>

<style lang="less" scoped>

.ivu-card{
    margin: 20px;
    .introCard{
        margin: 8%;
    }
    .movieTitle{
        margin-bottom: 4%;
    }
  }
.clickClass{
    cursor: pointer;
}

span.clickClass:hover{
    color: #3399ff;
}
div.clickClass:hover{
    color: #3399ff;
}
.activeClick{
    cursor: pointer;
}
i.activeClick:hover{
    color: red;
}
.commentNameLayout{
    margin-left: 1%;
}
.smallTitle{
    font-size: 20px;
}
.smallTitleLayout{
    margin-top: 3%;
}
.collectBox{
    margin-top: 3%;
}
    .titleClass{
        margin-top: 2%;
    }
    .rightDiv {
        margin-top: 12%;
    }
    .commentBtn{
        width: 150px;
        margin-top: 5%;
    }
    .actorname{
        width: 150px;
        text-align: center;
    }
    .position{
        width: 150px;
        text-align: center;
    }
    .commentRate{
        font-size: 13px;
        margin-left: 1%;
    }
    .comment{
        margin-top: 1%;
        font-size: 13px;
    }
    .dateClass{
        margin-left: 1%;
        font-size: 10px;
        color:#aaa;
    }
    .likevalueClass{
        margin-left: 90%;
        span{
            font-size: 13px;
            margin-left: 1%;
            color:#aaa;
        }
    }
    .activeLikeValue{
        cursor: pointer;
    }
    i.activeLikeValue:hover{
        color: red;
    }

</style>