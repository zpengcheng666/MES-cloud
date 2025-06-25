package com.miyu.cloud.mcs.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "mcs-server")
public interface McsDistributionApplicationApi {

    String PROCESS_KEY = "mcs_distribution";

    @PostMapping(value = "/mcs/updateApplicationStatus")
    void updateApplicationStatus(@RequestParam("applicationId")String applicationId, @RequestParam("status")Integer status);

}
