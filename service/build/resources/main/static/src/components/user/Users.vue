<template>
    <div>
      <!--顶部面包屑-->
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>用户管理</el-breadcrumb-item>
        <el-breadcrumb-item>用户列表</el-breadcrumb-item>
      </el-breadcrumb>

     <!-- 卡片视图-->
      <el-card class="box-card" shadow="hover" >
        <!--el-row表示一行,:gutter表示列之间的间隔距离el-col表示一列，:span表示宽度-->
        <el-row :gutter="20">
          <el-col :span="7">
            <!--搜索与添加-->
            <el-input placeholder="请输入内容"  class="input-with-select" v-model="queryInfo.query">
              <el-button slot="append" icon="el-icon-search" @click="searchUser"></el-button>
            </el-input>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" @click="addUserDialogVisible=true">添加用户</el-button>
          </el-col>
        </el-row>
        <!--添加用户对话框-->
        <el-dialog
          @close="addUserDialogClosed('addUserForm')"
          title="添加用户"
          :visible.sync="addUserDialogVisible"
          width="50%">
          <!--内容主体区域-->
          <span>
  <el-form :model="addUserForm" :rules="rules" ref="addUserForm" label-width="100px" class="demo-dynamic"
           status-icon>
     <el-form-item label="用户名" prop="username">
    <el-input type="text" v-model="addUserForm.username" autocomplete="off"></el-input>
  </el-form-item>
      <el-form-item label="密码" prop="password">
    <el-input type="password" v-model="addUserForm.password" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item label="确认密码" prop="checkpass">
    <el-input type="password" v-model="addUserForm.checkpass" autocomplete="off"></el-input>
  </el-form-item>
     <el-form-item label="姓名" prop="realname">
    <el-input type="text" v-model.number="addUserForm.realname" autocomplete="off"></el-input>
  </el-form-item>
     <el-form-item label="手机" prop="number">
    <el-input type="text" v-model.number="addUserForm.number" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item label="邮箱" prop="email">
    <el-input v-model="addUserForm.email" autocomplete="off"></el-input>
      </el-form-item>
</el-form>
          </span>
          <!--底部区域-->
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="addUserSubmit('addUserForm')">确 定</el-button>
          <el-button @click="addUserDialogVisible = false">取 消</el-button>
  </span>
        </el-dialog>
        <!--用户列表-->
        <template >
          <el-table
            :data="userList"
            border
          style="width: 100%">
            <el-table-column type="index" label="#"></el-table-column>
            <el-table-column label="管理员权限">
              <template slot-scope="scope">   <!--作用域插槽，scope.row获取一行的数据-->
                  <el-switch
                    @change="adminChanged(scope.row)"
                    v-model="scope.row.rowid"
                    active-value="1"
                    inactive-value="0">
                  </el-switch>
              </template>
            </el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="realname" label="姓名"></el-table-column>
            <el-table-column prop="number" label="联系方式"></el-table-column>
            <el-table-column prop="email" label="邮箱"></el-table-column>
            <el-table-column label="操作" width="100">
              <template slot-scope="scope">
                <el-tooltip content="修改" placement="top" :enterable="false">
                <el-button type="primary" icon="el-icon-edit" circle size="small" @click="openUpUserDialog(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top" :enterable="false">
                <el-button type="danger" icon="el-icon-delete" circle size="small" @click="deleteUser(scope.row.userid)"></el-button>
                </el-tooltip>
                <!--修改用户对话框-->
                <el-dialog
                  title="修改用户"
                  :visible.sync="upUserDialogVisible"
                  width="50%"
                @click="upUserDialogClosed('upUserForm')">
                  <!--内容主体区域-->
                  <span>
  <el-form :model="upUserForm" :rules="rules" ref="upUserForm" label-width="100px" class="demo-dynamic"
           status-icon>
     <el-form-item label="用户名" prop="username">
    <el-input type="text" v-model="upUserForm.username" autocomplete="off" :disabled="true"></el-input>
  </el-form-item>
    <el-form-item label="姓名" prop="realname">
    <el-input type="text" v-model.number="upUserForm.realname" autocomplete="off"></el-input>
  </el-form-item>
     <el-form-item label="手机" prop="number">
    <el-input type="text" v-model.number="upUserForm.number" autocomplete="off"></el-input>
  </el-form-item>
  <el-form-item label="邮箱" prop="email">
    <el-input v-model="upUserForm.email" autocomplete="off"></el-input>
      </el-form-item>
</el-form>
          </span>
                  <!--底部区域-->
                  <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="upUserSubmit('upUserForm')">确 定</el-button>
          <el-button @click="upUserDialogVisible = false">取 消</el-button>
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
        </template>
      </el-card>
    </div>
</template>

<script>
    export default {

        name: "Users",
        created() {
            this.getUserList();
        },
        data(){
            var validatePass = (rule, value, callback) => {
              if (value !== this.addUserForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            var validateRealname = (rule, value, callback) => {

                 if (Number.isFinite(value)) {
                    callback(new Error('请输入合法的姓名!'));
                    }
                 else if(value.length<2||value.length>20){
                     callback(new Error('请输入合法的姓名!'));
                 }
                 else {
                      callback();
                        }
            };
            var validateNumber = (rule, value, callback) => {
                //使用正则表达式验证手机号
            if (!(/^1[3456789]\d{9}$/.test(value))){
                callback(new Error('请输入合法的手机号!'));
            }
                else {
                    callback();
                }
            };
            return{
          userList:[],
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
                /*控制对话框显示与隐藏*/
                addUserDialogVisible:false,
                upUserDialogVisible:false,
                addUserForm:{
                    username:'',
                    password:'',
                    checkpass:'',
                    realname:'',
                    number:'',
                    email:''
                },
                upUserForm:{
                    userid:'',
                    rowid:'',
                    username:'',
                    password:'',
                    realname:'',
                    number:'',
                    email:''
                },
                rules:{
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                         { min:3,max:10, message: '用户名应该在3-10位', trigger: ['blur', 'change'] },
                         ],
                    password:[
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min:6,max:14, message: '密码应该在6-14位', trigger: ['blur', 'change'] }
                    ],
                    checkpass:[
                        { required: true, message: '请确认密码', trigger: 'blur' },
                        { validator: validatePass,  trigger: ['blur', 'change'] }
                    ],
                    realname:[
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { validator: validateRealname, trigger: ['blur', 'change'] }
                    ],
                    number:[
                        { required: true, message: '请输入手机号码', trigger: 'blur' },
                        { validator: validateNumber, trigger: ['blur', 'change'] }
                    ],
                    email:[
                        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                        { type: 'email', message: '请输入合法的邮箱地址', trigger: ['blur', 'change'] }
                    ]

                }
            }
        },


        methods:{
            getUserList:function () {
                this.$axios.get('/user/getUserList?current='+this.queryInfo.pagenum+'&size='+this.queryInfo.pagesize
                    +'&username='+this.queryInfo.query,
                    {headers:{'token':localStorage.getItem('token')}})
                    .then(res=>{
                   console.log(res)
                        this.userList=res.data.data.records
                        this.total=res.data.data.total
                    }).catch(err=>{
                        this.$message.error("服务器端错误")
                })
            },
            searchUser:function(){
                this.queryInfo.pagenum=1
                this.getUserList()
            },
            //监听每页数据量的变化
            handleSizeChange(newSize){
               console.log(newSize);
               this.queryInfo.pagesize=newSize;
                this.getUserList()
            },
            //监听页码值变化
            handleCurrentChange(newPage){
                console.log(newPage);
                this.queryInfo.pagenum=newPage;
                this.getUserList()
            },
            //监听switch开关状态的改变
            adminChanged(newUser){
                console.log(newUser);
                let user=this.$qs.stringify(newUser);
                this.$axios.post('/user/updateUser',user)   //发送整个更新后的user对象根据id进行更新
                    .then(res=>{
                        if(res.data.data.result){
                            this.$message.success("修改成功");
                        }else {
                            this.$message.error('修改失败')
                        }
                    }).catch(err=>{
                        this.$message.error('服务器端错误')
                })
            },
            addUserSubmit:function (formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let user=this.$qs.stringify(this.addUserForm);
                        this.$axios.post('/user/addUser',user)
                            .then(res=>{
                                console.log(res)
                                if(res.data.data.result){
                                    this.$message.success('添加成功');
                                    this.addUserDialogVisible=false;
                                    this.getUserList();
                                }else{
                                    this.$message.error('添加失败');
                                }
                            }).catch(err=>{
                            this.$message.error('服务器端错误')
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            openUpUserDialog:function(user){
                this.upUserDialogVisible=true;
                this.upUserForm=user;
                console.log(user)
            },
            upUserSubmit:function(formName){
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let user=this.$qs.stringify(this.upUserForm);
                        this.$axios.post('/user/updateUser',user)
                            .then(res=>{
                                console.log(res.data.data.result);
                                if(res.data.data.result){
                                    this.$message.success('修改成功');
                                    this.upUserDialogVisible=false;
                                    this.getUserList();
                                }else{
                                    this.$message.error('修改失败');
                                }
                            }).catch(err=>{
                            this.$message.error('服务器端错误')
                        });
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            //监听添加用户对话框关闭
            addUserDialogClosed:function (tablename) {
                    this.$refs[tablename].resetFields();
            },
            //监听修改用户对话框关闭
            upUserDialogClosed:function (tablename) {
                this.$refs[tablename].resetFields();
            },
            deleteUser:function (userid) {
                this.$confirm('此操作将永久删除该用户, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                  this.$axios.get('/user/deleteUser?userid='+userid)
                      .then(res=>{
                          console.log(res);
                          this.$message.success('删除成功')
                          this.getUserList();
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

        },

    }
</script>

<style lang="less"scoped>
.el-table{
  margin-top: 15px;
  font-size: 12px;
}
  .el-pagination{
    margin-top: 15px;
  }
</style>
