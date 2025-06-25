package com.miyu.module.ppm.dal.dataobject.purchaserequirement;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 采购需求明细，可以来源于采购申请或MRP DO
 *
 * @author Zhangyunfei
 */
@TableName("ppm_purchase_requirement_detail")
@KeySequence("ppm_purchase_requirement_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequirementDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请单ID
     */
    private String requirementId;

    /**
     * 编号
     */
    private String number;

    /**
     * 源单类型
     */
    private Integer sourceType;
    /**
     * 源单id
     */
    private String sourceId;
    /**
     * 需求物料
     */
    private String requiredMaterial;
    /**
     * 需求数量
     */
    private BigDecimal requiredQuantity;
    /**
     * 需求时间
     */
    private LocalDateTime requiredDate;
    /**
     * 预算单价
     */
    private BigDecimal estimatedPrice;
    /**
     * 供应商，即企业ID
     */
    private String supplier;

    /**
     * 是否有效
     */
    private Integer isValid;

    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 项目id
     */
    private String projectId;
    /**
     *订单id
     */
    private String orderId;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目子计划id
     */
    private String projectPlanItemId;
    /**
     * 计划类型(1普通加工,2外协,3工序外协)
     */
    private Integer planType;

    /**
     * 需求数量
     */
    @TableField(exist = false)
    private BigDecimal purchasedQuantity;




    /**
     * 采购类型
     */
    @TableField(exist = false)
    private Integer type;

    /**
     * 采购单号
     */
    @TableField(exist = false)
    private String requirementNumber;

    /**
     * 申请人
     */
    @TableField(exist = false)
    private String applicant;
    /**
     * 申请部门
     */
    @TableField(exist = false)
    private String applicationDepartment;
    /**
     * 申请日期
     */
    @TableField(exist = false)
    private LocalDateTime applicationDate;
    /**
     * 申请理由
     */
    @TableField(exist = false)
    private String applicationReason;
}
