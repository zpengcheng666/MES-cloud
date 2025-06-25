package com.miyu.module.ppm.api.purchaseConsignment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "RPC 服务 - 采购系统  采购收货")
@Data
public class PurchaseConsignmentDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 收货单
     */
    private String no;
    /**
     * 收货人
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
     * 发货人
     */
    private String consigner;
    /**
     * 发货人联系方式
     */
    private String consignerContact;
    /**
     * 发货日期
     */
    private LocalDateTime consignerDate;
    /**
     * 承运方式
     *
     */
    private Integer deliveryMethod;
    /**
     * 承运单号
     */
    private String deliveryNumber;
    /**
     * 承运人
     */
    private String deliveryBy;
    /**
     * 承运人电话
     */
    private String deliveryContact;
    /**
     * 审批结果
     *
     */
    private Integer status;

    /**
     *  状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废
     *
     */
    private Integer consignmentStatus;

    private List<PurchaseConsignmentDetailDTO> detailDTOList;

    /** 公司名 */
    private String companyName;
    private String companyId;

    /***
     * 收货单名称
     */
    private String name;

    /***
     * 收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货
     */
    private Integer consignmentType;

    /** 退货原因 */
    private String returnReason;


}
