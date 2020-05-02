<template>
  <div>
    <!--顶部面包屑-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电影管理</el-breadcrumb-item>
      <el-breadcrumb-item>电影列表</el-breadcrumb-item>
    </el-breadcrumb>
    <!-- 卡片视图-->
    <el-card class="box-card" shadow="hover" >
      <!--el-row表示一行,:gutter表示列之间的间隔距离el-col表示一列，:span表示宽度-->
      <el-row :gutter="20">
        <el-col :span="7">
          <!--搜索与添加-->
          <el-input placeholder="请输入内容"  class="input-with-select" v-model="queryInfo.query">
            <el-button slot="append" icon="el-icon-search" @click="searchMovie"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="goAddMovie">添加电影</el-button>
        </el-col>
      </el-row>
      <el-table
        border
        :data="movieList"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
               <el-form-item >
                <label>电影海报:</label>
                <div><el-image :src="props.row.movieimg">
                </el-image></div>
              </el-form-item>
              <el-form-item >
                <label>点赞数量:</label><div>
                <el-progress type="circle" :percentage="parseInt(props.row.movielike)" :format="format"></el-progress>
              </div>
              </el-form-item>
              <el-form-item>
              <label>电影名称:</label><span>{{ props.row.moviename }}</span>
            </el-form-item>
              <el-form-item >
                <label>电影ID:</label><span>{{ props.row.movieid }}</span>
              </el-form-item>
              <el-form-item >
                <label>电影简介:</label><span>{{ props.row.movieintro }}</span>
              </el-form-item>
              <el-form-item>
                <label>电影类型:</label><span>{{ props.row.moviesort.toString() }}</span>
              </el-form-item>
              <el-form-item>
                <label>制片国家:</label><span>{{ props.row.moviecountry }}</span>
              </el-form-item>
              <el-form-item>
                <label>上映时间:</label><span>{{ props.row.date }}</span>
              </el-form-item>
              <el-form-item>
                <label>参与演职员:</label><el-tag style="margin-left: 10px" size="small" v-for="(name,index) in props.row.actorname" :key="index">{{name}}</el-tag>
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          type="index"
          label="序号"
          >
        </el-table-column>
        <el-table-column
          label="电影名称"
          prop="moviename">
        </el-table-column>
        <el-table-column
          label="电影类型"
          prop="moviesort">
          <template slot-scope="scope">
            {{scope.row.moviesort.toString()}}
          </template>
        </el-table-column>
        <el-table-column
          label="制片国家"
          prop="moviecountry">
        </el-table-column>
        <el-table-column
          label="点赞数量"
          prop="movielike"
        sortable>
          <template slot-scope="scope">
          <el-progress :text-inside="true" :stroke-width="20" :percentage="parseInt(scope.row.movielike)" :format="format"></el-progress>
          </template>
        </el-table-column>
        <el-table-column
          label="上映时间"
          prop="date"
          sortable>
        </el-table-column>
        <el-table-column
          label="操作"
          width="190">
          <template slot-scope="scope">
            <el-tooltip content="修改信息" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-edit"  size="mini" @click="openUpMovieDialog(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="更换海报" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-picture-outline"  size="mini" @click="openUpImgDialog(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="deleteMovie(scope.row.movieid)"></el-button>
            </el-tooltip>

            <!--修改电影对话框-->
            <el-dialog
              title="修改电影信息"
              :visible.sync="upMovieDialogVisible"
              width="50%">
              <!--内容主体区域-->
              <span>
  <el-form :model="upMovieForm" :rules="rules" ref="upMovieForm" label-width="100px" class="demo-dynamic"
           status-icon>
     <el-form-item label="电影名称" prop="moviename">
    <el-input type="text" v-model="upMovieForm.moviename" autocomplete="off" ></el-input>
  </el-form-item>
    <el-form-item label="上映时间" prop="date">
      <el-date-picker
        v-model="upMovieForm.date"
        type="date"
        placeholder="选择日期"
        value-format="yyyy-MM-dd" >
            </el-date-picker>
    </el-form-item>
    <el-form-item label="电影简介" prop="movieintro">
    <el-input
      type="textarea"
      autosize
      placeholder="请输入内容"
      v-model="upMovieForm.movieintro"
      autocomplete="off">
</el-input>
    </el-form-item>
    <el-form-item label="电影类型" prop="moviesort">
       <el-checkbox-group v-model="upMovieForm.moviesort" size="mini" >
      <el-checkbox-button v-for="sort in sorts" :label="sort" :key="sort">{{sort}}</el-checkbox-button>
    </el-checkbox-group>
    </el-form-item>
     <el-form-item label="制片国家" prop="moviecountry">
       <el-radio-group v-model="upMovieForm.moviecountry" size="mini" >
      <el-radio-button v-for="con in country" :label="con" :key="con">{{con}}</el-radio-button>
    </el-radio-group>
    </el-form-item>
    <el-form-item label="演职员" prop="actorname">
            <el-cascader
              placeholder="添加相关艺人"
              :key="actorKey"
              @change="changeActor"
              :options="options"
              :props="{ multiple: true }"
              v-model="upMovieForm.actorname"
              filterable></el-cascader>
      <span style="color: red;margin-left: 5%">* 注意，将会覆盖原有演职员</span>
          </el-form-item>
</el-form>
          </span>
              <!--底部区域-->
              <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('upMovieForm')">确 定</el-button>
          <el-button @click="upMovieDialogVisible = false">取 消</el-button>
  </span>
            </el-dialog>

            <!--更换电影封面对话框-->
            <el-dialog
              title="更换电影封面"
              :visible.sync="upImgDialogVisible"
              width="50%">
              <!--内容主体区域-->
              <span>
  <el-form :model="upImgForm" :rules="rules" ref="upImgForm" label-width="100px" class="demo-dynamic"
           status-icon>
    <el-form-item label="电影封面" prop="movieimg">
      <!--:data为文件上传附带的参数，参数为此表单-->
         <el-upload
           ref="upload"
           class="upload-demo"
           drag
           :with-credentials="true"
           action
           :http-request="uploadReq"
           :limit="1"
           multiple
           :before-upload="beforeAvatarUpload"
           :on-exceed="handleExceed"
           :auto-upload="false">
  <i class="el-icon-upload"></i>
  <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
</el-upload>
    </el-form-item>
</el-form>
          </span>
              <!--底部区域-->
              <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitImg('upImgForm')">确 定</el-button>
          <el-button @click="upImgDialogVisible = false">取 消</el-button>
  </span>
            </el-dialog>

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
        name: "Movies",
        data(){
            return{
                actorKey:0,
                options:[
                    {
                        label: '导演',
                        children: [],
                    },
                    {
                        label: '演员',
                        children: [],
                    },
                ],
                //获取用户列表的参数对象
                queryInfo:{
                    //搜索
                    query:'',
                    //当前页码
                    pagenum:1,
                    //每页的数据量
                    pagesize:5
                },
                total:0,
                movieList:[],
                upMovieDialogVisible:false,
                upImgDialogVisible:false,
                upMovieForm:{
                    movieid:'',
                    moviename:'',
                    movieintro:'',
                    movieimg:'',
                    movielike:'',
                    moviesort:[],
                    moviecountry:'',
                    date:'',
                    actorname:''
                },
                sorts:[],
                country:[],
                upImgForm:{
                    movieid:'',
                    moviename:'',
                    movieintro:'',
                    movieimg:'',
                    movielike:'',
                    moviesort: [],
                    moviecountry:'',
                    date:'',
                    actorname:''
                },
                rules:{
                    moviename:[
                        { required: true, message: '请输入电影名', trigger: 'blur' },
                    ],
                    movieintro:[
                        { required: true, message: '请输入电影简介', trigger: 'blur' },
                    ]

                },
            }
        },
        created() {
            this.getMovieList();
            this.getSortList();
            this.getCountryList();
            this.getActorList();
        },
        methods:{
            changeActor(){
                ++this.actorKey;
            },
            getActorList(){
                this.$axios.get('/actor/getActorList?size='+'100000',{headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        console.log(res);
                        let actorList=res.data.data.records;
                        let listOne=[];
                        let listTwo=[];
                        let keyMap = {
                            "actorid" : "value",
                            "actorname" : "label"
                        };
                        for(let i = 0;i < actorList.length;i++){
                            let obj = actorList[i];
                            if(obj.position==="导演"){
                                listOne.push(obj);
                            }else if(obj.position==="演员"){
                                listTwo.push(obj);
                            }
                            for(let key in obj){
                                let newKey = keyMap[key];
                                if(newKey){
                                    obj[newKey] = obj[key];
                                    delete obj[key];
                                }
                            }
                        }
                        this.options[0].children=listOne;
                        this.options[1].children=listTwo;
                    }).catch(err=>{
                    this.$message.error('服务器端错误');
                })
            },
            format:function(percentage){
                    return percentage;
            },
            getMovieList:function () {
                this.$axios.get('/movie/getMovieList?current='+this.queryInfo.pagenum+'&size='+this.queryInfo.pagesize
                    +'&moviename='+this.queryInfo.query,
                    {headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        console.log(res.data.data.records)
                        console.log(res.data.data.total)
                        this.movieList=res.data.data.records
                        this.total=res.data.data.total
                    }).catch(err=>{
                    this.$message.error("服务器端错误")
                })
            },
            getSortList:function(){
                this.$axios.get('/movie/getSortList',{headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        console.log(res);
                        this.sorts=res.data.data;
                    }).catch(err=>{
                    this.$message.error('服务器端错误');
                })
            },
            getCountryList(){
                this.$axios.get('/movie/getCountryList',{headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        console.log(res);
                        this.country=res.data.data;
                    }).catch(err=>{
                    this.$message.error('服务器端错误');
                })
            },
            handleSizeChange:function (newSize) {
                console.log(newSize)
                this.queryInfo.pagesize=newSize
                this.getMovieList()
            },
            handleCurrentChange:function (newPage) {
                console.log(newPage)
                this.queryInfo.pagenum=newPage
                this.getMovieList()
            },
            searchMovie:function () {
               this.getMovieList()
            },
            goAddMovie:function () {
                this.$router.push('/movies/addMovie')
            },
            openUpMovieDialog:function (movie) {
                this.upMovieForm=movie
                this.upMovieDialogVisible=true
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG&&!isPNG) {
                    this.$message.error('上传文件只能是 JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 2MB!');
                }

                return (isJPG||isPNG) && isLt2M;
            },
            handleExceed(files, fileList) {
                this.$message.warning('只能上传一个文件');
            },
            submitForm:function (formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.upMovieForm.moviesort.length!==0){
                            let param = new FormData();
                            param.append('file',null);
                            param.append('moviename',this.upMovieForm.moviename);
                            param.append('movieintro',this.upMovieForm.movieintro);
                            param.append('movieid',this.upMovieForm.movieid);
                            param.append('movielike',this.upMovieForm.movielike);
                            param.append('moviesort',this.upMovieForm.moviesort);
                            param.append('moviecountry',this.upMovieForm.moviecountry);
                            param.append('date',this.upMovieForm.date);
                            param.append('selectedactorid',this.upMovieForm.actorname);
                       this.$axios.post('/movie/updateMovie',param)
                           .then(res=>{
                               console.log(res.data.data);
                               if(res.data.data.result){
                                   this.$message.success('修改成功');
                                   this.getMovieList()
                               }else{
                                   this.$message.error('修改失败');
                               }
                               this.upMovieDialogVisible=false
                           })
                           .catch(err=>{
                                   this.$message.error('服务器端错误');
                           })
                        }else{
                            this.$message.error('电影类型不能为空');
                        }
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            submitImg:function (formName) {
                        this.$refs.upload.submit()
            },
            uploadReq:function (file) {
                let param = new FormData();
                param.append('file',file.file);
                param.append('moviename',this.upImgForm.moviename);
                param.append('movieintro',this.upImgForm.movieintro);
                param.append('movieid',this.upImgForm.movieid);
                param.append('movielike',this.upImgForm.movielike);
                param.append('moviesort',this.upImgForm.moviesort);
                param.append('moviecountry',this.upImgForm.moviecountry);
                param.append('date',this.upImgForm.date);
                param.append('selectedactorid',this.upImgForm.actorname);
                this.$axios.post('/movie/updateMovie',param)
                    .then(res=>{
                        console.log(res.data.data)
                        if(res.data.data.result){
                            this.$message.success('修改成功');
                            this.getMovieList()
                        }else{
                            this.$message.error('修改失败');
                        }
                        this.upImgDialogVisible=false
                    })
                    .catch(err=>{
                        this.$message.error('服务器端错误');
                    })
            },
            openUpImgDialog:function (movie) {
                this.upImgForm=movie;
                this.upImgDialogVisible=true
            },
            deleteMovie:function (movieid) {
                this.$confirm('此操作将永久删除该电影相关信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get('/movie/deleteMovie?movieid='+movieid)
                        .then(res=>{
                            console.log(res);
                            if(res.data.data){
                            this.$message.success('删除成功')
                            this.getMovieList();
                            }
                            else {
                                this.$message.error('删除失败')
                            }
                        }).catch(err=>{
                        this.$message.error('服务器端错误')
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
  .demo-table-expand {
    font-size: 0;
  }

  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
    font-size: 12px;
    margin-right: 20px;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
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
  .el-image{
    width: 150px;
    height: 220px;
  }

</style>
