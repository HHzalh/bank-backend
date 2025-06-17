package com.king.bankbackend.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "管理员登录时传递的数据模型")
public class CustomerLoginDTO {

    @Schema(description = "账户")
    private String account;

    @Schema(description = "密码")
    private String password;
}
