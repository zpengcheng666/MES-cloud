package com.miyu.module.ppm.controller.admin.warehousedetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 入库详情表 对应仓库库存 来源WMS Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22626")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "入库单号(对应收货单号)", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入库单号(对应收货单号)")
    private String orderNumber;

    @Schema(description = "入库仓库Id", example = "24442")
    @ExcelProperty("入库仓库Id")
    private String warehouseId;

    @Schema(description = "入库状态(1.待质检 2.待入库 3.待上架 4.已完成 5.已关闭)")
    @ExcelProperty(value = "入库状态(1.待质检 2.待入库 3.待上架 4.已完成 5.已关闭)", converter = DictConvert.class)
    @DictFormat("wms_in_state") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer instate;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号")
    private String batchNumber;

    @Schema(description = "物料类型Id(对应产品编号)", example = "6538")
    @ExcelProperty("物料类型Id(对应产品编号)")
    private String materialConfigId;

    @Schema(description = "物料库存Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "7405")
    @ExcelProperty("物料库存Id")
    private String materialStockId;

    @Schema(description = "物料单位")
    @ExcelProperty("物料单位")
    private String meterialUnit;

    @Schema(description = "采购收货数量")
    @ExcelProperty("采购收货数量")
    private Long signedAmount;

    @Schema(description = "物料编号")
    @ExcelProperty("物料编号")
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "仓库编码")
    @ExcelProperty("仓库编码")
    private String warehouseCode;

    @Schema(description = "物料名称", example = "赵六")
    @ExcelProperty("物料名称")
    private String materialName;

    @Schema(description = "物料属性(1.成品 2.毛坯 3.辅助材料)")
    @ExcelProperty(value = "物料属性(1.成品 2.毛坯 3.辅助材料)", converter = DictConvert.class)
    @DictFormat("wms_material_type_properties") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer materialProperty;

    @Schema(description = "物料类型(1.零件 2.托盘 3.工装 4.夹具 5.刀具)", example = "1")
    @ExcelProperty(value = "物料类型(1.零件 2.托盘 3.工装 4.夹具 5.刀具)", converter = DictConvert.class)
    @DictFormat("wms_material_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer materialType;

    @Schema(description = "物料管理模式")
    @ExcelProperty("物料管理模式")
    private Integer materialManage;

    @Schema(description = "物料规格")
    @ExcelProperty("物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    @ExcelProperty("物料品牌")
    private String materialBrand;

    @Schema(description = "退货数量")
    @ExcelProperty("退货数量")
    private Long consignedAmount;

    @Schema(description = "库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库存")
    private Long quantity;

    /**
     * 是否免检  1是 2否
     */
    @Schema(description = "是否免检", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer qualityCheck;

}