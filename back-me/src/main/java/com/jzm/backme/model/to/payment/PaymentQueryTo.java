package com.jzm.backme.model.to.payment;

import com.jzm.backme.model.to.base.BasePageQueryTo;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-17 14:57
 **/

@Data
public class PaymentQueryTo extends BasePageQueryTo
{
    private String payer; // 缴纳账户地址
    private String contractNum; // 合同序列号
}
