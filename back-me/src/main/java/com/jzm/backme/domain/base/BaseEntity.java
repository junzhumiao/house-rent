package com.jzm.backme.domain.base;

import com.jzm.backme.controller.base.BaseBaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public abstract class BaseEntity extends BaseBaseEntity
{

    @ApiModelProperty("状态")
    private String status;

}

