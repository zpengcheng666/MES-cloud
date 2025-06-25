package com.miyu.module.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 *
 * @author Qianjy
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class TmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmsServerApplication.class, args);
    }

}