package com.miyu.module.wms.controller.admin.warehouse.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 仓库表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarehousePageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Schema(description = "仓库性质")
    private Integer warehouseNature;

    @Schema(description = "仓库类型", example = "1")
    private Integer warehouseType;

    @Schema(description = "仓库状态")
    private Integer warehouseState;

    @Schema(description = "仓库主管", example = "21840")
    private String userId;

}