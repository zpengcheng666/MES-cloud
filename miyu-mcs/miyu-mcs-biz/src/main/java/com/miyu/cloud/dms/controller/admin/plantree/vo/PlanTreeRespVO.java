package com.miyu.cloud.dms.controller.admin.plantree.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 计划关联树 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PlanTreeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14340")
    private String id;

    @Schema(description = "父节点")
    @ExcelProperty("父节点")
    private String parent;

    @Schema(description = "节点名", example = "李四")
    @ExcelProperty("节点名")
    private String name;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "关联设备", example = "12631")
    @ExcelProperty("关联设备")
    private String deviceId;

    @Schema(description = "关联设备类型", example = "21029")
    @ExcelProperty("关联设备类型")
    private String deviceTypeId;

}