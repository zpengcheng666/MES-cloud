package com.miyu.module.qms.controller.admin.inspectionscheme.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验方案分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSchemePageReqVO extends PageParam {

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

    @Schema(description = "检验级别")
    private Integer inspectionLevel;

    @Schema(description = "是否生效")
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

    @Schema(description = "物料类型ID", example = "5316")
    private String materialTypeId;

    @Schema(description = "物料类型id集合")
    private List<String> materialConfigIds;

}
