package com.king.bankbackend.mapper;


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
}
