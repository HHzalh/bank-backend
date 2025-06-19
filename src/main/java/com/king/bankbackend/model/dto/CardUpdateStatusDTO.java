package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡更新挂失状态DTO
 */
@Data
public class CardUpdateStatusDTO implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 挂失状态
     */
    private String isreportloss;
} 