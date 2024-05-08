package com.jzm.backme.model.to.payment;

import com.jzm.backme.domain.Payment;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-17 14:38
 **/

@Data
public class PaymentAddTo extends Payment
{
    private int money;
    private Long contractId;
}
