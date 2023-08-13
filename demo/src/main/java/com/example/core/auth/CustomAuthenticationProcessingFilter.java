package com.example.core.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        try {
            if (StringUtils.hasText(token) && jwtTokenProvider.validateAccessToken(token)) {
                // 인증이 완료되면 인증완료 처리를 한다.
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
//            response.sendError(403);
        }
        filterChain.doFilter(request, response);

    }

    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("access-token");
        if (StringUtils.hasText(header))
            return header.substring(6); // extract prefix [Bearer]

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token"))
                return cookie.getValue().substring(6);
        }
        return null;
    }


}
