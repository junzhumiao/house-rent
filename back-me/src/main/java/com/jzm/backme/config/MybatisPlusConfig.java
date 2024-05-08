package com.jzm.backme.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.jzm.backme.context.UserContext;
import com.jzm.backme.domain.User;
import com.jzm.backme.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

@Slf4j
@Component
@EnableTransactionManagement
public class MybatisPlusConfig implements MetaObjectHandler
{
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        User user = UserContext.get();
        if(StringUtil.isNotEmpty(user)){
            this.strictInsertFill(metaObject, "createBy", user::getUserId, Long.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime::now, LocalDateTime.class);
        User user = UserContext.get();
        if(StringUtil.isNotEmpty(user)){
            this.strictInsertFill(metaObject, "updateBy", user::getUserId, Long.class);
        }
    }
}