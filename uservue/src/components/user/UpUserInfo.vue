<template>
    <div>
        <Card  :style="'background:'+color+';margin: 4%'" >
            <Row>
                <Col :xs="{ span: 24, offset: 0 }" :sm="{ span: 20, offset: 0 }"  :md="{ span: 18, offset: 1 }" :lg="{ span: 13, offset: 1 }">
            <h2 style="text-align: center">修改信息</h2>
            <Form ref="upUserForm" :model="upUserForm" :rules="rules" :label-width="80" style="margin-top: 40px">
                <FormItem label="用户名" prop="username">
                    <Input v-model="upUserForm.username" placeholder="请输入用户名"></Input>
                </FormItem>
                <FormItem label="姓名" prop="realname">
                    <Input v-model="upUserForm.realname" placeholder="请输入姓名"></Input>
                </FormItem>
                <FormItem label="手机号" prop="number">
                    <Input v-model="upUserForm.number" placeholder="请输入手机号"></Input>
                </FormItem>
                <FormItem label="邮箱" prop="email">
                    <Input v-model="upUserForm.email" placeholder="请输入邮箱"></Input>
                </FormItem>
                <FormItem label="个性签名" prop="autograph">
                    <Input  maxlength="40" show-word-limit v-model="upUserForm.autograph" placeholder="请输入个性签名"></Input>
                </FormItem>
                <FormItem>
                    <Button type="primary" @click="addUserSubmit('upUserForm')">更新信息</Button>
                    <Button style="margin-left: 20px" @click="upPassModal=true">修改密码</Button>
                </FormItem>
            </Form>
                <Modal v-model="upPassModal"
                       title="修改密码">
                    <Form ref="upPassForm" :model="upPassForm" :rules="passRules" :label-width="80" style="margin-top: 20px">
                        <FormItem label="原密码" prop="oldPass">
                            <Input  type="password" v-model="upPassForm.oldPass" placeholder="请输入原密码" password></Input>
                        </FormItem>
                        <FormItem label="新密码" prop="password">
                            <Input type="password" v-model="upPassForm.password" placeholder="请输入新密码" password></Input>
                        </FormItem>
                        <FormItem label="确认密码" prop="checkPass">
                            <Input type="password" v-model="upPassForm.checkPass" placeholder="请确认新密码" password></Input>
                        </FormItem>
                    </Form>
                    <div slot="footer">
                        <Button type="primary" @click="upPass">提交</Button>
                        <Button  @click="upPassModal=false">取消</Button>
                    </div>
                </Modal>
                </Col>
                <Col  :xs="24" :sm="16" :md="12" :lg="8">
                    <Avatar :src="user.userimg" size="150" style="margin-left:40%;margin-top: 10%"></Avatar>
                    <div>
                        <!-- action="http://60.205.222.92:8088/api/customer/uploadUserImg"-->
                        <Upload action="http://localhost:8082/api/customer/uploadUserImg"
                                :headers="token" :data="user"
                                :on-success="handleSuccess"
                                :format="['jpg','jpeg','png']"
                                :max-size="2048"
                                :on-format-error="handleFormatError"
                                :on-exceeded-size="handleMaxSize"
                                :on-error="handleError">
                            <Button icon="ios-cloud-upload-outline" style="margin:50% 210%">修改头像</Button>
                        </Upload>
                    </div>
                </Col>
            </Row>
        </Card>
    </div>
</template>

<script>
    export default {
        name: "UpUserInfo",
        data(){
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
            var validateOldPass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入原密码'));
                } else {
                    let user=JSON.parse(localStorage.getItem('loginUser'));
                    let password=user.password;
                    if (this.upPassForm.checkPass !== '') {
                        this.$refs.upPassForm.validateField('checkPass');
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
                    if (this.upPassForm.checkPass !== '') {
                        this.$refs.upPassForm.validateField('checkPass');
                    }
                    if(value.length<6||value.length>14){
                        callback(new Error('密码长度在6-14个字符之间'));
                    }
                    if(value===this.upPassForm.oldPass){
                        callback(new Error('新密码不能与原密码一致'));
                    }
                    callback();
                }
            };
            var validatePass2 = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请再次输入新密码'));
                } else if (value !== this.upPassForm.password) {
                    callback(new Error('两次输入密码不一致!'));
                } else {
                    callback();
                }
            };
            return{
                user:JSON.parse(localStorage.getItem('loginUser')),
                upUserForm:{
                    userid:JSON.parse(localStorage.getItem('loginUser')).userid,
                    rowid:JSON.parse(localStorage.getItem('loginUser')).rowid,
                    username:JSON.parse(localStorage.getItem('loginUser')).username,
                    realname:JSON.parse(localStorage.getItem('loginUser')).realname,
                    number:JSON.parse(localStorage.getItem('loginUser')).number,
                    email:JSON.parse(localStorage.getItem('loginUser')).email,
                    autograph:JSON.parse(localStorage.getItem('loginUser')).autograph,
                    password:JSON.parse(localStorage.getItem('loginUser')).password
                },
                token:{token:localStorage.getItem('token')},
                rules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min:3,max:10, message: '用户名应该在3-10位', trigger: 'blur' },
                    ],
                    realname:[
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { validator: validateRealname, trigger: 'blur' }
                    ],
                    number:[
                        { required: true, message: '请输入手机号码', trigger: 'blur' },
                        { validator: validateNumber, trigger:'blur' }
                    ],
                    email:[
                        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                        { type: 'email', message: '请输入合法的邮箱地址', trigger: 'blur'}
                    ]
                },
                upPassModal:false,
                upPassForm:{
                    oldPass:'',
                    password:'',
                    checkPass:''
                },
                passRules:{
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
                color:sessionStorage.getItem('color')
            }
        },
        methods:{
            addUserSubmit (name) {
                this.$refs[name].validate((valid) => {
                    let user=this.$qs.stringify(this.upUserForm);
                    if (valid) {
                        this.axios.post('/user/updateUser',user)
                            .then(res=>{
                                console.log(res)
                                if(res.data.data.result){
                                    this.$Message.success('更新成功!');
                                    localStorage.setItem('loginUser',JSON.stringify(res.data.data.newUser))
                                }else {
                                    this.$Message.success('更新失败!')
                                }
                            }).catch(err=>{
                                console.log(err);
                        });
                    } else {
                        this.$Message.error('信息填写有误!');
                    }
                })
            },
            handleSuccess:function (response, file, fileList) {
                if(response.data.result){
                    this.$Message.success('上传成功');
                }else {
                    this.$Message.error('上传失败');
                }
                let newUser=JSON.stringify(response.data.newUser);
                localStorage.setItem('loginUser',newUser)
                window.location.reload()
            },
            handleFormatError (file) {
                this.$Message.warning('请上传JPEG/JPG/PNG格式图片');
            },
            handleMaxSize (file) {
                this.$Message.warning('文件大小不能超过2m');
            },
            handleError(){
                this.$Message.warning('上传失败');
            },
            upPass:function () {
                this.$refs['upPassForm'].validate((valid) => {   //validate接受回调函数，为验证结果true或者false
                    if (valid) {
                        let user = JSON.parse(localStorage.getItem('loginUser'));
                        user.password = this.upPassForm.password;
                        let newUser=this.$qs.stringify(user);
                        this.$axios.post('/user/updateUser',newUser,
                            {headers:{'token':localStorage.getItem('token')}})
                            .then(res=>{
                                if(res.data.data.result){
                                    this.$Message.success("修改成功,请重新登录");
                                    localStorage.clear();
                                    sessionStorage.clear();
                                    this.$router.push('/login');
                                }else {
                                    this.$Message.error('修改失败')
                                }
                            }).catch(err=>{
                           console.log(err)
                        });
                    }else {
                      this.$Message.error('请完善信息!');
                        return false;
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>