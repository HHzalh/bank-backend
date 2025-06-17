package com.king.bankbackend.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户登录返回的数据格式")
public class CustomerLoginVO {

    @Schema(description = "主键值")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "用户头像地址")
    private String imageUrl;

    @Schema(description = "jwt令牌")
    private String token;
}
