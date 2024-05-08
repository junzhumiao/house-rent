package com.jzm.backme.model.to.user;

import com.jzm.backme.domain.User;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-14 20:29
 **/

@Data
public class UserAddTo extends User
{
    private Long roleId;
}
