package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验单产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSheetSchemeMaterialPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "测量结果")
    private String content;

    @Schema(description = "是否合格")
    private Integer inspectionResult;

    @Schema(description = "物料ID", example = "3274")
    private String materialId;

    @Schema(description = "物料类型ID", example = "12392")
    private String materialConfigId;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

    @Schema(description = "检验单任务主键")
    private String inspectionSheetSchemeId;

    @Schema(description = "排除产品id集合")
    private List<String> excludeIds;

    @Schema(description = "仓库编码")
    private String warehouseCode;

}
