package com.jzm.backme.mapper;

import com.jzm.backme.domain.House;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 房屋表 -- 存储房屋信息 Mapper 接口
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Mapper
public interface HouseMapper extends BaseMapper<House> {

}
