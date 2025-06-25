package com.miyu.module.qms.controller.admin.inspectionschemeitem.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 检验方案检测项目详情新增/修改 Request VO")
@Data
public class InspectionSchemeItemSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "6464")
    private String id;

    @Schema(description = "方案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28444")
    @NotEmpty(message = "方案ID不能为空")
    private String inspectionSchemeId;

    @Schema(description = "检测项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1875")
    @NotEmpty(message = "检测项目ID不能为空")
    private String inspectionItemId;

    @Schema(description = "检测顺序")
    private Integer number;

    @Schema(description = "判断方式  文本   是否  数字  条件判断", example = "2")
    @ExcelProperty(value = "判断方式  文本   是否  数字  条件判断", converter = DictConvert.class)
    @DictFormat("reference_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer referenceType;

    @Schema(description = "上限值")
    @ExcelProperty("上限值")
    private BigDecimal maxValue;

    @Schema(description = "下限值")
    @ExcelProperty("下限值")
    private BigDecimal minValue;

    @Schema(description = "文本内容")
    @ExcelProperty("文本内容")
    private String content;

    @Schema(description = "判断")
    @ExcelProperty("判断")
    private Integer judgement;

    /**
     * 接收质量限（AQL）
     */
    @Schema(description = "接收质量限")
    @ExcelProperty("接收质量限")
    private BigDecimal acceptanceQualityLimit;

}