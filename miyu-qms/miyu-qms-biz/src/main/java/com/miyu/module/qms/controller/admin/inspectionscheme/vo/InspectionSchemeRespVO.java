package com.miyu.module.qms.controller.admin.inspectionscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 检验方案 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InspectionSchemeRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15949")
    private String id;

    @Schema(description = "方案名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("方案名称")
    private String schemeName;

    @Schema(description = "方案编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("方案编号")
    private String schemeNo;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "方案类型", converter = DictConvert.class)
    @DictFormat("qms_scheme_type")
    private Integer schemeType;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13208")
    private String materialConfigId;

    @Schema(description = "检验级别", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer inspectionLevel;

    @Schema(description = "物料编号")
    @ExcelProperty("物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "赵六")
    @ExcelProperty("物料名称")
    private String materialName;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）", example = "2")
    private Integer materialType;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）", example = "2")
    @ExcelProperty("物料类型")
    private String materialTypeName;

    @Schema(description = "物料规格")
    @ExcelProperty("物料规格")
    private String materialSpecification;

    @Schema(description = "物料单位")
    @ExcelProperty("物料单位")
    private String materialUnit;

    @Schema(description = "工艺ID", example = "24002")
    private String technologyId;

    @Schema(description = "工序ID", example = "12830")
    private String processId;

    @Schema(description = "抽样标准", example = "5316")
    private String samplingStandardId;

    @Schema(description = "抽样标准", example = "5316")
    private String samplingStandardName;

    @Schema(description = "抽样准则", example = "5316")
    private Integer samplingLimitType;

    @Schema(description = "接收质量限")
    private BigDecimal acceptanceQualityLimit;

    /**
     * 检验类型1抽检2全检
     */
    @Schema(description = "检验类型")
    @ExcelProperty(value = "检验类型", converter = DictConvert.class)
    @DictFormat("qms_inspection_sheet_type")
    private Integer inspectionSheetType;

    /**
     * 检验水平类型
     */
    @Schema(description = "检验水平")
    @DictFormat("inspection_level_type")
    private Integer inspectionLevelType;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     */
    @Schema(description = "抽样类型")
    @DictFormat("sampling_rule_type")
    private Integer samplingRuleType;


    @Schema(description = "工艺名称")
    private String technologyName;

    @Schema(description = "工艺规程编号", example = "123")
    private String processCode;

    @Schema(description = "工艺规程名称")
    @ExcelProperty("工艺")
    private String processName;

    @Schema(description = "工序序号")
    private String procedureNum;

    @Schema(description = "工序名称(加工路线)")
    @ExcelProperty("工序")
    private String procedureName;


    @Schema(description = "是否生效", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否生效")
    private Integer isEffective;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}
