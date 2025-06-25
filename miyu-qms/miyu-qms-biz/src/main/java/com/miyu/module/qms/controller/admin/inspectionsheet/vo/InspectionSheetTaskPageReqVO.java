package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 检验单任务分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InspectionSheetTaskPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检验单名称", example = "李四")
    private String sheetName;

    @Schema(description = "检验单号")
    private String sheetNo;

    @Schema(description = "生产单号")
    private String recordNumber;

    @Schema(description = "方案类型")
    private Integer schemeType;

    @Schema(description = "工艺ID", example = "24002")
    private String technologyId;

    @Schema(description = "部门ID")
    private String deptId;

}
