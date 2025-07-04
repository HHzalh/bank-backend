package com.king.bankbackend.service.impl;

import com.king.bankbackend.constant.*;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.CardMapper;
import com.king.bankbackend.mapper.UserMapper;
import com.king.bankbackend.model.dto.ChangedUserPwdRequest;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.UpdateProfileRequest;
import com.king.bankbackend.model.dto.UserLoginRequest;
import com.king.bankbackend.model.entity.Card;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.LoginUserVO;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.service.UserService;
import com.king.bankbackend.utils.BankCardIdGenerator;
import com.king.bankbackend.utils.JwtUtil;
import com.king.bankbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 用户登录并返回带有token的登录结果
     *
     * @param userLoginRequest
     * @return LoginUserVO包含用户信息和token
     */
    public LoginUserVO userLoginWithToken(UserLoginRequest userLoginRequest) {
        //获取请求体中的账号密码
        String account = userLoginRequest.getAccount();
        String password = userLoginRequest.getPassword();

        //加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //根据账户查询数据库中的数据
        User user = userMapper.getByAccount(account);
        if (user == null) {
            //账号不存在
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "账号或密码错误");
        }
        if (!password.equals(user.getPassword())) {
            //密码错误
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "账号或密码错误");
        }

        // 登录成功, 处理token
        String token = redisUtil.getUserToken(user.getUserid());

        // 检查Redis中是否存在token，并校验其是否过期
        if (token != null) {
            try {
                JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
                log.info("Redis中存在有效token，直接使用");
            } catch (Exception e) {
                log.info("Redis中token已过期，重新生成");
                token = null; // 标记为null，以便重新生成
            }
        }

        // 如果token为null（Redis中不存在或已过期），则生成新token
        if (token == null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.USER_ID, user.getUserid());
            token = JwtUtil.createJWT(
                    jwtProperties.getUserSecretKey(),
                    jwtProperties.getUserTtl(),
                    claims);
            // 将新token存入Redis,覆盖旧token
            redisUtil.setUserToken(user.getUserid(), token);
            log.info("生成新token并存入Redis");
        }

        // 封装登录返回结果
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        loginUserVO.setToken(token);
        return loginUserVO;
    }

    /**
     * 获取用户的所有信息
     *
     * @return
     */
    public User getUserInfo() {
        Long curId = BaseContext.getCurrentId();
        //根据账户查询数据库中的数据
        User user = userMapper.getById(curId);
        return user;
    }

    /**
     * 修改用户个人资料
     *
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
        Boolean result = userMapper.updateProfile(username, gender, telephone, address, imageurl, userId);
        return result;
    }

    /**
     * 用户注册
     *
     * @param customerDTO
     * @return
     */
    @Override
    @Transactional
    public Boolean userRegister(CustomerDTO customerDTO) {
        log.info("新增用户，用户信息：{}", customerDTO);

        // 参数校验
        if (customerDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息不能为空");
        }

        if (!StringUtils.hasText(customerDTO.getUsername())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名不能为空");
        }

        if (!StringUtils.hasText(customerDTO.getAccount())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能为空");
        }

        // 检查账号是否已存在
        User existingUser = userMapper.getByAccount(customerDTO.getAccount());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(customerDTO, user);

        //设置默认密码
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_USER_PASSWORD.getBytes()));
        user.setRole(RoleConstant.DEFAULT_USER);
        user.setImageurl(ImageUrlConstant.DEFAULT_IMAGE_URL);

        // 注意：insert方法返回的是int（受影响的行数），不是User对象
        try {
            // 执行插入操作
            int result = userMapper.insert(user);

            if (result <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户注册失败");
            }

            // 获取插入后的用户ID
            Long userId = user.getUserid();
            if (userId == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户注册失败，无法获取用户ID");
            }

            log.info("用户注册成功，用户ID：{}", userId);

            //新增银行卡
            Card card = new Card();
            card.setCardid(BankCardIdGenerator.generate());
            card.setSavingid(1L);
            card.setOpendate(LocalDate.now());
            card.setCurid("RMB");
            card.setOpenmoney(new BigDecimal(10));
            card.setBalance(new BigDecimal(10));
            //设置默认密码，且加密
            card.setPass(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_CARD_PASSWORD.getBytes()));
            card.setIsreportloss(CardConstant.DEFAULT_NOT_REPORT_LOSS);
            card.setCustomerid(userId);
            card.setCustomername(user.getUsername());

            int cardResult = cardMapper.insert(card);
            if (cardResult <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "银行卡开通失败");
            }

            return true;
        } catch (Exception e) {
            log.error("用户注册失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户注册失败: " + e.getMessage());
        }
    }

    /**
     * 修改用户头像
     *
     * @param url
     */
    public Boolean updateAvatar(String url) {
        Long userId = BaseContext.getCurrentId();
        return userMapper.updateProfile(null, null, null, null, url, userId);
    }

    /**
     * 修改用户密码
     *
     * @param changedUserPwdRequest
     * @return
     */
    @Override
    public Boolean changedUserPwd(ChangedUserPwdRequest changedUserPwdRequest) {
        // 获取当前用户ID
        Long userId = BaseContext.getCurrentId();

        // 获取请求中的旧密码和新密码
        String oldPassword = changedUserPwdRequest.getOldPassword();
        String newPassword = changedUserPwdRequest.getNewPassword();

        // 参数校验
        if (!StringUtils.hasText(oldPassword) || !StringUtils.hasText(newPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
        }

        // 将旧密码加密后与数据库中的密码进行比对
        String encryptedOldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());

        // 获取当前用户
        User user = userMapper.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 校验旧密码是否正确
        if (!encryptedOldPassword.equals(user.getPassword())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "旧密码不正确");
        }

        // 加密新密码
        String encryptedNewPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());

        // 更新密码
        boolean result = userMapper.updatePassword(userId, encryptedNewPassword);

        if (result) {
            // 密码修改成功，删除Redis中的旧token
            redisUtil.deleteUserToken(userId);
        }

        return result;
    }

}
