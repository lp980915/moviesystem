<template>
  <div>
    <Carousel
            :autoplay="true"
            :autoplay-speed="2000"
            dots="inside"
            :radius-dot="true"
            trigger="hover"
            arrow="hover"
            loop>
        <CarouselItem>
          <div class="demo-carousel">
            <img src="../../assets/img/carousel/shenghua.jpg"  width="50%"/>
            <img src="../../assets/img/carousel/shenghua2.jpg"  width="50%"/>
          </div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel">
            <img src="../../assets/img/carousel/dasheng.jpg" width="50%"/>
              <img src="../../assets/img/carousel/baishe.jpg" width="50%"/></div>
        </CarouselItem>
        <CarouselItem>
          <div class="demo-carousel">
            <img src="../../assets/img/carousel/mingzi.jpg"  width="50%"/>
            <img src="../../assets/img/carousel/mingzi2.jpg" width="50%"/></div>
        </CarouselItem>
    </Carousel>
   <div class="topMovie">今日电影推荐</div>
      <Divider />
      <Row><Col class="demo-spin-col" span="8">
      </Col>
          <Col :xs="24" :sm="12" :md="12" :lg="4" v-for="(movie,index) in movieList" :key="index">
              <Card :style="'background:'+color+';width:230px;height:370px;margin-left:9%'" >
                  <Spin fix v-if="spinShow"></Spin>
                  <div style="text-align:center">
                      <img @click="movieInfo(movie.movieid)" :src="movie.movieimg" width="100%" height="260px" class="point">
                      <h3 @click="movieInfo(movie.movieid)" class="point movieName">{{movie.moviename}}</h3>
                      <Rate show-text allow-half v-model="movie.moviescore/2" disabled class=" movieName">
                          <span style="color: #f5a623">{{ movie.moviescore }}</span>
                      </Rate>
                  </div>
              </Card>
          </Col>
      </Row>
      <Row class="topComment">
          <Col :xs="0" :sm="0" :md="1" :lg="2">
          </Col>
          <Col :xs="24" :sm="16" :md="18" :lg="18"><span>热门影评:</span><Divider /></Col>
      </Row>
      <Row>
          <Col :xs="0" :sm="0" :md="1" :lg="2">
          </Col>
          <Col :xs="24" :sm="16" :md="18" :lg="18">
              <Card :style="'backgroundu:'+color+';margin-left: 17%'" >
                  <Spin fix v-if="spinShow"></Spin>
                  <List item-layout="vertical">
                      <ListItem v-for="(comment,index) in topCommentList" :key="index">
                          <ListItemMeta slot="header"/>
                          <slot name="header">
                              <div class="ivu-list-item-meta">
                                  <div class="ivu-list-item-meta-avatar" @click="toUserInfo(comment.userid)">
                                      <Avatar class="activeLikeValue" :src="comment.userimg" ></Avatar>
                                  </div>
                                  <div class="ivu-list-item-content">
                                      <div class="ivu-list-item-meta-title" @click="toUserInfo(comment.userid)">
                                          {{comment.username}}
                                      </div>
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
                                  <Icon size="15" type="ios-thumbs-up" class="activeLikeValue" @click="addLikeValue(comment.commentid)"/>
                                  <span>{{comment.likevalue}}</span>
                              </li>
                          </template>
                          <template slot="extra">
                              <Tooltip :content="comment.moviename" placement="right" theme="light">
                              <img :src="comment.movieimg" class="clickClass" style="height: 120px;" @click="movieInfo(comment.movieid)">
                              </Tooltip>
                          </template>
                      </ListItem>
                  </List>
              </Card>
          </Col>
      </Row>
      <Row class="topComment">
          <Col :xs="0" :sm="0" :md="1" :lg="2">
          </Col>
          <Col :xs="24" :sm="16" :md="18" :lg="18"><span>电影精彩片段:</span><Divider /></Col>
      </Row>
           <div style="margin-left:13%">
             <span style="cursor: pointer" @click="bianfuxia()">
                 <Tooltip content="蝙蝠侠:黑暗骑士" placement="top" theme="light">
             <Avatar shape="square" :src="bianfuImg" size="80" />
                 </Tooltip>
             </span>
              <span style="margin-left:4%;cursor: pointer" @click="gangqinshi()">
                  <Tooltip content="海上钢琴师" placement="top" theme="light">
             <Avatar shape="square" :src="gangqinImg" size="80" />
                  </Tooltip>
              </span>
             <span style="margin-left:4%;cursor: pointer" @click="jiaofu()">
                 <Tooltip content="教父" placement="top" theme="light">
             <Avatar shape="square" :src="jiaofuImg" size="80" />
                 </Tooltip>
              </span>
             <span style="margin-left:4%;cursor: pointer" @click="mingzi()">
                 <Tooltip content="你的名字" placement="top" theme="light">
             <Avatar shape="square" :src="mingziImg" size="80" />
                 </Tooltip>
             </span>
           </div>

      <Modal v-model="videoOneModal" width="45%" footer-hide>
          <span style="margin-left: 40%">{{videoTitle}}</span>
          <video :src="videoSrc" controls="controls" style="margin:5% 15%;width:70%;height:70%">
              Your browser does not support the video tag.
          </video>
      </Modal>

  </div>
</template>

<script>
    export default {
        name: "Welcome",
        data(){
          return{
              movieList:[],
              topCommentList:[],
              videoOneModal:false,
              videoSrc:'',
              videoTitle:'',
              bianfuImg:require("../../assets/img/videoImg/bianfu.jpg"),
              gangqinImg:require("../../assets/img/videoImg/gangqin.jpg"),
              jiaofuImg:require("../../assets/img/videoImg/jiaofu.jpg"),
              mingziImg:require("../../assets/img/videoImg/mingzi.jpg"),
              spinShow:true,
              color:sessionStorage.getItem('color')
          }
        },
        methods:{
            bianfuxia:function(){
                this.videoSrc=require('@/assets/video/bianfuxia.mp4');
                this.videoTitle='蝙蝠侠:黑暗骑士';
               this.videoOneModal=true
            },
            gangqinshi:function(){
                this.videoSrc=require('@/assets/video/1900.mp4');
                this.videoTitle='海上钢琴师';
                this.videoOneModal=true
            },
            jiaofu:function(){
                this.videoSrc=require('@/assets/video/jiaofu.mp4');
                this.videoTitle='教父';
                this.videoOneModal=true
            },
            mingzi:function(){
                this.videoSrc=require('@/assets/video/mingzi.mp4');
                this.videoTitle='你的名字';
                this.videoOneModal=true
            },
            addLikeValue:function (id) {
                let commentid=this.$qs.stringify({commentid:id})
                this.$axios.post('/customer/addCommentLike',commentid)
                    .then(res=>{
                        if(res.data.data){
                            this.getTopComment();
                        }else {
                            this.$Message.error("点赞失败")
                        }
                    }).catch(err=>{
                    console.log(err)
                })
            },
            movieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
            getReport:function () {
                    this.axios.get('/report/getReport', {headers: {'token': localStorage.getItem('token')}})
                        .then(res => {
                            console.log(res.data.data);
                            this.movieList = res.data.data
                        }).catch(err => {
                        console.log(err)
                    });
            },
            getTopComment:function () {
                this.axios.get('/customer/getTopComment')
                    .then(res=>{
                        console.log(res)
                        this.topCommentList=res.data.data;
                        this.spinShow=false
                    }).catch(err=>{
                        console.log(err)
                })
            },
            toUserInfo:function (userid) {
                this.$router.push({path:'/userInfo',query:{userid:userid}});
            }
        },
        created(){
          this.getReport();
          this.getTopComment();
            sessionStorage.setItem('movieOrder','热度优先');
            sessionStorage.setItem('movieCountry','全部地区');
            sessionStorage.setItem('movieSort','全部类型');
        },
    }
</script>

<style lang="less" scoped>
    .ivu-carouselItem{
        height: 10%;
    }
.topMovie{
    font-size: 20px;
    margin-left: 40px;
    margin-top: 20px;
}
    .point{
        cursor: pointer;
    }
    .movieName{
        margin-top: 10px;
    }
    .topComment{
        font-size: 20px;
        margin-left: 13%;
        margin-top: 100px;
    }
    .activeLikeValue{
        cursor: pointer;
    }
    i.activeLikeValue:hover{
        color: red;
    }
    .demo-spin-icon-load{
        animation: ani-demo-spin 1s linear infinite;
    }
    .clickClass{
        cursor: pointer;
    }

</style>