package com.jzm.backme.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.jzm.backme.controller.base.BaseBaseEntity;
import com.jzm.backme.domain.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 缴纳记录表 -- 记录租金缴纳信息
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("payment")
@ApiModel(value = "Payment对象", description = "缴纳记录表 -- 记录租金缴纳信息")
public class Payment extends BaseBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("缴纳记录id")
    @TableId(value = "payment_id", type = IdType.AUTO)
    private Long paymentId;

    @ApiModelProperty("缴纳人账户地址")
    private String payer;

    @ApiModelProperty("缴纳人用户id")
    private Long payerId;

    @ApiModelProperty("合同序列码")
    private String contractNum;

}
