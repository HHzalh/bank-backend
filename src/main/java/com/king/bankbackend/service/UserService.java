package com.king.bankbackend.service;

import com.king.bankbackend.model.dto.ChangedUserPwdRequest;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.UpdateProfileRequest;
import com.king.bankbackend.model.dto.UserLoginRequest;
import com.king.bankbackend.model.entity.User;

public interface UserService {

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    User userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前用户信息
     *
     * @return
     */
    User getUserInfo();

    /**
     * 修改用户个人资料
     *
     * @param updateProfileRequest
     * @return
     */
    Boolean updateProfile(UpdateProfileRequest updateProfileRequest);

    /**
     * 用户注册
     *
     * @param customerDTO
     * @return
     */
    Boolean userRegister(CustomerDTO customerDTO);

    /**
     * 修改用户头像
     *
     * @param url
     */
    Boolean updateAvatar(String url);

    /**
     * 修改用户密码
     *
     * @param changedUserPwdRequest
     * @return
     */
    Boolean changedUserPwd(ChangedUserPwdRequest changedUserPwdRequest);

}
