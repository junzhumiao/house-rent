package com.jzm.backme.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户 - 角色表
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-15
 */
@Getter
@Setter
@TableName("user_role")
@ApiModel(value = "UserRole对象", description = "用户 - 角色表")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;
}
