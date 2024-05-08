package com.jzm.backme.mapper;

import com.jzm.backme.domain.Payment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 缴纳记录表 -- 记录租金缴纳信息 Mapper 接口
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Mapper
public interface PaymentMapper extends BaseMapper<Payment> {

}
