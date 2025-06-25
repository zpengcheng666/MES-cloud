package com.miyu.module;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@MapperScan("com.miyu.module.dc.dal.mysql")
@EnableScheduling   // 1.开启定时任务
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
public class DcServerApplication {

    public static void main(String[] args) {

        SpringApplication.run(DcServerApplication.class, args);

    }

}