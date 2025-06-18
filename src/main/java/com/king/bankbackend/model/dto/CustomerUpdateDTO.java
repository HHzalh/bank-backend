package com.king.bankbackend.model.dto;

import lombok.Data;

@Data
public class CustomerUpdateDTO {

    private Long userid;

    private String username;

    private String telephone;

    private String address;
}
