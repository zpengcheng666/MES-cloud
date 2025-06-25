package com.miyu.module.wms.controller.admin.materialstock.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 物料库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialStockRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17578")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料类型id")
    private String materialConfigId;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编号")
    private String materialNumber;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @Schema(description = "物料批次号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料批次号")
    private String batchNumber;

    /*@Schema(description = "物料id")
    @ExcelProperty("物料id")
    private String materialId;*/

    @Schema(description = "储位id")
    @ExcelProperty("储位id")
    private String storageId;

    @Schema(description = "库位id")
    @ExcelProperty("库位id")
    private String locationId;

    @Schema(description = "绑定类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "绑定类型", converter = DictConvert.class)
    @DictFormat("wms_bind_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer bindType;

    @Schema(description = "总库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总库存")
    private Integer totality;

    @Schema(description = "锁定库存")
    @ExcelProperty("锁定库存")
    private Integer locked;

    @Schema(description = "可用库存")
    @ExcelProperty("可用库存")
    private Integer available;

    /*@Schema(description = "容器满载比例")
    @ExcelProperty("容器满载比例")
    private Integer byOccupancyRatio;*/

    @Schema(description = "占用比例")
    @ExcelProperty("占用比例")
    private Integer occupancyRatio;

    private String storageCode;
    private String locationCode;
    private String stockBarcode;
    private String materialCode;
    private String locationName;
    private String orderNumber;
    private String storageName;
    private String atWarehouseId;
    private String materialType;
    // 库位是否锁定
    private Boolean locationLocked;

    /**
     * 储位列表
     */
    private MaterialStorageDO[][][] childrens;

    //物料管理模式
    private Integer materialManage;
    // 物料类型名称
    private String materialTypeName;
    private String materialName;
}
