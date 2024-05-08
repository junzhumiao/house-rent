package com.jzm.backme.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import com.jzm.backme.constant.CacheConstant;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.util.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * 验证码业务类
 *
 * @author: jzm
 * @date: 2024-02-28 12:12
 **/

@Component
public class CaptchaService
{

    @Autowired
    RedisCache redisCache;

    public AjaxResult getCaptcha(){
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(128, 50);
        String uuid = IdUtil.randomUUID();
        // 验证码缓存到redis
        String codeKey = CacheConstant.CAPTCHA_CODE_KEY + uuid;
        redisCache.setCacheObject(codeKey,lineCaptcha.getCode(),60, TimeUnit.SECONDS);
        HashMap<String,String> result = new HashMap<>();
        result.put("codeUrl",lineCaptcha.getImageBase64Data());
        result.put("codeKey",uuid);
        return AjaxResult.success(result);
    }

}
