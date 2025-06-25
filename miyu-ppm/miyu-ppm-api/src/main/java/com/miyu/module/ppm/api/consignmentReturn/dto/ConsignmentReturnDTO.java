package com.miyu.module.ppm.api.consignmentReturn.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 采购退货单")
@Data
public class ConsignmentReturnDTO {
    /** 主键id */
    private String id;

    /**
     * 退货单编号
     */
    private String no;
    /**
     * 退货单名称
     */
    private String name;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 退货人
     */
    private String consigner;
    /**
     * 退货日期
     */
    private LocalDateTime consignerDate;
    /**
     * 接收人
     */
    private String consignedBy;
    /**
     * 收货日期
     */
    private LocalDateTime consignedDate;
    /**
     * 收货人联系方式
     */
    private String consignedContact;
    /**
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link TODO return_type 对应的类}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;
    /**
     * 工作流编号
     */
    private String processInstanceId;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 状态  0已创建 1审批中 2待出库 3待确认 4 运输中 5结束 6审批不通过 7已作废
     *
     * 枚举 {@link TODO consignment_status 对应的类}
     */
    private Integer consignmentStatus;
    /**
     * 附件地址
     */
    private String fileUrl;
    /**
     * 备注
     */
    private String remark;

    private String contractName;

    private String partyName;

    private String ContractNum;

    private String companyName;

    private List<ConsignmentReturnDetailDTO> returnDetailDTOList;
}
