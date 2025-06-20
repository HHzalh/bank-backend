package com.king.bankbackend.mapper;


import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据账户查询用户信息
     *
     * @param
     * @return
     */
    @Select("select * from userInfo where account = #{account}")
    User getByAccount(String account);

    /**
     * 通过id获取用户信息
     *
     * @param id
     * @return
     */
    @Select("select * from userInfo where userID = #{id}")
    User getById(Long id);

    /**
     * 修改个人资料
     *
     * @param username
     * @param gender
     * @param telephone
     * @param address
     * @param imageurl
     * @param userId
     */
    Boolean updateProfile(String username, String gender, String telephone, String address, String imageurl, Long userId);

    /**
     * 插入用户数据
     *
     * @param user
     * @return 插入成功的记录数
     */
    @AutoFill(value = OperationType.INSERT)
    int insert(User user);
}
