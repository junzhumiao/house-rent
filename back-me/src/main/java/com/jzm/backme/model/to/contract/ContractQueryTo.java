package com.jzm.backme.model.to.contract;

import com.jzm.backme.model.to.base.BasePageQueryTo;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 22:12
 **/

@Data
public class ContractQueryTo extends BasePageQueryTo
{
    private Long landlordId;
    private String type;
    // 预发布
    // 签署未生效
    // 签署已经生效
    // 签署-过期
    // 签署-停止
}
