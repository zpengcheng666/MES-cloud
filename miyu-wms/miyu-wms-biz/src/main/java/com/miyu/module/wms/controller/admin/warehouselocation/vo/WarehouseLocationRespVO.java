package com.miyu.module.wms.controller.admin.warehouselocation.vo;

import com.miyu.module.wms.controller.admin.operatingterminal.vo.TrayInfoVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库位 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseLocationRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "库位编码")
    @ExcelProperty("库位编码")
    private String locationCode;

    @Schema(description = "库位名称")
    @ExcelProperty("库位名称")
    private String locationName;

    @Schema(description = "库区id")
    @ExcelProperty("库区id")
    private String warehouseAreaId;

    @Schema(description = "库区编码")
    @ExcelProperty("库区编码")
    private String areaCode;

    @Schema(description = "是否锁定")
    @ExcelProperty("是否锁定")
    private Integer locked;

    @Schema(description = "是否有效")
    @ExcelProperty("是否有效")
    private Integer valid;

    @Schema(description = "仓库编码")
    private String warehouseCode;

    @Schema(description = "仓库id")
    private String warehouseId;

    @Schema(description = "库区名称")
    private String areaName;

    @Schema(description = "库区属性")
    private Integer areaProperty;

    @Schema(description = "库区类型")
    private Integer areaType;

    @Schema(description = "位（前台展示排序用）")
    private Integer site;

    @Schema(description = "托盘信息 前端展示")
    private TrayInfoVO trayInfo;

}

