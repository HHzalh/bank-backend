package com.king.bankbackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡查询结果VO
 */
@Data
public class CardStatusVo implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 是否挂失
     */
    private String isreportloss;

} 