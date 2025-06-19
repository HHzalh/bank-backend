package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录dto
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 2912823635916649089L;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;
}
