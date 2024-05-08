package com.jzm.backme.service;

import cn.hutool.json.JSONArray;
import com.jzm.backme.config.fisco.contract.ContractResponse;
import com.jzm.backme.config.fisco.service.WeFrontService;
import com.jzm.backme.model.to.payment.PaymentAddTo;
import com.jzm.backme.model.vo.ContractVo;
import com.jzm.backme.model.vo.PaymentVo;
import com.jzm.backme.util.Convert;
import com.jzm.backme.util.DateUtil;
import io.swagger.models.auth.In;
import org.fisco.bcos.sdk.abi.datatypes.Int;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BcosBlock;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 主合约交互类
 *
 * @author: jzm
 * @date: 2024-04-15 22:14
 **/

@Component
public class MainContractService
{

    @Resource
    private WeFrontService weFrontService;

    public void addPerson(String account, Long role)
    {
        weFrontService.commonWrite("addPerson", Arrays.asList(account, role));
    }

    public void addBalance(String account, int amount)
    {
        weFrontService.commonWrite("addBalance", Arrays.asList(account, amount));
    }

    public int getBalance(String account)
    {
        ContractResponse response = weFrontService.commonRead("getPerson", Arrays.asList(account));
        String hex = response.getVals().getStr(0);
        return Convert.hexToInt(hex);
    }

    public void addHouse(String address, Long houseId, String password)
    {
        weFrontService.commonWrite(address, "addHouse", Arrays.asList(houseId, password));
    }

    public void changeHousePass(Long houseId, String password)
    {
        weFrontService.commonWrite("changeHousePass", Arrays.asList(houseId, password));
    }

    public String getHouseAdmin(Long houseId)
    {
        ContractResponse response = weFrontService.commonRead("getHouseAdmin", Arrays.asList(houseId));
        return response.getVals().getStr(1);
    }

    // 租客获取
    public String getHouse(Long houseId, Long contractId)
    {
        ContractResponse response = weFrontService.commonRead("getHouse", Arrays.asList(houseId, contractId));
        return response.getVals().getStr(1);
    }

    public void getContract(Long contractId, ContractVo contractVo)
    {
        ContractResponse response = weFrontService.commonRead("getContract", Arrays.asList(contractId));
        JSONArray vals = response.getVals();
        contractVo.setLandlord(vals.getStr(1));
        contractVo.setTenant(vals.getStr(2));
        contractVo.setEarnest(Convert.hexToInt(vals.getStr(3)));
        contractVo.setRent(Convert.hexToInt(vals.getStr(4)));
        contractVo.setBeginStartTime(DateUtil.parseLDT(vals.getStr(5)));
        contractVo.setBeginEndTime(DateUtil.parseLDT(vals.getStr(6)));
        contractVo.setIsTenEarnest(Convert.boolConv(vals.getBool(7)));
        contractVo.setIsTenRent(Convert.boolConv(vals.getBool(8)));
        contractVo.setIsLanEarnest(Convert.boolConv(vals.getBool(9)));
        contractVo.setBegin(Convert.boolConv(vals.getBool(10)));
        contractVo.setStop(Convert.boolConv(vals.getBool(11)));
        contractVo.setSignEndTime(DateUtil.parseLDT(vals.getStr(12)));
    }

    public void signContract(ContractVo contractVo)
    {
        Long contractId = contractVo.getContractId();
        String landlord = contractVo.getLandlord();
        String tenant = contractVo.getTenant();
        Integer earnest = contractVo.getEarnest();
        Long beginEndTime = DateUtil.datetimeToTimestamp(contractVo.getBeginEndTime()) / DateUtil.S;

        weFrontService.commonWrite("signContract", Arrays.asList(
                contractId, landlord, tenant, earnest, beginEndTime
        ));
    }

    public void tenantPrestore(String address, Long contractId)
    {
        weFrontService.commonWrite(address, "tenantPrestore", Arrays.asList(address, contractId));
    }

    public void landlordPrestore(String address, Long contractId)
    {
        weFrontService.commonWrite(address, "landlordPrestore", Arrays.asList(address, contractId));
    }

    public void backPrestoreMoney(String address, Long contractId)
    {
        weFrontService.commonWrite(address, "backPrestoreMoney", Arrays.asList(contractId, address));
    }

    public void stopContractTenant(String address, Long contractId)
    {
        weFrontService.commonWrite(address, "stopContractTenant", Arrays.asList(
                contractId, address
        ));
    }

    public void payRent(String address, PaymentAddTo pt)
    {
        weFrontService.commonWrite(address, "payRent", Arrays.asList(
                pt.getPayerId(),
                pt.getContractId(),
                pt.getMoney()
        ));
    }

    public void getPayRent(Long paymentId, PaymentVo paymentVo)
    {
        ContractResponse response = weFrontService.commonRead("getPayRent", Arrays.asList(paymentId));
        JSONArray vals = response.getVals();
        paymentVo.setMoney(Convert.hexToInt(vals.getStr(2)));
    }

    public void stopContractLandlord(String address, Long contractId)
    {
        weFrontService.commonWrite(address, "stopContractLandlord", Arrays.asList(
                contractId, address
        ));
    }


}
