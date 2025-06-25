package com.miyu.module.ppm.controller.admin.companyfinance.vo;

import cn.iocoder.yudao.framework.common.validation.Telephone;
import com.miyu.module.ppm.framework.operatelog.core.company.CompanyFinancePayParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 企业税务信息新增/修改 Request VO")
@Data
public class CompanyFinanceSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18664")
    private String id;

    @Schema(description = "公司ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7097")
    @NotEmpty(message = "公司ID不能为空")
    private String companyId;

    @Schema(description = "类型", example = "1")
    @DiffLogField(name = "账户类型", function = CompanyFinancePayParseFunction.NAME)
    private Integer type;

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "账号不能为空")
    @DiffLogField(name = "账号")
    private String accountNumber;

    @Schema(description = "银行")
    @DiffLogField(name = "银行")
    private String bank;

    @Schema(description = "地址")
    @DiffLogField(name = "地址")
    private String address;

    @Schema(description = "电话", example = "18000000000")
    @DiffLogField(name = "电话")
    @Telephone
    private String telephone;

    @Schema(description = "联行号")
    @DiffLogField(name = "联行号")
    private String bankAddress;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

}