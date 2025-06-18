package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositQueryDTO implements Serializable {

    private String savingname;

    private int page;

    private int pageSize;
} 