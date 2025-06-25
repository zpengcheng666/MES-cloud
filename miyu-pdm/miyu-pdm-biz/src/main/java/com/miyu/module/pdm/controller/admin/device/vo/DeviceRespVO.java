package com.miyu.module.pdm.controller.admin.device.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 设备列表-临时 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceRespVO {

    @Schema(description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("设备ID")
    private String id;

    @Schema(description = "设备编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String code;

    @Schema(description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String name;

    @Schema(description = "设备类型(0设备1工位2产线)")
    private Integer type;

    @Schema(description = "设备规格", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String specification;

    @TableField(exist = false)
    @Schema(description = "预估工时(min)", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String processingTime;
}
