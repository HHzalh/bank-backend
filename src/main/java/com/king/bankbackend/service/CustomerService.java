package com.king.bankbackend.service;

import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.User;

public interface CustomerService {

    /**
     * 员工登录
     *
     * @param customerLoginDTO
     * @return
     */
    User login(CustomerLoginDTO customerLoginDTO);

    /**
     * 获取管理员信息
     *
     * @return
     */
    User getAdminInfo();

    /**
     * 新增用户
     *
     * @param customerDTO
     */
    void addCustomer(CustomerDTO customerDTO);

    /**
     * 更新用户信息
     *
     * @param customerUpdateDTO
     */
    void updateCustomer(CustomerUpdateDTO customerUpdateDTO);

    /**
     * 删除用户
     *
     * @param id
     */
    void deleteCustomer(Long id);
}
