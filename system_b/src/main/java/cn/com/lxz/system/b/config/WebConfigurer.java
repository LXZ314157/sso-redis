package cn.com.lxz.system.b.config;

import cn.com.lxz.system.b.authorization.interceptor.AuthorizationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 类似springmvc的配置
 * 将拦截器加入到配置中
 */
@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    @Resource
    private AuthorizationInterceptor authorizationInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/**");
    }

}
