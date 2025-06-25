package com.miyu.cloud.macs.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 设备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25403")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "芋艿")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "区域ID")
    @ExcelProperty("区域ID")
    private String regionId;

    @ExcelProperty("区域名称")
    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "状态(0未连接,1正常,2故障...)", example = "1")
    @ExcelProperty("状态(0未连接,1正常,2故障...)")
    private Integer status;

    @Schema(description = "ip")
    @ExcelProperty("ip")
    private String ip;

    @Schema(description = "端口号")
    @ExcelProperty("端口号")
    private String port;

    @Schema(description = "账户")
    @ExcelProperty("账户")
    private String accountNumber;

    @Schema(description = "密码")
    @ExcelProperty("密码")
    private String password;

    @Schema(description = "启用状态(1启用,0禁用)", example = "2")
    @ExcelProperty("启用状态(1启用,0禁用)")
    private Integer enableStatus;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

}
