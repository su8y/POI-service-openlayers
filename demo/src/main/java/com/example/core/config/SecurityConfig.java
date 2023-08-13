package com.example.core.config;

import com.example.core.auth.CustomAuthenticationProcessingFilter;
import com.example.core.auth.JwtAccessDeninedHandler;
import com.example.core.auth.JwtTokenProvider;
import com.example.core.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
//@PropertySource(value = "application.yml")
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/corsConfiguration/ui",
            "/swagger-resources/**",
            "/corsConfiguration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/file/**",
            "/image/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/h2/**",
            "/ws/**",
            "/sub/**",
            "/pub/**",
            "/wss/**",
    };
    private final JwtAccessDeninedHandler jwtAccessDeninedHandler;
    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public DefaultSecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
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
                .addFilterBefore(new CustomAuthenticationProcessingFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(jwtAccessDeninedHandler);

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(((request, response, authentication) -> {
                    response.sendRedirect("/");
                }))
                .deleteCookies("access-token", "refresh-token");

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    //Password Encoder Register
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
