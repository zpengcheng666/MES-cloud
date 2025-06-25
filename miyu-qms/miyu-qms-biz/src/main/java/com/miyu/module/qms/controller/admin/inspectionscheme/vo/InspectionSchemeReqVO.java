package com.miyu.module.qms.controller.admin.inspectionscheme.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验方案 Request VO")
@Data
@ToString(callSuper = true)
public class InspectionSchemeReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "方案名称", example = "芋艿")
    private String schemeName;

    @Schema(description = "方案编号")
    private String schemeNo;

    @Schema(description = "方案类型 来料检验  过程检验 完工检验", example = "1")
    private Integer schemeType;

    @Schema(description = "物料类型ID", example = "13208")
    private String materialConfigId;

    @Schema(description = "物料条码", example = "13208")
    private String barCode;

    @Schema(description = "检验级别")
    private Integer inspectionLevel;

    @Schema(description = "是否生效")
    private Integer isEffective;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "赵六")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）")
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）", example = "2")
    private Integer materialType;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "工艺ID", example = "24002")
    private String technologyId;

    @Schema(description = "工序ID", example = "12830")
    private String processId;

    @Schema(description = "抽样规则ID", example = "5316")
    private String samplingStandardId;

}