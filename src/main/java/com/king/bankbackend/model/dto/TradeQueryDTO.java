package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 交易查询条件DTO
 */
@Data
public class TradeQueryDTO implements Serializable {


    /**
     * 交易ID
     */
    private Long tradeid;

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 交易类型
     */
    private String tradetype;

    /**
     * 交易金额下限
     */
    private BigDecimal minMoney;

    /**
     * 交易金额上限
     */
    private BigDecimal maxMoney;

    /**
     * 交易备注
     */
    private String remark;

    /**
     * 页码
     */
    private int page;

    /**
     * 每页记录数
     */
    private int pageSize;
} 