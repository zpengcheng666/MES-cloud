package com.miyu.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
public class McsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McsServerApplication.class, args);
    }
}
