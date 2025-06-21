package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求DTO
 */
@Data
public class ChangedUserPwdRequest implements Serializable {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}