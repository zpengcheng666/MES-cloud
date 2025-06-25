package com.miyu.module.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 项目的启动类
 *
 * @author Qianjy
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class WmsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WmsServerApplication.class, args);
    }

}