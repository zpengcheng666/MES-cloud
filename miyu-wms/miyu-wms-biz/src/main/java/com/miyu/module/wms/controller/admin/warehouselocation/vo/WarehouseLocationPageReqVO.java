package com.miyu.module.wms.controller.admin.warehouselocation.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库位分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarehouseLocationPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "库位编码")
    private String locationCode;

    @Schema(description = "库位名称")
    private String locationName;

    @Schema(description = "库区id")
    private String warehouseAreaId;

    @Schema(description = "是否锁定")
    private Integer locked;

    @Schema(description = "是否有效")
    private Integer valid;

}