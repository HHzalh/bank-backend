package com.king.bankbackend.constant;

/**
 * Redis常量
 */
public class RedisConstant {

    // 用户登录token前缀
    public static final String USER_LOGIN_TOKEN_PREFIX = "bank:user:login:token:";

    // 管理员登录token前缀
    public static final String ADMIN_LOGIN_TOKEN_PREFIX = "bank:admin:login:token:";

    // 令牌过期时间（1天，单位：秒）
    public static final long TOKEN_EXPIRE_TIME = 86400;
} 