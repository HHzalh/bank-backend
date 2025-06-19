package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 存款dto
 */
@Data
public class ChangedPwdRequest implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
     */
    private String cardid;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}