package com.jzm.backme.config.fisco;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 与指定合约交互的配置
 */
@Data
@ConfigurationProperties(prefix = "fisco.contract")
public class ContractConfig
{
    private String address;
    private String name;
    private String owner;
    private String bin;
    private String abi;
}
