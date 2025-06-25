package com.miyu.module.qms.controller.admin.inspectionschemeitem.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 检验方案检测项目详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSchemeItemRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "6464")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "方案ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "28444")
    @ExcelProperty("方案ID")
    private String inspectionSchemeId;

    @Schema(description = "检测项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1875")
    @ExcelProperty("检测项目ID")
    private String inspectionItemId;

    @Schema(description = "检测顺序")
    @ExcelProperty("检测顺序")
    private Integer number;


    /**
     * 方案名称
     */
    @Schema(description = "方案名称")
    @ExcelProperty("方案名称")
    private String inspectionSchemeName;
    /***
     * 检测项名称
     */
    @Schema(description = "检测项名称")
    @ExcelProperty("检测项名称")
    private String inspectionItemName;



    @Schema(description = "分类名称")
    @ExcelProperty("检测分类名称")
    private String itemTypeName;

    @Schema(description = "检测工具")
    @ExcelProperty("检测工具")
    private String inspectionToolName;


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


    @Schema(description = "接收质量限")
    @ExcelProperty("接收质量限")
    private BigDecimal acceptanceQualityLimit;
}