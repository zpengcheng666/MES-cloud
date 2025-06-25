package com.miyu.module;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 项目的启动类
 *
 * @author Qianjy
 */
@MapperScan("com.miyu.module.es.dal.mysql")
@EnableScheduling   // 1.开启定时任务
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
public class EsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsServerApplication.class, args);
    }

}