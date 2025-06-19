package com.king.bankbackend.model.vo;

import com.king.bankbackend.model.entity.Card;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserVO implements Serializable {

    /**
     * 用户 id
     */
    private Long userid;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 头像地址（可选）
     */
    private String imageurl;

    /**
     * jwt令牌
     */
    private String token;

    private static final long serialVersionUID = 1L;
}
