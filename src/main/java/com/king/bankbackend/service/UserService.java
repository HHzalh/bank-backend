package com.king.bankbackend.service;

import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.model.dto.UpdateProfileRequest;
import com.king.bankbackend.model.dto.UserLoginRequest;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.LoginUserVO;

public interface UserService {

    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    User userLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前用户信息
     * @return
     */
    User getUserInfo();

    /**
     * 修改用户个人资料
     * @param updateProfileRequest
     * @return
     */
    Boolean updateProfile(UpdateProfileRequest updateProfileRequest);

    /**
     * 修改用户头像
     * @param url
     */
    Boolean  updateAvatar(String url);
}
