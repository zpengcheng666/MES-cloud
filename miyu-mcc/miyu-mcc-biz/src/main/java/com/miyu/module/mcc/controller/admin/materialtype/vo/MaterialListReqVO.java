package com.miyu.module.mcc.controller.admin.materialtype.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 编码类别属性表(树形结构)列表 Request VO")
@Data
public class MaterialListReqVO {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "王五")
    private String name;

    @Schema(description = "父类型id", example = "24179")
    private String parentId;

    @Schema(description = "位数")
    private Integer bitNumber;


    @Schema(description = "分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer encodingProperty;

}