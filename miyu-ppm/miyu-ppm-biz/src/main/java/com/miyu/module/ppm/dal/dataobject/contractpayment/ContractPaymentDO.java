package com.miyu.module.ppm.dal.dataobject.contractpayment;

import lombok.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同付款 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_payment")
@KeySequence("pd_contract_payment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 合同ID
     */
    private String contractId;

    /**
     * 税务信息ID
     */
    private String financeId;

    /**
     * 业务类型1采购 2销售
     */
    private Integer businessType;

    /**
     * 计划编号
     */
    private String number;

    /**
     * 实际付款日期
     */
    private LocalDateTime payDate;
    /**
     * 实际付款金额
     */
    private BigDecimal amount;
    /**
     * 实际付款方式
     */
    private Integer method;
    /**
     * 付款凭证
     */
    private String evidence;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    @TableField(exist = false)
    private String contractNumber;

    @TableField(exist = false)
    private String contractName;

    /**
     * 合同发票用
     */
    // 支付金额
    @TableField(exist = false)
    private BigDecimal payAmount;

    // 剩余金额
    @TableField(exist = false)
    private BigDecimal remainAmount;

}