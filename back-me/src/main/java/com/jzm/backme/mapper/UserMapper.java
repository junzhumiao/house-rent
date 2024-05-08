package com.jzm.backme.mapper;

import com.jzm.backme.domain.Role;
import com.jzm.backme.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 -- 存储用户信息 Mapper 接口
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    Role selectRole(Long userId);

}
