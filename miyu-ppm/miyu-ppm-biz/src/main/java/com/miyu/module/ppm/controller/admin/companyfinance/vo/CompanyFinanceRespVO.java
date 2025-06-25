package com.miyu.module.ppm.controller.admin.companyfinance.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 企业税务信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CompanyFinanceRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18664")
    private String id;

    @Schema(description = "公司ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7097")
    private String companyId;

    @Schema(description = "公司名称")
    @ExcelProperty("公司名称")
    private String companyName;

    @Schema(description = "类型", example = "1")
    @ExcelProperty(value = "类型", converter = DictConvert.class)
    @DictFormat("pd_finance_pay_method")
    private Integer type;

    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("账号")
    private String accountNumber;

    @Schema(description = "银行")
    @ExcelProperty("银行")
    private String bank;

    @Schema(description = "地址")
    @ExcelProperty("地址")
    private String address;

    @Schema(description = "电话")
    @ExcelProperty("电话")
    private String telephone;

    @Schema(description = "联行号")
    @ExcelProperty("联行号")
    private String bankAddress;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;
}