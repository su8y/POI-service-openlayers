package com.example.core.model.redis;

import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Getter
public class RefreshToken {
    private String refreshToken;
    private String memberId;

    public RefreshToken(final String refreshToken, final String memberId) {
        this.refreshToken = refreshToken;
        this.memberId = memberId;
    }
}
