package com.miyu.module.qms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 *
 * @author Qianjy
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class QmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(QmsServerApplication.class, args);
    }

}