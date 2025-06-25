package com.miyu.module.wms.controller.admin.warehousearea.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 库区 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseAreaRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4729")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "仓库id")
    @ExcelProperty("仓库id")
    private String warehouseId;

    @Schema(description = "仓库编码")
    @ExcelProperty("仓库编码")
    private String warehouseCode;

    @Schema(description = "库区名称")
    @ExcelProperty("库区名称")
    private String areaName;

    @Schema(description = "库区编码")
    @ExcelProperty("库区编码")
    private String areaCode;

    @Schema(description = "库区属性")
    @ExcelProperty(value = "库区属性", converter = DictConvert.class)
    @DictFormat("wms_warehouse_area_property") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer areaProperty;

    @Schema(description = "库区长")
    @ExcelProperty("库区长")
    private Integer areaLength;

    @Schema(description = "库区宽")
    @ExcelProperty("库区宽")
    private Integer areaWidth;

    @Schema(description = "库区高")
    @ExcelProperty("库区高")
    private Integer areaHeight;

    @Schema(description = "库区承重")
    @ExcelProperty("库区承重")
    private Integer areaBearing;

    @Schema(description = "通道")
    @ExcelProperty("通道")
    private Integer areaChannels;

    @Schema(description = "组")
    @ExcelProperty("组")
    private Integer areaGroup;

    @Schema(description = "层")
    @ExcelProperty("层")
    private Integer areaLayer;

    @Schema(description = "位")
    @ExcelProperty("位")
    private Integer areaSite;

    @Schema(description = "库区类型", example = "2")
    @ExcelProperty("库区类型")
    private Integer areaType;

}