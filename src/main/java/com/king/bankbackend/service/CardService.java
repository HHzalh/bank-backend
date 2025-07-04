package com.king.bankbackend.service;

import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.model.dto.*;
import com.king.bankbackend.model.vo.CardListVo;
import com.king.bankbackend.model.vo.CardQueryVO;
import com.king.bankbackend.model.vo.CardStatusVo;
import com.king.bankbackend.model.vo.CardVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 银行卡服务接口
 */
public interface CardService {

    /**
     * 存款
     *
     * @param depositRequest
     * @return
     */
    Long depositByCardId(DepositRequest depositRequest);

    /**
     * 取款
     *
     * @param withdrawRequest
     * @return
     */
    Long withdrawByCardId(WithdrawRequest withdrawRequest);

    /**
     * 转账
     *
     * @param transferRequest
     */
    Boolean transfer(TransferRequest transferRequest);

    /**
     * 根据卡号查询余额
     *
     * @param cardId
     * @return
     */
    Long getBalanceByCardId(String cardId);

    /**
     * 挂失银行卡
     *
     * @param reportLossRequest
     */
    Boolean reportLossByCardId(ReportLossRequest reportLossRequest);

    /**
     * 修改银行卡密码
     *
     * @param changedPwdRequest
     * @return
     */
    Boolean changedPwd(ChangedPwdRequest changedPwdRequest);


    /**
     * 获取当前用户银行卡集合
     *
     * @return
     */
    List<CardVO> getCards();

    /**
     * 新增银行卡
     *
     * @param cardDTO
     */
    void addCard(CardDTO cardDTO);

    /**
     * 更新银行卡密码
     *
     * @param cardUpdatePwdDTO
     */
    void changePwd(CardUpdatePwdDTO cardUpdatePwdDTO);

    /**
     * 更新银行卡状态(是否挂失)
     *
     * @param cardUpdateStatusDTO
     * @return
     */
    void changeStatus(CardUpdateStatusDTO cardUpdateStatusDTO);

    /**
     * 获取银行卡状态(是否挂失)
     *
     * @param cardid
     * @return
     */
    CardStatusVo getStatus(String cardid);

    /**
     * 删除银行卡
     *
     * @param cardid
     */
    void deleteCard(String cardid);

    /**
     * 根据银行卡号获取银行卡
     *
     * @param cardid
     * @return
     */
    CardQueryVO getCard(String cardid);

    /**
     * 根据用户身份证获取用户拥有的银行卡列表
     *
     * @param pid
     * @return
     */
    List<CardListVo> getCardsByPid(String pid);

    /**
     * 范围分页查询银行卡
     *
     * @param cardQueryDTO
     * @param begin
     * @param end
     * @return
     */
    PageResult pageCard(CardQueryDTO cardQueryDTO, LocalDate begin, LocalDate end);


}