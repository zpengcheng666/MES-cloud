package com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 不合格品产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnqualifiedMaterialPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "检验单方案任务ID", example = "4041")
    private String inspectionSheetSchemeId;

    @Schema(description = "检验单产品ID", example = "26336")
    private String schemeMaterialId;

    @Schema(description = "不合格品登记ID", example = "13208")
    private String unqualifiedRegistrationId;

}