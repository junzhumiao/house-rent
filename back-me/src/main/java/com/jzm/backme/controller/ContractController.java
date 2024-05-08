package com.jzm.backme.controller;

import cn.hutool.core.util.IdUtil;
import com.jzm.backme.constant.Constant;
import com.jzm.backme.constant.UserConstant;
import com.jzm.backme.controller.base.BaseController;
import com.jzm.backme.domain.Contract;
import com.jzm.backme.domain.House;
import com.jzm.backme.domain.User;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.to.contract.ContractQueryTo;
import com.jzm.backme.model.vo.ContractVo;
import com.jzm.backme.service.*;
import com.jzm.backme.util.DateUtil;
import com.jzm.backme.util.NotEmptyUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.user.UserUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 合同表 -- 存储合同信息 前端控制器
 * </p>
 *
 * @author qhx2004
 * @since 2024-04-14
 */
// 预发布
// 签署未生效
// 签署已经生效
// 签署-停止
@RestController
@RequestMapping("/contract")
public class ContractController extends BaseController
{


    @Resource
    private ContractService contractService;

    @Resource
    private MainContractService mainContractService;

    @Resource
    private LoginService loginService;

    @Resource
    private UserService userService;

    @Resource
    private HouseService houseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public AjaxResult create(@RequestBody Contract contract)
    {
        loginService.checkLandlord();
        contract.setLandlordId(loginService.getUserId());

        String errPre = "添加合同失败:";
        String errMes = NotEmptyUtil.checkEmptyFiled(contract);
        if (StringUtil.isNotEmpty(errMes))
        {
            return error(errPre + errMes);
        }
        House house = houseService.getById(contract.getHouseId());
        if (StringUtil.isEmpty(house))
        {
            return error(errPre + "输入的房屋编号不存在");
        }
        String uuid = IdUtil.fastSimpleUUID();
        contract.setContractNum(uuid);
        boolean end = false;
        while (!end)
        { // 唯一序列号
            end = contractService.save(contract);
        }
        return success();
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.POST)
    public AjaxResult getAll(@RequestBody ContractQueryTo contractQueryTo)
    {
        int page = contractQueryTo.getPage();
        int pageSize = contractQueryTo.getPageSize();
        List<ContractVo> contractVos = startOrderPage(page, pageSize, ContractVo.class, () ->
        {
            if (loginService.isLandlord())
            { // 房东只能看到自己的合同
                contractQueryTo.setLandlordId(loginService.getUserId());
            }
            contractService.getAll(contractQueryTo);
        });
        return getAll(contractVos, contractQueryTo.getType());
    }

    private AjaxResult getAll(List<ContractVo> contractVos, String type)
    {
        for (ContractVo contractVo : contractVos)
        {
            Long contractId = contractVo.getContractId();
            mainContractService.getContract(contractId, contractVo);
        }
        if (StringUtil.isEmpty(type))
        {
            return toAjax(contractVos);
        }
        ArrayList<ContractVo> resList = new ArrayList<>();
        // 格局要求响应类型,获得交易参数
        for (ContractVo contractVo : contractVos)
        {
            String sign = contractVo.getSign();
            String begin = contractVo.getBegin();
            String stop = contractVo.getStop();
            // 预发布
            if (StringUtil.equals(Constant.Contract_0, type) && StringUtil.equals(sign, Constant.Zero))
            {
                resList.add(contractVo);
            }
            // 签署-未生效
            if (StringUtil.equals(Constant.Contract_1, type) && StringUtil.equals(sign, Constant.One) &&
                    StringUtil.equals(begin, Constant.Zero))
            {
                resList.add(contractVo);
            }
            // 签署-生效
            if (StringUtil.equals(Constant.Contract_2, type) && StringUtil.equals(begin, Constant.One)
                    && StringUtil.equals(stop, Constant.Zero))
            {
                resList.add(contractVo);
            }
            // 签署-终止
            if (StringUtil.equals(Constant.Contract_3, type) &&
                    StringUtil.equals(stop, Constant.One))
            {
                resList.add(contractVo);
            }
        }
        return success(resList, resList.size());
    }


    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    @ApiOperation("签署合同")
    public AjaxResult signContract(@RequestBody ContractVo contractVo)
    {
        loginService.checkTenant();
        String errPre = "签署合同失败：";
        String errMes = NotEmptyUtil.checkEmptyFiled(contractVo, "houseId");
        if (StringUtil.isNotEmpty(errMes))
        {
            return error(errPre + errMes);
        }
        if (!UserUtil.verifyAccount(contractVo.getTenant()))
        {
            return error(errPre + "租客地址不是合法区块链网络地址");
        }
        if (!StringUtil.equals(contractVo.getRent(), contractVo.getEarnest()))
        {
            return error(errPre + "规定保证金应等于租金");
        }
        LocalDateTime beginEndTime = contractVo.getBeginEndTime();
        LocalDateTime endTime = DateUtil.addTime(LocalDateTime.now(), 30);
        if (DateUtil.compare(beginEndTime, endTime) < 0)
        {
            return error("合同结束日期,至少大于当时间1个月");
        }
        // 设置房东地址
        User landlord = userService.getById(contractVo.getLandlordId());
        contractVo.setLandlord(landlord.getUser());
        contractVo.setSignTime(LocalDateTime.now());
        contractVo.setSign(UserConstant.EXCEPTION);
        mainContractService.signContract(contractVo);
        boolean end = contractService.updateById(contractVo);
        return toAjax(end);
    }


    @RequestMapping(value = "/tenantPrestore", method = RequestMethod.POST)
    @ApiOperation("租客预存赔偿金")
    public AjaxResult tenantPrestore(@RequestBody Contract contract)
    {
        loginService.checkTenant();
        String address = loginService.getAccountAddress();
        mainContractService.tenantPrestore(address, contract.getContractId());
        return success();
    }


    @RequestMapping(value = "/landlordPrestore", method = RequestMethod.POST)
    @ApiOperation("房东预存赔偿金")
    public AjaxResult landlordPrestore(@RequestBody Contract contract)
    {
        loginService.checkLandlord();
        String address = loginService.getAccountAddress();
        mainContractService.landlordPrestore(address, contract.getContractId());
        return success();
    }

    @RequestMapping(value = "/backPrestoreMoney", method = RequestMethod.POST)
    @ApiOperation("退回预存的钱")
    public AjaxResult backPrestoreMoney(@RequestBody Contract contract)
    {
        String address = loginService.getAccountAddress();
        mainContractService.backPrestoreMoney(address, contract.getContractId());
        return success();
    }


    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    @ApiOperation("终止合同")
    public AjaxResult stopContract(@RequestBody Contract contract)
    {
        String errPre = "终止合同失败：";
        if (!loginService.isTenant() && !loginService.isTenant())
        {
            return error(errPre + "操作者只能是租客/房东");
        }
        String address = loginService.getAccountAddress();
        if (loginService.isTenant())
        {
            mainContractService.stopContractTenant(address, contract.getContractId());
        } else
        {
            mainContractService.stopContractLandlord(address, contract.getContractId());
        }
        // 报错下面走不了
        Contract newCon = new Contract();
        newCon.setContractId(contract.getContractId());
        newCon.setStopTime(LocalDateTime.now());
        contractService.updateById(newCon);
        return success();
    }


}
