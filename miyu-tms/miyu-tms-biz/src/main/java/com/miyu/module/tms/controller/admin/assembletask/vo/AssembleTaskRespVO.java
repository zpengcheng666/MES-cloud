package com.miyu.module.tms.controller.admin.assembletask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 刀具装配任务 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssembleTaskRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "工单号")
    @ExcelProperty("工单号")
    private String orderNumber;

    @Schema(description = "需求数量")
    @ExcelProperty("需求数量")
    private Integer needCount;

    @Schema(description = "目标位置")
    @ExcelProperty("目标位置")
    private String targetLocation;
    private String targetLocationCode;

    @Schema(description = "配送截止时间")
    @ExcelProperty("配送截止时间")
    private LocalDateTime distributionDeadline;

    @Schema(description = "物料类型id")
    @ExcelProperty("物料类型id")
    private String materialConfigId;
    private String materialNumber;

    @Schema(description = "最短加工时长")
    @ExcelProperty("最短加工时长")
    private Integer minimumTime;

    @Schema(description = "状态（启用、作废）")
    @ExcelProperty(value = "状态（启用、作废）", converter = DictConvert.class)
    @DictFormat("infra_boolean_string") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

}