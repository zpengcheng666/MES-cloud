package com.miyu.module.ppm.dal.dataobject.contractpaymentscheme;

import lombok.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同付款计划 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_payment_scheme")
@KeySequence("pd_contract_payment_scheme_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentSchemeDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    /**
     * 计划编号
     */
    private String number;

    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 付款控制，货到付款、预付、货到票据支付、货到分期、货到延期等
     */
    private Integer paymentControl;
    /**
     * 付款日期
     */
    private LocalDateTime payDate;
    /**
     * 比例
     */
    private BigDecimal ratio;
    /**
     * 金额，与比例联动
     */
    private BigDecimal amount;
    /**
     * 付款方式，银行汇款、银行本票、商业汇票、现金、托收承付等
     */
    private Integer method;
    /**
     * 备注
     */
    private String remark;


    /**
     * 剩余支付金额
     */
    @TableField(exist = false)
    private BigDecimal remainAmount;

    /**
     * 已付金额合计
     */
//    @TableField(exist = false)
//    private BigDecimal payAmountSum;

}