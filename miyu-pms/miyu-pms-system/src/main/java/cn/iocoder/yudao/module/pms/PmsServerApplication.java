package cn.iocoder.yudao.module.pms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 项目的启动类
 *
 * 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
 * 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
 * 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
 *
 * @author 芋道源码
 */
@SpringBootApplication(scanBasePackages = {"com.miyu","cn.iocoder.yudao.module"})
@EnableFeignClients(basePackages = {"com.miyu","cn.iocoder.yudao.module"})
public class PmsServerApplication {

    public static void main(String[] args) {
        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章

        SpringApplication.run(PmsServerApplication.class, args);

        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
        // 如果你碰到启动的问题，请认真阅读 https://cloud.iocoder.cn/quick-start/ 文章
    }

}
