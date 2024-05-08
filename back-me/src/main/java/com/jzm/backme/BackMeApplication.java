package com.jzm.backme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages ={"com.jzm.backme"} )
@ConfigurationPropertiesScan(basePackages = {"com.jzm.backme"})
public class BackMeApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(BackMeApplication.class, args);
    }

}
