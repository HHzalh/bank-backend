package com.king.bankbackend.model.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerQueryDTO implements Serializable {

    private String username;

    private String pid;

    private String gender;

    private String account;

    private String role;

    private int page;

    private int pageSize;
}
