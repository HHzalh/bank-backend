package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 存款dto
 */
@Data
public class ReportLossRequest implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
    /**
     * 银行卡号（16位，格式：10103576xxxxxxxx）
     */
    private String cardid;

    /**
     * 是否挂失     是/否
     */
    private String isLoss;
}