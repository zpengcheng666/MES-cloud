package com.miyu.module.wms.controller.admin.materialmaintenance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 物料维护记录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialMaintenanceRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料库存id")
    @ExcelProperty("物料库存id")
    private String materialStockId;

    private String barCode;
    private String materialNumber;
    private String materialName;

    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer quantity;

    @Schema(description = "维护类型(1报废，2装配，3加工)")
    @ExcelProperty(value = "维护类型(1报废，2装配，3加工)", converter = DictConvert.class)
    @DictFormat("material_maintenance_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

}