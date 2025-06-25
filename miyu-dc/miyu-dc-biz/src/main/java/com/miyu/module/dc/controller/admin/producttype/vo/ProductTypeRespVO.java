package com.miyu.module.dc.controller.admin.producttype.vo;

import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 产品类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductTypeRespVO {

    @Schema(description = "产品类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32310")
    @ExcelProperty("产品类型id")
    private String Id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("产品类型名称")
    private String productTypeName;

    @Schema(description = "产品类型介绍", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品类型介绍")
    private String productTypeText;

    /**
     * 采集周期
     */
    private BigDecimal collectAttributesCycle;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private String topicId;

    /**
     * 采集周期
     */
    private BigDecimal collectAttributesType;

    /**
     * 采集属性
     */
    private List<CollectAttributesDO> collectAttributesDetails;

    /**
     * 采集属性格式
     */
    private String TypeCode;
}