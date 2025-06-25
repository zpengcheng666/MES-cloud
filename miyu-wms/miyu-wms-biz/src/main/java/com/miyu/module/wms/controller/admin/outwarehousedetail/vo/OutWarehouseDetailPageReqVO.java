package com.miyu.module.wms.controller.admin.outwarehousedetail.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出库详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutWarehouseDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "出库单号")
    private String orderNumber;

    @Schema(description = "出库类型（销售出库、外协出库、生产出库、检验出库、报损出库、采购退货出库、调拨出库、其他出库）")
    private Integer outType;

    @Schema(description = "出库状态（待出库、待送达、待签收、已完成、已关闭）")
    private Integer outState;

    @Schema(description = "出库仓库id")
    private String startWarehouseId;

    @Schema(description = "目标仓库id")
    private String targetWarehouseId;

    @Schema(description = "物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "物料库存id")
    private String materialStockId;
    private String chooseStockId;

}