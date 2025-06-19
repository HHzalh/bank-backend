package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 银行卡新增DTO
 */
@Data
public class CardDTO implements Serializable {

    /**
     * 币种
     */
    private String curid;

    /**
     * 存款类型ID
     */
    private Long savingid;

    /**
     * 开户金额（≥1元）
     */
    private BigDecimal openmoney;

    /**
     * 用户身份证
     */
    private String pid;

} 