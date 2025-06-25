package com.miyu.module.ppm.controller.admin.contractconsignmentreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 外协退货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractConsignmentReturnRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "30078")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "入库单名称", example = "王五")
    @ExcelProperty("入库单名称")
    private String name;

    @Schema(description = "入库单", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("入库单")
    private String no;

    @Schema(description = "合同ID", example = "17692")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "公司ID", example = "18415")
    @ExcelProperty("公司ID")
    private String companyId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4584")
    @ExcelProperty("项目ID")
    private String projectId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("审批状态")
    private Integer status;

    @Schema(description = "工作流编号", example = "17090")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    @ExcelProperty("附件地址")
    private String fileUrl;

    @Schema(description = "状态0已创建 1审批中 2 待签收 3待入库 4结束 8 审批失败 9作废", example = "1")
    @ExcelProperty("状态0已创建 1审批中 2 待签收 3待入库 4结束 8 审批失败 9作废")
    private Integer returnStatus;

    @Schema(description = "退货人")
    @ExcelProperty("退货人")
    private String consigner;

    @Schema(description = "退货日期")
    @ExcelProperty("退货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "接收人")
    @ExcelProperty("接收人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @ExcelProperty("收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    @ExcelProperty("收货人联系方式")
    private String consignedContact;

    @Schema(description = "处理方式 1返修2换货3退货退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("处理方式 1返修2换货3退货退款")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @ExcelProperty("退换货原因")
    private String returnReason;

}