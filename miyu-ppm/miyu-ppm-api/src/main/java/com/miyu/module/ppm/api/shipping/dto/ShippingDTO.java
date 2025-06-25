package com.miyu.module.ppm.api.shipping.dto;

import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "RPC 服务 - 销售系统  发货单")
@Data
public class ShippingDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 合作方ID
     */
    private String companyId;
    /**
     * 发货人
     */
    private String consigner;
    /**
     * 发货日期
     */
    private LocalDateTime consignerDate;
    /**
     * 承运方式
     */

    private Integer deliveryMethod;
    /**
     * 承运人
     */
    private String deliveryBy;
    /**
     * 承运单号
     */
    private String deliveryNumber;
    /**
     * 承运人电话
     */
    private String deliveryContact;
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
     * 审批状态
     *
     * 枚举 {@link DMAuditStatusEnum}
     */
    private Integer status;

    /***
     * 附件地址
     */
    private String fileUrl;
    /***
     * 发货单
     */
    private String shippingNo;
    /***
     * 发货单名称
     */
    private String shippingName;

    /**
     * 状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消
     *
     * 枚举 {@link ShippingStatusEnum}
     */
    private Integer shippingStatus;

    /** 公司名,收货方 */
    private String companyName;

    /***
     * 发货单类型
     * 1销售发货2外协发货3采购退货4委托加工退货
     */
    private Integer shippingType;

    private List<ShippingDetailDTO> shippingDetailDTOList;
    /**
     * 退货原因
     */
    private String returnReason;



}
