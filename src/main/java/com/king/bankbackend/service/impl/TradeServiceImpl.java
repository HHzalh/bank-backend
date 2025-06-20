package com.king.bankbackend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.king.bankbackend.common.PageResult;
import com.king.bankbackend.constant.LocalDateConstant;
import com.king.bankbackend.context.BaseContext;
import com.king.bankbackend.exception.BusinessException;
import com.king.bankbackend.exception.ErrorCode;
import com.king.bankbackend.mapper.TradeMapper;
import com.king.bankbackend.model.dto.TradeQueryDTO;
import com.king.bankbackend.model.entity.Trade;
import com.king.bankbackend.model.vo.CardVO;
import com.king.bankbackend.model.vo.TradeQueryVO;
import com.king.bankbackend.service.CardService;
import com.king.bankbackend.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * 交易服务实现类
 */
@Service
@Slf4j
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeMapper tradeMapper;

    @Autowired
    private CardService cardService;


    /**
     * 删除交易记录
     *
     * @param tradeid
     */
    @Override
    public void deleteTrade(Long tradeid) {
        log.info("删除交易记录，交易ID：{}", tradeid);

        // 参数校验
        if (tradeid == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "交易ID不能为空");
        }
        // 检查是否存在
        Trade existingTrade = tradeMapper.getById(tradeid);
        if (existingTrade == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "交易记录不存在");
        }

        tradeMapper.deleteById(tradeid);
    }


    /**
     * 范围分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param begin
     * @param end
     * @return
     */
    @Override
    public PageResult pageTrade(TradeQueryDTO tradeQueryDTO, LocalDate begin, LocalDate end) {
        log.info("分页查询交易记录，查询条件：{}", tradeQueryDTO);

        // 设置默认分页参数
        if (tradeQueryDTO.getPage() <= 0) {
            tradeQueryDTO.setPage(1);
        }
        if (tradeQueryDTO.getPageSize() <= 0) {
            tradeQueryDTO.setPageSize(10);
        }
        if (tradeQueryDTO.getMaxMoney().compareTo(BigDecimal.ZERO) == 0) {
            tradeQueryDTO.setMaxMoney(null);
        }
        if (tradeQueryDTO.getMinMoney().compareTo(BigDecimal.ZERO) == 0) {
            tradeQueryDTO.setMinMoney(null);
        }
        if(tradeQueryDTO.getTradeid()==0){
            tradeQueryDTO.setTradeid(null);
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

        log.info("处理后的查询条件：{}, 开始时间：{}, 结束时间：{}", tradeQueryDTO, beginTime, endTime);
        // 使用PageHelper进行分页
        PageHelper.startPage(tradeQueryDTO.getPage(), tradeQueryDTO.getPageSize());

        Page<TradeQueryVO> page = tradeMapper.pageQuery(tradeQueryDTO, beginTime, endTime);
        long total = page.getTotal();
        List<TradeQueryVO> records = page.getResult();

        return new PageResult(total, records);
    }


    public PageResult pageTradeByuser(TradeQueryDTO tradeQueryDTO, LocalDate begin, LocalDate end) {
        log.info("分页查询交易记录，查询条件：{}", tradeQueryDTO);

        // 设置默认分页参数
        if (tradeQueryDTO.getPage() <= 0) {
            tradeQueryDTO.setPage(1);
        }
        if (tradeQueryDTO.getPageSize() <= 0) {
            tradeQueryDTO.setPageSize(10);
        }
        if (tradeQueryDTO.getMaxMoney().compareTo(BigDecimal.ZERO) == 0) {
            tradeQueryDTO.setMaxMoney(null);
        }
        if (tradeQueryDTO.getMinMoney().compareTo(BigDecimal.ZERO) == 0) {
            tradeQueryDTO.setMinMoney(null);
        }
        if(tradeQueryDTO.getTradeid()==0){
            tradeQueryDTO.setTradeid(null);
        }
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;

        // 设置开始时间
        if (begin != null) {
            beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        } else {
            beginTime = LocalDateTime.of(LocalDateConstant.DEFAULT_TIMESTAMP, LocalTime.MIN);
        }

        // 设置结束时间
        if (end != null) {
            endTime = LocalDateTime.of(end, LocalTime.MAX);
        } else {
            endTime = LocalDateTime.now();
        }

        log.info("处理后的查询条件：{}, 开始时间：{}, 结束时间：{}", tradeQueryDTO, beginTime, endTime);
        
        // 获取用户的银行卡
        List<CardVO> cards = cardService.getCards();
        
        if(cards == null || cards.isEmpty()){
            log.warn("用户没有银行卡数据，无法查询交易记录");
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"没有银行卡数据");
        }
        
        log.info("用户拥有的银行卡数量: {}", cards.size());
        
        // 使用PageHelper进行分页 - 确保在进行查询之前设置
        PageHelper.startPage(tradeQueryDTO.getPage(), tradeQueryDTO.getPageSize());

        // 查询交易记录
        Page<TradeQueryVO> page = tradeMapper.pageQueryByUser(tradeQueryDTO, beginTime, endTime, cards);
        
        // 记录查询结果
        long total = page.getTotal();
        List<TradeQueryVO> records = page.getResult();
        
        log.info("查询到的交易记录总数: {}, 当前页记录数: {}", total, records.size());

        // 返回分页结果
        return new PageResult(total, records);
    }
}