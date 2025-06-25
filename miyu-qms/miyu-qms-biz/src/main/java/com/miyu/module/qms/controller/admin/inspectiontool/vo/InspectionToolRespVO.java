package com.miyu.module.qms.controller.admin.inspectiontool.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 检测工具 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionToolRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "21822")
    private String id;

    @Schema(description = "检测工具名称", example = "张三")
    @ExcelProperty(value="检测工具名称")
    private String name;

    @Schema(description = "规格", example = "张三")
    @ExcelProperty(value="规格")
    private String spec;

    @Schema(description = "测量范围", example = "张三")
    @ExcelProperty(value="测量范围")
    private String measuringRange;

    @Schema(description = "准确等级", example = "张三")
    @ExcelProperty(value="准确等级")
    private String accuracyLevel;


    @Schema(description = "制造商", example = "张三")
    @ExcelProperty(value="制造商")
    private String manufacturer;

    @Schema(description = "出厂编号", example = "张三")
    @ExcelProperty(value="出厂编号")
    private String manufacturerNumber;


    @Schema(description = "本厂编号", example = "张三")
    @ExcelProperty(value="本厂编号")
    private String barCode;

    @Schema(description = "状态", example = "1")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("qms_inspection_tool_status")
    private Integer status;

    @Schema(description = "检/校准日期", example = "张三")
    @ExcelProperty(value="检/校准日期")
    private LocalDateTime verificationDate;

    @Schema(description = "检/校定周期", example = "张三")
    @ExcelProperty(value="检/校定周期")
    private String verificationCycle;

    @Schema(description = "库存主键", example = "21822")
    private String stockId;

    @Schema(description = "物料类型主键", example = "21822")
    private String materialConfigId;

    @Schema(description = "物料类型名称", example = "21822")
    private String materialConfigName;

    @Schema(description = "创建时间")
    @ExcelProperty(value="创建时间")
    private LocalDateTime createTime;
}