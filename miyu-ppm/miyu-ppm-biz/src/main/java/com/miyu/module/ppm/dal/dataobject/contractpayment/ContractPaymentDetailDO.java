package com.miyu.module.ppm.dal.dataobject.contractpayment;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 合同付款详细 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_payment_detail")
@KeySequence("pd_contract_payment_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractPaymentDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 合同ID
     */
    private String paymentId;
    /**
     * 付款计划ID
     */
    private String schemeId;


    /**
     * 实际付款金额
     */
    private BigDecimal amount;
    /**
     * 工作流编号
     */
    private String processInstanceId;

    /**
     * 付款计划表数据
     */

    // 打款方式
    @TableField(exist = false)
    private Integer paymentControl;

    // 付款日期
    @TableField(exist = false)
    private LocalDateTime payDate;

    // 比例
    @TableField(exist = false)
    private BigDecimal ratio;

    // 金额
    @TableField(exist = false)
    private BigDecimal schemeAmount;

    // 付款方式
    @TableField(exist = false)
    private Integer method;

    // 备注
    @TableField(exist = false)
    private String remark;

    // 剩余支付金额
    @TableField(exist = false)
    private BigDecimal remainAmount;
//
//    /**
//     * 已付金额
//     */
//    @TableField(exist = false)
//    private BigDecimal payAmount;

}