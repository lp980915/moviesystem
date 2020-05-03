<template>

          <Layout>
              <Carousel autoplay v-model="value2" loop trigger="hover">
                  <CarouselItem>
                      <div class="demo-carousel">
                          <img src="../../assets/img/carousel/shenghua-login.jpg" height="750" width="100%" alt="生化危机"/>
                      </div>
                  </CarouselItem>
                  <CarouselItem>
                      <div class="demo-carousel">
                          <img src="../../assets/img/carousel/dasheng-login.jpg" height="750" width="100%" alt="大圣归来"/></div>
                  </CarouselItem>
                  <CarouselItem>
                      <div class="demo-carousel">
                          <img src="../../assets/img/carousel/mingzi.jpg" height="750" width="100%" alt="你的名字"/></div>
                  </CarouselItem>
              </Carousel>
              <Content>
                  <Row>
                      <Col span="6">
                          <div class="loginTitle"><span><Icon type="ios-film"/>电影交流平台</span></div>
                      </Col>
                      <Col span="16">
                          <Card>
                              <LoginForm></LoginForm>
                          </Card>
                      </Col>
                  </Row>

              </Content>
          </Layout>

</template>

<script>
    import LoginForm from "@/components/login/LoginForm";
    export default {
        name: "Login",
       components:{LoginForm},
        data(){
            return{
                value2: 0
            }
        }
    }
</script>

<style lang="less" scoped>
    .ivu-layout {
        height: 100%;
    }
    .ivu-card{
        margin-left: 10%;
        margin-top: 15px;
        padding: 10px;
        height: 230px;
        width: 600px;
    }
    .loginTitle{
        margin-left: 50%;
        margin-top: 18%;
        color:#464c5b;
        font-size: 25px;
    }

</style>