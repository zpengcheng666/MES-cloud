package com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo;

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

@Schema(description = "管理后台 - 入库异常处理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class InboundExceptionHandlingRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29034")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "产品表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19220")
    @ExcelProperty("产品表ID")
    private String infoId;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7692")
    @ExcelProperty("入库单ID")
    private String consignmentId;

    @Schema(description = "入库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入库单号")
    private String no;

    @Schema(description = "应收数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("应收数量")
    private BigDecimal consignedAmount;

    @Schema(description = "实收数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实收数量")
    private BigDecimal signedAmount;

    @Schema(description = "处理人")
    @ExcelProperty("处理人")
    private String handleBy;

    @Schema(description = "处理日期")
    @ExcelProperty("处理日期")
    private LocalDateTime handleTime;

    @Schema(description = "创建日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建日期")
    private LocalDateTime createTime;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25094")
    @ExcelProperty("物料类型ID")
    private String materialConfigId;

    @Schema(description = "状态  0待处理  1已处理", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "状态  0待处理  1已处理", converter = DictConvert.class)
    @DictFormat("ppm_exception_handle_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "类型 1采购收货 2外协退货 3原材料入库 4 销售退货", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "类型 1采购收货 2外协退货 3原材料入库 4 销售退货", converter = DictConvert.class)
    @DictFormat("consignment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer consignmentType;

    @Schema(description = "处理结果  1入库 2退货", example = "1")
    @ExcelProperty(value = "处理结果  1入库 2退货", converter = DictConvert.class)
    @DictFormat("ppm_exception_handle_result") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer rusultType;

    @Schema(description = "异常类型 1来货不足 2收货收多 ", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("异常类型 1来货不足 2收货收多 ")
    private Integer exceptionType;

    @Schema(description = "合同ID", example = "5401")
    @ExcelProperty("合同ID")
    private String contractId;
    private String contractName;
    private String contractNo;

    @Schema(description = "项目ID", example = "32229")
    @ExcelProperty("项目ID")
    private String projectId;
    private String projectName;
    private String projectCode;

    @Schema(description = "公司ID", example = "18492")
    @ExcelProperty("公司ID")
    private String companyId;
    private String companyName;

}