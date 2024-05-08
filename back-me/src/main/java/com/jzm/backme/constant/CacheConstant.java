package com.jzm.backme.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstant
{

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_USER_KEY = "login_users:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";


    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 限流 redis key
     */
    public static final String RATE_LIMIT_KEY = "rate_limit:";

    /**
     * 登录账户密码错误次数 redis key
     */
    // 剩余次数
    public static final String PWD_ERR_REM_CNT_KEY = "pwd_err_rem_cnt:";
    public static final String PWD_ERR_MAX_CNT_KEY = "pwd_err_max_cnt:";
}
