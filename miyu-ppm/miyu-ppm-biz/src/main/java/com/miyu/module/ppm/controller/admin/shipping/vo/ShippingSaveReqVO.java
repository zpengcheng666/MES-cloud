package com.miyu.module.ppm.controller.admin.shipping.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;

@Schema(description = "管理后台 - 销售发货新增/修改 Request VO")
@Data
public class ShippingSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22734")
    private String id;

    @Schema(description = "发货单")
    @NotEmpty(message = "发货单不能为空")
    private String no;

    @Schema(description = "发货单名称")
    private String name;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    private String contractId;

    @Schema(description = "发货人", requiredMode = Schema.RequiredMode.REQUIRED)

    private String consigner;

    @Schema(description = "发货日期", requiredMode = Schema.RequiredMode.REQUIRED)

    private LocalDateTime consignerDate;

    @Schema(description = "承运方式")
    private Integer deliveryMethod;

    @Schema(description = "承运人")
    private String deliveryBy;

    @Schema(description = "承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人电话")
    private String deliveryContact;

    @Schema(description = "收货人")
    private String consignedBy;

    @Schema(description = "收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;


    @Schema(description = "附件地址")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", example = "2")
    private Integer shippingStatus;


    @Schema(description = "发货单类型1销售发货2外协发货3采购退货4委托加工退货", example = "2")
    private Integer shippingType;

    @Schema(description = "出库仓库")
    private String warehouseId;

    /***
     * 项目ID
     */
    private String projectId;

    @Schema(description = "销售发货明细列表")
    @NotEmpty(message = "详情不能为空")
    private List<ShippingDetailDO> shippingDetails;



    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    private String returnReason;



}