package com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 收货明细新增/修改 Request VO")
@Data
public class PurchaseConsignmentDetailSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9313")
    private String id;

    @Schema(description = "合同ID", example = "8204")
    private String contractId;

    @Schema(description = "收货单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9994")
    @NotEmpty(message = "收货单ID不能为空")
    private String consignmentId;

    @Schema(description = "合同订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23497")
    private String orderId;

    @Schema(description = "发货数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "签收数量")
    private BigDecimal signedAmount;

    @Schema(description = "签收人")
    private String signedBy;

    @Schema(description = "签收日期")
    private LocalDateTime signedTime;


    private String materialConfigId;



    private String materialStockId;
    private String barCode;
    private String batchNumber;
    /***
     * 收货单
     */
    @TableField(exist = false)
    private String no;
    /***
     * 收货单名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 物料编号
     */
    @TableField(exist = false)
    private String materialNumber;

    /**
     * 物料类码
     */
    @TableField(exist = false)
    private String materialCode;

    /**
     * 物料名称
     */
    @TableField(exist = false)
    private String materialName;

    /**
     * 物料属性
     */
    @TableField(exist = false)
    private String materialProperty;

    /**
     * 物料类型
     */
    @TableField(exist = false)
    private String materialTypeId;
    @TableField(exist = false)
    private String materialParentTypeId;

    /**
     * 物料管理模式
     */
    @TableField(exist = false)
    private String materialManage;

    /**
     * 物料规格
     */
    @TableField(exist = false)
    private String materialSpecification;

    /**
     * 物料品牌
     */
    @TableField(exist = false)
    private String materialBrand;

    /**
     * 物料单位
     */
    @TableField(exist = false)
    private String materialUnit;

    /**
     * 收货状态
     */

    private Integer consignmentStatus;


    @TableField(exist = false)
    private String schemeId;


    private String projectId;
    private String projectOrderId;
    private String projectPlanId;
    private String projectPlanItemId;



    /**
     * 收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货
     */
    private Integer consignmentType;

    private String infoId;


    private String shippingId;
    private String shippingDetailId;

}