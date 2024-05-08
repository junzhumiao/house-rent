package com.jzm.backme.config.fisco.contract;


import com.jzm.backme.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jzm
 * @date: 2024-04-16 19:34
 **/

public class ContractErrCode
{
    public static final Map<String, String> errCode = new HashMap();

    {
        errCode.put("40001", "账户地址不能为空地址");
        errCode.put("40002", "参数租客地址与合同租客地址不相等");
        errCode.put("40003", "参数租客地址与合同租客地址不相等");
        errCode.put("40004", "房东终止合同失败:租客实际缴租金 + 保证金 < 应缴纳租金");
        errCode.put("40005", "租客终止合同失败:租客实际缴租金 < 应缴纳租金 + 2个月租金");
        errCode.put("40006", "参数房东地址与合同房东地址不相等");
        errCode.put("40007", "账户地址不能是空地址");
        errCode.put("40008", "账户月不足以支付money");
        errCode.put("40010", "");
        errCode.put("40011", "签署合同不存在");
        errCode.put("40012", "签署合同已经过了签署时间");
        errCode.put("40013", "这个Person account 不存在");
        errCode.put("40014", "调用者必须是房东角色");
        errCode.put("40015", "调用者必须是租客角色");
        errCode.put("40016", "调用者必须是管理员");
    }

    // 根据错误码匹配
    public static ContractException get(String code)
    {
        String val = errCode.get(code);
        if (StringUtil.isEmpty(val))
        { // 匹配不到,直接返回
            return new ContractException(code);
        }
        return new ContractException(val);
    }

    public static ContractException get(ContractResponse contractResponse)
    {
        String code = contractResponse.getMes();
        return get(code);
    }
}
