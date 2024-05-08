package com.jzm.backme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzm.backme.domain.Payment;
import com.jzm.backme.mapper.PaymentMapper;
import com.jzm.backme.model.to.payment.PaymentQueryTo;
import com.jzm.backme.service.PaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzm.backme.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 缴纳记录表 -- 记录租金缴纳信息 服务实现类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, Payment> implements PaymentService {

    @Override
    public List<Payment> getAll(PaymentQueryTo qt)
    {
        String payer = qt.getPayer();
        String contractNum = qt.getContractNum();
        LambdaQueryWrapper<Payment> queryWrapper = new LambdaQueryWrapper<Payment>()
                .eq(StringUtil.isNotEmpty(payer), Payment::getPayer, payer)
                .eq(StringUtil.isNotEmpty(contractNum),Payment::getContractNum,contractNum);


        return this.list(queryWrapper);
    }
}
