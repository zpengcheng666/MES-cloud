package com.miyu.module.ppm.dal.dataobject.consignmentinfo;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 收货产品 DO
 *
 * @author 上海弥彧
 */
@TableName("ppm_consignment_info")
@KeySequence("ppm_consignment_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentInfoDO extends BaseDO {

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
    /**
     * 物料类型ID
     */
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
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 项目订单ID
     */
    private String projectOrderId;
    /**
     * 项目计划ID
     */
    private String projectPlanId;
    /**
     * 项目子计划ID
     */
    private String projectPlanItemId;
    /**
     * 状态  状态  0已创建 1审批中 2待签收 3 入库中4待质检5质检中 6结束 7审批不通过 8已作废9待确认
     */
    private Integer consignmentStatus;
    /**
     * 收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货
     */
    private Integer consignmentType;


    private String shippingId;

    private Integer schemeResult;

    @TableField(exist = false)
    private String no;
    @TableField(exist = false)
    private String name;

    private String locationId;

    @TableField(exist = false)
    private String companyId;

}