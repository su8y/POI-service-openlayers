package com.example.core.model;

import com.example.core.controller.dto.MemberLoginRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "member")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {
    @Id
    private String id;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member from(MemberLoginRequestDto memberLoginRequestDto, PasswordEncoder passwordEncoder) {
        return Member.builder()
                .id(memberLoginRequestDto.getMemberId())
                .password(passwordEncoder.encode(memberLoginRequestDto.getPassword()))
                .role(Role.USER)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    @Override
    public String getUsername() {
        return id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
