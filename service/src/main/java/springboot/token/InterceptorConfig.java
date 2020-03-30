package springboot.token;
/***
   * 新建Token拦截器
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
 @Override
   public void addInterceptors(InterceptorRegistry registry) {
     registry.addInterceptor(authenticationInterceptor()).addPathPatterns("/**");     // 拦截所有请求，通过判断是否有 @LoginRequired 注解 决定是否需要登录
          }
   @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
             return new AuthenticationInterceptor();  // 自己写的拦截器
         }   //省略其他重写方法
}
