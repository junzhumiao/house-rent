package com.jzm.backme.model.to.house;

import lombok.Data;

/**
 * @author: jzm
 * @date: 2024-04-16 21:06
 **/

@Data
public class HousePassTo
{
    private Long houseId;
    private String oldPass;
    private String newPass;
}
