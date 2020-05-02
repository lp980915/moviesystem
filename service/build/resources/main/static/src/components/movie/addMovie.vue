<template>
  <div>
     <!--顶部面包屑-->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>电影管理</el-breadcrumb-item>
      <el-breadcrumb-item>电影列表</el-breadcrumb-item>
      <el-breadcrumb-item>添加电影</el-breadcrumb-item>
    </el-breadcrumb>

    <!-- 卡片视图-->
    <el-card class="box-card" shadow="hover" >
      <el-alert
        title="添加电影信息"
        type="info"
        center
        show-icon
        :closable="false">
      </el-alert>
      <el-steps :active="step-0" finish-status="success" align-center>
        <el-step title="电影信息" icon="el-icon-edit"></el-step>
        <el-step title="电影类型" icon="el-icon-set-up"></el-step>
        <el-step title="电影封面" icon="el-icon-picture"></el-step>
        <el-step title="完成"  icon="el-icon-circle-check"></el-step>
      </el-steps>
      <el-form :model="addForm" ref="addForm" :rules="addFormRules" label-position="left" label-width="80px">
      <el-tabs v-model="step" tab-position="left" :before-leave="activeTabs">
        <el-tab-pane label="电影信息" name="0">
          <el-form-item label="电影名称" prop="moviename">
            <el-input type="text" v-model="addForm.moviename" autocomplete="off" ></el-input>
          </el-form-item>
          <el-form-item label="电影简介" prop="movieintro">
            <el-input
              type="textarea"
              autosize
              v-model="addForm.movieintro"
              autocomplete="off">
            </el-input>
          </el-form-item>
          <el-form-item label="上映时间" prop="date">
            <el-date-picker
              v-model="addForm.date"
              type="date"
              placeholder="选择日期"
              value-format="yyyy-MM-dd"  >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="演职员" prop="actor">
            <el-cascader
              :key="actorKey"
              placeholder="添加相关艺人"
              :options="options"
              :props="{ multiple: true }"
              v-model="addForm.actor"
              @change="changeActor"
              filterable></el-cascader>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="电影类型" name="1" tab-click="toSort">
            <el-checkbox-group v-model="moviesort" size="small">
              <el-checkbox-button v-for="sort in sorts" :label="sort" :key="sort">{{sort}}</el-checkbox-button>
            </el-checkbox-group>
          <br>
          <el-radio-group v-model="moviecountry" size="small">
            <el-radio-button v-for="co in country" :label="co" :key="co">{{co}}</el-radio-button>
          </el-radio-group>
        </el-tab-pane>
        <el-tab-pane label="电影封面" name="2">
          <el-form-item  prop="movieimg">
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
          <el-button type="primary" round @click="submitForm">添加电影</el-button>
        </el-tab-pane>
      </el-tabs>
      </el-form>
    </el-card>
  </div>
</template>

<script>
    export default {
        name: "addMovie",
        data(){
            return{
                actorKey:0,
                step:'0',
                addForm:{
                    moviename:'',
                    movieintro:'',
                    date:'',
                    actor:''
                },
                addFormRules:{
                    moviename:[
                        { required: true, message: '请输入电影名', trigger: 'blur' },
                    ],
                    movieintro:[
                        { required: true, message: '请输入电影简介', trigger: 'blur' },
                    ],
                    date:[
                        { required: true, message: '请选择上映时间', trigger: 'blur' },
                    ],
                },
                moviesort: [],
                sorts: [],
                moviecountry:[],
                country:[],
                options:[
                    {
                    label: '导演',
                    children: [],
                    },
                    {
                        label: '演员',
                        children: [],
                    },
                ]
            }
        },
        created() {
            this.getSortList();
            this.getCountryList();
            this.getActorList();
        },
        methods:{
            changeActor(){
                ++this.actorKey;
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
            activeTabs:function(activeName, oldActiveName){
                if(activeName==='1') {
                    let result;
                    this.$refs['addForm'].validate((valid) => {
                        if (valid) {
                            result = true;
                        } else {
                            result = false;
                            this.$message.error("请填写电影信息")
                        }
                    });
                    return result;
                }
                else if(activeName==="2"){
                    let result;
                    this.$refs['addForm'].validate((valid) => {
                        if (valid) {
                            if(this.moviesort.length!==0&&this.moviecountry.length!==0){
                                result = true;
                            }else {
                                result = false;
                                this.$message.error("请选择电影分类及制片国家")
                            }
                        } else {
                            result = false;
                            this.$message.error("请填写电影信息")
                        }
                    });
                    return result;
                }
            },
            beforeAvatarUpload:function(file){
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
            uploadReq:function (file) {
                let param = new FormData();
                param.append('file',file.file);
                param.append('moviename',this.addForm.moviename);
                param.append('movieintro',this.addForm.movieintro);
                param.append('date',this.addForm.date);
                param.append('moviesort',this.moviesort);
                param.append('moviecountry',this.moviecountry);
                param.append('selectedactorid',this.addForm.actor);
                this.$axios.post('/movie/addMovie',param)
                    .then(res=>{
                        console.log(res);
                        if(res.data.data.result){
                            this.$message.success('添加成功');
                            this.step='4'
                        }else{
                            this.$message.error('添加失败');
                        }
                    })
                    .catch(err=>{
                        this.$message.error('服务器端错误');
                    })
            },
            submitForm:function () {
                this.$refs['upload'].submit();
            }
        },

    }
</script>

<style lang="less" scoped>
.el-checkbox-group{
  margin-top: 30px;
}

</style>
