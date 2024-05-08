package com.jzm.backme.model.to.user;

import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-20 21:26
 **/

@Data
public class PassTo
{
    private String oldPassword;
    private String newPassword;
}
