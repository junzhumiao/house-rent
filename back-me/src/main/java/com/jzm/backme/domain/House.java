package com.jzm.backme.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.jzm.backme.annotation.NotEmpty;
import com.jzm.backme.domain.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 房屋表 -- 存储房屋信息
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("house")
@ApiModel(value = "House对象", description = "房屋表 -- 存储房屋信息")
public class House extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "house_id", type = IdType.AUTO)
    private Long houseId;

    @ApiModelProperty("房屋地址")
    @NotEmpty(extra = "房屋地址")
    private String address;

    @ApiModelProperty("房东id")
    private Long landlordId;

    @ApiModelProperty("联系人")
    @NotEmpty(extra = "联系人")
    private String linkMan;

    @ApiModelProperty("联系电话")
    @NotEmpty(extra = "联系电话")
    private String linkPhone;

}
