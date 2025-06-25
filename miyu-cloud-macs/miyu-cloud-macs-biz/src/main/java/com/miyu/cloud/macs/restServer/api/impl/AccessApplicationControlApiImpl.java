package com.miyu.cloud.macs.restServer.api.impl;

import com.miyu.cloud.macs.api.AccessApplicationControlApi;
import com.miyu.cloud.macs.service.accessApplication.AccessApplicationService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class AccessApplicationControlApiImpl implements AccessApplicationControlApi {

    @Resource
    private AccessApplicationService accessApplicationService;

    @Override
    public void updateApplicationStatus(String applicationId, Integer status) {
        accessApplicationService.updateApplicationStatus(applicationId,status);
    }
}
