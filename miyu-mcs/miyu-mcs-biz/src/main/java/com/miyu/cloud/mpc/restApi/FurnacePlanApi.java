package com.miyu.cloud.mpc.restApi;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.mpc.dto.FurnacePlanStartDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@Component
@FeignClient(name = "MESDataAcquisitionSystem")
public interface FurnacePlanApi {

    @PostMapping("/api/Device/GSendTask/GSendTask")
    Map<String,Object> furnacePlanStart(@RequestBody FurnacePlanStartDTO furnacePlanStartDTO);
}
