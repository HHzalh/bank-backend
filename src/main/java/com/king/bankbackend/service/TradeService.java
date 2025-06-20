package com.king.bankbackend.service;

import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.model.dto.TradeQueryDTO;

import java.time.LocalDate;

/**
 * 交易服务接口
 */
public interface TradeService {

    /**
     * 删除交易记录
     *
     * @param tradeid
     */
    void deleteTrade(Long tradeid);


    /**
     * 范围分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param begin
     * @param end
     * @return
     */
    PageResult pageTrade(TradeQueryDTO tradeQueryDTO, LocalDate begin, LocalDate end);

    /**
     * 用户范围分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param begin
     * @param end
     * @return
     */
    PageResult pageTradeByuser(TradeQueryDTO tradeQueryDTO, LocalDate begin, LocalDate end);
}