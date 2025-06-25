package com.miyu.cloud.macs.controller.admin.door.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 门 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DoorRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12669")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "名称", example = "李四")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "关联区域id", example = "31210")
    @ExcelProperty("关联区域id")
    private String regionId;

    @Schema(description = "关联设备id", example = "31452")
    @ExcelProperty("关联设备id")
    private String deviceId;

    @Schema(description = "位置", example = "赵六")
    @ExcelProperty("位置")
    private String locationName;

    @Schema(description = "门禁状态(0关闭,1打开,2故障)", example = "2")
    @ExcelProperty("门禁状态")
    @DictFormat("doorStatus")
    private Integer doorStatus;

    @Schema(description = "描述/备注", example = "随便")
    @ExcelProperty("描述/备注")
    private String description;

    @Schema(description = "关联设备位置")
    @ExcelProperty("关联设备位置")
    private Integer devicePort;

    private String regionName;

    private String deviceName;

}