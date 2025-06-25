package com.miyu.module.ppm.dal.dataobject.consignmentdetail;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收货明细 DO
 *
 * @author 芋道源码
 */
@TableName("ppm_consignment_detail")
@KeySequence("ppm_consignment_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 收货单ID
     */
    private String consignmentId;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 合同订单ID
     */
    private String orderId;
    private String materialConfigId;
    /**
     * 发货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 签收数量
     */
    private BigDecimal signedAmount;
    /**
     * 签收人
     */
    private String signedBy;
    /**
     * 签收日期
     */
    private LocalDateTime signedTime;


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


    private String locationId;
}
