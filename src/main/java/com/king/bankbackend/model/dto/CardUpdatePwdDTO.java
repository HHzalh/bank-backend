package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡更新密码DTO
 */
@Data
public class CardUpdatePwdDTO implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 原密码（6位数字）
     */
    private String oldpass;

    /**
     * 新密码（6位数字）
     */
    private String newpass;
} 