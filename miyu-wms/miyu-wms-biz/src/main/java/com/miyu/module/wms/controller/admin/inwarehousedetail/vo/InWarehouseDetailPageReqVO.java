package com.miyu.module.wms.controller.admin.inwarehousedetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 入库详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class InWarehouseDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "入库单号 默认为来源单号；自建单则自动生成")
    private String orderNumber;

    @Schema(description = "入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）")
    private Integer inType;

    private String startWarehouseId;

    private String targetWarehouseId;

    @Schema(description = "入库状态（待质检、待入库、待上架、已完成、已关闭）")
    private Integer inState;

    @Schema(description = "物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "物料id")
    private String materialStockId;

    private String chooseStockId;

}