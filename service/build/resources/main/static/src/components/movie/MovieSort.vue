<template>
  <div>
  <!--顶部面包屑-->
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>电影管理</el-breadcrumb-item>
    <el-breadcrumb-item>电影分类</el-breadcrumb-item>
  </el-breadcrumb>

  <!-- 卡片视图-->
  <el-card class="box-card" shadow="hover" >
    <!--el-row表示一行,:gutter表示列之间的间隔距离el-col表示一列，:span表示宽度-->
    <el-row :gutter="20">
      <el-col :span="7">
      </el-col>
      <el-col :span="4">
        <el-button type="primary" @click="addSort">添加分类</el-button>
      </el-col>
    </el-row>
    <el-table
      border
      :data="sortForm"
      style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
              <el-tag v-for="item in props.row.movieList" key="index">{{item}}</el-tag>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column
        label="序号"
        type="index">
      </el-table-column>
      <el-table-column
        label="电影类型"
        prop="moviesort">
      </el-table-column>
      <el-table-column
        label="电影数量"
        prop="movieNum">
      </el-table-column>
      <el-table-column
        label="操作"
        width="130">
        <template slot-scope="scope">
          <el-tooltip content="修改分类" placement="top" :enterable="false">
            <el-button type="primary" icon="el-icon-edit"  size="mini" @click="openUpSort(scope.row.moviesort)"></el-button>
          </el-tooltip>
          <el-tooltip content="删除" placement="top" :enterable="false">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteSort(scope.row.moviesort)"></el-button>
          </el-tooltip>
        </template>
      </el-table-column>
    </el-table>
    <!--layout表示页面显示哪些功能组件-->
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
        name: "MovieSort",
        data(){
            return{
                sortForm:[],
                queryInfo:{
                    //当前页码
                    pagenum:1,
                    //每页的数据量
                    pagesize:5
                },
                total:0,
            }
        },
        created() {
            this.getSortList();
        },
        methods:{
            getSortList:function () {
                this.$axios.get('/movie/getSortAndMovie?size='+this.queryInfo.pagesize+"&current="+this.queryInfo.pagenum,{headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        console.log(res);
                        this.sortForm=res.data.data.records;
                        this.total=res.data.data.total;
                    }).catch(err=>{
                    this.$notify.error('服务器端错误');
                })
            },
            handleSizeChange:function (pageSize) {
                this.queryInfo.pagesize=pageSize;
                this.getSortList();
            },
            handleCurrentChange:function (pageNum) {
                this.queryInfo.pagenum=pageNum;
                this.getSortList();
            },
            addSort:function () {
                this.$prompt('请输入分类',  {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /\S/,
                    inputErrorMessage: '请输入分类'
                }).then(({ value }) => {
                  this.$axios.get('/movie/addSort?moviesort='+value)
                      .then(res=>{
                          console.log(res.data.data);
                          if(res.data.data){
                              this.$message.success("添加成功")
                              this.getSortList()
                          }else{
                              this.$message.error("添加失败")
                          }
                      }).catch(err=>{
                          this.$message.error("服务器端错误")
                  })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消添加'
                    });
                });
            },
            openUpSort:function (sort) {
                this.$prompt('修改分类',  {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    inputPattern: /\S/,
                    inputErrorMessage: '请输入分类',
                    inputValue:sort
                }).then(({ value }) => {
                    this.$axios.get('/movie/updateSort?moviesort='+value+'&oldMovieSort='+sort)
                        .then(res=>{
                            console.log(res.data.data);
                            if(res.data.data){
                                this.$message.success("修改成功")
                                this.getSortList()
                            }else{
                                this.$message.error("修改失败")
                            }
                        }).catch(err=>{
                        this.$message.error("服务器端错误")
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '取消修改'
                    });
                });
            },
            deleteSort:function (sort) {
                this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get('/movie/deleteSort?moviesort='+sort)
                        .then(res=>{
                            console.log(res);
                            this.$message.success('删除成功')
                            this.getSortList()
                        }).catch(err=>{
                        this.$message.error('删除失败')
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
  .el-tag{
    margin-right: 6px;
    margin-top: 6px;
    margin-bottom: 6px;
  }
  .el-pagination{
    margin-top: 15px;
  }
</style>
