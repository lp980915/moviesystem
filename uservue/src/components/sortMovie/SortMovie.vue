<template>
    <div>
        <Card  :style="'background:'+color+';margin:2%'"  dis-hover>
            <Spin fix v-if="spinShow"></Spin>
            <RadioGroup v-model="selectedCountry" @on-change="countryChange" size="small" style="margin-top: 10px;margin-left: 22%;margin-right: 20%">
                <Radio label="全部地区"  border></Radio>
                <Radio :label="country"  border v-for="(country,index) in countryList" :key="index" ></Radio>
            </RadioGroup>
            <RadioGroup v-model="selectedSort"  @on-change="sortChange" size="small" style="margin-top: 15px;margin-left: 21%;margin-right: 2%">
                <Radio label="全部类型" border></Radio>
                <Radio :label="sort" border  v-for="(sort,index) in sortList" :key="index" ></Radio>
            </RadioGroup>
            <Row style="margin-left: 20%;margin-right: 20%;margin-top: 40px">
                <Card  :style="'background:'+color" >
                <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 12, offset: 0 }" :md="{ span: 12, offset: 0 }" :lg="{ span: 5, offset: 1 }"
                v-for="(movie,index) in movieList" :key="index"  v-show="index<showNum" style="margin-top: 20px">
                   <img @click="toMovieInfo(movie.movieid)" class=" clickClass" :src="movie.movieimg" height="230px" width="150px"  alt="movie.moviename"/>
                    <Rate class="Rate" disabled show-text v-model="movie.moviescore/2"><span style="color: #f5a623">{{movie.moviescore}}</span></Rate>
                    <div @click="toMovieInfo(movie.movieid)" class="moviename clickClass">{{movie.moviename}}</div>
                </Col>
                <div>
                <Button style="margin-top: 30px" type="primary" v-show="showNum<movieList.length" @click="showMore" :loading="loading" long>加载更多</Button>
                <Button style="margin-top: 30px" v-show="showNum>=movieList.length" disabled long>莫得了</Button>
                </div>
                </Card>
            </Row>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "SortMovie",
        data(){
          return{
              sortList:[],
              selectedSort:sessionStorage.getItem('movieSort'),
              countryList:[],
              selectedCountry:sessionStorage.getItem('movieCountry'),
              movieList:[],
              showNum:12,
              spinShow:true,
              loading:false,
              color:sessionStorage.getItem('color')
          }
        },
        created(){
            this.getAllMovieAndCountryAndSort();
        },
        methods:{
            showMore(){
                this.loading=true;
                setTimeout(()=>{
                    this.showNum =this.showNum+4;
                    this.loading=false;
                },500);
            },
            getAllMovieAndCountryAndSort:function () {
                this.axios.get('/customer/getAllMovieAndCountryAndSort?moviesort='+this.selectedSort+'&moviecountry='+this.selectedCountry
                    )
                    .then(res=>{
                        this.sortList=res.data.data.sortList;
                        this.countryList=res.data.data.countryList;
                        this.movieList=res.data.data.movieList;
                        this.spinShow=false
                    }).catch(err=>{
                        console.log(err)
                })
            },
            sortChange:function (sort) {
                sessionStorage.setItem('movieSort',sort);
                this.getAllMovieAndCountryAndSort();
            },
            countryChange:function (country) {
                sessionStorage.setItem('movieCountry',country);
                this.getAllMovieAndCountryAndSort();
            },
            toMovieInfo:function (movieid) {
                this.$router.push({path:'/movieInfo',query:{movieid:movieid}})
            }
        }
    }
</script>

<style lang="less" scoped>
.Rate{
    font-size: 13px;
}
.moviename{
    width: 150px;
    text-align: center;
    font-size: 13px;
}
.clickClass{
    cursor: pointer;
}
div.clickClass:hover{
    color: #3399ff;
}
</style>