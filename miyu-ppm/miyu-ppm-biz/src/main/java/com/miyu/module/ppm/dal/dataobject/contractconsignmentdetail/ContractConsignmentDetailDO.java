package com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 外协发货单详情 DO
 *
 * @author 上海弥彧
 */
@TableName("ppm_contract_consignment_detail")
@KeySequence("ppm_contract_consignment_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractConsignmentDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 发货单ID
     */
    private String consignmentId;
    /**
     * 发货数量
     */
    private BigDecimal consignedAmount;
    /**
     * 出库数量
     */
    private BigDecimal inboundAmount;
    /**
     * 出库人
     */
    private String inboundBy;
    /**
     * 出库时间
     */
    private LocalDateTime inboundTime;
    /**
     * 确认数量
     */
    private BigDecimal signedAmount;
    /**
     * 确认人
     */
    private String signedBy;
    /**
     * 确认日期
     */
    private LocalDateTime signedTime;
    /**
     * 物料库存ID
     */
    private String materialStockId;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目订单ID
     */
    private String projectOrderId;
    /**
     * 项目子计划ID
     */
    private String projectPlanId;
    private String projectPlanItemId;
    /**
     * 合同订单ID
     */
    private String orderId;

}