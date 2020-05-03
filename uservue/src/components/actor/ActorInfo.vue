<template>
    <div>
        <Card :bordered="false"  :style="'background:'+color">
            <Spin fix v-if="spinShow"></Spin>
            <Row>
                <Col :xs="{span:24,offset:0}" :sm="{span:18,offset:1}" :md="{span:12,offset:1}" :lg="{span:8,offset:2}">
                    <h1 class="actorTitle">{{actor.actorname}}</h1>
                    <img :src="actor.actorimg" height="350px" width="250px" :alt="actor.actorname">
                    <div class="smallTitleLayout">
                        <Tag color="blue">{{actor.position}}</Tag>
                    </div>
                </Col>
            </Row>
            <Card class="introCard"  :style="'background:'+color">
                <p slot="title">{{actor.actorname}}的简介:</p>
                <p>{{actor.actordetails}}</p>
            </Card>
            <Card class="introCard"  :style="'background:'+color">
                <p slot="title">{{actor.actorname}}的获奖情况:</p>
                <p>{{actor.actorreward}}</p>
            </Card>
            <Row>
                <Col class="titleClass" :xs="0" :sm="0" :md="1" :lg="2">
                </Col>
                <Col :xs="24" :sm="16" :md="13" :lg="13"><span class="smallTitle">{{actor.actorname}}参与制作的电影:</span><Divider /></Col>
            </Row>
            <Row>
                <Col class="titleClass":xs="0" :sm="0" :md="1" :lg="2"></Col>
                <Col :xs="24" :sm="12" :md="8" :lg="4" v-for="(movie,index) in movieList" :key="index">
                    <img :src="movie.movieimg" height="250px" width="170px" class="clickClass" @click="toMovieInfo(movie.movieid)">
                    <div class="moviename clickClass" @click="toMovieInfo(movie.movieid)">{{movie.moviename}}</div>
                    <div class="position"><Rate style="font-size: 15px" show-text allow-half v-model="movie.moviescore/2" disabled>
                        <span style="color: #f5a623">{{ movie.moviescore }}</span>
                    </Rate></div>
                </Col>
            </Row>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "ActorInfo",
        data(){
            return{
                actor:{},
                movieList:[],
                spinShow:true,
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            getActorInfo(){
                this.axios.get('/customer/getActorInfo?actorid='+this.$route.query.actorid)
                    .then(res=>{
                        console.log(res)
                        this.actor=res.data.data.actor;
                        this.movieList=res.data.data.movieList;
                        this.spinShow=false
                    }).catch(err=>{
                        console.log(err)
                })
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
        },
        created(){
            this.getActorInfo();
        }
    }
</script>

<style lang="less" scoped>
    .ivu-col{
        margin-top: 2%;
    }
    .ivu-card{
        margin: 20px;
        .introCard{
            margin: 8%;
        }
        .actorTitle{
            margin-bottom: 4%;
        }
    }
    .activeClick{
        cursor: pointer;
    }
    .smallTitleLayout{
        margin-top: 6%;
    }
    .titleClass{
        margin-top: 2%;
    }
    .clickClass{
        cursor: pointer;
    }
    div.clickClass:hover{
        color: #3399ff;
    }
    .moviename{
        width: 150px;
        text-align: center;
    }
    .smallTitle{
        font-size: 20px;
    }
</style>