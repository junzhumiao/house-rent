package com.jzm.backme.model.to.user;

import com.jzm.backme.domain.User;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 19:58
 **/

@Data
public class UserResTo extends User
{
    private int balance;
}
