package com.king.bankbackend.service;

import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerQueryDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CustomerLoginVO;

import java.time.LocalDate;

public interface CustomerService {

    /**
     * 管理员登录并返回带有token的登录结果
     *
     * @param customerLoginDTO
     * @return
     */
    CustomerLoginVO loginWithToken(CustomerLoginDTO customerLoginDTO);

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

    /**
     * 分页查询用户信息
     *
     * @param customerQueryDTO
     * @param begin
     * @param end
     * @return
     */
    PageResult pageCustomer(CustomerQueryDTO customerQueryDTO, LocalDate begin, LocalDate end);

    /**
     * 管理员修改头像
     *
     * @param imageUrl
     */
    void updateAvatar(String imageUrl);
}
