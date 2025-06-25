package com.miyu.module.ppm.controller.admin.companyproduct.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 企业产品表，用于销售和采购新增/修改 Request VO")
@Data
public class CompanyProductSaveReqVO {

    @Schema(description = "编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1699")
    private String id;

    @Schema(description = "企业ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18942")
    @NotEmpty(message = "企业ID不能为空")
    private String companyId;

    @Schema(description = "初始单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "8667")
    private BigDecimal initPrice;

    @Schema(description = "初始税率", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String initTax;

    @Schema(description = "供货周期", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer leadTime;

    @Schema(description = "是否免检", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer qualityCheck;

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

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private String materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;
    @Schema(description = "主物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "物料类型ID")
    private String materialId;

}