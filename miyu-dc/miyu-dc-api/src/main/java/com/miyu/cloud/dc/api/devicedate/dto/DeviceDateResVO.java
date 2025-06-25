package com.miyu.cloud.dc.api.devicedate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DeviceDateResVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "时间")
    private String time;

    @Schema(description = "数据值")
    private List<Object> items;
}
