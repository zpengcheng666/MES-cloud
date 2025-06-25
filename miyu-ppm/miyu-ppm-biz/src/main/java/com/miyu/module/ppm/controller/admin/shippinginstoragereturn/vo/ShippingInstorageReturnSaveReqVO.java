package com.miyu.module.ppm.controller.admin.shippinginstoragereturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售订单入库退货新增/修改 Request VO")
@Data
public class ShippingInstorageReturnSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "2094")
    private String id;

    @Schema(description = "入库单名称", example = "王五")
    private String name;

    @Schema(description = "入库单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单不能为空")
    private String no;

    @Schema(description = "合同ID", example = "20825")
    private String contractId;

    @Schema(description = "公司ID", example = "16936")
    private String companyId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23951")
    @NotEmpty(message = "项目ID不能为空")
    private String projectId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "审批状态不能为空")
    private Integer status;

    @Schema(description = "工作流编号", example = "262")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9取消 8审批失败", example = "2")
    private Integer shippingInstorageReturnStatus;

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

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "处理方式 1返修2换货3退货退款4仅退款不能为空")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不喜欢")
    @NotEmpty(message = "退换货原因不能为空")
    private String returnReason;

//    @Schema(description = "销售订单退货明细列表")
//    private List<ShippingInstorageReturnDetailDO> shippingInstorageReturnDetails;

}