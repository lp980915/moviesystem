<template>
  <div>
  <!--顶部面包屑-->
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
    <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    <el-breadcrumb-item>修改密码</el-breadcrumb-item>
  </el-breadcrumb>
  <!-- 卡片视图-->
  <el-card class="box-card" shadow="hover" >
    <div slot="header" class="clearfix">
      <span>修改密码</span>
      <el-popover
        placement="top"
        width="160"
        v-model="visible">
        <p><i class="el-icon-info redColor"/>确定要修改密码吗？</p>
        <div style="text-align: right; margin: 0">
          <el-button size="mini" type="text" @click="visible = false">取消</el-button>
          <el-button type="primary" size="mini" @click="upPass('ruleForm')">确定</el-button>
        </div>
      <el-button style="float: right; padding: 3px 0" type="text" slot="reference">提交</el-button>
      </el-popover>
    </div>
    <div>
      <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item label="原密码" prop="oldPass">
          <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="password">
          <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
  </div>
</template>

<script>
    import {mapMutations} from "vuex";
    export default {
        name: "UpPass",
        data(){
            var validateOldPass = (rule, value, callback) => {

                if (value === '') {
                    callback(new Error('请输入原密码'));
                } else {
                    let user=JSON.parse(localStorage.getItem('user'));
                    let password=user.password;
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }if(password!==value){
                        callback(new Error('原密码错误'));
                    }
                    callback();
                }
            };
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入新密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    if(value.length<6||value.length>14){
                        callback(new Error('密码长度在6-14个字符之间'));
                    }
                    if(value===this.ruleForm.oldPass){
                        callback(new Error('新密码不能与原密码一致'));
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入新密码'));
                } else if (value !== this.ruleForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return{
                ruleForm: {
                    oldPass: '',
                    password: '',
                    checkPass: ''
                },
                rules: {
                    oldPass: [
                        { validator: validateOldPass, trigger: 'blur' }
                    ],
                    password: [
                        { validator: validatePass, trigger: 'blur' }
                    ],
                    checkPass: [
                        { validator: validatePass2, trigger: 'blur' }
                    ],
                },
                visible: false,
            }
        },
        methods:{
            ...mapMutations(['delToken']),
            upPass:function (formName) {
                this.$refs[formName].validate((valid) => {   //validate接受回调函数，为验证结果true或者false
                    if (valid) {
                        let user = JSON.parse(localStorage.getItem('user'));
                        user.password = this.ruleForm.password;
                        let newUser=this.$qs.stringify(user)
                        this.$axios.post('/user/updateUser',newUser)
                            .then(res=>{
                                if(res.data.data.result){
                                    this.$notify.success("修改成功,请重新登录");
                                    this.delToken({token:''});
                                    sessionStorage.removeItem('index')
                                    this.$router.push('/login');

                            }else {
                                this.$message.error('修改失败')
                            }
                            }).catch(err=>{
                            this.$message.error('服务器端错误')
                        });
                    }else {
                        console.log('error submit!!');
                        return false;
                    }
                });
                this.visible=false
            }
        }
    }
</script>

<style lang="less" scoped>
.redColor{
  color: red;
  margin-right: 5px;
}
</style>
