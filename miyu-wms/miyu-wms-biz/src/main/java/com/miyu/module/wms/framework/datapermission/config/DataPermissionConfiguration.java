package com.miyu.module.wms.framework.datapermission.config;

import cn.iocoder.yudao.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.miyu.module.wms.dal.dataobject.alarm.AlarmDO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * system 模块的数据权限 Configuration
 *
 * @author 芋道源码
 */
@Configuration(proxyBeanMethods = false)
public class DataPermissionConfiguration {

    @Bean
    public DeptDataPermissionRuleCustomizer wmsDataPermissionRuleCustomizer() {
        return rule -> {
            // dept
            rule.addDeptDataColumn(AlarmDO.class, "dept_id");
            // user
            rule.addUserDataColumn(AlarmDO.class, "creator");
        };
    }

}
