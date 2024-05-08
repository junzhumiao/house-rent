package com.jzm.backme.model.to;

import com.jzm.backme.annotation.NotEmpty;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-14 16:17
 **/

@Data
public class LoginTo
{
    @NotEmpty(extra = "密码")
    private String password;

    @NotEmpty(extra = "电话")
    private String phone;
}
