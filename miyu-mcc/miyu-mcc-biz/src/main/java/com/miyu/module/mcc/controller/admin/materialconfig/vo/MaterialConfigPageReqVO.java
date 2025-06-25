package com.miyu.module.mcc.controller.admin.materialconfig.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料类型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialConfigPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "王五")
    private String materialName;

    @Schema(description = "当前类别", example = "8228")
    private String materialTypeId;

    @Schema(description = "主类别（工件、托盘、工装、夹具、刀具）", example = "22245")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "来源物料类型", example = "31201")
    private String materialSourceId;

    @Schema(description = "类码")
    private String materialTypeCode;

    @Schema(description = "物料管理模式")
    private Integer materialManage;
}