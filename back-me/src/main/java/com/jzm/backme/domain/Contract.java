package com.jzm.backme.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.jzm.backme.annotation.NotEmpty;
import com.jzm.backme.controller.base.BaseBaseEntity;
import com.jzm.backme.domain.base.BaseEntity;
import com.jzm.backme.util.user.UserUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 合同表 -- 存储合同信息
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("contract")
@ApiModel(value = "Contract对象", description = "合同表 -- 存储合同信息")
public class Contract extends BaseBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("合同id")
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Long contractId;

    @NotEmpty(extra = "房屋编号")
    private Long houseId;

    private Long landlordId;

    @ApiModelProperty("0 未签署 1签署")
    private String sign;

    @ApiModelProperty("合同编号")
    private String contractNum;

    @ApiModelProperty("签署日期")
    private LocalDateTime signTime;

    @ApiModelProperty("停止日期")
    private LocalDateTime stopTime;


    public static void main(String[] args)
    {
        System.out.println(UserUtil.cryptPass("123458"));
    }
}
