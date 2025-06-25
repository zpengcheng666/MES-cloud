package com.miyu.module.wms.controller.admin.warehouse.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 仓库表 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "11444")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建者", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String creator;

    @Schema(description = "更新者", requiredMode = Schema.RequiredMode.REQUIRED, example = "admin")
    private String updater;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "仓库名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("仓库名称")
    private String warehouseName;

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库编码")
    private String warehouseCode;

    @Schema(description = "仓库地址")
    @ExcelProperty("仓库地址")
    private String warehouseAddress;

    @Schema(description = "仓库容量")
    @ExcelProperty("仓库容量")
    private String warehouseCapacity;

    @Schema(description = "仓库性质", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库性质")
    private Integer warehouseNature;

    @Schema(description = "仓库类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("仓库类型")
    private Integer warehouseType;

    @Schema(description = "仓库状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("仓库状态")
    private Integer warehouseState;

    @Schema(description = "仓库主管ID", example = "21840")
    private String userId;

    @Schema(description = "仓库主管名称", example = "启明")
    @ExcelProperty("仓库主管名称")
    private String nickName;

}