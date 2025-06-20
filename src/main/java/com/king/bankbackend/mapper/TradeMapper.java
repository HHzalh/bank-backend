package com.king.bankbackend.mapper;

import com.github.pagehelper.Page;
import com.king.bankbackend.model.dto.TradeQueryDTO;
import com.king.bankbackend.model.entity.Trade;
import com.king.bankbackend.model.vo.TradeQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 交易数据访问层
 */
@Mapper
public interface TradeMapper {


    /**
     * 根据ID删除交易记录
     *
     * @param tradeid
     */
    void deleteById(Long tradeid);

    /**
     * 根据ID查询交易记录
     *
     * @param tradeid
     * @return
     */
    @Select("select * from tradeinfo where tradeID = #{tradeid}")
    Trade getById(Long tradeid);

    /**
     * 分页查询交易记录
     *
     * @param tradeQueryDTO
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    Page<TradeQueryVO> pageQuery(TradeQueryDTO tradeQueryDTO, LocalDateTime beginDateTime, LocalDateTime endDateTime);
} 