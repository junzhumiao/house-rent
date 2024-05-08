package com.jzm.backme.constant;

public class Constant
{


    /**
     * 响应成功、失败标识
     */
    public static final int OK_CODE = 200;
    public static final int ERROR_CODE = 400;
    public static final int WARN_CODE = 300;
    public static final String OK_MES = "ok";
    public static final String FAIL_MES = "fail";

    public static String UTF_8 = "utf-8";


    /**
     * 密码错误允许最大次数
     */
    public static final Integer PWD_ERR_MAX_CNT = 5;

    /**
     * token自定义字段
     */
    public static String USER_ID = "userId";
    public static String PASSWORD = "password";

    /**
     * 产品相关
     */
    public static final int CATEGORY_NAME_MIN_LEN = 1;
    public static final int CATEGORY_NAME_MAX_LEN = 5;


    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";


    /**
     *  前缀
     */
    public static final String HTTPS_PREFIX = "https://";

    public static final String HTTP_PREFIX = "http://";

    public static int In_Code_Pre_Len = 6; // 就是随机的6位数字。


    /**
     * 自动识别json对象白名单配置（仅允许解析的包名，范围越小越安全）
     */
    public static final String[] JSON_WHITELIST_STR = { "org.springframework", "com.jzm" };

    /**
     * 排除拦截列表
     */
    public static final String[] ExclusionList = new String[]{
            "/test","/login","/register","/get/captcha",
            "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/*/api-docs"
    };

    /**
     * 公共响应状态(类似于 启用 0 禁用 1的判别)
     */
    public static final String Zero = "0";
    public static final String One = "1";


    public static final String Contract_0 = "0"; // 预发布
    public static final String Contract_1 = "1"; // 签署-未生效
    public static final String Contract_2 = "2"; // 生效
    public static final String Contract_3 = "3"; // 停止




}