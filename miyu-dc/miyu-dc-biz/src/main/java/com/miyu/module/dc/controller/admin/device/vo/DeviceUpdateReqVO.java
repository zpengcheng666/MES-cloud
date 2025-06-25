package com.miyu.module.dc.controller.admin.device.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 设备详情/修改 Request VO")
@Data
public class DeviceUpdateReqVO {

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4659")
    @ExcelProperty("设备id")
    private String id;

    @Schema(description = "设备id", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "设备类型", example = "王五")
    private String deviceTypeId;

    @Schema(description = "产品类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30217")
    private String[] productTypeId;

    @Schema(description = "通信类型")
    private Integer commType;

    @Schema(description = "mqtt客户端id")
    private String deviceClientId;

    @Schema(description ="通讯url")
    private String deviceUrl;

    @Schema(description ="账号")
    private String username;

    @Schema(description ="密码")
    private String password;

}
