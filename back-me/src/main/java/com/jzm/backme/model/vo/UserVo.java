package com.jzm.backme.model.vo;

import com.jzm.backme.domain.User;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-14 19:07
 **/

@Data
public class UserVo extends User
{
    private Long roleId;
    private String roleName;
    private int balance;
}
