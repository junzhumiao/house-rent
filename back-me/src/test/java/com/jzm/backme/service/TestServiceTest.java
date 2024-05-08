package com.jzm.backme.service;

import org.fisco.bcos.sdk.abi.ABICodec;
import org.fisco.bcos.sdk.abi.ABICodecException;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.builder.TransactionBuilderService;
import org.fisco.bcos.sdk.transaction.codec.encode.TransactionEncoderService;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionWithRemoteSignProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.fisco.bcos.sdk.transaction.model.exception.TransactionBaseException;
import org.fisco.bcos.sdk.transaction.model.po.RawTransaction;
import org.fisco.bcos.sdk.transaction.pusher.TransactionPusherService;
import org.fisco.bcos.sdk.transaction.signer.RemoteSignCallbackInterface;
import org.fisco.bcos.sdk.transaction.signer.RemoteSignProviderInterface;
import org.fisco.bcos.sdk.utils.Hex;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * @author: jzm
 * @date: 2024-04-26 08:30
 **/

@SpringBootTest
public class TestServiceTest
{

    @Autowired
    private Client client;


    /**
     * 外部签名调用合约(除了组成调用的处理器差不多,其余感觉用法上没啥差别)
     */
    @Test
    public void test() throws ABICodecException, TransactionBaseException
    {
        CustomExternalSignAndCallBack externalSign = new CustomExternalSignAndCallBack();

        AssembleTransactionWithRemoteSignProcessor asWt =
                TransactionProcessorFactory.createAssembleTransactionWithRemoteSignProcessor(
                        client, client.getCryptoSuite().getCryptoKeyPair(), "Test", externalSign);

        TransactionBuilderService builder = new TransactionBuilderService(client);
        TransactionEncoderService encoder = new TransactionEncoderService(client.getCryptoSuite());
        TransactionPusherService pusher = new TransactionPusherService(client);
        ABICodec abiCodec = new ABICodec(client.getCryptoSuite());
        // 生成行交易
        RawTransaction rawTransaction = builder.createTransaction(null,
                abiCodec.encodeConstructor("abi", "bin", new ArrayList<>()), new BigInteger(String.valueOf(1)), BigInteger.valueOf(1L));

        // 对rawTransaction进行编码
        asWt.deployAsync(rawTransaction,externalSign);
        // 对交易进行签名
        byte[] encode = encoder.encode(rawTransaction, null);
        byte[] hashEncode = client.getCryptoSuite().hash(encode);
        // 签名服务对交易进行签名
        // 拼接未签名交易与签名,
        // 发送带有交易的签名
        TransactionReceipt transactionReceipt = pusher.push(Hex.toHexString(hashEncode));


        // 部署、交易、查询
        TransactionResponse response = asWt.deployByContractLoader("Test", new ArrayList<>());
        // 交易查询
        TransactionResponse transactionResponse = asWt.sendTransactionAndGetResponse("to", "abi", "funcName", new ArrayList<>());
        // 调用合约查询
        CallResponse callResponse = asWt.sendCallByContractLoader("Test", "address", "get", new ArrayList<>());
        // 定义回调类
        BigInteger call = new BigInteger(String.valueOf("12"));



    }


}


class CustomExternalSignAndCallBack implements RemoteSignProviderInterface, RemoteSignCallbackInterface
{
    AssembleTransactionWithRemoteSignProcessor assembleTransactionWithRemoteSignProcessor;
    RawTransaction rawTransaction;

    public CustomExternalSignAndCallBack(){

    }

    public CustomExternalSignAndCallBack(
            AssembleTransactionWithRemoteSignProcessor asWt,
            RawTransaction rawTransaction
    ) {
        this.assembleTransactionWithRemoteSignProcessor = asWt;
        this.rawTransaction = rawTransaction;
    }



    // TODO 处理外部签名
    @Override
    public int handleSignedTransaction(SignatureResult signature)
    {
        System.out.println(System.currentTimeMillis() + " SignatureResult: " + new String(signature.getSignatureBytes()));

        // 在发送叫之前,对数据进行签名
        CompletableFuture<TransactionReceipt> signAndPush = // 完成
                assembleTransactionWithRemoteSignProcessor.signAndPush(rawTransaction, signature.getSignatureBytes());
        return 0;
    }


    // TODO 调用外部签名
    @Override
    public SignatureResult requestForSign(byte[] dataToSign, int cryptoType)
    {
        return null;
    }

    @Override
    public void requestForSignAsync(byte[] dataToSign, int cryptoType, RemoteSignCallbackInterface callback)
    {

    }
}
