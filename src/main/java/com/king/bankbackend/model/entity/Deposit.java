package com.king.bankbackend.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Deposit implements Serializable {
    //序列化权值
    private static final long serialVersionUID = 1L;
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
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
