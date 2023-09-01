package com.example.core.auth.dto;

public class AuthResponse {
    public static String REGISTER_DENINED_MESSAGE = "이미 사용하고 있는 아이디입니다.";
    public static String REGISTER_SUCCESSED_MESSAGE = "회원가입 완료";

    private String message;
    private String memberId;

    public AuthResponse(String memberId) {
        this.memberId = memberId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getMemberId() {
        return memberId;
    }
}
