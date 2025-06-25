package com.miyu.module.ppm.controller.admin.home.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 供应商采购数量统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConsignmentCompanyNumberRespVO {


    @Schema(description = "签收数量")
    private BigDecimal signedAmount;
    @Schema(description = "公司ID")
    private String companyId;
    @Schema(description = "公司名称")
    private String companyName;
    @Schema(description = "退货数量")
    private BigDecimal returnAmount;
    @Schema(description = "退货率")
    private  BigDecimal rate;

}