package com.miyu.cloud.macs.controller.admin.collector.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - (通行卡,人脸,指纹)采集器 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CollectorRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27281")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "编号")
    @ExcelProperty("编号")
    private String code;

    @Schema(description = "名称", example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "位置(0内侧,1外侧...)")
    @ExcelProperty("位置(0内侧,1外侧...)")
    private String locationCode;

    @Schema(description = "关联门id", example = "25260")
    @ExcelProperty("关联门id")
    private String doorId;

    @Schema(description = "关联设备id", example = "17988")
    @ExcelProperty("关联设备id")
    private String deviceId;

    @Schema(description = "关联设备位置")
    @ExcelProperty("关联设备位置")
    private Integer devicePort;

    @Schema(description = "设备状态(0未连接,1正常,2读取,3故障...)", example = "1")
    @ExcelProperty("设备状态(0未连接,1正常,2读取,3故障...)")
    private Integer status;

    @Schema(description = "采集设备类型(1读取设备,2读写设备)", example = "2")
    @ExcelProperty("采集设备类型(1读取设备,2读写设备)")
    private Integer type;

    @Schema(description = "描述/备注", example = "你说的对")
    @ExcelProperty("描述/备注")
    private String description;

    @Schema(description = "连接信息")
    @ExcelProperty("连接信息")
    private String connectionInformation;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

    private String deviceName;
    private String doorName;
    private String regionName;

}