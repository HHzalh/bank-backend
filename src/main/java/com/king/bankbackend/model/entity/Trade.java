package com.king.bankbackend.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Trade {

    /**
     * 交易ID（自增主键）
     */
    private Long tradeid;

    /**
     * 交易日期
     */
    private Date tradedate;

    /**
     * 交易类型
     */
    private Object tradetype;

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 交易金额（＞0元）
     */
    private BigDecimal trademoney;

    /**
     * 交易备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    //序列化权值
    private static final long serialVersionUID = 1L;
}
