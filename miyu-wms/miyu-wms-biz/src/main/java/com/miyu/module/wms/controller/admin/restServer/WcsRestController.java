package com.miyu.module.wms.controller.admin.restServer;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.core.util.SendUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "外部接口 - Wcs")
@RestController
@Slf4j
@RequestMapping("/wms/wcs-rest")
public class WcsRestController {


    @Value("${otherServer.wcs.url}")
    private String wcsUrl;

    /**
     * 仓库查询
     */
    @GetMapping("/queryWarehouseList")
    @Operation(summary = "仓库查询")
    public JSONArray queryWarehouseList() {
        return SendUtils.getJSONArrayRequestSend(wcsUrl + "/queryWarehouseList");
    }

    /**
     * 库区查询
     */
    @PostMapping("/queryRegion")
    @Operation(summary = "库区查询")
    public CommonResult<?> queryRegionByWarehouseCode(String warehouseCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("WarehouseCode", warehouseCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/queryRegion",jsonObject);
    }
    /**
     * 库位查询
     */
    @PostMapping("/queryStorageLocation")
    @Operation(summary = "库位查询")
    public CommonResult<?> queryStorageLocationByWarehouseCode(String warehouseCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("WarehouseCode", warehouseCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/queryStorageLocation",jsonObject);
    }

    /**
     * 节点查询
     */
    @PostMapping("/queryNodeLocation")
    @Operation(summary = "节点查询")
    public CommonResult<?> queryNodeLocationByWarehouseCode(String warehouseCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("WarehouseCode", warehouseCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/queryNodeLocation",jsonObject);
    }


    /**
     * 容器查询
     */
    @PostMapping("/queryContainer")
    @Operation(summary = "容器查询")
    public CommonResult<?> queryContainerByWarehouseCode(String warehouseCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("WarehouseCode", warehouseCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/queryContainer",jsonObject);
    }

    /**
     * 任务发送
     */
    @PostMapping("/sendTask")
    @Operation(summary = "任务发送")
    public CommonResult<?> sendTask(String taskNo, String startPoint, String endPoint, String priority, String containerCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskNo", taskNo);
        jsonObject.put("StartPoint", startPoint);
        jsonObject.put("EndPoint", endPoint);
        jsonObject.put("Priority", priority);
        jsonObject.put("ContainerCode", containerCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/sendTask",jsonObject);
    }

    /**
     * 设备状态查询
     */
    @PostMapping("/queryDeviceInfo")
    @Operation(summary = "设备状态查询")
    public CommonResult<?> queryDeviceInfoByDeviceCode(String deviceCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("DeviceCode", deviceCode);
        return SendUtils.postStringRequestSend(wcsUrl + "/queryDeviceInfo",jsonObject);
    }

}
