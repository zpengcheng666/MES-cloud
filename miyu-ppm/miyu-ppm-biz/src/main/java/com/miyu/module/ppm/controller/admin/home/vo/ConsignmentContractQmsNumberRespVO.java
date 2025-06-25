package com.miyu.module.ppm.controller.admin.home.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 采购合同检测统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConsignmentContractQmsNumberRespVO {


    @Schema(description = "检测数量")
    private BigDecimal checkAmount;
    @Schema(description = "编号")
    private String no;
    @Schema(description = "不合格数量")
    private BigDecimal unQuantityAmount;
    @Schema(description = "合格率")
    private  BigDecimal rate;

    @Schema(description = "采购数")
    private BigDecimal totalAmount;

}