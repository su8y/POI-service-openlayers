package com.example.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addViewController("/home").setViewName("/home");
        registry.addViewController("/signIn").setViewName("/user/sign-in");
        registry.addViewController("/signUp").setViewName("/user/sign-up");
        registry.addViewController("/poi/save").setViewName("/poi/save");
        registry.addViewController("/poi/manage").setViewName("/poi/manage");
        registry.addViewController("/route/find").setViewName("/route/find");
        registry.addViewController("/route/list").setViewName("/route/list");
        registry.addViewController("/route/detail/**").setViewName("/route/detail/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/*").addResourceLocations("classpath:/static/");
    }
}
