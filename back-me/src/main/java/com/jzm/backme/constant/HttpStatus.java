package com.jzm.backme.constant;


import com.jzm.backme.model.AjaxResult;

/**
 * http错误状态的常量
 *
 * @author: jzm
 * @date: 2024-02-27 08:44
 **/

public class HttpStatus extends AjaxResult
{
   public static final HttpStatus  PASSWORD_NOT_MATCH = new HttpStatus(401,"密码长度不匹配!");
    public static final HttpStatus PASSWORD_RETRY_LIMIT_EXCEED(String blockedTime){
        return new HttpStatus(403,"密码输入错误次数达到上限,剩余解禁时间:" + blockedTime);
    }
    public static final HttpStatus  PASSWORD_REMAIN_INPUT_COUNT(int count) {
        return new HttpStatus(402,"密码输入错误,当前剩余输入当前次数:" + count);
    }

    // 密码重复提交最大次数
    public static final HttpStatus USERNAME_NOT_MATCH=  new HttpStatus(404,"用户名长度不匹配!");
    public static final HttpStatus USER_NOT_EXISTS=  new HttpStatus(405,"用户不存在!");
    public static final HttpStatus  USER_DELETE=  new HttpStatus(406,"用户已经注销!");
    public static final HttpStatus  CAPTCHA_CHECK_FAILED=  new HttpStatus(407,"验证码错误!");
    public static final HttpStatus CAPTCHA_EXPIRED=  new HttpStatus(407,"验证码已经过期!");
    public static final HttpStatus USER_BLOCKED=  new HttpStatus(407,"用户已经被锁定!");
    public static final HttpStatus  USER_NOT_LOGIN=  new HttpStatus(408, "用户没登录!");
    public static final HttpStatus USER_LOGIN_EXPIRED=  new HttpStatus(409, "用户登录过期,需要重新登录!");
    public static final HttpStatus USER_TOKEN_ILLICIT=  new HttpStatus(410,"用户令牌格式不对!" );
    public static final HttpStatus PASSWORD_OR_PHONE_NOT_EMPTY=  new HttpStatus(411, "用户密码和电话号是不能为空!");
    public static final HttpStatus PHONE_NOT_MATCH=  new HttpStatus(412, "电话输入格式不对!");
    public static final HttpStatus USER_EXISTS=  new HttpStatus(413, "用户已经存在!");
    public static final HttpStatus PHONE_EXISTS=  new HttpStatus(414,"该电话号已经绑定其他账户!" );
    public static final HttpStatus EMAIL_NOT_MATCH=  new HttpStatus(415, "邮箱格式不匹配!");
    public static final HttpStatus EMAIL_EXISTS=  new HttpStatus(416, "该邮箱已经绑定其他账户!");
    public static final HttpStatus PASSWORD_OR_USERNAME_NOT_EMPTY=  new HttpStatus(417, "用户名/密码不能为空");
    public static final HttpStatus PASSWORD_EXISTS = new HttpStatus(418,"该密码已经绑定其他账户");
    public static final HttpStatus REQ_HED_PAM_NOT_ENOUGH = new HttpStatus(419,"请求头部参数不全！");
    public static final HttpStatus OFF_LIMIT_EXCEEDED = new HttpStatus(420,"用户离线超时,请重新登录!");

    private  HttpStatus(int code, String mes){
       super(null,code,mes);
    }


}
