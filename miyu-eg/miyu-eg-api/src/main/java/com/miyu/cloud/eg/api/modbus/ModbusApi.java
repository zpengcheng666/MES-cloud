package com.miyu.cloud.eg.api.modbus;

import com.miyu.cloud.eg.enums.ApiConstants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME , url = "http://192.168.3.4:48999")
public interface ModbusApi {

    String PREFIX = ApiConstants.PREFIX + "/modbus/";

    @PostMapping(PREFIX + "updateModbus")
    @Operation(summary = "modbus设备通信测试")
    String updateModbus(@RequestParam("ipAddress") String ipAddress,
                        @RequestParam("hexData") String hexData);

    @PostMapping(PREFIX + "openDoor")
    @Operation(summary = "modbus开门")
    String openDoor(@RequestParam("ipAddress") String ipAddress);

    @PostMapping(PREFIX + "closeDoor")
    @Operation(summary = "modbus关门")
    String closeDoor(@RequestParam("ipAddress") String ipAddress);

    @PostMapping(PREFIX + "SwitchDoor")
    @Operation(summary = "modbus模式切换")
    String SwitchDoor(@RequestParam("ipAddress") String ipAddress);

//    @PostMapping(PREFIX + "updateBytes")
//    @Operation(summary = "modbus切换波特率")
//    String updateBytes(@RequestParam("ipAddress") String ipAddress);

    //-----------------------------------------以下为查询 后续需根据实际情况判定----------------------------------

    @PostMapping(PREFIX + "updateBytes")
    @Operation(summary = "modbus切换Byte")
    String updateBytes(String ipAddress);

    @PostMapping(PREFIX + "selectAddress")
    @Operation(summary = "modbus查询地址")
    String selectAddress(@RequestParam("ipAddress") String ipAddress);

    @PostMapping(PREFIX + "selectState")
    @Operation(summary = "modbus查询状态")
    String selectState(@RequestParam("ipAddress") String ipAddress);


}
