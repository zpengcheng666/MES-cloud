package com.miyu.cloud.dms.controller.admin.devicetype.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 设备类型分页 List VO")
@Data
@ToString(callSuper = true)
public class DeviceTypeListVo {
    @Schema(description = "id", example = "15312")
    private String id;

    @Schema(description = "类型编号", example = "HSG-9387")
    @ExcelProperty("类型编号")
    private String code;

    @Schema(description = "类型名称", example = "4号控制器")
    @ExcelProperty("类型名称")
    private String name;

    @Schema(description = "设备/工位")
    private Integer type;
}
