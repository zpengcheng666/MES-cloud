package com.miyu.module.wms.controller.admin.materialstock.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialStockPageReqVO extends PageParam {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "物料类型id")
    private String materialConfigId;

    @Schema(description = "物料条码")
    private String barCode;

    @Schema(description = "物料批次号")
    private String batchNumber;

    /*@Schema(description = "物料id")
    private String materialId;*/

    @Schema(description = "储位id")
    private String storageId;

    @Schema(description = "库位id")
    private String locationId;

    @Schema(description = "跟库位id")
    private String rootLocationId;

    @Schema(description = "绑定类型")
    private Integer bindType;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "库区类型")
    private List<Integer> warehouseAreaTypes;
}