package com.miyu.module.wms.controller.admin.outwarehousedetail.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 出库详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OutWarehouseDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "出库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库单号")
    private String orderNumber;

    @Schema(description = "出库类型（销售出库、外协出库、生产出库、检验出库、报损出库、采购退货出库、调拨出库、其他出库）")
    @ExcelProperty("出库类型（销售出库、外协出库、生产出库、检验出库、报损出库、采购退货出库、调拨出库、其他出库）")
    private Integer outType;

    @Schema(description = "出库状态（待出库、待送达、待签收、已完成、已关闭）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库状态（待出库、待送达、待签收、已完成、已关闭）")
    private Integer outState;

    @Schema(description = "出库仓库id")
    @ExcelProperty("出库仓库id")
    private String startWarehouseId;

    @Schema(description = "目标仓库id")
    @ExcelProperty("目标仓库id")
    private String targetWarehouseId;

    @Schema(description = "物料批次号(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "物料库存id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料库存id")
    private String materialStockId;
    private String chooseStockId;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数量")
    private Integer quantity;

    @Schema(description = "操作人")
    @ExcelProperty("操作人")
    private String operator;
    @Schema(description = "操作时间")
    @ExcelProperty("操作时间")
    private LocalDateTime operateTime;
    @Schema(description = "签收人")
    @ExcelProperty("签收人")
    private String signer;
    @Schema(description = "签收时间")
    @ExcelProperty("签收时间")
    private LocalDateTime signTime;

    private String materialNumber;

    private String realBarCode;
    private String chooseBarCode;

    private String warehouseCode;

    private String targetWarehouseCode;

}