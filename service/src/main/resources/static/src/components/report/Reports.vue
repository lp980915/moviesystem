<template>
  <div>
  <!--顶部面包屑-->
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>数据统计</el-breadcrumb-item>
    <el-breadcrumb-item>数据报表</el-breadcrumb-item>
  </el-breadcrumb>
  <!-- 卡片视图-->
  <el-card class="box-card" shadow="hover" >
    <!-- 2.为 ECharts 准备一个具备大小（宽高）的 DOM -->
    <div id="main" style="width: 1400px;height:700px;"></div>
  </el-card>
  </div>
</template>

<script>
  //1.导入echarts
  import echarts from 'echarts';
  import lodash from 'lodash';
    export default {
        name: "Reports",
        data(){
            return{
                chartArr:[],
                movieNameArr:[],
                movieLikeArr:[],
                movieCommentArr:[],
                movieScoreArr:[]
            }
        },
        //dom元素初始化完毕才会加载mounted函数，此时页面上的元素已经被渲染完毕了,created相反
        mounted() {
            // 3.基于准备好的dom，初始化echarts实例
            const myChart = echarts.init(document.getElementById('main'));

            this.$axios.get('/report/getReport',
                {headers:{'token':localStorage.getItem('token')}})
                .then(res=>{
                    this.chartArr=res.data.data;
                    console.log(this.chartArr);
                    for (let index in this.chartArr) {
                        this.movieNameArr.push(this.chartArr[index].moviename);
                        this.movieLikeArr.push(this.chartArr[index].movielike);
                        this.movieCommentArr.push(this.chartArr[index].commentNum);
                        this.movieScoreArr.push(this.chartArr[index].moviescore);
                    }
                    console.log(this.movieNameArr);
                    //4.准备数据和配置项
                    var option = {
                        title: {
                            text: '电影TOP6数据统计'
                        },
                        tooltip: { trigger: 'axis',
                            axisPointer: {
                                type: 'cross',
                                label: {
                                    backgroundColor: '#6a7985'
                                }
                            }},

                        grid: {
                            left: '3%',
                            right: '4%',
                            bottom: '3%',
                            containLabel: true
                        },
                        legend: {
                            data:['收藏数','评分','评论数']
                        },
                        xAxis: {
                            type: 'category',
                            boundaryGap: false,
                            data: this.movieNameArr,
                        },
                        yAxis: [
                    {
                        type: 'value'
                    }
                        ],
                        series: [{
                            name: '收藏数',
                            type: 'line',
                            stack: '总量',
                            areaStyle: {},
                            data: this.movieLikeArr
                        },
                            {
                            name:'评分',
                                type:'line',
                                stack: '总量',
                                areaStyle: {},
                                data:this.movieScoreArr
                        },
                            {
                                name: '评论数',
                                type: 'line',
                                stack: '总量',
                                areaStyle: {},
                                data: this.movieCommentArr
                            }]
                    };
                    /*const result=lodash.merge(option,this.options);*/
                    //5.展示数据
                    myChart.setOption(option);
                }).catch(err=>{
               this.$message.error('服务器端错误');
            });

        }
    }
</script>

<style scoped>

</style>
