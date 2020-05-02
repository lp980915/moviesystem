<template>
  <el-tooltip content="注销" placement="bottom" transition="el-zoom-in-center">
  <el-button type="danger" icon="el-icon-close" circle @click="logout"></el-button>
  </el-tooltip>
</template>

<script>

    import {mapMutations} from "vuex";
    export default {
        name: "LogoutBtn",
        methods:{
            ...mapMutations(['delToken']),
            logout:function () {
                this.delToken({token:''});
                sessionStorage.removeItem('index');
                //localStorage.removeItem('token');
                this.$router.push('/login');
                this.$notify.success('成功注销');
            }
        }
    }
</script>

<style lang="less" scoped>

</style>
