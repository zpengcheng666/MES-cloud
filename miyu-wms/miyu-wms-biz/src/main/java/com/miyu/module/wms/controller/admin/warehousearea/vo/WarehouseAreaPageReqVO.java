package com.miyu.module.wms.controller.admin.warehousearea.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库区分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarehouseAreaPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "库区名称")
    private String areaName;

    @Schema(description = "库区编码")
    private String areaCode;

    @Schema(description = "库区属性")
    private Integer areaProperty;

    @Schema(description = "通道")
    private Integer areaChannels;

    @Schema(description = "组")
    private Integer areaGroup;

    @Schema(description = "层")
    private Integer areaLayer;

    @Schema(description = "位")
    private Integer areaSite;

    @Schema(description = "库区类型", example = "2")
    private Integer areaType;

    @Schema(description = "仓库id")
    private String warehouseId;

}