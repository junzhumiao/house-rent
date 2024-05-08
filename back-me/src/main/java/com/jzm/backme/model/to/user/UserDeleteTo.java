package com.jzm.backme.model.to.user;

import lombok.Data;

import java.util.List;

/**
 * @author: jzm
 * @date: 2024-04-14 19:24
 **/

@Data
public class UserDeleteTo
{
    private List<Long> userIds;
}
