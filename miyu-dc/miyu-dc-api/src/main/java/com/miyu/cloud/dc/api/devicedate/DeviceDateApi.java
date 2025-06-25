package com.miyu.cloud.dc.api.devicedate;

import com.miyu.cloud.dc.api.devicedate.dto.CommonDevice;
import com.miyu.cloud.dc.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = ApiConstants.NAME , url = "http://192.168.3.4:48099")
public interface DeviceDateApi {

    String PREFIX = ApiConstants.PREFIX + "/deviceR/";

    @PostMapping(PREFIX + "creat/{topic}")
    @Operation(summary = "时序库插入采集值")
    void insertDeviceDate(@RequestBody CommonDevice deviceDateRespDTO, @PathVariable("topic") String topic);


//    @GetMapping(PREFIX + "/queryDeviceDate")
//    @Operation(summary = "时序库查询设备数据")
//    List<DeviceDateResVO> queryDeviceDate(DeviceDatePageReqVO deviceDatePageReqVO);


}
