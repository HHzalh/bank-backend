package com.king.bankbackend.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    /**
     * 用户编号（自增主键）
     */
    private Long userid;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 身份证号（18位，唯一）
     */
    private String pid;

    /**
     * 性别
     */
    private Object gender;

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

    /**
     * 权限：0-普通用户,1-管理员
     */
    private Integer role;

    /**
     * 登录账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

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
