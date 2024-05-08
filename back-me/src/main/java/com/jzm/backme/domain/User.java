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
 * 用户表 -- 存储用户信息
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("user")
@ApiModel(value = "User对象", description = "用户表 -- 存储用户信息")
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    private String username;

    @ApiModelProperty("用户区块链账户地址")
    @NotEmpty(extra = "区块链账户地址")
    private String user;

    @NotEmpty(extra = "用户电话")
    private String phone;

    @ApiModelProperty("用户密码")
    @NotEmpty(extra = "密码")
    private String password;

    private String sex;

}
