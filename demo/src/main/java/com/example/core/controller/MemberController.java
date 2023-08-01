package com.example.core.controller;

import com.example.core.auth.CookieUtil;
import com.example.core.controller.dto.MemberRegisterRequestDto;
import com.example.core.model.Member;
import com.example.core.model.common.TokenInfo;
import com.example.core.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/auth/login")
    public String login(@RequestParam(name = "memberId", defaultValue = "qotndk123") String memberId,
                        @RequestParam(name = "password", defaultValue = "qotndk123") String password
            , HttpServletResponse response
            , HttpServletRequest request
    ) throws UnsupportedEncodingException {
        TokenInfo tokenInfo = memberService.login(memberId, password);

        // Put Access Token
        HttpSession session = request.getSession();
        session.setAttribute("access_token",
                tokenInfo.getGrantType() + " " + tokenInfo.getAccessToken());
        // refresh-token Set Cookie
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token",
                        tokenInfo.getRefreshToken())
                .maxAge(60 * 60 * 7)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        return "home";
    }

    @PostMapping(value = "/auth/signup", consumes = {"application/json"})
    public EntityModel<ResponseEntity> register(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) throws UnsupportedEncodingException {
        memberService.signup(Member.from(memberRegisterRequestDto, passwordEncoder));
        return EntityModel.of(ResponseEntity.ok("memberId: " + memberRegisterRequestDto.getMemberId()),
                linkTo(methodOn(MemberController.class).login(memberRegisterRequestDto.getMemberId(),memberRegisterRequestDto.getPassword(), null, null)).withRel("register-member"),
                linkTo(methodOn(MemberController.class).register(memberRegisterRequestDto)).withSelfRel()
        );
    }

}
