package com.example.core.auth;

import com.example.core.util.TokenInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final long COOKIE_EXPIRATION = 7776000;

    @PostMapping(value = "/auth/login")
    public String login(@RequestParam(name = "memberId", defaultValue = "qotndk123") String memberId,
                        @RequestParam(name = "password", defaultValue = "qotndk123") String password
            , HttpServletResponse response
            , HttpServletRequest request
    ) {
        TokenInfo tokenInfo = memberService.login(memberId, password);
        // optional properties
        Cookie cookie = new Cookie("access-token", tokenInfo.getGrantType()+tokenInfo.getAccessToken());
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        Cookie rc = new Cookie("refresh-token", tokenInfo.getRefreshToken());
        rc.setMaxAge(7 * 24 * 60 * 60);
        rc.setSecure(true);
        rc.setHttpOnly(true);
        rc.setPath("/");

        response.addCookie(rc);
        response.addCookie(cookie);
        return "/route/find";
    }

    /**
     * 회원가입
     *
     * @param memberRegisterRequestDto memberId: String, password: String, email: String
     * @return response status & link status
     * @throws UnsupportedEncodingException
     */
    @PostMapping(value = "/auth/signup", consumes = {"application/json"})
    public EntityModel<ResponseEntity<String>> register(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) throws UnsupportedEncodingException {
        memberService.signup(Member.from(memberRegisterRequestDto, passwordEncoder));
        return EntityModel.of(ResponseEntity.ok("memberId: " + memberRegisterRequestDto.getMemberId()),
                linkTo(methodOn(MemberController.class).login(memberRegisterRequestDto.getMemberId(), memberRegisterRequestDto.getPassword(), null, null)).withRel("register-member"),
                linkTo(methodOn(MemberController.class).register(memberRegisterRequestDto)).withSelfRel()
        );
    }

    @PostMapping("/auth/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!memberService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build(); // 재발급 필요X
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 재발급 필요
        }
    }

    @PostMapping("/auth/reissue")
    @ResponseBody
    public ResponseEntity<?> reissue(@CookieValue(name = "refresh-token") String requestRefreshToken,
                                     @CookieValue(name = "access-token") String requestAccessToken,
                                     HttpServletResponse response) {
        System.out.println(requestAccessToken);
        System.out.println(requestRefreshToken);
        TokenInfo reissuedTokenDto = memberService.reissue(requestAccessToken, requestRefreshToken);
        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            // RT 저장
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", reissuedTokenDto.getRefreshToken())
                    .maxAge(COOKIE_EXPIRATION)
                    .httpOnly(true)
                    .secure(true)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    // AT 저장
                    .header(HttpHeaders.AUTHORIZATION, "Bearer" + reissuedTokenDto.getAccessToken())
                    .build();

        } else { // Refresh Token 탈취 가능성
            // Cookie 삭제 후 재로그인 유도
            ResponseCookie responseCookie = ResponseCookie.from("refresh-token", "")
                    .maxAge(0)
                    .path("/")
                    .build();
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                    .build();
        }
    }

}
