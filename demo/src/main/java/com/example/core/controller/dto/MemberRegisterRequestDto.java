package com.example.core.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterRequestDto {
    String memberId;
    String password;
    String email;

}
