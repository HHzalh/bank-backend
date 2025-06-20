package com.king.bankbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Trade implements Serializable {

    //序列化权值
    private static final long serialVersionUID = 1L;
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
     * 交易金额（>=0元）
     */
    private BigDecimal trademoney;
    /**
     * 交易备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
