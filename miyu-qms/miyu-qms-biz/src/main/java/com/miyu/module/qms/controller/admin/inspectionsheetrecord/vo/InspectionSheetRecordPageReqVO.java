package com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验记录分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSheetRecordPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检测项目ID", example = "21030")
    private String inspectionSchemeItemId;

    @Schema(description = "测量结果")
    private String content;

    @Schema(description = "是否合格")
    private Integer inspectionResult;

    @Schema(description = "检验单物料表ID", example = "18307")
    private String schemeMaterialId;

    @Schema(description = "物料条码（如果批量的 根据数量生成多个）")
    private String barCode;

}