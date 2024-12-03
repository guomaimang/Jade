package tech.hirsun.jade.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.hirsun.jade.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;


    /**
     * Add the interceptor. The method will be called when the application starts.
     * @param registry is the InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // The larger the order, the later the interceptor is executed
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/demo/proc/**")
                .addPathPatterns("/userauth/refreshtoken")
//                .addPathPatterns("/picture/**")
                .addPathPatterns("/message/**")
                .addPathPatterns("/user/**")
                .order(0);
    }
}
