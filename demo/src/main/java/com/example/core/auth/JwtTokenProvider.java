package com.example.core.auth;

import com.example.core.redis.RedisService;
import com.example.core.util.TokenInfo;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    private final RedisService redisService;
    public static final String REFRESH_TOKEN_NAME = "refresh_token";
    public static final String TOKEN_GRANT_TYPE = "Bearer";
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretkey, RedisService redisService) {
        byte[] decode = Decoders.BASE64.decode(secretkey);
        this.key = Keys.hmacShaKeyFor(decode);
        this.redisService = redisService;
    }

    public TokenInfo generateToken(Authentication authentication) {
        // refreshToken이 있는 경우 삭제
        if (redisService.getValues(authentication.getName()) != null) {
            redisService.deleteValues(authentication.getName());
        }
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setExpiration(accessTokenExpiresIn)
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // refreshtoken username 쌍으로 저장
        redisService.setValuesWithTimeout(authentication.getName(), refreshToken, getTokenExpirationTime(refreshToken));

        return TokenInfo.builder()
                .grantType(TOKEN_GRANT_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public boolean validateRefreshToken(String refreshToken) {
        try {
//            if (redisService.getValues(refreshToken).equals("delete")) { // 회원 탈퇴했을 경우
//                return false;
//            }
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        } catch (NullPointerException e) {
            log.error("JWT Token is empty.");
        }
        return false;
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateAccessToken(String token) {
        try {
            String value = redisService.getValues(token);
            if (value != null && value.equals("logout")) return false;
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            throw new ExpiredJwtException(null, null, e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.", e);
        }
        return false;
    }

    public long getTokenExpirationTime(String token) {
        return parseClaims(token).getExpiration().getTime();
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateAccessTokenOnlyExpired(String accessToken) {
        try {
            return parseClaims(accessToken)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * requestToken이 null이 아니고 Bearer 토큰인지 검사.
     *
     * @param requestToken
     * @return substring at 7 ex::Bearer AT return AT
     */
    public String resolveToken(String requestToken) {
        if (requestToken == null || !requestToken.startsWith("Bearer")) return null;
        return requestToken.substring(6);
    }

}
