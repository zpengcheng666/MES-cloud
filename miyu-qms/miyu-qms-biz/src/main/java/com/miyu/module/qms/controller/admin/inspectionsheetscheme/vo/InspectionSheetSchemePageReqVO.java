package com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验单方案任务计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSheetSchemePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检验单Id", example = "30999")
    private String inspectionSheetId;

    @Schema(description = "分配类型 1人员 2班组", example = "1")
    private Integer assignmentType;

    @Schema(description = "分配的检验人员", example = "4880")
    private String assignmentId;

    @Schema(description = "分配日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] assignmentDate;

    @Schema(description = "质检状态  0待派工1待检验2检验中3检验完成", example = "1")
    private Integer status;

    @Schema(description = "方案类型 来料检验  生产检验", example = "1")
    private Integer schemeType;

    @Schema(description = "检验类型1抽检2全检", example = "1")
    private Integer inspectionSheetType;

    @Schema(description = "是否首检")
    private Integer needFirstInspection;

    @Schema(description = "物料类型ID", example = "32568")
    private String materialConfigId;

    @Schema(description = "工艺ID", example = "8843")
    private String technologyId;

    @Schema(description = "工序ID", example = "25350")
    private String processId;

    @Schema(description = "检验方案ID", example = "31934")
    private String inspectionSchemeId;

    @Schema(description = "通过准则")
    private String passRule;

    @Schema(description = "计划检验时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planTime;

    @Schema(description = "实际开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] beginTime;

    @Schema(description = "实际结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "检测数量")
    private BigDecimal inspectionQuantity;

    @Schema(description = "合格数量")
    private BigDecimal qualifiedQuantity;

    @Schema(description = "检测结果 1合格 2不合格")
    private Integer inspectionResult;


    @Schema(description = "检验单名称")
    private String sheetName;

    @Schema(description = "检验单号")
    private String sheetNo;

    @Schema(description = "生产单号")
    private String recordNumber;

}
