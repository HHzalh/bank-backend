package com.king.bankbackend.utils;

import com.king.bankbackend.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类，用于操作Redis中的token
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存储用户token
     *
     * @param userId 用户ID
     * @param token  JWT令牌
     */
    public void setUserToken(Long userId, String token) {
        String key = RedisConstant.USER_LOGIN_TOKEN_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("用户token已存入Redis，userId：{}", userId);
    }

    /**
     * 获取用户token
     *
     * @param userId 用户ID
     * @return token JWT令牌
     */
    public String getUserToken(Long userId) {
        String key = RedisConstant.USER_LOGIN_TOKEN_PREFIX + userId;
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    /**
     * 删除用户token
     *
     * @param userId 用户ID
     */
    public void deleteUserToken(Long userId) {
        String key = RedisConstant.USER_LOGIN_TOKEN_PREFIX + userId;
        redisTemplate.delete(key);
        log.info("用户token已从Redis删除，userId：{}", userId);
    }

    /**
     * 存储管理员token
     *
     * @param adminId 管理员ID
     * @param token   JWT令牌
     */
    public void setAdminToken(Long adminId, String token) {
        String key = RedisConstant.ADMIN_LOGIN_TOKEN_PREFIX + adminId;
        redisTemplate.opsForValue().set(key, token, RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("管理员token已存入Redis，adminId：{}", adminId);
    }

    /**
     * 获取管理员token
     *
     * @param adminId 管理员ID
     * @return token JWT令牌
     */
    public String getAdminToken(Long adminId) {
        String key = RedisConstant.ADMIN_LOGIN_TOKEN_PREFIX + adminId;
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    /**
     * 删除管理员token
     *
     * @param adminId 管理员ID
     */
    public void deleteAdminToken(Long adminId) {
        String key = RedisConstant.ADMIN_LOGIN_TOKEN_PREFIX + adminId;
        redisTemplate.delete(key);
        log.info("管理员token已从Redis删除，adminId：{}", adminId);
    }
} 