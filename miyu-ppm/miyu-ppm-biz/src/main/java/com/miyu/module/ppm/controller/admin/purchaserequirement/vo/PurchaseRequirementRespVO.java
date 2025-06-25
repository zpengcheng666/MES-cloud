package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 采购申请主 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseRequirementRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "81")
    private String id;

    @Schema(description = "采购类型", example = "2")
    @ExcelProperty(value = "采购类型", converter = DictConvert.class)
    @DictFormat("ppm_requirement_type")
    private Integer type;

    @Schema(description = "申请人")
    @ExcelProperty("申请人")
    private String applicant;

    @Schema(description = "申请部门")
    @ExcelProperty("申请部门")
    private String applicationDepartment;

    @Schema(description = "申请日期")
    @ExcelProperty("申请日期")
    private LocalDateTime applicationDate;

    @Schema(description = "申请理由", example = "不香")
    @ExcelProperty("申请理由")
    private String applicationReason;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("pm_requirement_audit_status")
    private Integer status;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    private String updatedIp;

    @Schema(description = "工作流编号")
    private String processInstanceId;

    @Schema(description = "采购申请详情列表")
    private List<PurchaseRequirementRespVO.Detail> details;

    @Schema(description = "采购申请详情")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail {

        @Schema(description = "主键")
        private String id;

        @Schema(description = "源单类型")
        private Integer sourceType;

        @Schema(description = "需求物料")
        private String requiredMaterial;

        @Schema(description = "单位")
        private String materialUnit;

        @Schema(description = "需求数量")
        private BigDecimal requiredQuantity;

        @Schema(description = "需求时间")
        private LocalDateTime requiredDate;

        @Schema(description = "预算单价")
        private BigDecimal estimatedPrice;

        @Schema(description = "供应商")
        private String supplier;

        @Schema(description = "物料名称")
        private String materialName;
    }

}