package com.jzm.backme.config.fisco;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * fisco网络连接配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "fisco")
@Component

public class FiscoConfig
{
    private String peers;

    private int groupId = 1;

    private String certPath = "conf";

    private String hexPrivateKey;

    // 嵌套属性映射,作为一个嵌套属性注入
    @NestedConfigurationProperty
    private ContractConfig contract; // 选填属性

    private String frontUrl; // 选填属性
}
