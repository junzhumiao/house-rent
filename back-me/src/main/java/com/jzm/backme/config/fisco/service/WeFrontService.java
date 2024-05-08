package com.jzm.backme.config.fisco.service;

import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jzm.backme.config.fisco.ContractConfig;
import com.jzm.backme.config.fisco.FiscoConfig;
import com.jzm.backme.config.fisco.contract.ContractErrCode;
import com.jzm.backme.config.fisco.contract.ContractResponse;
import com.jzm.backme.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 基于webase-front的合约交互
 *
 * @author: jzm
 * @date: 2024-04-16 15:53
 **/

@Component
@Slf4j
public class WeFrontService
{
    public static final String callErr_pre = "Call contract return error:";
    public static final String network_err = "区块链网络交互异常"; // 除了合约交互异常
    public static final String tran_message = "message";
    public static final String tran_mes_suc = "Success";

    @Autowired
    private FiscoConfig fiscoConfig;

    private String commonReq(String userAddress, String funcName, List funcParam)
    {
        JSONObject data = new JSONObject();
        FiscoConfig f = fiscoConfig;
        ContractConfig c = f.getContract();
        JSONArray abiJSON = JSONUtil.parseArray(c.getAbi());
        data.putOpt("groupId", f.getGroupId());
        data.putOpt("user", userAddress);
        data.putOpt("contractName", c.getName());
        data.putOpt("funcName", funcName);
        data.putOpt("funcParam", funcParam);
        data.putOpt("contractAddress", c.getAddress());
        data.putOpt("contractAbi", abiJSON);

        HttpResponse response = HttpUtil.createPost(f.getFrontUrl()).header(Header.CONTENT_TYPE, "application/json; charset=utf-8").
                body(data.toString()).execute();
        return response.body();
    }

    public ContractResponse sendTranGetValues(String to, String funcName, List funcParam)
    {
        String res = commonReq(to, funcName, funcParam);
        JSONObject jsonObj = JSONUtil.parseObj(res);
        ContractResponse cr = new ContractResponse();

        // 错误消息
        if (jsonObj.containsKey(tran_message) && jsonObj.getStr(tran_message).equals(tran_mes_suc))
        {
            String output = jsonObj.getStr("output");
            if (StringUtil.isNotEmpty(output))
            {
                try
                {
                    JSONArray resAry = JSONUtil.parseArray(output);
                    cr.setVals(resAry);
                } catch (Exception e)
                { // 1个参数
                    cr.setVals(new JSONArray().put(output));
                }
            }
            return cr;
        }
        // 失败消息
        if (jsonObj.containsKey(tran_message))
        {
            cr.setMes(jsonObj.getStr(tran_message));
            return cr;
        }
        // 其余,没有返回交易凭证,但是报错了
        cr.setMes(network_err);
        log.error(JSONUtil.toJsonStr(jsonObj)); // 打印日志
        return cr;
    }

    public ContractResponse sendTranGetValues(String funcName, List funcParam)
    {
        String owner = fiscoConfig.getContract().getOwner();
        return sendTranGetValues(owner, funcName, funcParam);
    }

    public ContractResponse sendCallGetValues(String to, String funcName, List funcParam)
    {
        String res = commonReq(to, funcName, funcParam);
        ContractResponse cr = new ContractResponse();
        try
        {
            JSONArray jsonAry = JSONUtil.parseArray(res);
            if (jsonAry.size() == 1 && jsonAry.getStr(0).contains(callErr_pre))
            { // 合约异常
                cr.setMes(jsonAry.getStr(0).substring(callErr_pre.length() + 1));
            }
            if (jsonAry.size() != 0)
            {
                cr.setVals(jsonAry);
            }
        } catch (Exception e)
        { // 网络交互异常
            cr.setMes(network_err);
            log.error(JSONUtil.toJsonStr(e));
        }
        return cr;
    }

    public ContractResponse sendCallGetValues(String funcName, List funcParam)
    {
        String owner = fiscoConfig.getContract().getOwner();
        return sendCallGetValues(owner, funcName, funcParam);
    }


    public void commonWrite(String funcName, List funcParams)
    {
        String owner = fiscoConfig.getContract().getOwner();
        commonWrite(owner, funcName, funcParams);
    }

    public void commonWrite(String callAdd, String funcName, List funcParams)
    {
        ContractResponse response;
        if (StringUtil.isNotEmpty(callAdd))
        {
            response = sendTranGetValues(callAdd, funcName, funcParams);
        } else
        {
            response = sendTranGetValues(funcName, funcParams);
        }
        if (StringUtil.isNotEmpty(response.getMes()))
        {
            throw ContractErrCode.get(response.getMes());
        }
    }

    public ContractResponse commonRead(String callAdd, String funcName, List funcParams)
    {
        ContractResponse response;
        if (StringUtil.isNotEmpty(callAdd))
        {
            response = sendCallGetValues(callAdd, funcName, funcParams);
        } else
        {
            response = sendCallGetValues(funcName, funcParams);
        }
        if (StringUtil.isNotEmpty(response.getMes()))
        {
            throw ContractErrCode.get(response.getMes());
        }
        return response;
    }

    public ContractResponse commonRead(String funcName, List funcParams)
    {
        String owner = fiscoConfig.getContract().getOwner();
        return commonRead(owner, funcName, funcParams);
    }
}
