package com.miyu.module.qms.controller.admin.inspectionscheme.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.constraints.*;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;

@Schema(description = "管理后台 - 检验方案新增/修改 Request VO")
@Data
public class InspectionSchemeSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "15949")
    private String id;

    @Schema(description = "方案名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "方案名称不能为空")
    private String schemeName;

    @Schema(description = "方案编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "方案编号不能为空")
    private String schemeNo;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "方案类型 来料检验  过程检验 完工检验不能为空")
    private Integer schemeType;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13208")
    @NotEmpty(message = "物料类型ID不能为空")
    private String materialConfigId;

    @Schema(description = "检验级别", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "检验级别不能为空")
    private Integer inspectionLevel;

    @Schema(description = "是否生效", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isEffective;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "赵六")
    private String materialName;

    @Schema(description = "工艺ID", example = "24002")
    private String technologyId;

    @Schema(description = "工序ID", example = "12830")
    private String processId;

    @Schema(description = "抽样规则ID", example = "5316")
    private String samplingStandardId;

    @Schema(description = "抽样准则", example = "5316")
    private Integer samplingLimitType;

    @Schema(description = "接收质量限")
    @ExcelProperty("接收质量限")
    private BigDecimal acceptanceQualityLimit;

    /**
     * 检验类型1抽检2全检
     */
    @Schema(description = "检验类型")
    @ExcelProperty("检验类型")
    private Integer inspectionSheetType;

    /**
     * 检验水平类型
     */
    @Schema(description = "检验水平")
    @ExcelProperty("检验水平")
    private Integer inspectionLevelType;
    /**
     * 类型  1正常检查2加严检查3放宽检查
     */
    @Schema(description = "抽样类型")
    @ExcelProperty("抽样类型")
    private Integer samplingRuleType;

    @Schema(description = "是否检验")
    private String isInspect;

    @Schema(description = "检验方案检测项目详情列表")
    private List<InspectionSchemeItemDO> inspectionSchemeItems;

}
