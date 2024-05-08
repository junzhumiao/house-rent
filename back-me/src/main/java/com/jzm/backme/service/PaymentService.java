package com.jzm.backme.service;

import com.jzm.backme.domain.Payment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzm.backme.model.to.payment.PaymentQueryTo;

import java.util.List;

/**
 * <p>
 * 缴纳记录表 -- 记录租金缴纳信息 服务类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
public interface PaymentService extends IService<Payment> {

    List<Payment> getAll(PaymentQueryTo qt);

}
