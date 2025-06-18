package com.king.bankbackend.model.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositUpdateDTO implements Serializable {

    private Long savingid;

    private String savingname;

    private String descrip;
} 