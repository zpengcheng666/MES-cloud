package com.miyu.cloud.macs.controller.admin.accessRecords.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 通行记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AccessRecordsRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "358")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "访问人员id", example = "26945")
    @ExcelProperty("访问人员id")
    private String userId;

    @Schema(description = "访客访问人员id", example = "16008")
    @ExcelProperty("访客访问人员id")
    private String visitorId;

    @Schema(description = "访问人员编号")
    @ExcelProperty("访问人员编号")
    private String userCode;

    @Schema(description = "访问人员姓名", example = "王五")
    @ExcelProperty("访问人员姓名")
    private String userName;

    @Schema(description = "操作人员id", example = "4702")
    @ExcelProperty("操作人员id")
    private String operatorId;

    @Schema(description = "设备id", example = "20212")
    @ExcelProperty("设备id")
    private String deviceId;

    @Schema(description = "门id", example = "17978")
    @ExcelProperty("门id")
    private String doorId;

    @Schema(description = "门名称", example = "赵六")
    @ExcelProperty("门名称")
    private String doorName;

    @Schema(description = "采集器Id", example = "28400")
    @ExcelProperty("采集器Id")
    private String collectorId;

    @Schema(description = "采集器名称", example = "李四")
    @ExcelProperty("采集器名称")
    private String collectorName;

    @Schema(description = "采集器编码")
    @ExcelProperty("采集器编码")
    private String collectorCode;

    @Schema(description = "位置Id", example = "3462")
    @ExcelProperty("位置Id")
    private String regionId;

    @Schema(description = "位置名称", example = "张三")
    @ExcelProperty("位置名称")
    private String regionName;

    @Schema(description = "动作(-1,未知,0校验,1进,2出,3开门,4关门)")
    @ExcelProperty("动作(-1,未知,0校验,1进,2出,3开门,4关门)")
    private Integer action;

    @Schema(description = "备注信息")
    @ExcelProperty("备注信息")
    private String message;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}