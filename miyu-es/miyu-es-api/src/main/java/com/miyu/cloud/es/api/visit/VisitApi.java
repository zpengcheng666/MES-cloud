package com.miyu.cloud.es.api.visit;

import com.miyu.cloud.es.api.visit.dto.ModianDTO;
import com.miyu.cloud.es.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@FeignClient(name = ApiConstants.NAME , url = "http://192.168.2.136:443")
//如需开放需调整为以下url
//@FeignClient(name = ApiConstants.NAME , url = "http://dingding.miyutech.cn")
public interface VisitApi {

    String PREFIX = ApiConstants.PREFIX + "/deviceR/";

    @PermitAll
    @PostMapping(PREFIX + "visitCallBack")
    @Operation(summary = "访客记录调取")
    void visitCallBack(@RequestBody ModianDTO object,
                       @RequestParam("nonce") String nonce,
                       @RequestParam("orgId") String orgId,
                       @RequestParam("signVersion") String signVersion,
                       @RequestParam("signature") String signature,
                       @RequestParam("timestamp") String timestamp);
}
