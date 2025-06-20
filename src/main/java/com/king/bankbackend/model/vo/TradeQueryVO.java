package com.king.bankbackend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易查询结果VO
 */
@Data
public class TradeQueryVO implements Serializable {

    /**
     * 交易ID
     */
    private Long tradeid;

    /**
     * 交易日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tradedate;

    /**
     * 交易类型
     */
    private String tradetype;

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 交易金额
     */
    private BigDecimal trademoney;

    /**
     * 交易备注
     */
    private String remark;
} 