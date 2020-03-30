<template>
  <div>
    <center>
    <el-button icon="el-icon-search" circle @click="getTableData"></el-button>
    <el-table
      :data="customertabledata"
      border
      style="width: 75%">
      <el-table-column
        prop="userid"
        label="userid"
        width="180">
      </el-table-column>
      <el-table-column
        prop="username"
        label="username"
        width="180">
      </el-table-column>
      <el-table-column
        prop="password"
        label="password">
      </el-table-column>
      <el-table-column
        prop="sex"
        label="sex">
      </el-table-column>
      <el-table-column
        prop="tell"
        label="tell">
      </el-table-column>
    </el-table>
  <el-pagination
    v-if="page.total>page.size"
    :page-size="page.size"
    :current-page="page.current"
    @current-change="currentChange"
    :total="page.total"
    :pager-count="11"
    layout="prev, pager, next">
  </el-pagination>
    </center>

  </div>
</template>

<script>
    export default {
        name: "CustomerTable",
        data(){
            return{
                page:{
                    current:1,
                    size:5,
                    total:0
                },
                customertabledata:[]
            }
        },
        methods:{

            //分页点击事件
            currentChange:function(current){
                console.log(current);
                this.page.current=current;
                this.getTableData();
            },
            getTableData:function () {
                this.$axios.get('/user/getList?current='+this.page.current+"&size="+this.page.size)
                    .then(res=>{
                        console.log(res);
                        this.customertabledata=res.data.data.records;
                        this.page.total=res.data.data.total;

                    }).catch(err=>{
                    alert(err);
                })
            }
        },


    }
</script>

<style lang="less" scoped>

</style>
