package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdateProfileRequest implements Serializable {


    //序列化权值
    private static final long serialVersionUID = 1L;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 性别
     */
    private String gender;

    /**
     * 联系电话（11位数字）
     */
    private String telephone;
    /**
     * 联系地址（可选）
     */
    private String address;
    /**
     * 头像地址（可选）
     */
    private String imageurl;

}
