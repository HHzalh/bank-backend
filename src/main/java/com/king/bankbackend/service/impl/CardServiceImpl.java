package com.king.bankbackend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.constant.CardConstant;
import com.king.bankbackend.constant.LocalDateConstant;
import com.king.bankbackend.constant.PasswordConstant;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.exception.ThrowUtils;
import com.king.bankbackend.mapper.CardMapper;
import com.king.bankbackend.mapper.CustomerMapper;
import com.king.bankbackend.model.dto.*;
import com.king.bankbackend.model.entity.Card;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CardListVo;
import com.king.bankbackend.model.vo.CardQueryVO;
import com.king.bankbackend.model.vo.CardStatusVo;
import com.king.bankbackend.model.vo.CardVO;
import com.king.bankbackend.service.CardService;
import com.king.bankbackend.utils.BankCardIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 银行卡服务实现类
 */
@Service
@Slf4j
public class CardServiceImpl implements CardService {

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CustomerMapper customerMapper;


    /**
     * 存款
     *
     * @param depositRequest
     * @return
     */
    @Override
    public Long depositByCardId(DepositRequest depositRequest) {
        String cardid = depositRequest.getCardid();
        Long savingid = depositRequest.getSavingid();
        Long amount = depositRequest.getAmount();
        String remark = depositRequest.getRemark();
        ThrowUtils.throwIf(cardid == null, ErrorCode.PARAMS_ERROR, "银行卡号不能为空");
        ThrowUtils.throwIf(amount < 0, ErrorCode.PARAMS_ERROR, "存款金额不能小于0");

        CardStatusVo cardStatusVo = this.getStatus(cardid);
        if (!cardStatusVo.getIsreportloss().equals(CardConstant.DEFAULT_NOT_REPORT_LOSS)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该卡已挂失");
        }
        // 执行存款操作
        cardMapper.depositByCardId(cardid, savingid, amount);

        // 插入交易记录
        cardMapper.insertTradeRecord("存款", cardid, amount, remark);

        // 返回当前余额
        return cardMapper.getBalance(cardid);
    }

    /**
     * 取款
     *
     * @param withdrawRequest
     * @return
     */
    @Override
    public Long withdrawByCardId(WithdrawRequest withdrawRequest) {
        String cardid = withdrawRequest.getCardid();
        Long amount = withdrawRequest.getAmount();
        String pass = withdrawRequest.getPass();
        String remark = withdrawRequest.getRemark();
        //加密
        pass = DigestUtils.md5DigestAsHex(pass.getBytes());
        // 验证密码
        String correctPass = cardMapper.getCardPass(cardid);

        if (!pass.equals(correctPass)) {
            throw new IllegalArgumentException("密码错误");
        }

        CardStatusVo cardStatusVo = this.getStatus(cardid);
        if (!cardStatusVo.getIsreportloss().equals(CardConstant.DEFAULT_NOT_REPORT_LOSS)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该卡已挂失");
        }

        // 检查余额是否足够（取款后余额必须至少为1元）
        Long balance = cardMapper.getBalance(cardid);
        if (balance == null || balance < amount || (balance - amount) < 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "余额不足，取款后余额必须至少保留1元");
        }

        // 执行取款操作
        cardMapper.withdrawByCardId(cardid, amount);

        // 插入交易记录
        cardMapper.insertTradeRecord("取款", cardid, amount, remark);

        // 返回当前余额
        return cardMapper.getBalance(cardid);
    }

    /**
     * 转账
     *
     * @param transferRequest
     */
    @Override
    @Transactional
    public Boolean transfer(TransferRequest transferRequest) {
        //获取参数中的信息
        String fromCardId = transferRequest.getFromCardId();
        String pass = transferRequest.getPassword();
        String toCardId = transferRequest.getToCardId();
        Long amount = transferRequest.getAmount();
        //校验账号是否存在
        Long balance1 = cardMapper.getBalance(fromCardId);
        Long balance2 = cardMapper.getBalance(toCardId);
        //加密
        pass = DigestUtils.md5DigestAsHex(pass.getBytes());
        if (balance2 == null || balance1 == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        //先校验密码是否正确
        String cardPass = cardMapper.getCardPass(fromCardId);
        if (!cardPass.equals(pass)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        CardStatusVo cardStatusVo = this.getStatus(fromCardId);
        if (!cardStatusVo.getIsreportloss().equals(CardConstant.DEFAULT_NOT_REPORT_LOSS)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该卡已挂失");
        }

        if (amount <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "转账金额不能小于0");
        }

        // 检查转账银行卡当前余额是否足够（取款后余额必须至少为1元）
        Long balance = cardMapper.getBalance(fromCardId);
        if (balance == null || balance < amount || (balance - amount) < 1) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "余额不足");
        }
        //转账卡号扣款
        cardMapper.withdrawByCardId(fromCardId, amount);
        //收款账号收款
        cardMapper.transfer(toCardId, amount);

        // 插入交易记录
        String remark = "转账给" + toCardId + ",金额：" + amount;
        cardMapper.insertTradeRecord("转账", fromCardId, amount, remark);

        return true;
    }

    /**
     * 根据卡号查询余额
     *
     * @param cardId
     * @return
     */
    @Override
    public Long getBalanceByCardId(String cardId) {
        Long balance = cardMapper.getBalance(cardId);
        return balance;
    }

    /**
     * 挂失银行卡
     *
     * @param reportLossRequest
     * @return
     */
    @Override
    public Boolean reportLossByCardId(ReportLossRequest reportLossRequest) {
        String cardid = reportLossRequest.getCardid();
        String isLoss = reportLossRequest.getIsLoss();
        //校验账号是否存在
        Long balance = cardMapper.getBalance(cardid);
        if (balance == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        Boolean result = cardMapper.reportLossByCardId(cardid, isLoss);
        return result;
    }

    /**
     * 修改银行卡密码
     *
     * @param changedPwdRequest
     * @return
     */
    @Override
    public Boolean changedPwd(ChangedPwdRequest changedPwdRequest) {
        String cardid = changedPwdRequest.getCardid();
        String oldPassword = changedPwdRequest.getOldPassword();
        String newPassword = changedPwdRequest.getNewPassword();
        //判断卡号是否存在
        Long balance = cardMapper.getBalance(cardid);
        if (cardid == null || balance == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "银行卡号错误");
        }
        //判断密码是否正确
        //加密原密码
        oldPassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
        String cardPass = cardMapper.getCardPass(cardid);
        if (!oldPassword.equals(cardPass)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        if (newPassword == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "新密码为空");
        }
        //加密新密码
        newPassword = DigestUtils.md5DigestAsHex(newPassword.getBytes());
        Boolean result = cardMapper.changedPwd(cardid, newPassword);
        return result;
    }

    /**
     * 获取当前用户银行卡集合
     *
     * @return
     */
    @Override
    public List<CardVO> getCards() {
        Long userId = BaseContext.getCurrentId();
        List<CardVO> cards = cardMapper.getCards(userId);
        return cards;
    }

    /**
     * 新增银行卡
     *
     * @param cardDTO
     */
    public void addCard(CardDTO cardDTO) {
        if (cardDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "银行卡信息不能为空");
        }
        User user = customerMapper.getByPid(cardDTO.getPid());
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        Card card = new Card();
        BeanUtils.copyProperties(cardDTO, card);
        //随机生成银行卡号格式为10103576xxxxxxxx
        String cardId = BankCardIdGenerator.generate();
        // 检查卡号是否已存在
        Card existingCard = cardMapper.getByCardNumber(cardId);
        if (existingCard != null) {
            cardId = BankCardIdGenerator.generate();
        }
        card.setCardid(cardId);
        card.setOpendate(LocalDate.now());
        card.setBalance(card.getOpenmoney());
        card.setPass(PasswordConstant.DEFAULT_CARD_PASSWORD);
        card.setIsreportloss(CardConstant.DEFAULT_NOT_REPORT_LOSS);
        card.setCustomerid(user.getUserid());
        card.setCustomername(user.getUsername());
        cardMapper.insert(card);
    }

    /**
     * 更新银行卡密码
     *
     * @param cardUpdatePwdDTO
     */
    public void changePwd(CardUpdatePwdDTO cardUpdatePwdDTO) {
        // 检查是否存在
        Card existingCard = cardMapper.getById(cardUpdatePwdDTO.getCardid());
        if (existingCard == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "银行卡不存在");
        }
        if (!existingCard.getPass().equals(cardUpdatePwdDTO.getOldpass())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 封装数据
        Card card = new Card();
        BeanUtils.copyProperties(cardUpdatePwdDTO, card);
        card.setPass(cardUpdatePwdDTO.getNewpass());
        cardMapper.update(card);
    }

    /**
     * 更新银行卡状态(是否挂失)
     *
     * @param cardUpdateStatusDTO
     * @return
     */
    public void changeStatus(CardUpdateStatusDTO cardUpdateStatusDTO) {
        // 检查是否存在
        Card existingCard = cardMapper.getById(cardUpdateStatusDTO.getCardid());
        if (existingCard == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "银行卡不存在");
        }
        // 封装数据
        Card card = new Card();
        BeanUtils.copyProperties(cardUpdateStatusDTO, card);
        card.setIsreportloss(cardUpdateStatusDTO.getIsreportloss());
        cardMapper.update(card);
    }

    /**
     * 获取银行卡状态(是否挂失)
     *
     * @param cardid
     * @return
     */
    public CardStatusVo getStatus(String cardid) {
        // 检查是否存在
        Card existingCard = cardMapper.getById(cardid);
        if (existingCard == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "银行卡不存在");
        }
        CardStatusVo cardStatusVo = new CardStatusVo();
        BeanUtils.copyProperties(existingCard, cardStatusVo);
        return cardStatusVo;
    }


    /**
     * 删除银行卡
     *
     * @param cardid
     */
    public void deleteCard(String cardid) {
        // 检查是否存在
        Card existingCard = cardMapper.getById(cardid);
        if (existingCard == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "银行卡不存在");
        }
        // TODO: 账户还有余额不能删除
        if (existingCard.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户还有余额不能删除");
        }

        cardMapper.deleteById(cardid);
    }

    /**
     * 根据银行卡号获取银行卡
     *
     * @param cardid
     * @return
     */
    public CardQueryVO getCard(String cardid) {
        log.info("获取银行卡信息，银行卡号：{}", cardid);

        // 参数校验
        if (!StringUtils.hasText(cardid)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "银行卡号不能为空");
        }

        Card card = cardMapper.getById(cardid);
        if (card == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "银行卡不存在");
        }
        CardQueryVO cardQueryVO = new CardQueryVO();
        BeanUtils.copyProperties(card, cardQueryVO);

        return cardQueryVO;
    }

    /**
     * 根据用户身份证获取用户拥有的银行卡列表
     *
     * @param pid
     * @return
     */
    public List<CardListVo> getCardsByPid(String pid) {
        // 参数校验
        User user = customerMapper.getByPid(pid);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "查询不到该用户");
        }
        Long customerid = user.getUserid();
        List<CardListVo> list = cardMapper.getByUserId(customerid);
        return list;
    }


    /**
     * 范围分页查询银行卡
     *
     * @param cardQueryDTO
     * @param begin
     * @param end
     * @return
     */
    public PageResult pageCard(CardQueryDTO cardQueryDTO, LocalDate begin, LocalDate end) {
        log.info("分页查询银行卡，查询条件：{}", cardQueryDTO);

        // 设置默认分页参数
        if (cardQueryDTO.getPage() <= 0) {
            cardQueryDTO.setPage(1);
        }
        if (cardQueryDTO.getPageSize() <= 0) {
            cardQueryDTO.setPageSize(10);
        }
        if (cardQueryDTO.getSavingid() == 0) {
            cardQueryDTO.setSavingid(null);
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

        log.info("处理后的查询条件：{}, 开始时间：{}, 结束时间：{}", cardQueryDTO, beginTime, endTime);
        // 使用PageHelper进行分页
        PageHelper.startPage(cardQueryDTO.getPage(), cardQueryDTO.getPageSize());

        Page<CardQueryVO> page = cardMapper.pageQuery(cardQueryDTO, beginTime, endTime);
        long total = page.getTotal();
        List<CardQueryVO> records = page.getResult();

        return new PageResult(total, records);
    }

} 