package com.miyu.module.ppm.controller.admin.purchaseconsignment.vo;

import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailSaveReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;

@Schema(description = "管理后台 - 采购收货新增/修改 Request VO")
@Data
public class PurchaseConsignmentSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6249")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14173")
    private String contractId;

    private String projectId;

    @Schema(description = "收货人")
    private String consignedBy;

    @Schema(description = "收货单")
    private String no;

    @Schema(description = "收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "发货人")
    private String consigner;

    @Schema(description = "发货人联系方式")
    private String consignerContact;

    @Schema(description = "发货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "承运方式")
    private Integer deliveryMethod;

    @Schema(description = "承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人")
    private String deliveryBy;

    @Schema(description = "承运人电话")
    private String deliveryContact;

    @Schema(description = "流程实例编号", example = "18623")
    private String processInstanceId;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "收货明细列表")
    @NotEmpty(message = "详情不能为空")
    private List<PurchaseConsignmentDetailSaveReqVO> purchaseConsignmentDetails;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

    /**
     *  状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废
     *
     * 枚举 {@link ConsignmentStatusEnum}
     */
    @Schema(description = "0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废", example = "2")
    private Integer consignmentStatus;

    private Integer type;
    /***
     * 收货单名称
     */
    private String name;


    private Integer contractType;

    private Integer consignmentType;

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    private String returnReason;





}