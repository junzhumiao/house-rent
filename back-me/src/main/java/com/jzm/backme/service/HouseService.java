package com.jzm.backme.service;

import com.jzm.backme.model.to.house.HouseQueryTo;
import com.jzm.backme.model.to.user.UserQueryTo;
import com.jzm.backme.domain.House;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 房屋表 -- 存储房屋信息 服务类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
public interface HouseService extends IService<House> {

    List<House> getAll(HouseQueryTo houseQueryTo);

    boolean updateStatus(House house);

}
