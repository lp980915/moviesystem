<template>
    <div>           <!--:rules选择数据校验对象-->      <!--ref表示绑定表单实例对象-->
      <el-form :model="ruleForm"  label-position="top" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm"
      style="margin-top: 50px">
        <el-form-item label="用户名" prop="username"> <!-- prop表示选择rules里的username为验证规则-->
          <el-input type="text" v-model="ruleForm.username" autocomplete="off" style="width: 300px"
                    prefix-icon="el-icon-user" placeholder="请输入用户名" ></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="pass" >
          <el-input type="password" v-model="ruleForm.pass" autocomplete="off" style="width: 300px"
                    prefix-icon="el-icon-lock" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item style="margin-top: 15px;">
          <el-button type="primary" @click="login('ruleForm')" size="medium">登录</el-button>
        </el-form-item>
        <el-form-item style="padding-top: 3px;">
          <el-button  @click="resetForm('ruleForm')" size="medium">重置</el-button>
        </el-form-item>
      </el-form>
      <el-dialog :visible.sync="verifyModal" :closable="false" footer-hide width="350px">
        <slide-verify  ref="slideblock"
                       @again="onAgain"
                       @success="onSuccess"
                       @fail="onFail"
                       :accuracy="accuracy"
                       :imgs="imgs"
                       slider-text="滑一滑"
        ></slide-verify>
      </el-dialog>
    </div>
</template>

<script>
    import {mapMutations} from 'vuex';

    export default {
        data() {
            var validateUsername=(rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户名'));
                }
                    callback();

            };

            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    if (this.ruleForm.checkPass !== '') {
                        this.$refs.ruleForm.validateField('checkPass');
                    }
                    callback();
                }
            };
            return {
                ruleForm: {
                    username:'',
                    pass: '',
                },
                rules: {   //rules验证对象填写验证规则
                    username: [
                        { validator: validateUsername, trigger: 'blur' },
                        { min:3,max:10,message:'用户名长度在3-10个字符之间',trigger: 'blur'}
                    ],
                    pass: [
                        { validator: validatePass, trigger: 'blur' },
                        { min:6,max:14,message:'密码长度在6-14个字符之间',trigger: 'blur'}
                    ],

                },
              verifyModal:false,
              // 精确度小，可允许的误差范围小；为1时，则表示滑块要与凹槽完全重叠，才能验证成功。默认值为5
              accuracy: 7,
              imgs:["http://q7u9zowxe.bkt.clouddn.com/guimie.jpg",
                "http://q7u9zowxe.bkt.clouddn.com/wlopbackground.jpg",
                "http://q7u9zowxe.bkt.clouddn.com/timg.jpg",
                "http://q7u9zowxe.bkt.clouddn.com/yourname.jpg"]
            }
        },
        created:function () {
            let that = this;
            document.onkeydown = function(e){

                let key = window.event.keyCode;

                if(key === 13 || key === 100){

                    that.login('ruleForm');

                }

            }
        },
        methods: {
            ...mapMutations(['setToken']),
          onSuccess(){
            let that=this;
            let url='/user/login';
            let user=this.$qs.stringify({
              username:this.ruleForm.username,
              password:this.ruleForm.pass
            });
            this.$axios.post(url,user
            ).then(res=>{
              console.log(res);
              if(res.data.token!=null&&res.data.token!==''){
                that.token=res.data.token;
                console.log(that.token);
                that.setToken({token:that.token});    //vuex方式存储token
                //localStorage.setItem('token',that.token);
                console.log(res.data.user);
                localStorage.setItem("user",JSON.stringify(res.data.user));  //localStorage只能存储字符串类型
                that.$router.push('/home');
                that.$notify.success('登录成功');
              }else {
                that.$notify.error('用户名或密码错误');
                that.verifyModal=false;
                this.$refs.slideblock.reset();
              }
            }).catch(err=>{
              this.$notify.error('服务器端错误');
              console.log(err);
            });
          },
          onFail(){
            this.$message.error("验证失败");
          },
          onAgain() {
            this.$message.warning("检测到非人为操作");
            this.$refs.slideblock.reset();
          },
            login(formName) {
                this.$refs[formName].validate((valid) => {   //validate接受回调函数，为验证结果true或者false
                    if (valid) {
                      this.verifyModal=true;
                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style lang="less" scoped>
.el-form{
  margin-left: 30px;
  .el-form-item{
    margin-top: -15px;
  }
}
  .el-button{
    width: 300px;
  }
</style>
