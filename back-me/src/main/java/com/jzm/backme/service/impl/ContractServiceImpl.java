package com.jzm.backme.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.jzm.backme.constant.Constant;
import com.jzm.backme.domain.Contract;
import com.jzm.backme.mapper.ContractMapper;
import com.jzm.backme.model.to.contract.ContractQueryTo;
import com.jzm.backme.service.ContractService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzm.backme.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 合同表 -- 存储合同信息 服务实现类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
@Service
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {

    @Override
    public List<Contract> getAll(ContractQueryTo contractQueryTo)
    {
        Long landlordId = contractQueryTo.getLandlordId();
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<Contract>()
                .eq(StringUtil.isNotEmpty(landlordId), Contract::getLandlordId, landlordId);

        return this.list(queryWrapper);
    }

    @Override
    public Contract getOne(SFunction<Contract, ?> column, Object val)
    {
        LambdaQueryWrapper<Contract> queryWrapper = new LambdaQueryWrapper<Contract>()
                .eq(column, val);
        return this.getOne(queryWrapper);
    }



}
