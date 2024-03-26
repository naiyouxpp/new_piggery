package zhushe.demo.new_piggery.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //addInterceptors是继承类WebMvcConfigurationSupport提供的空方法，向方法内传拦截器
        registry.addInterceptor(jwtInterceptor())                       //配置jwt的拦截器规则
                .addPathPatterns("/**")                                 //拦截所有的请求路径
                .excludePathPatterns("/user/login","/user/register","/file/download/{fileName}");   //放行路径
        super.addInterceptors(registry);
    }

    //通过@Bean将new出来的拦截器注入到Bean中
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}