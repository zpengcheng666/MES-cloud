package com.miyu.module.wms.controller.admin.materialconfigarea.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料类型关联库区配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialConfigAreaRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "库区ID")
    @ExcelProperty("库区ID")
    private String warehouseAreaId;

    @Schema(description = "库区编码")
    @ExcelProperty("库区编码")
    private String areaCode;

    @Schema(description = "物料类型ID")
    @ExcelProperty("物料类型ID")
    private String materialConfigId;

    @Schema(description = "物料类型名称")
    @ExcelProperty("物料编号")
    private String materialNumber;

}