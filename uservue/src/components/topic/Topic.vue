<template>
    <div>
        <Card  :style="'background:'+color+';margin:2%'"  dis-hover>
            <Spin fix v-if="spinShow"></Spin>
            <Row style="margin-top: 30px">
                <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 18, offset: 1 }" :md="{ span: 13, offset: 5 }" :lg="{ span: 13, offset: 5 }"><span class="smallTitle">电影话题:</span><Divider /></Col>
            </Row>
            <Row>
                <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 18, offset: 1 }" :md="{ span: 13, offset: 5 }" :lg="{ span: 13, offset: 5 }">
                <Card  :style="'background:'+color" >
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
                                    <img :src="topic.movieimg" class="clickClass" style="height: 120px;margin-top:50%" @click="toMovieInfo(topic.movieid)">
                                </Tooltip>
                            </template>
                        </ListItem>
                    </List>
                </Card>
                    <Page style="margin:15px 20%" show-total :total="this.pageForm.total"
                          :current="this.pageForm.current"
                          show-elevator show-sizer :page-size="5"
                          :page-size-opts="[3, 5, 10, 20]"
                          @on-change="handleCurrentChange"
                          @on-page-size-change="handleSizeChange"/>
                </Col>
            </Row>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "Topic",
        data(){
            return{
                spinShow:true,
                pageForm:{
                    current:1,
                    size:5,
                    total:0
                },
                topicList:[],
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            getTopicList:function () {
                this.axios.get('/customer/getTopicList?size='+this.pageForm.size+'&current='+this.pageForm.current)
                    .then(res=>{
                        console.log(res);
                        this.topicList=res.data.data.records;
                        this.pageForm.total=res.data.data.total;
                        this.spinShow=false
                    }).catch(err=>{
                        console.log(err);
                });
            },
            handleCurrentChange:function (current) {
                this.pageForm.current=current;
                this.getTopicList()
            },
            handleSizeChange:function (size) {
                this.pageForm.size=size;
                this.getTopicList()
            },
            toUserInfo:function (userid) {
                this.$router.push({path:'/userInfo',query:{userid:userid}});
            },
            toTopicContent:function (topic) {
                this.$router.push({path:'/topicContent',query:{topic:JSON.stringify(topic)}})
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
        },
        created(){
            this.getTopicList();
        }

    }
</script>

<style lang="less" scoped>
    .smallTitle{
        font-size: 20px;
    }
    .clickClass{
           cursor: pointer;
       }
    div.clickClass:hover{
        color: #3399ff;
    }
</style>