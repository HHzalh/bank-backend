package com.king.bankbackend.mapper;


import com.github.pagehelper.Page;
import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.dto.CustomerQueryDTO;
import com.king.bankbackend.model.entity.User;
import com.king.bankbackend.model.vo.CustomerQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

/**
 * 客户数据访问层
 */
@Mapper
public interface CustomerMapper {

    /**
     * 根据账户查询用户信息
     *
     * @param
     * @return
     */
    @Select("select * from userinfo where account = #{account}")
    User getByAccount(String account);

    /**
     * 通过id获取管理员信息
     *
     * @param id
     * @return
     */
    @Select("select * from userinfo where userID = #{id}")
    User getById(Long id);

    /**
     * 通过身份证号获取用户信息
     *
     * @param pid
     * @return
     */
    @Select("select * from userinfo where PID = #{pid}")
    User getByPid(String pid);

    /**
     * 插入用户数据
     *
     * @param user
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(User user);

    /**
     * 更新用户数据
     *
     * @param user
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(User user);

    /**
     * 根据ID删除用户
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 条件查询用户列表
     *
     * @param customerQueryDTO
     * @param beginDateTime
     * @param endDateTime
     * @return
     */
    Page<CustomerQueryVO> pageQuery(CustomerQueryDTO customerQueryDTO, LocalDateTime beginDateTime, LocalDateTime endDateTime);

    /**
     * 管理学修改头像
     *
     * @param id
     * @param imageUrl
     */
    void updateAvatar(Long id, String imageUrl);
}
