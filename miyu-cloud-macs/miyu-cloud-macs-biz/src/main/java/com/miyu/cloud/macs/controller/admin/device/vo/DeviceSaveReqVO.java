package com.miyu.cloud.macs.controller.admin.device.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 设备新增/修改 Request VO")
@Data
public class DeviceSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25403")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "芋艿")
    private String name;

    @Schema(description = "区域ID")
    private String regionId;

    @Schema(description = "区域名称")
    private String regionName;

    @Schema(description = "状态(0未连接,1正常,2故障...)", example = "1")
    private Integer status;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "端口号")
    private String port;

    @Schema(description = "账户")
    private String accountNumber;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "启用状态(1启用,0禁用)", example = "2")
    private Integer enableStatus;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "更新人")
    private String updateBy;

}
