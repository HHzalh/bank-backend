package com.king.bankbackend.mapper;

import com.github.pagehelper.Page;
import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.dto.CardQueryDTO;
import com.king.bankbackend.model.entity.Card;
import com.king.bankbackend.model.vo.CardListVo;
import com.king.bankbackend.model.vo.CardQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 银行卡数据访问层
 */
@Mapper
public interface CardMapper {

    /**
     * 新增银行卡
     *
     * @param card
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Card card);

    /**
     * 更新银行卡
     *
     * @param card
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Card card);

    /**
     * 根据银行卡号删除银行卡
     *
     * @param cardid
     */
    void deleteById(String cardid);

    /**
     * 根据银行卡号查询银行卡
     *
     * @param cardid
     * @return
     */
    @Select("select * from cardinfo where cardid = #{cardid}")
    Card getById(String cardid);

    /**
     * 根据卡号查询银行卡
     *
     * @param cardid
     * @return
     */
    @Select("select * from cardinfo where cardID = #{cardid}")
    Card getByCardNumber(String cardid);

    /**
     * 根据用户id查询银行卡
     *
     * @param customerid
     * @return
     */
    @Select("select * from cardinfo where customerID = #{customerid}")
    List<CardListVo> getByUserId(Long customerid);

    /**
     * 范围分页查询银行卡
     *
     * @param cardQueryDTO
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    Page<CardQueryVO> pageQuery(
            @Param("cardQueryDTO") CardQueryDTO cardQueryDTO,
            @Param("beginDateTime") LocalDateTime beginDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);
} 