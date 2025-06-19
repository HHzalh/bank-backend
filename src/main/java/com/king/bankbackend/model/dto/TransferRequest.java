package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 转账dto
 */
@Data
public class TransferRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 转账账号
     */
    private String fromCardId;

    /**
     * 密码
     */
    private String password;

    /**
     * 收款账号
     */
    private String toCardId;

    /**
     * 转账金额
     */
    private Long amount;

}
