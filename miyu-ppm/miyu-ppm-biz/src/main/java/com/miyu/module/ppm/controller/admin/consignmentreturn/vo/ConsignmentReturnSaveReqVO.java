package com.miyu.module.ppm.controller.admin.consignmentreturn.vo;

import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 销售退货单新增/修改 Request VO")
@Data
public class ConsignmentReturnSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4930")
    private String id;

    @Schema(description = "退货单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "退货单编号不能为空")
    private String consignmentReturnNo;

    @Schema(description = "退货单名称", example = "王五")
    private String consignmentReturnName;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20431")
    @NotEmpty(message = "合同ID不能为空")
    private String contractId;

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

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "处理方式 1返修2换货3退货退款4仅退款不能为空")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @NotEmpty(message = "退换货原因不能为空")
    private String returnReason;

    @Schema(description = "工作流编号", example = "7107")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废", example = "1")
    private Integer consignmentStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "出库仓库ID", example = "你猜")
    private String warehouseId;

    @Schema(description = "销售退货单详情列表")
    @NotEmpty(message = "销售退货单详情不能为空")
    private List<ConsignmentReturnDetailDO> consignmentReturnDetails;





}