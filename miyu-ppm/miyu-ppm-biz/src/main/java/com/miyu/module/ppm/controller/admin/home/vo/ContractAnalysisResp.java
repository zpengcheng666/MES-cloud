package com.miyu.module.ppm.controller.admin.home.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售统计 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractAnalysisResp {

    /***
     * 年
     */
    private Integer year;
    /***
     * 月
     */
    private Integer month;

    /***
     * 销售金额
     */
    private BigDecimal price;
}



