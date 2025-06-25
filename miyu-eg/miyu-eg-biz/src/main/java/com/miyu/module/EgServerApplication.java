package com.miyu.module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目的启动类
 *
 * @author yuhao
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
//@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
public class EgServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EgServerApplication.class, args);
    }
}
