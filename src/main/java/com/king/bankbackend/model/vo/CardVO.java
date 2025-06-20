package com.king.bankbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CardVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
     */
    private String cardid;
    /**
     * 存款类型ID
     */
    private Long savingid;
    /**
     * 账户余额（≥0元）
     */
    private BigDecimal balance;
    /**
     * 是否挂失
     */
    private String isreportloss;
}
