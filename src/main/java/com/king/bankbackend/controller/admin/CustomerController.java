package com.king.bankbackend.controller.admin;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.constant.JwtClaimsConstant;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerQueryDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CustomerLoginVO;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.service.CustomerService;
import com.king.bankbackend.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Admin/customer")
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
        ThrowUtils.throwIf(customerLoginDTO == null, ErrorCode.PARAMS_ERROR);
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

    /**
     * 新增用户
     *
     * @param customerDTO
     * @return
     */
    @PostMapping("/addCustomer")
    public BaseResponse addCustomer(@RequestBody CustomerDTO customerDTO) {
        ThrowUtils.throwIf(customerDTO == null, ErrorCode.PARAMS_ERROR);
        customerService.addCustomer(customerDTO);
        return Result.success();
    }

    /**
     * 更新用户信息
     *
     * @param customerUpdateDTO
     * @return
     */
    @PutMapping("/updateCustomer")
    public BaseResponse updateCustomer(@RequestBody CustomerUpdateDTO customerUpdateDTO) {
        customerService.updateCustomer(customerUpdateDTO);
        return Result.success();
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DeleteMapping("/deleteCustomer/{id}")
    public BaseResponse deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return Result.success();
    }

    /**
     * 范围分页查询
     *
     * @param customerQueryDTO
     * @return
     */
    @PostMapping("/pageCustomer")
    public BaseResponse<PageResult> pageCustomer(
            @RequestBody CustomerQueryDTO customerQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        PageResult pageResult = customerService.pageCustomer(customerQueryDTO, begin, end);
        return Result.success(pageResult);
    }
}
