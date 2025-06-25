package com.miyu.module.ppm.controller.admin.purchaserequirement.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 采购需求明细，可以来源于采购申请或MRP Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseRequirementDetailRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "11930")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "申请单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "17326")
    @ExcelProperty("申请单ID")
    private String requirementId;

    @Schema(description = "源单类型", example = "1")
    @ExcelProperty("源单类型")
    private Integer sourceType;

    @Schema(description = "源单id", example = "22119")
    @ExcelProperty("源单id")
    private String sourceId;

    @Schema(description = "需求物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求物料")
    private String requiredMaterial;

    @Schema(description = "需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "已采购数量")
    @ExcelProperty("已采购数量")
    private BigDecimal purchasedQuantity;

    @Schema(description = "需求时间")
    @ExcelProperty("需求时间")
    private LocalDateTime requiredDate;

    @Schema(description = "预算单价", example = "6721")
    @ExcelProperty("预算单价")
    private BigDecimal estimatedPrice;

    @Schema(description = "供应商，即企业ID")
    @ExcelProperty("供应商，即企业ID")
    private String supplier;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    @ExcelProperty("创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    @ExcelProperty("更新ip")
    private String updatedIp;

    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "物料名称")
    private String requiredMaterialName;

    @Schema(description = "编号")
    private String number;


    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "当前类名称")
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @Schema(description = "主类别名")
    private String materialParentTypeName;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;


    /**
     * 项目id
     */
    private String projectId;
    private String projectName;
    private String projectCode;
    /**
     *订单id
     */
    private String orderId;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目子计划id
     */
    private String projectPlanItemId;
    /**
     * 计划类型(1普通加工,2外协,3工序外协)
     */
    private Integer planType;



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


}
