package com.miyu.module.mcc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 *
 * @author Qianjy
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class MccServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MccServerApplication.class, args);
    }

}