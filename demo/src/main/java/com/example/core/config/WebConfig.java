package com.example.core.config;

import com.example.core.util.PolygonArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/signIn").setViewName("user/sign-in");
        registry.addViewController("/signUp").setViewName("user/sign-up");
        registry.addViewController("/poi/save").setViewName("poi/save");
        registry.addViewController("/poi/manage").setViewName("poi/manage");
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
        resolvers.add(new PolygonArgumentResolver());

    }
}
