package com.miyu.module.wms.controller.admin.takedelivery.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料收货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class TakeDeliveryRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "15644")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "到货单号")
    @ExcelProperty("到货单号")
    private String orderNumber;

    @Schema(description = "物料类型id", example = "19072")
    @ExcelProperty("物料类型id")
    private String materialConfigId;

    @Schema(description = "收货数量")
    @ExcelProperty("收货数量")
    private Integer tdQuantity;

    @Schema(description = "绑定库位", example = "17995")
    @ExcelProperty("绑定库位")
    private String locationId;

    @Schema(description = "绑定储位", example = "9281")
    @ExcelProperty("绑定储位")
    private String storageId;

    @Schema(description = "绑定物料", example = "17710")
    @ExcelProperty("绑定物料")
    private String materialId;

}