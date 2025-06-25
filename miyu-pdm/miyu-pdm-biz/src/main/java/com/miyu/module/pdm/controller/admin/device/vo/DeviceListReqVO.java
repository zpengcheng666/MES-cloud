package com.miyu.module.pdm.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - PDM 设备列表-临时 Request VO")
@Data
public class DeviceListReqVO {

    @Schema(description = "设备编号", example = "芋艿")
    private String code;

    @Schema(description = "设备名称", example = "芋艿")
    private String name;

    @Schema(description = "设备类型(0设备1工位2产线)")
    private Integer type;

    @Schema(description = "产线或单机设备id数组")
    private List<String> deviceIds;
}
