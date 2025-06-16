package com.king.bankbackend.controller;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class testcontroller {
    @GetMapping("/health")
    public BaseResponse<Integer> heart(){
        return Result.success(1);
    }
}
