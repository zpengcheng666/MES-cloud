package com.miyu.module.qms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.database.DatabaseApi;
import com.miyu.module.qms.service.managementdatabase.ManagementDatabaseService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DatabaseApiImpl implements DatabaseApi {

    @Resource
    private ManagementDatabaseService managementDatabaseService;

    @Override
    public CommonResult<String> updateDatabaseAuditStatus(String businessKey, Integer status) {
        managementDatabaseService.updateDatabaseAuditStatus(businessKey, status);
        return null;
    }
}
