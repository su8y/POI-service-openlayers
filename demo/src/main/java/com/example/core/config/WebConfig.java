package com.example.core.config;

import com.example.core.poi.util.GeomMethodArgumentsResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    //    @Bean
//    public ObjectMapper objectMapper(){
//        return new ObjectMapper();
//    }
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GeomMethodArgumentsResolver geomMethodArgumentsResolver;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/signIn").setViewName("user/sign-in");
        registry.addViewController("/signUp").setViewName("user/sign-up");
        registry.addViewController("/poi/save").setViewName("poi/save");
        registry.addViewController("/poi/manage").setViewName("poi/manage");
        registry.addViewController("/poi/detail/{poiId}").setViewName("poi/detail/index");
        registry.addViewController("/route/find").setViewName("route/find");
        registry.addViewController("/route/list").setViewName("route/list");
        registry.addViewController("/route/detail/**").setViewName("route/detail/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/*").addResourceLocations("classpath:/static/");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
        resolvers.add(geomMethodArgumentsResolver);
    }


}
