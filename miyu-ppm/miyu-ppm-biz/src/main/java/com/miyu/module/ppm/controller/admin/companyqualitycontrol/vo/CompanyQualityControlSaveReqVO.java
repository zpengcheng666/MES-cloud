package com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo;

import com.miyu.module.ppm.framework.operatelog.core.company.*;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 企业质量控制信息新增/修改 Request VO")
@Data
public class CompanyQualityControlSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25610")
    private String id;

    @Schema(description = "企业编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19277")
    @NotEmpty(message = "企业编号不能为空")
    private String companyId;

    @Schema(description = "质量管理体系认证")
    @DiffLogField(name = "质量管理体系认证", function = CompanyQuantityQmscParseFunction.NAME)
    private Integer qmsc;

    @Schema(description = "是否专职检验")
    @DiffLogField(name = "专职检验", function = CompanyQuantityInspectionParseFunction.NAME)
    private Integer inspection;

    @Schema(description = "是否不合格品控制")
    @DiffLogField(name = "不合格品控制", function = CompanyQuantityControlParseFunction.NAME)
    private Integer  nonconformingControl;

    @Schema(description = "生产可追溯")
    @DiffLogField(name = "生产可追溯", function = CompanyQuantityProductionTraceabilityParseFunction.NAME)
    private Integer productionTraceability;

    @Schema(description = "是否采购质量控制")
    @DiffLogField(name = "采购质量控制", function = CompanyQuantityPurchasingControlParseFunction.NAME)
    private Integer purchasingControl;

    @Schema(description = "出厂质量控制")
    @DiffLogField(name = "出厂质量控制", function = CompanyQuantityOqcParseFunction.NAME)
    private Integer oqc;

    @Schema(description = "备注", example = "随便")
    @DiffLogField(name = "备注")
    private String remark;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

}