package com.miyu.module.dc.controller.admin.offlinecollect.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OfflineCollectResVO {

    @Schema(description = "时间")
    private String time;

    @Schema(description = "topicId", example = "1")
    private String topicId;

    private List<Map<String,String>> objs;

    @Schema(description = "数据值")
    private String items;

    @Schema(description = "deviceId", example = "1")
    private String deviceId;

    private String productTypeName;

    private Integer commType;

    private String code;

    private Integer alarmType;
}
