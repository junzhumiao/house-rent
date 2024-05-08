package com.jzm.backme.service;

import com.jzm.backme.config.fisco.contract.ContractResponse;
import com.jzm.backme.config.fisco.service.WeFrontService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

/**
 * @author: jzm
 * @date: 2024-04-16 16:38
 **/

@SpringBootTest
public class WebaseFrontServiceTest
{

    @Autowired
    private WeFrontService weFrontService;

    @Test
    public void testWeFront()
    {
        ContractResponse setAge = weFrontService.sendTranGetValues("setAge", Arrays.asList(20111));
        System.out.println(12);
        ContractResponse getAge = weFrontService.sendCallGetValues("getAge", Arrays.asList(2));
        System.out.println(12);
    }
}
