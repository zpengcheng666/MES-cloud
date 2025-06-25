package com.miyu.module.ppm.api.company.dto;


import com.fhs.core.trans.vo.VO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 采购系统 企业 Response DTO")
@Data
public class CompanyRespDTO implements VO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")

    private String id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")

    private String name;

    @Schema(description = "统一社会信用代码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String usci;

    @Schema(description = "组织结构代码")
    private String organizationCode;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "行业分类，参见国民经济行业分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer industryClassification;

    @Schema(description = "供求类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer supplyType;

    @Schema(description = "电话")
    private String telephone;


}