package com.jzm.backme.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *  角色表
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Getter
@Setter
@TableName("role")
@ApiModel(value = "Role对象", description = " 角色表")
@AllArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;

    private String roleName;
}
