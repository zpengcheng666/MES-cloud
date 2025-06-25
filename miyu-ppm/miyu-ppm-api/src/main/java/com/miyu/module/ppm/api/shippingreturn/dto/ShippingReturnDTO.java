package com.miyu.module.ppm.api.shippingreturn.dto;

import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "RPC 服务 - 销售系统  退货单")
@Data
public class ShippingReturnDTO {

    /**
     * 主键
     */
    private String id;


    /**
     * 退货单编号
     */
    private String no;
    /**
     * 退货单名称
     */
    private String name;
//    /**
//     * 发货单
//     */
//    private String shippingId;
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
     * 枚举 {@link ShippingReturnTypeEnum}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 状态  0已创建 1审批中 2 待签收 3待入库 4结束 8 审批失败 9作废
     * 枚举 {@link }
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

    /** 退货公司名 */
    private String companyName;

    private List<ShippingReturnDetailDTO> returnDetailDTOList;

}
