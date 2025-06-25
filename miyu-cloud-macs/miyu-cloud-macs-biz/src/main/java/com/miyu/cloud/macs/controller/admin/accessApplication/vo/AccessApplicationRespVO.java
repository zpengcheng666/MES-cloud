package com.miyu.cloud.macs.controller.admin.accessApplication.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 通行申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AccessApplicationRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6123")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "申请单号")
    @ExcelProperty("申请单号")
    private String applicationNumber;

    @Schema(description = "申请代理人")
    @ExcelProperty("申请代理人")
    private String agent;

    @Schema(description = "公司/组织")
    @ExcelProperty("公司/组织")
    private String organization;

    @Schema(description = "部门")
    @ExcelProperty("部门")
    private String department;

    @Schema(description = "申请原因/目的", example = "不香")
    @ExcelProperty("申请原因/目的")
    private String reason;

    @Schema(description = "申请状态", example = "1")
    @ExcelProperty("申请状态")
    private String status;

    @Schema(description = "创建人")
    @ExcelProperty("创建人")
    private String createBy;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新人")
    @ExcelProperty("更新人")
    private String updateBy;

}