package com.king.bankbackend.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.constant.*;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.CardMapper;
import com.king.bankbackend.mapper.CustomerMapper;
import com.king.bankbackend.model.dto.CustomerDTO;
import com.king.bankbackend.model.dto.CustomerLoginDTO;
import com.king.bankbackend.model.dto.CustomerQueryDTO;
import com.king.bankbackend.model.dto.CustomerUpdateDTO;
import com.king.bankbackend.model.entity.Card;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CardVO;
import com.king.bankbackend.model.vo.CustomerLoginVO;
import com.king.bankbackend.model.vo.CustomerQueryVO;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.service.CustomerService;
import com.king.bankbackend.utils.BankCardIdGenerator;
import com.king.bankbackend.utils.JwtUtil;
import com.king.bankbackend.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RedisUtil redisUtil;

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

        //密码加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        if (!password.equals(user.getPassword())) {
            //密码错误
            log.error("登录失败，密码错误，账号：{}", account);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        log.info("登录成功，用户信息：{}", user);
        return user;
    }

    /**
     * 管理员登录并返回带有token的登录结果
     *
     * @param customerLoginDTO
     * @return CustomerLoginVO包含管理员信息和token
     */
    @Override
    public CustomerLoginVO loginWithToken(CustomerLoginDTO customerLoginDTO) {
        // 登录验证
        User user = login(customerLoginDTO);

        // 查询Redis中是否已存在token
        String token = redisUtil.getAdminToken(user.getUserid());

        // 如果Redis中不存在token，则生成新token并存入Redis
        if (token == null) {
            // 登录成功生成Jwt令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put(JwtClaimsConstant.Admin_ID, user.getUserid());
            token = JwtUtil.createJWT(
                    jwtProperties.getAdminSecretKey(),
                    jwtProperties.getAdminTtl(),
                    claims);
            // 存入Redis
            redisUtil.setAdminToken(user.getUserid(), token);
        }

        // 封装登录返回结果
        CustomerLoginVO customerLoginVO = CustomerLoginVO.builder()
                .userId(user.getUserid())
                .userName(user.getUsername())
                .imageUrl(user.getImageurl())
                .token(token)
                .build();
        return customerLoginVO;
    }

    /**
     * 获取管理员的全部信息
     *
     * @return
     */
    public User getAdminInfo() {
        Long curId = BaseContext.getCurrentId();
        //根据账户查询数据库中的数据
        User user = customerMapper.getById(curId);
        return user;
    }

    /**
     * 新增用户且实现自动开卡
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

        Card card = new Card();
        //随机生成银行卡号格式为10103576xxxxxxxx
        String cardId = BankCardIdGenerator.generate();
        // 检查卡号是否已存在
        Card existingCard = cardMapper.getByCardNumber(cardId);
        if (existingCard != null) {
            cardId = BankCardIdGenerator.generate();
        }
        card.setCardid(cardId);
        card.setCurid("RMB");
        card.setSavingid(1L);
        card.setOpendate(LocalDate.now());
        card.setBalance(BigDecimal.valueOf(10));
        card.setOpenmoney(BigDecimal.valueOf(10));
        card.setPass(PasswordConstant.DEFAULT_CARD_PASSWORD);
        card.setIsreportloss(CardConstant.DEFAULT_NOT_REPORT_LOSS);
        card.setCustomerid(user.getUserid());
        card.setCustomername(user.getUsername());

        customerMapper.insert(user);
        //新增用户后实现自动开卡
        cardMapper.insert(card);
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
        // 更新用户信息
        customerMapper.update(user);

        // 如果用户名被修改，同步更新银行卡表中的用户名
        List<CardVO> list = cardMapper.getCards(user.getUserid());
        for (CardVO cardVO : list) {
            String cardid = cardVO.getCardid();
            cardMapper.updateCardName(cardid, user.getUsername());
        }
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
        // 如果用户存在的银行卡余额有一张不为0就不能删除用户

        // 检查用户是否有余额不为0的银行卡
        List<CardVO> cardsWithBalance = cardMapper.findCardsByUserIdWithBalance(id);
        if (cardsWithBalance != null && !cardsWithBalance.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,
                    "用户名下有" + cardsWithBalance.size() + "张银行卡仍有余额，无法删除用户");
        }

        //删除用户旗下的银行卡
        cardMapper.deleteByUserId(id);

        // 删除用户
        customerMapper.deleteById(id);


        log.info("用户删除成功，ID：{}", id);
    }

    /**
     * 分页查询用户信息
     *
     * @param customerQueryDTO
     * @param begin
     * @param end
     * @return
     */
    public PageResult pageCustomer(CustomerQueryDTO customerQueryDTO, LocalDate begin, LocalDate end) {
        log.info("分页查询用户信息，查询条件：{}{}{}", customerQueryDTO, begin, end);

        // 设置默认分页参数
        if (customerQueryDTO.getPage() <= 0) {
            customerQueryDTO.setPage(1);
        }
        if (customerQueryDTO.getPageSize() <= 0) {
            customerQueryDTO.setPageSize(10);
        }
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;

        if (begin != null) {
            beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        } else {
            beginTime = LocalDateTime.of(LocalDateConstant.DEFAULT_TIMESTAMP, LocalTime.MIN);
        }

        if (end != null) {
            endTime = LocalDateTime.of(end, LocalTime.MAX);
        } else {
            endTime = LocalDateTime.now();
        }

        // 使用PageHelper进行分页
        PageHelper.startPage(customerQueryDTO.getPage(), customerQueryDTO.getPageSize());

        Page<CustomerQueryVO> page = customerMapper.pageQuery(customerQueryDTO, beginTime, endTime);
        long total = page.getTotal();
        List<CustomerQueryVO> records = page.getResult();

        return new PageResult(total, records);
    }


}

