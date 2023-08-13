package com.example.core.auth;

import com.example.core.redis.RedisService;
import com.example.core.util.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;


    public TokenInfo login(String memberId, String password) {
        // 인증을 위한 유저네임패스워드인증토큰을 이용해 UserDetailservice를 UserDetailService를 통해서 인증을 한다.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }

    public void signup(Member from) {
        if (memberRepository.existsById(from.getId())) {
            throw new DuplicateKeyException("이미 존재하는 아이디입니다.");
        }
        Member save = memberRepository.save(from);
    }

    public boolean validate(String requestAccessTokenInHeader) {
        String requestAccessToken = jwtTokenProvider.resolveToken(requestAccessTokenInHeader);
        return jwtTokenProvider.validateAccessTokenOnlyExpired(requestAccessToken); // true = 재발급
    }

    public TokenInfo reissue(String requestAccessToken, String requestRefreshToken) {
        String accessToken = jwtTokenProvider.resolveToken(requestAccessToken);
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        String memberId = authentication.getName();

        String redisRefreshToken = redisService.getValues(memberId);
        if (redisRefreshToken == null) return null;
        if (!jwtTokenProvider.validateRefreshToken(redisRefreshToken) || !redisRefreshToken.equals(requestRefreshToken))
            return null;

        SecurityContextHolder.getContext().setAuthentication(authentication);
        //기존 삭제
        redisService.deleteValues(memberId);
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        long tokenExpirationTime = jwtTokenProvider.getTokenExpirationTime(tokenInfo.getRefreshToken());
        redisService.setValuesWithTimeout(memberId, tokenInfo.getRefreshToken(), tokenExpirationTime);
        return tokenInfo;
    }

    public void logout(String requestToken) {
        String accessToken = jwtTokenProvider.resolveToken(requestToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        // 로그아웃을 할시에는 refresh와 accestoken을 모두 무효화 한다.
        redisService.deleteValues(authentication.getName());
        // 레디스 저장소에서 refresh토큰을 지운다면
    }

}
