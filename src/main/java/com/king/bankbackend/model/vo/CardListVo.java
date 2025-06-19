package com.king.bankbackend.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户旗下的银行卡查询结果VO
 */
@Data
public class CardListVo implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 存款类型ID
     */
    private Long savingid;

    /**
     * 账户余额
     */
    private BigDecimal balance;
}
