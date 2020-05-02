<template>
  <div>
    <!--顶部面包屑-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电影管理</el-breadcrumb-item>
      <el-breadcrumb-item>演职员列表</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图-->
    <el-card class="box-card" shadow="hover" >
      <el-row :gutter="20">
        <el-col :span="7">
          <!--搜索与添加-->
          <el-input placeholder="请输入内容"  class="input-with-select" v-model="queryInfo.query">
            <el-button slot="append" icon="el-icon-search" @click="searchActor"></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addActordialog = true">添加演职员</el-button>
        </el-col>
      </el-row>
      <el-table
        border
        :data="actorList"
        style="width: 100%">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item>
                <label>演职员姓名:</label><span>{{ props.row.actorname }}</span>
              </el-form-item>
              <el-form-item>
                <label>职位:</label><span>{{ props.row.position }}</span>
              </el-form-item>
              <el-form-item >
                <label>演职员简介:</label><span>{{ props.row.actordetails }}</span>
              </el-form-item>
              <el-form-item >
                <label>演职员奖项:</label><span>{{ props.row.actorreward }}</span>
              </el-form-item>
              <el-form-item >
                <label>演职员图片:</label>
                <div><el-image :src="props.row.actorimg">
                </el-image></div>
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
          label="演职员名称"
          prop="actorname">
        </el-table-column>
        <el-table-column
          label="演职员职位"
          prop="position"
          :filters="[{ text: '导演', value: '导演' }, { text: '演员', value: '演员' }]"
          :filter-method="filterTag"
          filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag
              :type="scope.row.position === '导演' ? 'primary' : 'success'"
              disable-transitions>{{scope.row.position}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          width="150">
          <template slot-scope="scope">
            <el-tooltip content="修改信息" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-edit" circle size="small" @click="openUpActorDialog(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="更换演职员图片" placement="top" :enterable="false">
              <el-button type="primary" icon="el-icon-picture-outline" circle size="small" @click="openUpImgDialog(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top" :enterable="false">
              <el-button type="danger" icon="el-icon-delete" circle size="small" @click="deleteActor(scope.row.actorid)"></el-button>
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
    <!--修改演职员对话框-->
    <el-dialog
      title="修改演职员信息"
      :visible.sync="upActorDialog"
      width="50%">
      <!--内容主体区域-->
      <span>
  <el-form :model="upActorForm" :rules="rules" ref="upActorForm" label-width="100px" class="demo-dynamic"
           status-icon>
     <el-form-item label="演职员名称" prop="actorname">
    <el-input type="text" v-model="upActorForm.actorname" autocomplete="off" ></el-input>
  </el-form-item>
    <el-form-item label="演职员简介" prop="actordetails">
    <el-input
      type="textarea"
      autosize
      placeholder="请输入内容"
      v-model="upActorForm.actordetails"
      autocomplete="off">
</el-input>
    </el-form-item>
    <el-form-item label="演职员奖项" prop="actorreward">
    <el-input
      type="textarea"
      autosize
      placeholder="请输入内容"
      v-model="upActorForm.actorreward"
      autocomplete="off">
</el-input>
    </el-form-item>
    <el-form-item label="演职员职位" prop="position">
       <el-radio-group  size="small"  v-model="upActorForm.position">
      <el-radio label="导演" border></el-radio>
          <el-radio label="演员" border></el-radio>
    </el-radio-group>
    </el-form-item>
</el-form>
          </span>
      <!--底部区域-->
      <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm('upActorForm')">确 定</el-button>
          <el-button @click="upActorDialog = false">取 消</el-button>
  </span>
    </el-dialog>

    <!--更换演职员图片对话框-->
    <el-dialog
      title="更换演职员图片"
      :visible.sync="upImgDialog"
      width="50%">
      <!--内容主体区域-->
      <span>
  <el-form :model="upImgForm" :rules="rules" label-width="100px" class="demo-dynamic"
           status-icon>
    <el-form-item label="演职员图片" prop="actorimg">
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
          <el-button @click="upImgDialog = false">取 消</el-button>
  </span>
    </el-dialog>

    <!-- 添加演职员抽屉-->
    <el-drawer
      title="添加演职员"
      size="35%"
      :before-close="addActorClose"
      :visible.sync="addActordialog"
      direction="rtl"
      custom-class="demo-drawer"
      ref="drawer"
    >
      <div class="demo-drawer__content">
        <el-form :model="addActorForm" :rules="rules" ref="addActorForm" class="addForm">
          <el-form-item label="演职员名称" label-width="100px" style="width:350px" prop="actorname">
            <el-input v-model="addActorForm.actorname" autocomplete="off" placeholder="请输入内容" ></el-input>
          </el-form-item>
          <el-form-item label="演职员职位" label-width="100px" prop="position">
            <el-select v-model="addActorForm.position" placeholder="请输入内容" >
              <el-option label="导演" value="导演"></el-option>
              <el-option label="演员" value="演员"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="演职员简介" prop="actordetails">
            <el-input
              type="textarea"
              autosize
              placeholder="请输入内容"
              v-model="addActorForm.actordetails"
              autocomplete="off">
            </el-input>
          </el-form-item>
          <el-form-item label="演职员奖项" prop="actorreward">
            <el-input
              type="textarea"
              autosize
              placeholder="请输入内容"
              v-model="addActorForm.actorreward"
              autocomplete="off">
            </el-input>
          </el-form-item>
          <el-form-item label="演职员图片" prop="actorimg">
            <el-upload
              ref="addUpload"
              class="upload-demo"
              drag
              :with-credentials="true"
              action
              :http-request="addUploadReq"
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
        <div class="demo-drawer__footer">
          <el-button type="primary" @click="$refs.drawer.closeDrawer()" :loading="loading" size="medium">{{ loading ? '提交中 ...' : '确 定' }}</el-button>
          <el-button @click="cancelForm" size="medium">取 消</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
    export default {
        name: "Actors",
        created(){
            this.getActorList();
        },
        data(){
            return{
                table: false,
                dialog: false,
                loading: false,
                queryInfo:{
                    //搜索
                    query:'',
                    //当前页码
                    pagenum:1,
                    //每页的数据量
                    pagesize:5
                },
                total:0,
                actorList:[],
                upActorDialog:false,
                upImgDialog:false,
                addActordialog:false,
                upActorForm:{
                    actorid:'',
                    actorname:'',
                    actorimg:'',
                    actordetails:'',
                    actorreward:'',
                    position:''
                },
                upImgForm:{
                    actorid:'',
                    actorname:'',
                    actorimg:'',
                    actordetails:'',
                    actorreward:'',
                    position:''
                },
                addActorForm:{
                    actorid:'',
                    actorname:'',
                    actorimg:'',
                    actordetails:'',
                    actorreward:'',
                    position:''
                },
                rules:{
                    actorname:[
                        { required: true, message: '请输入演职员名称', trigger: 'blur' },
                    ],
                    actordetails: [
                        { required: true, message: '请输入演职员简介', trigger: 'blur' },
                    ],
                    actorreward: [
                        { required: true, message: '请输入演职员奖项', trigger: 'blur' },
                    ],
                    position:[
                        { required: true, message: '请选择演职员职位', trigger: 'blur' },
                    ]
                }
            }
        },
        methods:{
            handleSizeChange:function(size){
                this.queryInfo.pagesize=size
                this.getActorList()
            },
            handleCurrentChange:function(pagenum){
                this.queryInfo.pagenum=pagenum
                this.getActorList()
            },
            getActorList:function(){
                this.$axios.get('/actor/getActorList?current='+this.queryInfo.pagenum+'&size='+this.queryInfo.pagesize+'&actorname='+this.queryInfo.query,
                    {headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                        this.actorList=res.data.data.records
                        this.total=res.data.data.total
                    }).catch(err=>{
                        this.$message.error("服务器端错误")
                })
            },
            searchActor:function () {
             this.getActorList()
            },
            filterTag(value, row) {
                return row.position === value;
            },
            openUpActorDialog(actor){
                this.upActorForm=actor;
                this.upActorDialog=true;
            },
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let actor=this.$qs.stringify(this.upActorForm);
                        this.$axios.post('/actor/updateActor',actor)
                            .then(res=>{
                                if(res.data.data){
                                    this.$message.success("修改成功")
                                    this.upActorDialog=false
                                    this.getActorList()
                                }else{
                                    this.$message.error("修改失败")
                                }
                            }).catch(err=>{
                                this.$message.error("服务器端错误")
                        })
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
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
            submitImg:function(){
                this.$refs.upload.submit()
            },
            uploadReq:function (file) {
                let param = new FormData();
                param.append('file',file.file);
                param.append('actorname',this.upImgForm.actorname);
                param.append('actordetails',this.upImgForm.actordetails);
                param.append('actorid',this.upImgForm.actorid);
                param.append('actorreward',this.upImgForm.actorreward);
                param.append('position',this.upImgForm.position);
                this.$axios.post('/actor/updateActor',param)
                    .then(res=>{
                        console.log(res.data.data)
                        if(res.data.data){
                            this.$message.success('修改成功');
                            this.upImgDialog=false
                            this.getActorList()
                        }else{
                            this.$message.error('修改失败');
                        }
                    })
                    .catch(err=>{
                        this.$message.error('服务器端错误');
                    })
            },
            openUpImgDialog:function (actor) {
                this.upImgForm=actor
                this.upImgDialog=true
            },
            deleteActor:function (actorid) {
                this.$confirm('此操作将永久删除该演职员相关信息, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$axios.get('/actor/deleteActor?actorid='+actorid)
                        .then(res=>{
                            console.log(res);
                            if(res.data.data){
                                this.$message.success('删除成功')
                                this.getActorList();
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
            },
            addActorClose(done) {
                if (this.loading) {
                    return;
                }
                this.$confirm('确定要提交信息吗？')
                    .then(_ => {
                        this.loading = true;
                        this.timer = setTimeout(() => {
                            this.$refs['addActorForm'].validate((valid) => {
                                if (valid) {
                                    this.$refs.addUpload.submit()
                                    done();
                                }else {
                                    console.log("error submit");
                                    this.$message.error("请完善信息")
                                    return false;
                                }
                            });
                            // 动画关闭需要一定的时间
                            setTimeout(() => {
                                this.loading = false;
                            }, 400);
                        }, 2000);
                    })
                    .catch(_ => {});
            },
            cancelForm() {
                this.loading = false;
                this.addActordialog = false;
                clearTimeout(this.timer);
            },

            addUploadReq:function (file) {
                let param=new FormData();
                param.append("file",file.file);
                param.append("actorname",this.addActorForm.actorname);
                param.append("actordetails",this.addActorForm.actordetails);
                param.append("actorreward",this.addActorForm.actorreward);
                param.append("position",this.addActorForm.position);
                this.$axios.post('/actor/addActor',param)
                    .then(res=>{
                       if(res.data.data){
                           this.$message.success("添加成功")
                           this.$refs['addActorForm'].resetFields();
                       }else {
                           this.$message.error("添加失败")
                       }
                    }).catch(err=>{
                        this.$message.error("服务器端错误")
                })
            },

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
  .demo-drawer__footer{
   margin-left: 30px;
    .el-button{
      width: 260px;

    }
  }
  .addForm{
    margin-left: 30px;
  }
</style>
