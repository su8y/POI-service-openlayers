package com.example.core.config;

import com.example.core.auth.CookieUtil;
import com.example.core.auth.CustomAuthenticationProcessingFilter;
import com.example.core.auth.JwtAccessDeninedHandler;
import com.example.core.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
//@PropertySource(value = "application.yml")
public class SecurityConfig {
    private final JwtAccessDeninedHandler jwtAccessDeninedHandler;
    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //인가 authorization
        http.authorizeHttpRequests()
                .antMatchers("/poi/**").hasRole("user")
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/route/list/**").hasAnyRole("user")
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/home/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(customAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.formLogin()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeninedHandler);

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    response.sendRedirect("/home");
                }))
                .deleteCookies("access_token", "refresh_token");

        return http.build();
    }

    @Bean
    CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter() throws Exception {
        CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter = new CustomAuthenticationProcessingFilter(jwtTokenProvider);
        return customAuthenticationProcessingFilter;
    }

    //Password Encoder Register
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
