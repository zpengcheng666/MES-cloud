package com.miyu.module.wms.controller.admin.inwarehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 入库详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InWarehouseDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "入库单号 默认为来源单号；自建单则自动生成", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入库单号 默认为来源单号；自建单则自动生成")
    private String orderNumber;

    @Schema(description = "入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "入库类型（采购入库、外协入库、生产入库、退料入库、检验入库、其他入库）", converter = DictConvert.class)
    @DictFormat("wms_in_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer inType;


    private String startWarehouseId;

    @Schema(description = "入库仓库ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入库仓库ID")
    private String targetWarehouseId;

    @Schema(description = "入库状态（待质检、待入库、待上架、已完成、已关闭）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "入库状态（待质检、待入库、待上架、已完成、已关闭）", converter = DictConvert.class)
    @DictFormat("wms_in_state_detail") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer inState;

    @Schema(description = "物料批次号(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号(冗余方便查询)")
    private String batchNumber;

    @Schema(description = "物料类型id(冗余方便查询)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料类型id(冗余方便查询)")
    private String materialConfigId;

    @Schema(description = "物料id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料id")
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

    private String chooseBarCode;

    private String realBarCode;

    private String startWarehouseCode;
    private String warehouseCode;

}