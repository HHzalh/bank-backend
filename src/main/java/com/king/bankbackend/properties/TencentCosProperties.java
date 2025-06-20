package com.king.bankbackend.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cos.client")
@Data
public class TencentCosProperties {
    private String secretId;
    private String secretKey;
    private String region;
    private String bucket;
    private String host;
}

