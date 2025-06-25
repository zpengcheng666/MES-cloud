package com.miyu.module.ppm.dal.dataobject.contract;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同订单 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_order")
@KeySequence("pd_contract_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractOrderDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 产品ID
     */
    private String materialId;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 含税单价
     */
    private BigDecimal taxPrice;
    /**
     * 交货日期
     */
    private LocalDateTime leadDate;
    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 关联采购申请明细ID
     */
    private String requirementDetailId;

    /**
     * 默认税率
     */
    @TableField(exist = false)
    private String initTax;

    @TableField(exist = false)
    private String requirementDetailNumber;


    /***
     * 项目ID
     */
    private String projectId;
    /***
     * 订单ID
     */
    private String orderId;
    /***
     * 项目计划ID
     */
    private String projectPlanId;
    /***
     * 项目子计划ID
     */
    private String projectPlanItemId;


    /**
     * 合同编号
     */
    @TableField(exist = false)
    private String number;

    /**
     * 合同名称
     */
    @TableField(exist = false)
    private String name;

    /**
     * 合同分类
     */
    @TableField(exist = false)
    private Integer contractType;

    /**
     * 合同方
     */
    @TableField(exist = false)
    private String partyName;

    /**
     * 合同状态
     */
    @TableField(exist = false)
    private Integer status;


}
