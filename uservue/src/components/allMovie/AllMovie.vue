<template>
    <div>
        <Card  :style="'background:'+color+';margin:2%'" >
            <Spin fix v-if="spinShow"></Spin>
        <RadioGroup v-model="border" style="margin-left: 35%" @on-change="radioChange">
            <Radio label="热度优先" border></Radio>
            <Radio label="评价优先" border></Radio>
            <Radio label="时间优先" border></Radio>
        </RadioGroup>
            <Row class="topComment">
                <Col :xs="{span:20,offset:4}" :sm="{span:16,offset:3}" :md="{span:16,offset:3}" :lg="{span:16,offset:3}">
                    <span>{{border}}:</span><Divider /></Col>
            </Row>
            <Row>
                <Col :xs="{span:20,offset:4}" :sm="{span:16,offset:3}" :md="{span:16,offset:3}" :lg="{span:16,offset:3}">
                    <List>
                    <ListItem v-for="(movie,index) in movieList" :key="index+'movie'">
                        <img :src="movie.movieimg" style="width:60px;height:85px;cursor:pointer" @click="toMovieInfo(movie.movieid)">
                        <ListItemMeta slot="header"/>
                        <slot name="header">
                            <div class="ivu-list-item-meta">
                                <div class="ivu-list-item-content">
                                    <div class="ivu-list-item-meta-title" @click="toMovieInfo(movie.movieid)">
                                        {{movie.moviename}}
                                    </div>
                                    <div class="ivu-list-item-meta-description" >
                                        {{moviesortToString(movie.moviesort)}}/{{movie.date}}({{movie.moviecountry}})
                                    </div></div></div></slot>
                        <Rate class="Rate" disabled show-text v-model="movie.moviescore/2"><span style="color: #f5a623">{{movie.moviescore}}</span>
                            <span>({{movie.scoreperson}}人评价)</span></Rate>
                    </ListItem>
                    </List>
                </Col>
            </Row>
            <Page style="margin:15px 28%" show-total :total="this.pageForm.total"
                  :current="this.pageForm.current"
                   show-elevator show-sizer :page-size="5"
                  :page-size-opts="[5, 10, 20, 40]"
                  @on-change="handleCurrentChange"
                  @on-page-size-change="handleSizeChange"/>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "AllMovie",
        data(){
            return{
                border:sessionStorage.getItem('movieOrder'),
                pageForm:{
                    current:1,
                    size:5,
                    total:0
                },
                movieList:[],
                spinShow:true,
                color:sessionStorage.getItem('color')
            }
        },
        created:function(){
          this.getMovieOrder();
        },
        methods:{
            radioChange:function (value) {
                sessionStorage.setItem('movieOrder',value)
                this.getMovieOrder();
            },
            getMovieOrder:function () {
                this.axios.get('/customer/getMovieOrder?order='+this.border+'&current='+this.pageForm.current+'&size='+this.pageForm.size)
                    .then(res=>{
                        console.log(res)
                        this.pageForm.total=res.data.data.total
                        this.movieList=res.data.data.records;
                        this.spinShow=false
                    }).catch(err=>{
                        console.log(err)
                })
            },
            handleCurrentChange:function (current) {
                this.pageForm.current=current;
                this.getMovieOrder()
            },
            handleSizeChange:function (size) {
                this.pageForm.size=size;
                this.getMovieOrder()
            },
            moviesortToString:function (moviesort) {
                return moviesort.join('/');
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
        }
    }
</script>

<style lang="less" scoped>
    .topComment{
        font-size: 20px;
        margin-top: 30px;
    }
    .ivu-list-item-meta{
        margin-left: 2%;
    }

    .ivu-rate{
        font-size: 13px;
        span{
            font-size: 13px;
            margin-right: 15px;
        }
    }
</style>