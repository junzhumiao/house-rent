package com.jzm.backme.util;

import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWTUtil;
import com.jzm.backme.constant.Constant;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * token工具类
 *
 * @author: jzm
 * @date: 2024-02-26 21:31
 **/

public class TokenUtil extends JWTUtil
{
    private static String CREATE_TIME = "create_time";

    private static String EXPIRE_TIME = "expire_time";


    private static final String SECRET = "abcdefghijklmnopqrstuvwxyz";

    protected static final long MILLIS_SECOND = 1000; // 单位 1s

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND; // 单位 1 min

    protected static final Long MILLIS_HOUR = 60 * MILLIS_MINUTE; // 单位 1 hour
    protected static final Long MILLIS_DAY = 24 * MILLIS_HOUR; // 单位 1 day



    // 默认一天后过期
    public static String createToken(Long userId) {
        return createToken(userId,MILLIS_DAY);
    }

    public static String createTokenByPassWord(String password,long expireTime) {
        return createToken(Constant.PASSWORD,password,expireTime);
    }

    public static String createToken(Long userId, long expireTime)
    {
      return createToken(Constant.USER_ID,userId,expireTime);
    }

    // 创建令牌
   public static String  createToken(String key,Object val, long expireTime){
       Map<String, Object> claims = new HashMap<>();
       claims.put(key,val);
       claims.put(CREATE_TIME, System.currentTimeMillis());
       claims.put(EXPIRE_TIME, expireTime * MILLIS_MINUTE + System.currentTimeMillis());
       return createToken(claims,SECRET);
   }


    private static String createToken(Map<String, Object> claims,String key)
    {
        try
        {
            return TokenUtil.createToken(claims,key.getBytes(Constant.UTF_8));
        } catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }

    public static String parseTokenGetUserId(String token) {
        return parseTokenGetClaims(token, Constant.USER_ID).toString();
    }

    public static Object parseTokenGetClaims(String token,String name) {
        return  parseToken(token).getPayload(name);
    }

    /** 校验token是否过期
     *
     * @param token
     * @return
     */
    public static boolean verify(String token){
        long currentTime = System.currentTimeMillis();
        return Long.parseLong(parseTokenGetClaims(token,EXPIRE_TIME).toString()) > currentTime;
    }

    /**
     * 校验token从创建时间 - 到现在超过time分钟否
     *
     * @param token
     * @param time 单位 min
     * @return
     */
    // 按照我们的设计,用户每次携带token,校验,
    // 都会去更新这个token创建时间,那么直到我们有很长一次才被校验时,说明我们用户已经长期未登录了。
    public static boolean verify(String token,Long time)
    {
        Long creteTime = Long.valueOf(parseTokenGetClaims(token, CREATE_TIME).toString());
        Long currentTime = System.currentTimeMillis();
        return time > (currentTime - creteTime )/MILLIS_MINUTE;
    }

    public static String verifyRefresh(String token)
    {
        if(verify(token,20L)) // 离线没超过20min
        {
            Long userId = Long.valueOf(parseTokenGetUserId(token));
            return createToken(userId);
        }
        return null;
    }


    public static void main(String[] args)
    {
        System.out.println(MD5.create().digestHex("12345"));
    }
}
