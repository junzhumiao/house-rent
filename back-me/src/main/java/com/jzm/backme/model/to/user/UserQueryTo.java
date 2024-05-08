package com.jzm.backme.model.to.user;

import com.jzm.backme.model.to.base.BasePageQueryTo;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-14 20:05
 **/

@Data
public class UserQueryTo extends BasePageQueryTo
{
    private String phone;
}
