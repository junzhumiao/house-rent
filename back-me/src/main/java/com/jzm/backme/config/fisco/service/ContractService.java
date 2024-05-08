package com.jzm.backme.config.fisco.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.jzm.backme.config.fisco.contract.ContractResponse;
import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionBaseException;

import java.util.Arrays;
import java.util.List;

/**
 * ContractService 顶层类(基于fisco-sdk的合约交互)
 * // 目前能确定的是发送交易的调用者地址,就是client里面私钥指定的。
 *
 * @author: jzm
 * @date: 2024-03-22 11:08
 **/

public class ContractService
{
    protected String contractAddress;
    protected String owner;
    protected String ABI;
    protected String BIN;

    protected String SM_BIN;

    private Client client;


    protected AssembleTransactionProcessor txProcessor;


    protected ContractService()
    {

    }

    protected void initConfig(String ABI, String BIN, String contractAddress)
    {
        this.ABI = ABI;
        this.BIN = BIN;
        this.contractAddress = contractAddress;
    }

    protected ContractService client(Client client)
    {
        this.client = client;
        return this;
    }

    protected void init() throws Exception
    {
        this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, client.getCryptoSuite().getCryptoKeyPair());
    }

    /**
     * <p>1.returnMes == Success,resVals.size() == 0 ;mes:null,vals :null // 成功没返回值<p/>
     * <p>2.returnMes == Success，resVals.size() > 0; mes:null,vals: resVals // 成功有返回值<p/>
     * <p>3.returnMes != Success，resVals == null; mes: 错误消息,vals: null // 出异常<p/>
     *
     * @param funcName   函数名
     * @param funcParams 函数参数
     * @return
     */
    public ContractResponse sendCallGetValues(String from, String funcName, List<Object> funcParams)
    {
        CallResponse response = _setCallGetValues(from, funcName, funcParams);
        ContractResponse res = new ContractResponse();
        if (response.getReturnMessage().equals("Success"))
        {
            JSONArray resAry = JSONUtil.parseArray(response.getValues());
            if (resAry.size() != 0)
            {
                res.setVals(resAry);
            }
            return res;
        }
        // 调用合约错误
        res.setMes(response.getReturnMessage());
        return res;
    }

    public ContractResponse sendCallGetValues(String funcName, List<Object> funcParams)
    {
        return sendCallGetValues(owner, funcName, funcParams);
    }


    protected CallResponse _setCallGetValues(String from, String funcName, List<Object> funcParams)
    {
        try
        {
            return this.txProcessor.sendCall(from, contractAddress, ABI, funcName, funcParams);
        } catch (ABICodecException e) // 转化为自定义响应
        {
            CallResponse callResponse = new CallResponse();
            callResponse.setReturnMessage("ABI异常:" + e.getMessage());
            return callResponse;
        } catch (TransactionBaseException e)
        {
            CallResponse callResponse = new CallResponse();
            callResponse.setReturnMessage("合约异常:" + e.getMessage());
            return callResponse;
        }
    }


    public ContractService deploy(List<Object> params)
    {
        try
        {
            TransactionResponse response = _deploy(params);
            this.contractAddress = response.getContractAddress();
            this.owner = response.getTransactionReceipt().getFrom();
            return this;
        } catch (ABICodecException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    private TransactionResponse _deploy(List<Object> params) throws ABICodecException
    {
        return this.txProcessor.deployAndGetResponse(ABI, getBin(client.getCryptoSuite()), params);
    }


    private String getBin(CryptoSuite cryptoSuite)
    {
        return (cryptoSuite.getCryptoTypeConfig() == CryptoType.ECDSA_TYPE ? BIN : SM_BIN);
    }

    // set、get
    public void setSM_BIN(String SM_BIN)
    {
        this.SM_BIN = SM_BIN;
    }

    protected void setAbiBinCon(String abi, String bin, String contractAddress)
    {
        this.initConfig(abi, bin, contractAddress);
    }

    public String getContractAddress()
    {
        return contractAddress;
    }

    static class Builder
    {

        private Client client;
        private String ABI;
        private String BIN;
        private String SM_BIN;
        private String contractAddress;
        private String ownerAddress;

        private Builder()
        {
        }

        public static Builder create()
        {
            return new Builder();
        }

        public Builder client(Client client)
        {
            this.client = client;
            return this;
        }

        public Builder BIN(String BIN)
        {
            this.BIN = BIN;
            return this;
        }

        public Builder SM_BIN(String SM_BIN)
        {
            this.SM_BIN = SM_BIN;
            return this;
        }

        public Builder contractAddress(String conAdd)
        {
            this.contractAddress = conAdd;
            return this;
        }

        public Builder ownerAddress(String ownerAdd)
        {
            this.ownerAddress = ownerAdd;
            return this;
        }

        public Builder ABI(String ABI)
        {
            this.ABI = ABI;
            return this;
        }


        public ContractService execute() throws Exception
        {
            ContractService cs = new ContractService();
            cs.setAbiBinCon(this.ABI, this.BIN, this.contractAddress);
            cs.client(this.client);
            cs.SM_BIN = this.SM_BIN;
            cs.owner = this.ownerAddress;
            cs.init();
            return cs;
        }

    }

}