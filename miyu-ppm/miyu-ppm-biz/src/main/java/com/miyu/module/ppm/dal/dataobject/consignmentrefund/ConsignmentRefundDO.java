package com.miyu.module.ppm.dal.dataobject.consignmentrefund;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 采购退款单 DO
 *
 * @author 芋道源码
 */
@TableName("ppm_consignment_refund")
@KeySequence("ppm_consignment_refund_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentRefundDO extends BaseDO {

    /**
     * 采购退款单主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 采购退款单号
     */
    private String no;
    /**
     * 采购退货单
     */
    private String consignmentReturnId;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 退款方式现金、承兑、信用证、支付宝、二维码等
     */
    private Integer refundType;
    /**
     * 退款日期
     */
    private LocalDateTime refundTime;
    /**
     * 退款金额
     */
    private BigDecimal refundPrice;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 工作流编号
     */
    private String processInstanceId;
    /**
     * 状态  0已创建 1审批中 2退款中 3结束 8审核失败 9作废
     *
     * 枚举 {@link TODO qms_is_effective 对应的类}
     */
    private Integer refundStatus;

}