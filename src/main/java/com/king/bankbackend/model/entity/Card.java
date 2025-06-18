package com.king.bankbackend.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
public class Card implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
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
    private Date opendate;
    /**
     * 开户金额（≥1元）
     */
    private BigDecimal openmoney;
    /**
     * 账户余额（≥1元）
     */
    private BigDecimal balance;
    /**
     * 密码（6位数字）
     */
    private String pass;
    /**
     * 是否挂失
     */
    private Object isreportloss;
    /**
     * 用户ID
     */
    private Long customerid;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
