package cn.iocoder.yudao.module.pms.framework.security.config;

import cn.iocoder.yudao.framework.security.config.AuthorizeRequestsCustomizer;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

@Configuration(proxyBeanMethods = false)
public class SecurityConfiguration {

    @Bean
    public AuthorizeRequestsCustomizer authorizeRequestsCustomizer() {
        return new AuthorizeRequestsCustomizer() {

            @Override
            public void customize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry) {
                // Swagger 接口文档
                registry.antMatchers("/v3/api-docs/**").permitAll() // 元数据
                        .antMatchers("/swagger-ui.html").permitAll(); // Swagger UI
                // Druid 监控
                registry.antMatchers("/druid/**").permitAll();
                // Spring Boot Actuator 的安全配置
                registry.antMatchers("/actuator").permitAll()
                        .antMatchers("/actuator/**").permitAll();
                // RPC 服务的安全配置
                registry.antMatchers(ApiConstants.PREFIX + "/**").permitAll();
                registry.antMatchers("/mcs/rest/**").permitAll();
                registry.antMatchers("/pms/**").permitAll();
            }

        };
    }
}
