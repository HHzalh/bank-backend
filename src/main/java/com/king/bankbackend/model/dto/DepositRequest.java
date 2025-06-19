package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 存款dto
 */
@Data
public class DepositRequest implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
     */
    private String cardid;

    /**
     * 存款类型ID（自增主键）
     */
    private Long savingid;

    /**
     * 转账金额
     */
    private Long amount;

    /**
     * 交易备注
     */
    private String remark;

}