package com.jzm.backme.model.to.house;

import com.jzm.backme.model.to.base.BasePageQueryTo;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 20:51
 **/

@Data
public class HouseQueryTo extends BasePageQueryTo
{
    private String linkPhone;
}

