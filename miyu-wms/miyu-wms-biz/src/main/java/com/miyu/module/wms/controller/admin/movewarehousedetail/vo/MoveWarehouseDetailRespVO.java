package com.miyu.module.wms.controller.admin.movewarehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 库存移动详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MoveWarehouseDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "移库单号")
    @ExcelProperty("移库单号")
    private String orderNumber;

    @Schema(description = "移库类型（生产移库，检验移库，调拨移库）")
    @ExcelProperty(value = "移库类型（生产移库，检验移库，调拨移库）", converter = DictConvert.class)
    @DictFormat("wms_out_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer moveType;

    @Schema(description = "移库状态（待移交、待送达、待签收、已完成、已关闭）")
    @ExcelProperty(value = "移库状态（待移交、待送达、待签收、已完成、已关闭）", converter = DictConvert.class)
    @DictFormat("wms_out_state") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer moveState;

    @Schema(description = "起始仓库id")
    @ExcelProperty("起始仓库id")
    private String startWarehouseId;

    @Schema(description = "目标仓库id")
    @ExcelProperty("目标仓库id")
    private String targetWarehouseId;

    @Schema(description = "物料批次号(冗余方便查询)")
    @ExcelProperty("物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)")
    @ExcelProperty("物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "物料库存id")
    @ExcelProperty("物料库存id")
    private String materialStockId;
    private String chooseStockId;

    @Schema(description = "数量")
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

    private String startWarehouseCode;

    private String targetWarehouseCode;


}