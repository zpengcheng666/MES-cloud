package com.miyu.cloud.macs.controller.admin.collector.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - (通行卡,人脸,指纹)采集器新增/修改 Request VO")
@Data
public class CollectorSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27281")
    private String id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "位置(0内侧,1外侧...)")
    private String locationCode;

    @Schema(description = "关联门id", example = "25260")
    private String doorId;

    @Schema(description = "关联设备id", example = "17988")
    private String deviceId;

    @Schema(description = "关联设备位置")
    private Integer devicePort;

    @Schema(description = "设备状态(0未连接,1正常,2读取,3故障...)", example = "1")
    private Integer status;

    @Schema(description = "采集设备类型(1读取设备,2读写设备)", example = "2")
    private Integer type;

    @Schema(description = "描述/备注", example = "你说的对")
    private String description;

    @Schema(description = "连接信息")
    private String connectionInformation;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

}