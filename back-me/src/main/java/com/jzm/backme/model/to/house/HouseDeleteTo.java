package com.jzm.backme.model.to.house;

import lombok.Data;

import java.util.List;

/**
 * @author: jzm
 * @date: 2024-04-14 20:02
 **/

@Data
public class HouseDeleteTo
{
    private List<Long> houseIds;
}
