package com.miyu.cloud.mcs.restServer.api.DistributionApplication;

import com.miyu.cloud.mcs.api.McsDistributionApplicationApi;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DistributionApplicationApiImpl implements McsDistributionApplicationApi {

    @Resource
    private DistributionApplicationService distributionApplicationService;

    @Override
    public void updateApplicationStatus(String applicationId, Integer status) {
        distributionApplicationService.updateApplicationStatus(applicationId,status);
    }

}
