package com.miyu.module.ppm.controller.admin.companyproduct.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 企业产品表，用于销售和采购 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CompanyProductRespVO {

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1699")
    private String id;

    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18942")
    private String companyId;

    @Schema(description = "material表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7721")
    private String materialId;

    @Schema(description = "初始单价", example = "8667")
    @ExcelProperty(value="初始单价", order = 2)
    private BigDecimal initPrice;

    @Schema(description = "初始税率", example = "1")
    @ExcelProperty(value="初始税率", order = 3)
    private String initTax;

    @Schema(description = "供货周期")
    @ExcelProperty(value="供货周期", order = 4)
    private Integer leadTime;

    @Schema(description = "是否免检")
    @ExcelProperty(value="是否免检", order = 5, converter = DictConvert.class)
    @DictFormat("pd_product_quality_check")
    private Integer qualityCheck;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

//    @Schema(description = "物料属性",requiredMode = Schema.RequiredMode.REQUIRED)
//    private Integer materialProperty;

    @Schema(description = "物料类型",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;


    @Schema(description = "主类别（工件、托盘、工装、夹具、刀具）", example = "22245")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    private String companyName;

    @ExcelProperty(value="物料名称", order = 1)
    private String productName;

    private String avgPrice;

    private String maxPrice;

    private String minPrice;

    private String latestPrice;
}