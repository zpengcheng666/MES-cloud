package com.miyu.module.ppm.controller.admin.company.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 企业基本信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CompanyRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String id;

    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "统一社会信用代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("统一社会信用代码")
    private String usci;

    @Schema(description = "组织结构代码")
    @ExcelProperty("组织结构代码")
    private String organizationCode;

    @Schema(description = "公司状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "公司状态不能为空")
    @DictFormat("pd_company_status")
    private Integer companyStatus;

    @Schema(description = "类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("pd_company_type")
    private Integer type;

    @Schema(description = "行业分类，参见国民经济行业分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "行业分类", converter = DictConvert.class)
    @DictFormat("pd_company_industry_classification")
    private Integer industryClassification;

    @Schema(description = "供求类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty(value = "供求类型", converter = DictConvert.class)
    @DictFormat("pd_company_supply_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer supplyType;

    @Schema(description = "成立时间")
    @ExcelProperty("成立时间")
    private LocalDateTime formed;

    @Schema(description = "注册资金")
    @ExcelProperty("注册资金")
    private BigDecimal registrationFund;

    @Schema(description = "纳税人资质")
    @ExcelProperty(value = "纳税人资质", converter = DictConvert.class)
    @DictFormat("pd_company_taxpayer")
    private Integer taxpayer;

    @Schema(description = "区域")
//    @ExcelProperty("区域")
    private Integer area;

    @Schema(description = "区域名称")
    private String areaName;

    @Schema(description = "注册地址")
    @ExcelProperty("注册地址")
    private String registrationAddress;

    @Schema(description = "企业规模，几个区间")
    @ExcelProperty(value = "企业规模", converter = DictConvert.class)
    @DictFormat("pd_company_firm_size")
    private Integer firmSize;

    @Schema(description = "电话")
    @ExcelProperty("电话")
    private String telephone;

    @Schema(description = "邮箱")
    @ExcelProperty("邮箱")
    private String email;

    @Schema(description = "公司官网")
    @ExcelProperty("公司官网")
    private String website;

    @Schema(description = "简介")
    @ExcelProperty("简介")
    private String introduction;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("pd_company_status")
    private Integer status;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "工作流编号")
    private String processInstanceId;

    @Schema(description = "供应商产品集合")
    private List productList;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    private List<String> fileUrl;
}