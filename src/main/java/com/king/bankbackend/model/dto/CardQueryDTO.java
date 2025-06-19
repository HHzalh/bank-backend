package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 银行卡查询条件DTO
 */
@Data
public class CardQueryDTO implements Serializable {

    /**
     * 银行卡号
     */
    private String cardid;

    /**
     * 币种
     */
    private String curid;

    /**
     * 存款类型id
     */
    private Long savingid;

    /**
     * 是否挂失
     */
    private String isreportloss;

    /**
     * 客户姓名
     */
    private String customername;

    /**
     * 页码
     */
    private int page;

    /**
     * 每页记录数
     */
    private int pageSize;
} 