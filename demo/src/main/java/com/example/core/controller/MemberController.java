package com.example.core.controller;

import com.example.core.controller.dto.MemberRegisterRequestDto;
import com.example.core.model.Member;
import com.example.core.model.common.TokenInfo;
import com.example.core.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/auth/login", consumes = {"application/json"})
    public ResponseEntity<?> login(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto, BindingResult bindingResult) {
        log.info("controller");
        String memberId = memberRegisterRequestDto.getMemberId();
        String password = memberRegisterRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(memberId, password);
        log.info("controller - end");

        Map<String, String> resp = new HashMap<>();
        resp.put("access-token", tokenInfo.getGrantType() + tokenInfo.getAccessToken());

        ResponseCookie cookie = ResponseCookie.from("refresh-token", tokenInfo.getRefreshToken())
                .maxAge(60 * 60 * 7)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();


        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(resp);
    }

    @PostMapping(value = "/auth/signup", consumes = {"application/json"})
    public EntityModel<ResponseEntity> register(@RequestBody MemberRegisterRequestDto memberRegisterRequestDto) {
        memberService.signup(Member.from(memberRegisterRequestDto, passwordEncoder));
        return EntityModel.of(ResponseEntity.ok("memberId: " + memberRegisterRequestDto.getMemberId()),
                linkTo(methodOn(MemberController.class).login(memberRegisterRequestDto, null)).withRel("register-member"),
                linkTo(methodOn(MemberController.class).register(memberRegisterRequestDto)).withSelfRel()
        );
    }
}
