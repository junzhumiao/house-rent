package com.jzm.backme.model.to.house;

import com.jzm.backme.annotation.NotEmpty;
import com.jzm.backme.domain.House;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 20:44
 **/

@Data
public class HouseAddTo extends House
{
    @NotEmpty(extra = "房屋密码")
    private String password;
}
