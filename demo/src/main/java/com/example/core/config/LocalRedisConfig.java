package com.example.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisServer;

import javax.annotation.PostConstruct;

@Configuration
@Profile("dev")
@Slf4j
public class LocalRedisConfig {
    private final int redisPort;

    public LocalRedisConfig(@Value("${spring.redis.port}") int redisPort, @Value("${spring.redis.host}") String redisHost) {
        log.info("Redis :: contructor");
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }


    private final String redisHost;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedisServer() {
        log.info("Redis :: postcontructor");
        redisServer = new RedisServer(redisHost, redisPort);
//        redisServer.start();
    }


}
