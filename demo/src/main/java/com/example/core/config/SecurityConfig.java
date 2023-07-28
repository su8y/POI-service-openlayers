package com.example.core.config;

import com.example.core.auth.CustomAuthenticationProcessingFilter;
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

    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //인가 authorization
        http.authorizeHttpRequests()
                .antMatchers("/poi/**").hasRole("user")
                .antMatchers("/admin/**").hasRole("admin")
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .formLogin().disable()
                .logout().disable()
                .addFilterBefore(customAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);


        return http.build();
    }

    @Bean
    CustomAuthenticationProcessingFilter customAuthenticationProcessingFilter() throws Exception {
        return new CustomAuthenticationProcessingFilter(jwtTokenProvider);
    }

    //Password Encoder Register
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
