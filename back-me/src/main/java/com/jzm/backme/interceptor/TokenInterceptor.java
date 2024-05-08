package com.jzm.backme.interceptor;

import cn.hutool.Hutool;
import cn.hutool.json.JSONUtil;
import com.jzm.backme.constant.*;
import com.jzm.backme.context.UserContext;
import com.jzm.backme.domain.User;
import com.jzm.backme.model.AjaxResult;
import com.jzm.backme.model.vo.UserVo;
import com.jzm.backme.util.ServletUtil;
import com.jzm.backme.util.StringUtil;
import com.jzm.backme.util.TokenUtil;
import com.jzm.backme.util.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.MessageDigest;

/**
 * @author: jzm
 * @date: 2024-04-14 16:58
 **/

@Component
public class TokenInterceptor implements HandlerInterceptor
{

    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        // 设置跨域
        response.setHeader("Access-Control-Allow-Origin", "*"); // 修改携带cookie,PS
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        // 预检请求缓存时间（秒），即在这个时间内相同的预检请求不再发送，直接使用缓存结果。
        response.setHeader("Access-Control-Max-Age", "3600");

        if (request.getMethod().equalsIgnoreCase("options"))
        { // 设置预请求直接通过
            return true;
        }
        String path = request.getRequestURI();
        // 排除不过滤列表
        if (isExclusionList(path, UserConstant.ExclusionList))
        {
            return true;
        }
        // token拿取和校验
        String authorization = request.getHeader(HeaderConstant.AUTHORIZATION);
        if (StringUtil.isEmpty(authorization))
        {
            ServletUtil.renderString(response, JSONUtil.toJsonStr(HttpStatus.USER_NOT_LOGIN));
            return false;
        }
        if (!authorization.startsWith(Constant.TOKEN_PREFIX))
        {
            ServletUtil.renderString(response, JSONUtil.toJsonStr(HttpStatus.USER_TOKEN_ILLICIT));
            return false;
        }
        String token = authorization.substring(Constant.TOKEN_PREFIX.length());
        String userId = TokenUtil.parseTokenGetUserId(token);
        String loginKey = CacheConstant.LOGIN_USER_KEY + userId;
        User user = redisCache.getCacheObject(loginKey);
        if (StringUtil.isEmpty(user))
        {
            ServletUtil.renderString(response, JSONUtil.toJsonStr(HttpStatus.USER_LOGIN_EXPIRED));
            return false;
        }
        UserContext.set(user);
        return true;
    }

    // 排除不拦截列表(其实mvc配置就行,但是我这里面懒写了)。
    private boolean isExclusionList(String path, String... exclusionList)
    {
        for (String ep : exclusionList)
        {
            if (ep.equals(path))
            {
                return true;
            }
            // 这是 /path/qhx => /path/**
            if (ep.contains("/**"))
            {
                int len = ep.length();
                ep = ep.substring(0, len - "/**".length());
                if (path.contains(ep))
                {
                    return true;
                }
            }
        }
        return false;
    }


}
