<template>
    <div>
        <Card style="margin: 2%"  :style="'background:'+color">
    <Row class="smallTitleLayout">
        <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 18, offset: 1 }" :md="{ span: 13, offset: 2 }" :lg="{ span: 13, offset: 2 }"><span class="smallTitle">搜索结果:</span><Divider /></Col>
    </Row>
        <Row>
            <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 18, offset: 1 }" :md="{ span: 13, offset: 2 }" :lg="{ span: 13, offset: 2 }">
                    <List>
                        <ListItem v-for="(actor,index) in actorList" :key="index">
                            <img :src="actor.actorimg"  style="width:50px;cursor:pointer" @click="toActorInfo(actor.actorid)">
                            <ListItemMeta slot="header"/>
                            <slot name="header">
                                <div class="ivu-list-item-meta">
                                    <div class="ivu-list-item-content">
                                        <div class="ivu-list-item-meta-title" @click="toActorInfo(actor.actorid)">
                                            {{actor.actorname}}
                                        </div>
                                        <div class="ivu-list-item-meta-description" >
                                            {{actor.position}}
                                        </div></div></div></slot>
                        </ListItem>
                        <ListItem v-for="(movie,index) in movieList" :key="index+'movie'">
                            <img :src="movie.movieimg" style="width:50px;cursor:pointer" @click="toMovieInfo(movie.movieid)">
                            <ListItemMeta slot="header"/>
                            <slot name="header">
                                <div class="ivu-list-item-meta">
                                <div class="ivu-list-item-content">
                                    <div class="ivu-list-item-meta-title" @click="toMovieInfo(movie.movieid)">
                                        {{movie.moviename}}
                                    </div>
                                    <div class="ivu-list-item-meta-description" >
                                        {{moviesortToString(movie.moviesort)}}
                                    </div></div></div></slot>
                            <Rate class="Rate" disabled show-text v-model="movie.moviescore/2"><span style="color: #f5a623">{{movie.moviescore}}</span>
                                <span>({{movie.scoreperson}}人评价)</span></Rate>
                        </ListItem>
                    </List>
            </Col>
        </Row>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "Search",
        data(){
            return{
                actorList:JSON.parse(sessionStorage.getItem('actorList')),
                movieList:JSON.parse(sessionStorage.getItem('movieList')),
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            moviesortToString:function (moviesort) {
                return moviesort.join('/');
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            },
            toActorInfo:function (actorid) {
                this.$router.push({path:'/actorInfo',query:{actorid:actorid}})
            }
        },

    }
</script>

<style lang="less" scoped>
    .smallTitleLayout{
        margin-top: 2%;
    }
    .smallTitle{
        font-size: 20px;
    }
    .ivu-list-item-meta{
     margin-left: 2%;
    }

    .ivu-rate{
        font-size: 12px;
        span{
            font-size: 12px;
            margin-right: 15px;
        }
    }

</style>