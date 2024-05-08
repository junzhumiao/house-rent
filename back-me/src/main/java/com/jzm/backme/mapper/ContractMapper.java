package com.jzm.backme.mapper;

import com.jzm.backme.domain.Contract;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 合同表 -- 存储合同信息 Mapper 接口
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Mapper
public interface ContractMapper extends BaseMapper<Contract> {

}
