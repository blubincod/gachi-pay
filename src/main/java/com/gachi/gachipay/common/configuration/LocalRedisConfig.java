package com.gachi.gachipay.common.configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import java.io.File;
import java.util.Objects;

/**
 * Embedded Redis 설정
 */
@Slf4j
@Configuration
public class LocalRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    // 애플리케이션 시작 시 Redis 시작
    @PostConstruct
    public void startRedis() {
        if (isArmArchitecture()) {
            log.info("ARM Architecture");
            redisServer = new RedisServer(Objects.requireNonNull(getRedisServerExecutable()), redisPort);
        } else {
            redisServer = RedisServer.builder()
                    .port(redisPort)
                    .setting("maxmemory 128M")
                    .build();
        }

        redisServer.start();
    }

    // 애플리케이션 종료 시 Redis 종료
    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    private File getRedisServerExecutable() {
        try {
            return new File("src/main/resources/binary/redis/redis-server-mac-arm64");
        } catch (Exception e) {
            throw new RuntimeException("FAILED");
        }
    }

    //ARM CPU 체크
    private boolean isArmArchitecture() {
        return System.getProperty("os.arch").contains("aarch64");
    }
}

