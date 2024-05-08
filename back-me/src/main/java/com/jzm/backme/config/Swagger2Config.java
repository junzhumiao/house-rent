package com.jzm.backme.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2 // 3.0版本加不加无所谓
public class Swagger2Config
{

   private boolean enabled = true;
 
    @Bean
    public Docket coreApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())
                .groupName("group1")
                .enable(enabled)
                .select()
                .build();
    }
 
    private ApiInfo adminApiInfo(){
        return new ApiInfoBuilder()
                .title("后台管理系统--api文档")
                .description("后台管理系统接口描述")
                .version("1.0")
                .contact(new Contact("郡主喵","http://baidu.com","728831102@qq.com"))
                .build();
    }
}