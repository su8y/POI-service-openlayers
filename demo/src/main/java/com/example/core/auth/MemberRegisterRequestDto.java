package com.example.core.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRegisterRequestDto {
    @NotEmpty
    String memberId;
    @NotEmpty
    String password;
    @Null
    String email;

}
