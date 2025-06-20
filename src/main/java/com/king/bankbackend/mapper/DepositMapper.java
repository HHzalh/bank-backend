package com.king.bankbackend.mapper;

import com.github.pagehelper.Page;
import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.dto.DepositQueryDTO;
import com.king.bankbackend.model.entity.Deposit;
import com.king.bankbackend.model.vo.DepositQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 存款类型数据访问层
 */
@Mapper
public interface DepositMapper {

    /**
     * 新增存款类型
     *
     * @param deposit
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Deposit deposit);

    /**
     * 更新存款类型
     *
     * @param deposit
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Deposit deposit);

    /**
     * 根据ID删除存款类型
     *
     * @param savingid
     */
    void deleteById(Long savingid);

    /**
     * 根据ID查询存款类型
     *
     * @param savingid
     * @return
     */
    @Select("select * from deposit where savingID = #{savingid}")
    Deposit getById(Long savingid);

    /**
     * 根据存款类型名字查询存款类型
     *
     * @param savingname
     * @return
     */
    @Select("select * from deposit where savingName = #{savingname}")
    Deposit getByName(String savingname);

    /**
     * 范围分页查询存款类型
     *
     * @param depositQueryDTO
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    Page<DepositQueryVO> pageQuery(DepositQueryDTO depositQueryDTO, LocalDateTime beginDateTime, LocalDateTime endDateTime);

    /**
     * 统计使用指定存款类型的存款记录数量
     *
     * @param savingid 存款类型ID
     * @return 使用该存款类型的记录数量
     */
    @Select("SELECT COUNT(*) FROM cardinfo WHERE savingID = #{savingid}")
    int countDeposit(Long savingid);
}