package com.king.bankbackend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 银行卡查询结果VO
 */
@Data
public class CardQueryVO implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 币种
     */
    private String curid;

    /**
     * 存款类型ID
     */
    private Long savingid;

    /**
     * 开户日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate opendate;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 是否挂失
     */
    private String isreportloss;

    /**
     * 用户名字
     */
    private String customername;

} 