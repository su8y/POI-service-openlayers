package com.example.core.controller;

import com.example.core.controller.dto.MemberLoginRequestDto;
import com.example.core.model.Member;
import com.example.core.model.common.TokenInfo;
import com.example.core.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(value = "/auth/login", consumes = {"application/json"})
    public EntityModel<ResponseEntity> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, BindingResult bindingResult) {
        log.info("controller");
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        TokenInfo tokenInfo = memberService.login(memberId, password);
        log.info("controller - end");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("access-token",tokenInfo.getAccessToken());
        httpHeaders.add("refresh-token", tokenInfo.getRefreshToken());
        return EntityModel.of(
                ResponseEntity.status(HttpStatus.OK).headers(httpHeaders).body("login Success"),
                linkTo(methodOn(MemberController.class).login(memberLoginRequestDto, null)).withSelfRel(),
                linkTo(methodOn(MemberController.class).register(memberLoginRequestDto)).withRel("register-member")
        );
    }

    @PostMapping(value = "/auth/signup", consumes = {"application/json"})
    public EntityModel<ResponseEntity> register(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        memberService.signup(Member.from(memberLoginRequestDto, passwordEncoder));
        return EntityModel.of(ResponseEntity.ok("memberId: " + memberLoginRequestDto.getMemberId()),
                linkTo(methodOn(MemberController.class).login(memberLoginRequestDto, null)).withRel("register-member"),
                linkTo(methodOn(MemberController.class).register(memberLoginRequestDto)).withSelfRel()
        );
    }
}
