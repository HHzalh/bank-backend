package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 取款dto
 */
@Data
public class WithdrawRequest implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
     */
    private String cardid;

    /**
     * 取款金额
     */
    private Long amount;

    /**
     * 密码（6位数字）
     */
    private String pass;

    /**
     * 交易备注
     */
    private String remark;

}