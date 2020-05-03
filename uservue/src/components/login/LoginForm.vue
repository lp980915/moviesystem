<template>
    <div>
        <Form ref="loginForm" :model="loginForm" :rules="loginRules" label-position="left" :label-width="70"
        :hide-required-mark="true" :label-colon="true">
            <FormItem prop="username" label="用户名">
                <Input type="text" v-model="loginForm.username" placeholder="请输入用户名" style="width: 350px" clearable>
                </Input>
            </FormItem>
            <FormItem prop="password" label="密码">
                <Input type="password" v-model="loginForm.password" placeholder="请输入密码" style="width: 350px" password>
                </Input>
                <span class="forgetPass" @click="findPassModal=true">忘记密码?</span>
            </FormItem>
            <FormItem>
                <Button type="primary" @click="login('loginForm')" style="width: 150px">登录</Button>
                <Button type="info" @click="addUserDrawer=true" style="width: 150px;margin-left:20px">注册</Button>
            </FormItem>
        </Form>

        <Drawer
                title="欢迎注册"
                v-model="addUserDrawer"
                width="500"
                :mask-closable="false"
        >
            <Form ref="addUserForm" :model="addUserForm" :rules="addUserRules" :label-width="80">
                <FormItem label="用户名" prop="username">
                    <Input v-model="addUserForm.username" placeholder="请填写用户名"></Input>
                </FormItem>
                <FormItem label="密码" prop="password">
                    <Input v-model="addUserForm.password" placeholder="请填写密码" type="password" password></Input>
                </FormItem>
                <FormItem label="确认密码" prop="checkpass">
                    <Input v-model="addUserForm.checkpass" placeholder="请确认密码" type="password" password></Input>
                </FormItem>
                <FormItem label="姓名" prop="realname">
                    <Input v-model="addUserForm.realname" placeholder="请填写姓名"></Input>
                </FormItem>
                <FormItem label="手机号" prop="number">
                    <Input v-model="addUserForm.number" placeholder="请填写手机号"></Input>
                </FormItem>
                <FormItem label="邮箱" prop="email">
                    <Input v-model="addUserForm.email" placeholder="请填写邮箱"></Input>
                </FormItem>
                <FormItem class="addUserDrawerFooter">
                <Button type="primary" @click="addUserSubmit('addUserForm')">注册</Button>
                <Button @click="addUserDrawer=false" style="margin-left: 8px">退出</Button>
                </FormItem>
            </Form>
        </Drawer>

        <Modal
                :mask-closable="false"
                v-model="findPassModal"
                title="找回密码"
                width="40%"
                :loading="loading"
                @on-ok="findPass">
            <Form ref="findPassForm" :model="findPassForm" :rules="addUserRules" :label-width="80">
                <FormItem label="邮箱" prop="email">
                    <Input v-model="findPassForm.email" placeholder="请填写邮箱"></Input>
                </FormItem>
            <FormItem label="新密码" prop="password">
                <Input v-model="findPassForm.password" placeholder="请填写密码" type="password" password></Input>
            </FormItem>
            <FormItem label="确认密码" prop="checkpass">
                <Input v-model="findPassForm.checkpass" placeholder="请确认密码" type="password" password></Input>
            </FormItem>
            </Form>
        </Modal>

        <Modal v-model="verifyModal" :closable="false" footer-hide width="350px">
            <slide-verify  ref="slideblock"
                           @again="onAgain"
                           @success="onSuccess"
                           @fail="onFail"
                           :accuracy="accuracy"
                           :imgs="imgs"
                           slider-text="滑一滑"
            ></slide-verify>
        </Modal>
    </div>
</template>

<script>
    export default {
        name: "LoginForm",
        data(){
            var validatePass = (rule, value, callback) => {
                if (value !== this.addUserForm.password && value !==this.findPassForm.password) {
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
                loginForm: {
                    username: '',
                    password: ''
                },
                loginRules: {
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { type: 'string', min: 3,max:10, message: '用户名长度在3-10个字符之间', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { type: 'string', min: 6,max:14, message: '密码长度在6-14个字符之间', trigger: 'blur' }
                    ]
                },
                addUserForm:{
                    username:'',
                    password:'',
                    checkpass:'',
                    realname:'',
                    number:'',
                    email:''
                },
                addUserRules:{
                    username: [
                        { required: true, message: '请输入用户名', trigger: 'blur' },
                        { min:3,max:10, message: '用户名应该在3-10位', trigger: 'blur' },
                    ],
                    password:[
                        { required: true, message: '请输入密码', trigger: 'blur' },
                        { min:6,max:14, message: '密码应该在6-14位', trigger: 'blur' }
                    ],
                    checkpass:[
                        { required: true, message: '请确认密码', trigger: 'blur' },
                        { validator: validatePass,  trigger: 'blur' }
                    ],
                    realname:[
                        { required: true, message: '请输入姓名', trigger: 'blur' },
                        { validator: validateRealname, trigger: 'blur'}
                    ],
                    number:[
                        { required: true, message: '请输入手机号码', trigger: 'blur' },
                        { validator: validateNumber, trigger: 'blur' }
                    ],
                    email:[
                        { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                        { type: 'email', message: '请输入合法的邮箱地址', trigger: 'blur' }
                    ]
                },
                findPassForm:{
                    email:'',
                    password:'',
                    checkpass:''
                },
                findPassModal:false,
                verifyModal:false,
                loading:true,
                addUserDrawer:false,
                // 精确度小，可允许的误差范围小；为1时，则表示滑块要与凹槽完全重叠，才能验证成功。默认值为5
                accuracy: 7,
                imgs:["http://cdn.ljcblog.top/guimie.jpg",
                    "http://cdn.ljcblog.top/wlopbackground.jpg",
                    "http://cdn.ljcblog.top/timg.jpg",
                    "http://cdn.ljcblog.top/yourname.jpg"]
            }
        },
        methods:{
            verfiySuccess(){
                this.verifyModal=false;
                let user=this.$qs.stringify(this.loginForm);
                this.axios.post('/customer/login',user)
                    .then(res=>{
                        console.log(res)
                        if(res.data.data.token!=null&&res.data.data.token!==''){
                            localStorage.setItem('token',res.data.data.token);
                            localStorage.setItem('loginUser',JSON.stringify(res.data.data.user));
                            sessionStorage.setItem('color','white');
                            this.axios.defaults.headers.common['token']=res.data.data.token;
                            this.$router.push('/welcome');
                            this.$Message.success('登录成功!');
                        }else {
                            this.$Message.error(res.data.data);
                            this.verifyModal=false;
                            this.$refs.slideblock.reset();
                        }
                    }).catch(err=>{
                    console.log(err)
                })
            },
            onSuccess(){
              this.verfiySuccess();
            },
            onFail(){
                this.$Message.error("验证失败");
            },
            onAgain() {
                this.$Message.warning("检测到非人为操作");
                this.$refs.slideblock.reset();
            },
            login(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.verifyModal=true;
                    } else {
                        this.$Message.error('请填写正确的登录信息!');
                    }
                })
            },
            findPass () {
                setTimeout(() => {
                        this.$refs['findPassForm'].validate((valid) => {
                            if (valid) {
                                let findPassUser=this.$qs.stringify(this.findPassForm);
                                this.axios.post('/customer/findPassword',findPassUser)
                                    .then(res=>{
                                        if(res.data.data){
                                            this.loading=false;
                                            this.findPassModal = false;
                                            this.$Message.success('成功找回密码!');
                                            this.$refs['findPassForm'].resetFields();
                                        }else{
                                            this.$Message.error('请填写正确的邮箱!');
                                            this.loading=false
                                        }
                                    }).catch(err=>{
                                    console.log(err)
                                    this.loading=false
                                });
                            }else {
                                this.$Message.error('请完善信息!');
                                this.loading=false
                            }
                        });
                }, 1500);
            },
            addUserSubmit (name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        let user=this.$qs.stringify(this.addUserForm);
                      this.axios.post('/customer/addCustomer',user)
                          .then(res=>{
                              if(res.data.data.result){
                                  this.$refs[name].resetFields();
                                  this.addUserDrawer=false;
                                  this.$Message.success('注册成功!');
                              }else {
                                  this.$Message.error('注册失败!');
                              }
                          }).catch(err=>{
                          console.log(err)
                      })
                    } else {
                        this.$Message.error('请按要求填写注册信息!');
                    }
                })
            },
        },
        created:function () {
            let that = this;
            document.onkeydown = function(e){
                let key = window.event.keyCode;
                if(key === 13 || key === 100){
                    that.login('loginForm');

                }

            }
        },
    }
</script>

<style lang="less" scoped>
.ivu-form-item{
margin-top: 20px;
}
    .forgetPass{
        margin-left: 20px;
        cursor:pointer;
        font-size: 12px;
        color: darkgray;
    }
    .addUserDrawerFooter{
        width: 100%;
        position: absolute;
        bottom: 0;
        left: 0;
        border-top: 1px solid #e8e8e8;
        padding: 10px 16px;
        text-align: right;
        background: #fff;

    }
</style>