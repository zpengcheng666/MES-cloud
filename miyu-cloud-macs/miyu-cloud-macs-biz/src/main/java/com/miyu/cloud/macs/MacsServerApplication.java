package com.miyu.cloud.macs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
@SpringBootApplication(scanBasePackages = {"com.miyu"})
public class MacsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MacsServerApplication.class, args);
    }
}
