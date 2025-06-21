package com.king.bankbackend.controller.user;


import cn.hutool.core.bean.BeanUtil;
import com.king.bankbackend.common.BaseResponse;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.common.Result;
import com.king.bankbackend.constant.JwtClaimsConstant;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.model.dto.*;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CardVO;
import com.king.bankbackend.model.vo.LoginUserVO;
import com.king.bankbackend.properties.JwtProperties;
import com.king.bankbackend.service.CardService;
import com.king.bankbackend.service.TradeService;
import com.king.bankbackend.service.UserService;
import com.king.bankbackend.utils.JwtUtil;
import com.king.bankbackend.utils.TencentCosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private TencentCosUtils tencentCosUtils;

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/user/login")
    public BaseResponse<LoginUserVO> loginUser(@RequestBody UserLoginRequest userLoginRequest) {

        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        //登录
        User user = userService.userLogin(userLoginRequest);
        //登录成功生成Jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, user.getUserid());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);
        //封装登录返回结果
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        loginUserVO.setToken(token);
        return Result.success(loginUserVO);
    }

    /**
     * 用户注册
     *
     * @param customerDTO
     * @return
     */
    @PostMapping("/user/register")
    public BaseResponse<String> userRegister(@RequestBody CustomerDTO customerDTO) {
        ThrowUtils.throwIf(customerDTO == null, ErrorCode.PARAMS_ERROR);
        //注册用户
        Boolean result = userService.userRegister(customerDTO);
        return Result.success("用户注册完成，自动开卡成功");
    }

    /**
     * 获得用户的信息
     *
     * @return
     */
    @GetMapping("/user/userInfo")
    public BaseResponse<User> getUserInfo() {
        User user = userService.getUserInfo();
        return Result.success(user);
    }

    /**
     * 存款
     *
     * @param depositRequest
     * @return
     */
    @PostMapping("/cards/deposit")
    public BaseResponse deposit(@RequestBody DepositRequest depositRequest) {
        ThrowUtils.throwIf(depositRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //获得存款之后的余额
        Long balance = cardService.depositByCardId(depositRequest);
        if (balance > 0) {
            return Result.success(balance);
        }
        return Result.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 取款
     *
     * @param withdrawRequest
     * @return
     */
    @PostMapping("/cards/withdraw")
    public BaseResponse withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        ThrowUtils.throwIf(withdrawRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //获取取款之后的余额
        Long balance = cardService.withdrawByCardId(withdrawRequest);
        if (balance != null && balance >= 1) {
            return Result.success(balance);
        }
        return Result.error(ErrorCode.OPERATION_ERROR);
    }

    /**
     * 转账
     *
     * @param transferRequest
     * @return
     */
    @PostMapping("/cards/transfer")
    public BaseResponse transfer(@RequestBody TransferRequest transferRequest) {
        ThrowUtils.throwIf(transferRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //转账
        Boolean result = cardService.transfer(transferRequest);
        if (result) {
            return Result.success(result);
        }
        return Result.error(ErrorCode.OPERATION_ERROR, "转账失败");
    }

    /**
     * 根据卡号查询余额
     *
     * @param cardId
     * @return
     */
    @GetMapping("/cards/{cardId}")
    public BaseResponse getBalanceByCardId(@PathVariable String cardId) {
        ThrowUtils.throwIf(cardId == null, ErrorCode.NOT_FOUND_ERROR);
        //根据卡号查询余额
        Long balance = cardService.getBalanceByCardId(cardId);
        if (balance == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "卡号错误");
        }
        return Result.success(balance);
    }

    /**
     * 挂失银行卡
     *
     * @param reportLossRequest
     * @return
     */
    @PutMapping("/cards/reportLoss")
    public BaseResponse reportLoss(@RequestBody ReportLossRequest reportLossRequest) {
        ThrowUtils.throwIf(reportLossRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //挂失银行卡
        Boolean result = cardService.reportLossByCardId(reportLossRequest);
        //TODO:挂失之后 银行卡不能使用
        if (!result) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "卡号错误");
        }
        return Result.success(result);
    }

    /**
     * 修改密码
     *
     * @param changedPwdRequest
     * @return
     */
    @PutMapping("/cards/changedPwd")
    public BaseResponse changedPwd(@RequestBody ChangedPwdRequest changedPwdRequest) {
        ThrowUtils.throwIf(changedPwdRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //更改银行卡密码
        Boolean result = cardService.changedPwd(changedPwdRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "卡号错误");
        }
        return Result.success(result);
    }

    /**
     * 更新用户个人资料
     *
     * @param updateProfileRequest
     * @return
     */
    @PutMapping("/user/updateProfile")
    public BaseResponse updateProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        ThrowUtils.throwIf(updateProfileRequest == null, ErrorCode.NOT_FOUND_ERROR);
        //修改个人资料
        //TODO:前端限制性别下拉框（性别不能为空）
        //TODO:前端限制电话号码格式，且电话号码不能为空
        //TODO:修改用户资料后，银行卡的用户名也对应修改
        Boolean result = userService.updateProfile(updateProfileRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return Result.success(result);
    }

    /**
     * 查询当前用户银行卡集合
     *
     * @return
     */
    @GetMapping("/user/cards")
    public BaseResponse<List<CardVO>> cards() {
        //查询当前用户的所有银行卡，并返回脱敏后的结果
        List<CardVO> cards = cardService.getCards();
        if (cards.size() <= 0 || cards == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return Result.success(cards);
    }

    /**
     * 范围分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param begin
     * @param end
     * @return
     */
    @PostMapping("/trade/pageTradeUser")
    public BaseResponse<PageResult> pageTrade(
            @RequestBody TradeQueryDTO tradeQueryDTO,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {

        log.info("用户查询交易记录，查询条件：{}, 开始日期：{}, 结束日期：{}", tradeQueryDTO, begin, end);

        PageResult pageResult = tradeService.pageTradeByuser(tradeQueryDTO, begin, end);

        return Result.success(pageResult);
    }

    /**
     * 用户修改头像
     *
     * @param file 上传的图片文件
     */
    @PostMapping("/uploadImage")
    public BaseResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        // 检查文件是否为空
        ThrowUtils.throwIf(file == null || file.isEmpty(), ErrorCode.PARAMS_ERROR, "上传文件不能为空");

        // 检查文件类型是否为图片
        String contentType = file.getContentType();
        ThrowUtils.throwIf(contentType == null || !contentType.startsWith("image/"),
                ErrorCode.PARAMS_ERROR, "只能上传图片文件");

        try {
            // 调用腾讯云工具类上传图片
            String imageUrl = tencentCosUtils.uploadFile(file);
            userService.updateAvatar(imageUrl);
            log.info("图片上传成功，URL: {}", imageUrl);

            return Result.success(imageUrl);
        } catch (Exception e) {
            log.error("图片上传失败", e);
            return new BaseResponse<>(ErrorCode.SYSTEM_ERROR.getCode(), null, "图片上传失败");
        }
    }

    /**
     * 修改用户密码
     *
     * @param changedUserPwdRequest
     * @return
     */
    @PutMapping("/user/changedUserPwd")
    public BaseResponse changedUserPwd(@RequestBody ChangedUserPwdRequest changedUserPwdRequest) {
        ThrowUtils.throwIf(changedUserPwdRequest == null, ErrorCode.PARAMS_ERROR);
        // 修改用户密码
        Boolean result = userService.changedUserPwd(changedUserPwdRequest);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "密码修改失败");
        }
        return Result.success(result);
    }

}
