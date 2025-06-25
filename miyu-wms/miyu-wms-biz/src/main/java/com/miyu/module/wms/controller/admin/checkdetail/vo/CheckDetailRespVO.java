package com.miyu.module.wms.controller.admin.checkdetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 库存盘点详情 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CheckDetailRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料id")
    @ExcelProperty("物料id")
    private String materialStockId;

    @Schema(description = "账面数量")
    @ExcelProperty("账面数量")
    private Integer realTotality;

    @Schema(description = "盘点数量")
    @ExcelProperty("盘点数量")
    private Integer checkTotality;

    @Schema(description = "盘点时间")
    @ExcelProperty("盘点时间")
    private LocalDateTime checkTime;

    @Schema(description = "盘点容器id")
    @ExcelProperty("盘点容器id")
    private String checkContainerId;

}