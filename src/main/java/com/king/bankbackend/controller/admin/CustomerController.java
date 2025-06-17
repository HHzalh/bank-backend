package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.constant.JwtClaimsConstant;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CustomerLoginVO;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.service.CustomerService;
import com.king.bankbackend.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/customer")
@Component("adminCustomerController")
@Slf4j
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 管理员登录
     *
     * @param customerLoginDTO
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<CustomerLoginVO> login(@RequestBody CustomerLoginDTO customerLoginDTO) {
        //登录
        User user = customerService.login(customerLoginDTO);
        //登录成功生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.Admin_ID, user.getUserid());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        //封装登录返回结果
        CustomerLoginVO customerLoginVO = CustomerLoginVO.builder()
                .userId(user.getUserid())
                .userName(user.getUsername())
                .imageUrl(user.getImageurl())
                .token(token)
                .build();
        return Result.success(customerLoginVO);
    }

    /**
     * 获得管理员的信息
     *
     * @return
     */
    @GetMapping("/getAdminInfo")
    public BaseResponse<User> getAdminInfo() {
        User user = customerService.getAdminInfo();
        return Result.success(user);
    }

}
