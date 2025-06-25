package com.miyu.module.ppm.controller.admin.company.vo;

import cn.iocoder.yudao.framework.common.validation.Telephone;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 企业基本信息新增/修改 Request VO")
@Data
public class CompanyCoordSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16888")
    private String id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "名称不能为空")
    private String name;

    @Schema(description = "统一社会信用代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "统一社会信用代码不能为空")
    private String usci;

    @Schema(description = "组织结构代码")
    private String organizationCode;

    @Schema(description = "公司状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "公司状态不能为空")
    private Integer companyStatus;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @Schema(description = "行业分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "行业分类")
    private Integer industryClassification;

    @Schema(description = "供求类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "供求类型")
    private Integer supplyType;

    @Schema(description = "成立时间")
    private LocalDateTime formed;

    @Schema(description = "注册资金", example = "100.00")
    @DiffLogField(name = "注册资金")
    @DecimalMin(value = "0", inclusive = false, message = "注册资金必须大于零")
    private BigDecimal registrationFund;

    @Schema(description = "纳税人资质")
    private Integer taxpayer;

    @Schema(description = "区域")
    private Integer area;

    @Schema(description = "注册地址")
    private String registrationAddress;

    @Schema(description = "企业规模")
    private Integer firmSize;

    @Schema(description = "电话", example = "18000000000")
    @DiffLogField(name = "电话")
    @Telephone
    private String telephone;

    @Schema(description = "邮箱", example = "123456789@qq.com")
    @DiffLogField(name = "邮箱")
    @Email
    private String email;

    @Schema(description = "公司官网")
    private String website;

    @Schema(description = "简介")
    private String introduction;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "流程key")
    private String processKey;

    @Schema(description = "附件", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "附件不能为空")
    private List<String> fileUrl;

}