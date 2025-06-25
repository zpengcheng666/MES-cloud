package com.miyu.cloud.macs.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@FeignClient(name = "macs-cloud")
public interface AccessApplicationControlApi {

    String PROCESS_KEY = "macs_application";

    @PostMapping(value = "/macs/updateApplicationStatus")
    void updateApplicationStatus(@RequestParam("applicationId")String applicationId, @RequestParam("status")Integer status);

}
