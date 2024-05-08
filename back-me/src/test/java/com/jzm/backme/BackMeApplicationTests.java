package com.jzm.backme;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jzm.backme.config.fisco.FiscoConfig;
import com.jzm.backme.domain.User;
import com.jzm.backme.service.MainContractService;
import com.jzm.backme.service.UserService;
import com.jzm.backme.util.redis.RedisCache;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.signature.SignatureResult;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionWithRemoteSignProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.fisco.bcos.sdk.transaction.signer.RemoteSignCallbackInterface;
import org.fisco.bcos.sdk.transaction.signer.RemoteSignProviderInterface;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;

@SpringBootTest
class BackMeApplicationTests
{


    @Resource
    private FiscoConfig fiscoConfig;

    @Resource
    private MainContractService mainContractService;


    AssembleTransactionWithRemoteSignProcessor txProcessor;

    @Test
    void fiscoConnetcTest()
    {
        ArrayList<Object> params = new ArrayList<>();
        String calAdd = "0xa5c4f1b07f513a586b19be93fd44966bcbd5d2c5";
        params.add(calAdd);
        params.add(2);
        String add1 = "0x3269c74547b619586160e462ee1d47366e2a7c64";
        System.out.println(12);

    }

    @Resource
    private Client client;


    @Test
    void sendTransactionTest() throws Exception
    {
        AssembleTransactionProcessor assembleTransactionProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(client, client.getCryptoSuite().getCryptoKeyPair());
        String address = client.getCryptoSuite().getCryptoKeyPair().getAddress();
        System.out.println(12);

    }

    @Resource
    private UserService userService;

    @Resource
    private RedisCache redisCache;


    @Test
    void Test() throws Exception
    {
        User user = new User();
        user.setUserId(1L);
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUserId, user.getUserId());
        boolean update = userService.update(user, wrapper);
    }


    @Test
    void   redisTest() throws Exception
    {
        redisCache.setCacheObject("qhx",12);
    }






}
