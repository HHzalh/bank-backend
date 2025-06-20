package com.king.bankbackend.config;


import com.king.bankbackend.properties.TencentCosProperties;
import com.king.bankbackend.utils.TencentCosUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类，用于创建TencentCos对象
 */
@Configuration
@Slf4j
public class CosConfiguration {
    @Bean   //将方法的返回值注册为 Spring 容器管理的 Bean  这样在程序启动的的时候就会自动由spring创建、初始化这个Bean 从而避免了手动new一个TencentCosUtils对象
    @ConditionalOnMissingBean
    //@ConditionalOnMissingBean 是 Spring Boot 提供的一个条件化注解，其核心作用是：仅在当前 Spring 容器中不存在指定类型的 Bean 时，才会创建被标注的 Bean。
    public TencentCosUtils tencentCosUtils(TencentCosProperties tencentCosProperties) {
        log.info("初始化腾讯云对象存储...");
        log.info("{}", tencentCosProperties);
        return new TencentCosUtils(
                tencentCosProperties.getSecretId(),
                tencentCosProperties.getSecretKey(),
                tencentCosProperties.getRegion(),
                tencentCosProperties.getBucket(),
                tencentCosProperties.getHost()
        );
    }
}