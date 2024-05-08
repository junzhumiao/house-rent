package com.jzm.backme.service;

import com.jzm.backme.constant.CacheConstant;
import com.jzm.backme.constant.HeaderConstant;
import com.jzm.backme.constant.HttpStatus;
import com.jzm.backme.context.UserContext;
import com.jzm.backme.domain.User;
import com.jzm.backme.exception.PermissionException;
import com.jzm.backme.model.vo.UserVo;
import com.jzm.backme.util.ServletUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: jzm
 * @date: 2024-04-14 16:39
 **/

@Service
public class LoginService
{

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取userId
     * @return
     */
    public final Long getUserId(){
        User user = getUser();
        if(StringUtil.isEmpty(user)){
            return null;
        }
        return user.getUserId();
    }

    public final String getAccountAddress(){
        return getUser().getUser();
    }

    public  final User getUser(){
        return UserContext.get();
    }

    public final boolean isRole(Long roleId){
        User user = getUser();
        if(user instanceof  UserVo){
            UserVo userVo = (UserVo) user;
            if(userVo.getRoleId() == roleId){
               return true;
            }
        }
        return false;
    }

    public final boolean isAdmin(){
        return isRole(1L);
    }

    public final boolean isLandlord(){
        return isRole(2L);
    }

    public final boolean isTenant(){
        return isRole(3L);
    }


    public final boolean isNotAdmin(){
        return !isAdmin();
    }

    public final void checkAdmin(){
        if(!isAdmin()){
           throw new PermissionException();
        }
    }

    public final void checkLandlord(){
        if(!isLandlord()){
            throw new PermissionException("操作者必须是房东");
        }
    }

    public final void checkTenant(){
        if(!isTenant()){
            throw new PermissionException("操作者必须是租客");
        }
    }


    /**
     * 校验验证码
     */
    public  final HttpStatus verifyCaptcha(){
        String codeVal = ServletUtil.getHeader(HeaderConstant.CAPTCHA_VAL);
        String codeKey = ServletUtil.getHeader(HeaderConstant.CAPTCHA_KEY);

        if(codeVal != null){
            codeKey = CacheConstant.CAPTCHA_CODE_KEY + codeKey;
            String val = redisCache.getCacheObject(codeKey);
            if(val == null) { // 验证码过期
                return HttpStatus.CAPTCHA_EXPIRED;
            }
            if(!StringUtil.equals(val,codeVal)){  // 验证码不一致
                return HttpStatus.CAPTCHA_CHECK_FAILED;
            }
            return null;
        }
        return HttpStatus.CAPTCHA_CHECK_FAILED;
    }
}
