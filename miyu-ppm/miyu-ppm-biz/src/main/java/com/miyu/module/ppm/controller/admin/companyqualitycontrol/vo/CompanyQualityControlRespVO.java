package com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 企业质量控制信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CompanyQualityControlRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25610")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "企业编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19277")
    @ExcelProperty("企业编号")
    private String companyId;

    @Schema(description = "质量管理体系认证")
    @ExcelProperty("质量管理体系认证")
    private Integer qmsc;

    @Schema(description = "是否专职检验")
    @ExcelProperty("是否专职检验")
    private Integer inspection;

    @Schema(description = "是否不合格品控制")
    @ExcelProperty("是否不合格品控制")
    private Integer  nonconformingControl;

    @Schema(description = "生产可追溯")
    @ExcelProperty("生产可追溯")
    private Integer productionTraceability;

    @Schema(description = "是否采购质量控制")
    @ExcelProperty("是否采购质量控制")
    private Integer purchasingControl;

    @Schema(description = "出厂质量控制")
    @ExcelProperty("出厂质量控制")
    private Integer oqc;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    @ExcelProperty("创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    @ExcelProperty("更新ip")
    private String updatedIp;

    private String companyName;
}