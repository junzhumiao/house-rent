package com.jzm.backme.model.to.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-18 10:46
 **/

@Data
@AllArgsConstructor
public class UserPassTo
{
    private String  password;
    private String newPassword;
}
