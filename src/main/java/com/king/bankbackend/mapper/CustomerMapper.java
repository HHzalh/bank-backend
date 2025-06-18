package com.king.bankbackend.mapper;


import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
