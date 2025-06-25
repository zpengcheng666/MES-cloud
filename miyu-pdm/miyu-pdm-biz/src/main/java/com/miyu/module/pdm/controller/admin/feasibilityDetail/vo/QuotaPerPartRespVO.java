package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 单件定额管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class QuotaPerPartRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "长度(mm)")
    private Integer length;

    @Schema(description = "宽度(mm)")
    private String width;

    @Schema(description = "厚度(mm)")
    private String height;

    @Schema(description = "材料牌号")
    private String materialDesg;

    @Schema(description = "材料状态")
    private String materialCondition;

    @Schema(description = "工时定额(min)")
    private String timeQuota;

}
