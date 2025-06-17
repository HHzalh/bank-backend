package com.king.bankbackend.service;

import com.king.bankbackend.model.dto.CustomerLoginDTO;
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
     * 获取管理员的全部信息
     *
     * @return
     */
    User getAdminInfo();

}
