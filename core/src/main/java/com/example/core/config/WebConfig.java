package com.example.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addViewController("/home").setViewName("zido");
        registry.addViewController("/signIn").setViewName("user/sign-in");
        registry.addViewController("/signUp").setViewName("user/sign-up");
        registry.addViewController("/poi/save").setViewName("poi/save");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/*").addResourceLocations("classpath:/static/");
    }
}
