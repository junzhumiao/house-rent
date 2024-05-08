package com.jzm.backme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzm.backme.model.to.house.HouseQueryTo;
import com.jzm.backme.model.to.user.UserQueryTo;
import com.jzm.backme.domain.House;
import com.jzm.backme.mapper.HouseMapper;
import com.jzm.backme.service.HouseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzm.backme.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 房屋表 -- 存储房屋信息 服务实现类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements HouseService {

    @Override
    public List<House> getAll(HouseQueryTo houseQueryTo)
    {
        String linkPhone = houseQueryTo.getLinkPhone();
        LambdaQueryWrapper<House> queryWrapper = new LambdaQueryWrapper<House>()
                .eq(StringUtil.isNotEmpty(linkPhone),House::getLinkPhone,linkPhone);

        return this.list(queryWrapper);
    }

    @Override
    public boolean updateStatus(House house)
    {
        Long houseId = house.getHouseId();
        House newHouse = new House();

        newHouse.setHouseId(houseId);
        newHouse.setStatus(house.getStatus());
        LambdaQueryWrapper<House> updateWrapper = new LambdaQueryWrapper<House>()
                .eq(House::getHouseId, houseId);

        return this.update(newHouse,updateWrapper);
    }
}
