package com.miyu.cloud.macs.framework.security.config;

import cn.iocoder.yudao.module.infra.enums.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.iocoder.yudao.framework.security.config.AuthorizeRequestsCustomizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * macs 模块的 Security 配置
 */
@Configuration("macsSecurityConfiguration")
public class SecurityConfiguration {

    @Bean
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
                // Swagger 接口文档
                registry.antMatchers("/v3/api-docs/**").permitAll() // 元数据
                        .antMatchers("/swagger-ui.html").permitAll() // Swagger UI
                        .antMatchers("/macs/rest/pushInformation").permitAll()
                        .antMatchers("/macs/rest/pushDeviceStatus").permitAll()
                        .antMatchers("/macs/rest/getImageByName/**").permitAll()
                        .antMatchers("/macs/rest/inlineUser").permitAll();
                // Druid 监控
                registry.antMatchers("**/druid/**").anonymous();

                registry.antMatchers("/test/**").permitAll();
                // Spring Boot Actuator 的安全配置
                registry.antMatchers("/actuator").anonymous()
                        .antMatchers("/actuator/**").anonymous();
                // RPC 服务的安全配置
                registry.antMatchers(ApiConstants.PREFIX + "/**").permitAll();
            }

        };
    }

}
