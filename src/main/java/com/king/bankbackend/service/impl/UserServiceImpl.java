package com.king.bankbackend.service.impl;

import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.mapper.UserMapper;
import com.king.bankbackend.model.dto.UpdateProfileRequest;
import com.king.bankbackend.model.dto.UserLoginRequest;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Autowired
    private UserMapper userMapper;

    @Override
    public User userLogin(UserLoginRequest userLoginRequest) {

        //获取请求体中的账号密码
        String account = userLoginRequest.getAccount();
        String password = userLoginRequest.getPassword();

        //根据账户查询数据库中的数据
        User user = userMapper.getByAccount(account);
        if (user == null) {
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

    @Override
    public User getUserInfo() {
        Long curId = BaseContext.getCurrentId();
        //根据账户查询数据库中的数据
        User user = userMapper.getById(curId);
        return user;
    }

    /**
     * 修改用户个人资料
     * @param updateProfileRequest
     * @return
     */
    @Override
    public Boolean updateProfile(UpdateProfileRequest updateProfileRequest) {

        String username = updateProfileRequest.getUsername();
        String gender = updateProfileRequest.getGender();
        String telephone = updateProfileRequest.getTelephone();
        String address = updateProfileRequest.getAddress();
        String imageurl = updateProfileRequest.getImageurl();
        Long userId = BaseContext.getCurrentId();
        Boolean result = userMapper.updateProfile(username, gender, telephone, address, imageurl,userId);
        return result;
    }

}
