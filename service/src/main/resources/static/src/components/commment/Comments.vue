<template>
  <div>
    <!--顶部面包屑-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>评论管理</el-breadcrumb-item>
      <el-breadcrumb-item>评论列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图-->
    <el-card class="box-card" shadow="hover" >
      <!--el-row表示一行,:gutter表示列之间的间隔距离el-col表示一列，:span表示宽度-->
      <el-row :gutter="20">
        <el-col :span="7">
          <!--搜索与添加-->
          <el-input placeholder="请输入内容"  class="input-with-select" v-model="queryInfo.query">
            <el-button slot="append" icon="el-icon-search" @click="getCommentList"></el-button>
          </el-input>
        </el-col>
      </el-row>
      <el-table
        :data="commentList"
        style="width: 100%"
        border
      >
        <el-table-column
          label="#"
          type="index">
        </el-table-column>
        <el-table-column
          prop="date"
          label="日期"
          sortable
          width="180">
        </el-table-column>
        <el-table-column
          prop="comment"
          label="评论"
          width="500">
          <template slot-scope="scope">
            <span v-html="scope.row.comment"></span>
          </template>
        </el-table-column>
        <el-table-column
          prop="moviename"
          label="电影名">
        </el-table-column>
        <el-table-column
          prop="username"
          label="用户名">
        </el-table-column>
        <el-table-column
          prop="likevalue"
          label="点赞数"
          sortable
        width="400">
          <template slot-scope="scope">
            <el-progress :text-inside="true" :stroke-width="20" :percentage="parseInt(scope.row.likevalue)" :format="format"></el-progress>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
        width="70">
          <template slot-scope="scope">
            <el-tooltip content="删除" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteComment(scope.row.commentid,scope.row.movieid,scope.row.score)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[1, 2, 5, 10]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
    export default {
        name: "Comments",
        data(){
            return{
                queryInfo:{
                    //搜索
                    query:'',
                    //当前页码
                    pagenum:1,
                    //每页的数据量
                    pagesize:5
                },
                total:0,
                commentList:[

                ]
            }
        },
        created() {
            this.getCommentList();
        },
        methods:{
            format:function(percentage){
                return percentage;
            },
        getCommentList() {
            this.$axios.get('/movie/getCommentList?current='+this.queryInfo.pagenum+'&size='+this.queryInfo.pagesize+'&comment='+this.queryInfo.query,
                {headers:{'token':localStorage.getItem('token')}})
                .then(res=>{
                    console.log(res)
                    this.commentList=res.data.data.records
                    this.total=res.data.data.total
                }).catch(err=>{
                    this.$message.error("服务器端错误");
            })
        },
            handleSizeChange(size){
            this.queryInfo.pagesize=size;
            this.getCommentList()
            },
            handleCurrentChange(pageNum){
            this.queryInfo.pagenum=pageNum;
            this.getCommentList()
            },
            deleteComment:function (commentid,movieid,score) {
                this.$confirm('确定要删除此评论吗, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get('/movie/deleteComment?commentid='+commentid+'&score='+score+'&movieid='+movieid)
                        .then(res=>{
                            console.log(res)
                            if(res.data.data){
                                this.$message.success("删除成功")
                                this.getCommentList();
                            }else{
                                this.$message.error("删除失败")
                            }
                        }).catch(err=>{
                        this.$message.error("服务器端错误")
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });

            }
        }
    }
</script>

<style lang="less" scoped>
  .el-table{
    margin-top: 15px;
    font-size: 12px;
    span{
      font-size: 12px;
    }
  }
  .el-pagination{
    margin-top: 15px;
  }
</style>
