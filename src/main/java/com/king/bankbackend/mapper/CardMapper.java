package com.king.bankbackend.mapper;

import com.github.pagehelper.Page;
import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.dto.CardQueryDTO;
import com.king.bankbackend.model.entity.Card;
import com.king.bankbackend.model.vo.CardListVo;
import com.king.bankbackend.model.vo.CardQueryVO;
import com.king.bankbackend.model.vo.CardVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 银行卡数据访问层
 */
@Mapper
public interface CardMapper {


    /**
     * 存款
     * @param cardid
     * @param savingid
     * @param amount
     * @return
     */
    @Update("UPDATE cardInfo SET balance = balance + #{amount}, savingID = #{savingid} WHERE cardID = #{cardid}")
    int depositByCardId(String cardid, Long savingid, Long amount);

    /**
     * 插入交易信息
     * @param tradeType
     * @param cardid
     * @param amount
     * @param remark
     * @return
     */
    @Insert("INSERT INTO tradeInfo (tradeType, cardID, tradeMoney, remark) VALUES (#{tradeType}, #{cardid}, #{amount}, #{remark})")
    int insertTradeRecord(String tradeType, String cardid, Long amount, String remark);

    /**
     * 获取银行卡余额
     * @param cardid
     * @return
     */
    @Select("SELECT balance FROM cardInfo WHERE cardID = #{cardid}")
    Long getBalance(String cardid);

    /**
     * 获取银行卡密码
     * @param cardid 银行卡号
     * @return 密码
     */
    String getCardPass(String cardid);

    /**
     * 执行取款操作
     * @param cardid 银行卡号
     * @param amount 取款金额
     */
    void withdrawByCardId(String cardid, Long amount);

    /**
     * 向指定银行卡号转账
     * @param cardid
     */
    @Update("UPDATE cardInfo SET balance = balance + #{amount} WHERE cardID = #{cardid}")
    void transfer(String cardid,Long amount);

    /**
     * 银行卡挂失
     * @param cardId
     * @return
     */
    @Update("UPDATE cardInfo SET IsReportLoss = #{isLoss} WHERE cardID = #{cardId}")
    Boolean reportLossByCardId(String cardId,String isLoss);

    /**
     * 修改密码
     * @param cardid
     * @param newPassword
     */

    @Update("UPDATE cardInfo SET pass = #{newPassword} WHERE cardID = #{cardid}")
    Boolean changedPwd(String cardid, String newPassword);

    /**
     * 根据用户ID查询银行卡列表
     * @param userId 用户ID
     * @return 银行卡列表
     */
    List<CardVO> getCards(Long userId);

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
    Page<CardQueryVO> pageQuery(CardQueryDTO cardQueryDTO, LocalDateTime beginDateTime, LocalDateTime endDateTime);
} 