<template>
    <div >
        <Card  :style="'background:'+color+';margin:2%'"  dis-hover>
            <Spin fix v-if="spinShow"></Spin>
            <Tooltip content="发表我的看法" theme="light" placement="top" style="margin-left: 80%;margin-top:1%">
                <Button @click="topicMessageModal=true" type="primary" shape="circle" icon="md-add" size="large"></Button>
            </Tooltip>
            <Poptip :transfer="true"
                    confirm
                    title="确定删除这个话题吗?"
                    @on-ok="deleteTopic(topic.topicid)"
            style="margin-left: 2%">
                <Tooltip content="删除这个话题" theme="light" placement="top">
                <Button type="error" shape="circle" icon="md-close" size="large" v-show="topic.userid===userid||rowid==='1'"></Button>
                </Tooltip>
            </Poptip>
            <Modal
                    width="70%"
                    :mask-closable="false"
                    v-model="topicMessageModal">
                <p slot="header" style="text-align:center">
                    <Icon type="ios-brush"></Icon>
                    <span>发表看法</span>
                </p>
                        <quill-editor
                                v-model="topicMessage"
                                ref="topicMessageRef">
                        </quill-editor>
                <div slot="footer">
                    <Button type="primary" @click="submitTopicMessage" :disabled="topicMessage===''">提交</Button>
                    <Button   @click="topicMessageModal=false" >取消</Button>
                </div>
            </Modal>
            <Row>
                <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 18, offset: 1 }" :md="{ span: 13, offset: 5 }" :lg="{ span: 13, offset: 5 }">
            <Card  :style="'background:'+color+';margin:2%'" >
                <p style="font-size:25px;text-align: center">{{topic.title}}</p>
                <p style="margin-top:20px">{{topic.describe}}</p>
            </Card>
                    <div  style="margin-top: 50px" ></div>

            <a-comment  v-for="(topicMessage,index) in topicMessageList" :key="index" v-show="index<showNum">
                <span slot="actions" @click="addReply(topicMessage.userid,topicMessage.username,topicMessage.topicmessageid)">回复</span>
                <span slot="author" style="font-size: 15px">
                    <span class="clickClass">{{topicMessage.username}}</span><Icon size="12" type="md-time" style="padding-left: 20px" /> <Time style="font-size: 12px" :time="topicMessage.topicmessagedate" />
                    <span style="font-size: 12px;padding-left: 20px"><strong>{{index+1}}楼</strong></span>
                       <Poptip :transfer="true"
                               confirm
                               title="确定删除这条看法吗?"
                               @on-ok="deleteTopicMessage(topicMessage.topicmessageid)">
                    <span class="deleteClass" style="font-size: 10px;padding-left:20px" v-show="topicMessage.userid===userid||rowid==='1'">删除</span>
                </Poptip>
                </span>
                <Avatar
                        slot="avatar"
                        :src="topicMessage.userimg"
                        :alt="topicMessage.username"
                />
                <p slot="content" v-html="topicMessage.content"></p>
                <Button  :style="'background:'+color"  v-show="existReply(topicMessage.topicmessageid)" @click="showReply(index)" :id="index" :loading="isloading===index" icon="ios-arrow-down" size="small" shape="circle"></Button>
                <Button  :style="'background:'+color"  v-show="false" @click="hiddenReply(index)" :id="index+'*'" icon="ios-arrow-up" size="small" shape="circle"></Button>
                <a-comment v-for="(topicMessageReply,index1) in topicMessageReplyList" :key="index1"
                           v-show="topicMessageReply.topicmessageid===topicMessage.topicmessageid&&replyIndex.indexOf(index)>-1">
                    <span slot="actions">
                        <span @click="addReply(topicMessageReply.userid,topicMessageReply.username,topicMessage.topicmessageid)">回复</span>
                           </span>
                    <span slot="author" style="font-size: 15px"><span class="clickClass">{{topicMessageReply.username}}</span><span style="font-size: 10px;color: #515a6e"> 回复 </span>
                        <span class="clickClass">{{topicMessageReply.tousername}}</span><Icon size="12" type="md-time" style="padding-left: 20px" /> <Time style="font-size: 12px" :time="topicMessageReply.topicmessagereplydate" />
                        <Poptip :transfer="true"
                                confirm
                                title="确定删除这个回复吗?"
                                @on-ok="deleteReply(topicMessageReply.topicmessagereplydate)">
                        <span class="deleteClass" style="font-size: 10px;padding-left:20px" v-show="topicMessageReply.userid===userid||rowid==='1'">删除</span>
                        </Poptip>
                    </span>
                    <Avatar
                            slot="avatar"
                            :src="topicMessageReply.userimg"
                            :alt="topicMessageReply.username"
                    />
                    <p slot="content">{{topicMessageReply.replycontent}}</p>
                </a-comment>
                <Divider/>
            </a-comment>
                    <Button size="small" v-show="topicMessageList.length>showNum" style="margin-left: 45%;font-size: 11px" @click="load" :loading="loadingMore">加载更多</Button>
                    <Button size="small" v-show="topicMessageList.length<=showNum" style="margin-left: 45%;font-size: 11px" disabled>到底了</Button>
                </Col>
            </Row>
        </Card>
        <Modal v-model="replyModal" :mask-closable="false" draggable scrollable>
            <p slot="header" style="text-align:center">
                <Icon type="md-brush" size="13"></Icon>
                <span style="font-size:12px">回复</span>
            </p>
            <div style="text-align:center">
                <Input  v-model="replyValue" type="textarea" :autosize="{minRows: 4,maxRows: 10}"  :placeholder="'给'+replyToUserName+'的回复...'"
                        maxlength="200" aria-required="true"/>
            </div>
            <div slot="footer">
                <Button type="primary" size="small" @click="submitReply" style="font-size: 12px" :disabled="replyValue===''">提交</Button>
                <Button size="small"  @click="replyModal=false" style="font-size: 12px">取消</Button>
            </div>
        </Modal>
    </div>
</template>

<script>
    import { quillEditor } from 'vue-quill-editor' // 调用富文本编辑器
    import 'quill/dist/quill.snow.css' // 富文本编辑器外部引用样式  三种样式三选一引入即可
    export default {
        name: "TopicContent",
        components:{quillEditor},
        computed:{

        },
        data(){
            return{
                userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                rowid:JSON.parse(localStorage.getItem('loginUser')).rowid,
                spinShow:true,
                topic:{},
                topicMessageList:[],
                topicMessageReplyList:[],
                replyIndex:[],
                loading:false,
                existIndex:'',
                showNum:5,
                loadingMore:false,
                replyModal:false,
                replyValue:'',
                replyToUserId:'',
                replyToUserName:'',
                replyTopicMessageId:'',
                isloading:'-1',
                topicMessage:'',
                topicMessageModal:false,
                color:sessionStorage.getItem('color')
            }
        },
        created(){
            this.getTopicContent();
        },
        methods:{
            deleteTopic:function(topicid){
                this.axios.get('/customer/deleteTopic?topicid='+topicid)
                    .then(res=>{
                        if(res.data.data){
                            this.$router.push('/topic');
                            this.$Message.success('删除成功!');
                        }else {
                            this.$Message.error('删除失败!');
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            submitTopicMessage:function(){
                let topicMessage=this.$qs.stringify({
                    userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    topicid:this.topic.topicid,
                    content:this.topicMessage
                });
                this.axios.post('/customer/addTopicMessage',topicMessage)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('发表成功!');
                            this.topicMessageModal=false;
                            this.topicMessage='';
                            this.getTopicContent();
                        }else {
                            this.$Message.error('发表失败!');
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            deleteTopicMessage:function(topicmessageid){
                console.log(topicmessageid)
                this.axios.get('/customer/deleteTopicMessage?topicmessageid='+topicmessageid)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('删除成功!');
                            this.getTopicContent();
                        }else{
                            this.$Message.error('删除失败!');
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            submitReply:function(){
                let reply = this.$qs.stringify({
                    userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    touserid:this.replyToUserId,
                    replycontent:this.replyValue,
                    topicmessageid:this.replyTopicMessageId,
                    topicid:this.topic.topicid
                })
                this.axios.post('/customer/addTopicMessageReply',reply)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('回复成功!');
                            this.replyModal=false;
                            this.replyValue=''
                            this.getTopicContent();
                        }else{
                            this.$Message.error('回复失败!');
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            addReply:function(touserid,tousername,topicmessageid){
                this.replyToUserId=touserid;
                this.replyToUserName=tousername;
                this.replyTopicMessageId=topicmessageid
                this.replyModal=true;
            },
            deleteReply:function(date){
                this.axios.get('/customer/deleteReply?topicmessagereplydate='+date)
                    .then(res=>{
                        if(res.data.data){
                            this.$Message.success('成功删除!');
                            this.getTopicContent();
                        }else {
                            this.$Message.error('删除失败!')
                        }
                    }).catch(err=>{
                        console.log(err);
                })
            },
            load:function(){
                this.loadingMore=true;
                setTimeout(()=>{
                    this.showNum=this.showNum+3;
                    this.loadingMore=false;
                },500);
            },
            getTopicContent:function () {
                this.topic=JSON.parse(this.$route.query.topic);
              this.axios.get('/customer/getTopicContent?topicid='+this.topic.topicid)
                    .then(res=>{
                        console.log(res);
                        this.topicMessageList=res.data.data.topicMessageList;
                        this.topicMessageReplyList=res.data.data.topicMessageReplyList;
                        this.spinShow=false;
                    }).catch(err=>{
                        console.log(err);
                })
            },
            existReply:function (topicmessageid) {
                let result=0;
                for(let topicMessageReply of this.topicMessageReplyList){
                    if(topicMessageReply.topicmessageid===topicmessageid){
                       result+=1;
                    }
                }
                return result>0;
            },
            showReply:function (index) {
                this.isloading=index;
                setTimeout(() => {
                    document.getElementById(index).style.display="none";
                    document.getElementById(index+'*').style.display="block";
                    this.replyIndex.push(index);
                    this.isloading='-1';
                }, 500);
            },
            hiddenReply:function (index) {
                document.getElementById(index).style.display="block";
                document.getElementById(index+'*').style.display="none";
                let index1=this.replyIndex.indexOf(index);
                this.replyIndex.splice(index1, 1);
            }
        }
    }
</script>

<style lang="less" scoped>
    @import '../../assets/css/antd.css';
    .clickClass{
        cursor: pointer;
    }
    span.clickClass:hover{
        color: #3399ff;
    }
    .deleteClass{
        cursor: pointer;
    }
    span.deleteClass:hover{
        color: red;
    }
</style>