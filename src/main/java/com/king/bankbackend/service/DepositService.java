package com.king.bankbackend.service;

import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.model.dto.DepositDTO;
import com.king.bankbackend.model.dto.DepositQueryDTO;
import com.king.bankbackend.model.dto.DepositUpdateDTO;
import com.king.bankbackend.model.entity.Deposit;

import java.time.LocalDate;

/**
 * 存款类型服务接口
 */
public interface DepositService {

    /**
     * 新增存款类型
     *
     * @param depositDTO
     */
    void addDeposit(DepositDTO depositDTO);

    /**
     * 更新存款类型
     *
     * @param depositUpdateDTO
     */
    void updateDeposit(DepositUpdateDTO depositUpdateDTO);

    /**
     * 删除存款类型
     *
     * @param savingid
     */
    void deleteDeposit(Long savingid);

    /**
     * 根据ID获取存款类型
     *
     * @param savingid
     * @return
     */
    Deposit getDeposit(Long savingid);


    /**
     * 范围分页查询存款类型
     *
     * @param depositQueryDTO
     * @param begin
     * @param end
     * @return
     */
    PageResult pageDeposit(DepositQueryDTO depositQueryDTO, LocalDate begin, LocalDate end);
}