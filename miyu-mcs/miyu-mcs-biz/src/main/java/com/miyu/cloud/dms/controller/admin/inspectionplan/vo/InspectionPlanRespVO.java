package com.miyu.cloud.dms.controller.admin.inspectionplan.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备检查计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionPlanRespVO {


    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10995")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "计划编码")
    @ExcelProperty("计划编码")
    private String code;

    @Schema(description = "所属计划关联树")
    @ExcelProperty("所属计划关联树")
    private String tree;

    @Schema(description = "设备")
    @ExcelProperty("设备")
    private String device;

    @Schema(description = "启用状态", example = "2")
    @ExcelProperty(value = "启用状态", converter = DictConvert.class)
    @DictFormat("enableStatus") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer enableStatus;

    @Schema(description = "是否超期停机", example = "2")
    @ExcelProperty(value = "是否超期停机", converter = DictConvert.class)
    @DictFormat("dms_expiration_shutdown")
    private Integer expirationShutdown;

    @Schema(description = "超期时间")
    @ExcelProperty("超期时间")
    private Integer expirationTime;

    @Schema(description = "检查类型", example = "1")
    @ExcelProperty(value = "检查类型", converter = DictConvert.class)
    @DictFormat("dms_inspection_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "负责人")
    @ExcelProperty("负责人")
    private String superintendent;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "cron表达式")
    @ExcelProperty("cron表达式")
    private String cornExpression;

    @Schema(description = "计划任务id")
    @ExcelProperty("计划任务id")
    private String jobId;

    @Schema(description = "检查内容")
    @ExcelProperty("检查内容")
    private String content;

    @Schema(description = "说明", example = "你说的对")
    @ExcelProperty("说明")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
