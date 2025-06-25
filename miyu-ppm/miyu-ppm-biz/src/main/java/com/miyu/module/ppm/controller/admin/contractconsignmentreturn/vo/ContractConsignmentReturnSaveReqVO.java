package com.miyu.module.ppm.controller.admin.contractconsignmentreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 外协退货新增/修改 Request VO")
@Data
public class ContractConsignmentReturnSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "30078")
    private String id;

    @Schema(description = "入库单名称", example = "王五")
    private String name;

    @Schema(description = "入库单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单不能为空")
    private String no;

    @Schema(description = "合同ID", example = "17692")
    private String contractId;

    @Schema(description = "公司ID", example = "18415")
    private String companyId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4584")
    @NotEmpty(message = "项目ID不能为空")
    private String projectId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "审批状态不能为空")
    private Integer status;

    @Schema(description = "工作流编号", example = "17090")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态0已创建 1审批中 2 待签收 3待入库 4结束 8 审批失败 9作废", example = "1")
    private Integer returnStatus;

    @Schema(description = "退货人")
    private String consigner;

    @Schema(description = "退货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "接收人")
    private String consignedBy;

    @Schema(description = "收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "处理方式 1返修2换货3退货退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "处理方式 1返修2换货3退货退款不能为空")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @NotEmpty(message = "退换货原因不能为空")
    private String returnReason;


}