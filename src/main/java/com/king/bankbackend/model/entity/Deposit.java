package com.king.bankbackend.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Deposit {
    /**
     * 存款类型ID（自增主键）
     */
    private Long savingid;

    /**
     * 存款类型名称
     */
    private String savingname;

    /**
     * 存款类型描述
     */
    private String descrip;

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
