package com.king.bankbackend.service.impl;

import com.king.bankbackend.constant.ImageUrlConstant;
import com.king.bankbackend.constant.PasswordConstant;
import com.king.bankbackend.constant.RoleConstant;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.CustomerMapper;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 用户登录
     *
     * @param customerLoginDTO
     * @return
     */
    @Override
    public User login(CustomerLoginDTO customerLoginDTO) {
        String account = customerLoginDTO.getAccount();
        String password = customerLoginDTO.getPassword();

        log.info("用户登录，账号：{}，密码：{}", account, password);

        //根据账户查询数据库中的数据
        User user = customerMapper.getByAccount(account);
        if (user == null) {
            //账号不存在
            log.error("登录失败，账号不存在：{}", account);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        //密码对比
        //password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(user.getPassword())) {
            //密码错误
            log.error("登录失败，密码错误，账号：{}", account);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        log.info("登录成功，用户信息：{}", user);
        return user;
    }

    /**
     * 获取管理员的全部信息
     *
     * @return
     */
    @Override
    public User getAdminInfo() {
        Long curId = BaseContext.getCurrentId();
        //根据账户查询数据库中的数据
        User user = customerMapper.getById(curId);
        return user;
    }

    /**
     * 新增用户
     *
     * @param customerDTO
     */
    @Override
    public void addCustomer(CustomerDTO customerDTO) {
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
        User existingUser = customerMapper.getByAccount(customerDTO.getAccount());
        if (existingUser != null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(customerDTO, user);


        //设置默认密码
        user.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_USER_PASSWORD.getBytes()));
        user.setRole(RoleConstant.DEFAULT_USER);
        user.setImageurl(ImageUrlConstant.DEFAULT_IMAGE_URL);

        customerMapper.insert(user);
    }

    /**
     * 更新用户信息
     *
     * @param customerUpdateDTO
     */
    public void updateCustomer(CustomerUpdateDTO customerUpdateDTO) {
        // 参数校验
        if (customerUpdateDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户信息不能为空");
        }

        User user = new User();
        BeanUtils.copyProperties(customerUpdateDTO, user);
        customerMapper.update(user);
    }

    /**
     * 删除用户
     *
     * @param id
     */
    public void deleteCustomer(Long id) {
        log.info("删除用户，ID：{}", id);

        // 检查用户是否存在
        User existingUser = customerMapper.getById(id);
        if (existingUser == null) {
            //log.error("删除失败，用户不存在，ID：{}", id);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        //  TODO：后续优化删除需要连带用户连带的银行卡信息一起删除
        // 删除用户
        customerMapper.deleteById(id);
        log.info("用户删除成功，ID：{}", id);
    }
}
