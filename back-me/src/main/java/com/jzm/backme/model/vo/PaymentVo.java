package com.jzm.backme.model.vo;

import com.jzm.backme.domain.Payment;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-17 15:01
 **/

@Data
public class PaymentVo extends Payment
{
    private int money; // 缴纳金额
}
