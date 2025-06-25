package com.miyu.module.ppm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 项目的启动类
 *
 * @author zhangyunfei
 */
@SpringBootApplication
public class PpmServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PpmServerApplication.class, args);
    }

}