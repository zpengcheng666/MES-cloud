package com.miyu.module.ppm.dal.dataobject.contractrefund;

import com.miyu.module.ppm.enums.contractrefund.ContractRefundEnum;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同退款 DO
 *
 * @author 芋道源码
 */
@TableName("dm_contract_refund")
@KeySequence("dm_contract_refund_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractRefundDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /***
     * 退款单号
     */
    private String no;
    /**
     * 退货单
     */
    private String shippingReturnId;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 退款方式现金、承兑、信用证、支付宝、二维码等
     *
     * 枚举 {@link TODO pd_finance_pay_method 对应的类}
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
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 审批状态
     *
     * 枚举 {@link DMAuditStatusEnum}
     */
    private Integer status;


    /**
     * 状态  0已创建 1审批中 2退款中 3结束 8审核失败9作废
     *
     * 枚举 {@link ContractRefundEnum}
     */
    private Integer refundStatus;

}