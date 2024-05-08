package com.jzm.backme.service;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.jzm.backme.domain.Contract;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jzm.backme.model.to.contract.ContractQueryTo;

import java.util.List;

/**
 * <p>
 * 合同表 -- 存储合同信息 服务类
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
public interface ContractService extends IService<Contract> {

    List<Contract> getAll(ContractQueryTo contractQueryTo);

    Contract getOne(SFunction<Contract,?> column,Object val);



}
