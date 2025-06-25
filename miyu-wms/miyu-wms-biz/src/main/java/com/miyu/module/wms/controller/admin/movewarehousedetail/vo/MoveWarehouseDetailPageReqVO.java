package com.miyu.module.wms.controller.admin.movewarehousedetail.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 库存移动详情分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MoveWarehouseDetailPageReqVO extends PageParam {

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "移库单号")
    private String orderNumber;

    @Schema(description = "移库类型（生产移库，检验移库，调拨移库）")
    private Integer moveType;

    @Schema(description = "移库状态（待移交、待送达、待签收、已完成、已关闭）")
    private Integer moveState;

    @Schema(description = "起始仓库id")
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