package com.king.bankbackend.service.impl;

import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.CustomerMapper;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.service.CustomerService;
import com.sky.constant.MessageConstant;
import com.sky.exception.PasswordErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Slf4j
@Service

public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 管理员登录
     *
     * @param customerLoginDTO
     * @return
     */
    public User login(CustomerLoginDTO customerLoginDTO){
        String account = customerLoginDTO.getAccount();
        String password = customerLoginDTO.getPassword();

        //根据账户查询数据库中的数据
        User user = customerMapper.getByAccount(account);
        if(user==null){
            //账号不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //密码对比
        //password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return user;
    }

    /**
     * 获取管理员的全部信息
     *
     * @return
     */
    public User getAdminInfo() {

        Long curId= BaseContext.getCurrentId();

        //根据账户查询数据库中的数据
        User user = customerMapper.getById(curId);
        return user;
    }
}
