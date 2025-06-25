package com.miyu.module.ppm.controller.admin.warehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 入库详情表 对应仓库库存 来源WMS新增/修改 Request VO")
@Data
public class WarehouseDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22626")
    private String id;

    @Schema(description = "入库单号(对应收货单号)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单号(对应收货单号)不能为空")
    private String orderNumber;

    @Schema(description = "入库仓库Id", example = "24442")
    private String warehouseId;

    @Schema(description = "入库状态(1.待质检 2.待入库 3.待上架 4.已完成 5.已关闭)")
    private Integer instate;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料批次号不能为空")
    private String batchNumber;

    @Schema(description = "物料类型Id(对应产品编号)", example = "6538")
    private String materialConfigId;

    @Schema(description = "物料库存Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7405")
    @NotEmpty(message = "物料库存Id不能为空")
    private String materialStockId;

    @Schema(description = "物料单位")
    private String meterialUnit;

    @Schema(description = "采购收货数量")
    private Long signedAmount;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Schema(description = "物料名称", example = "赵六")
    private String materialName;

    @Schema(description = "物料属性(1.成品 2.毛坯 3.辅助材料)")
    private Integer materialProperty;

    @Schema(description = "物料类型(1.零件 2.托盘 3.工装 4.夹具 5.刀具)", example = "1")
    private Integer materialType;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "退货数量")
    private Long consignedAmount;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "库存不能为空")
    private Long quantity;

}