package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositDTO implements Serializable {

    private String savingname;

    private String descrip;
} 