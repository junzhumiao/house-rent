package com.jzm.backme.model.vo;

import com.jzm.backme.domain.House;
import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 21:25
 **/

@Data
public class HouseVo extends House
{
    private String password;
}
