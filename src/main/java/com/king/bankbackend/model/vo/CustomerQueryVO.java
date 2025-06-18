package com.king.bankbackend.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class CustomerQueryVO implements Serializable {

    private String userid;

    private String username;

    private String pid;

    private String gender;

    private String telephone;

    private String account;

    private String imageUrl;

    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
