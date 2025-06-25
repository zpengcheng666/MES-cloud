package com.miyu.module.ppm.dal.dataobject.contractinvoice;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 购销合同发票表详细 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_invoice_detail")
@KeySequence("pd_contract_invoice_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractInvoiceDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 合同发票ID
     */
    private String invoiceId;
    /**
     * 付款ID
     */
    private String paymentId;
    /**
     * 金额
     */
    private BigDecimal amount;


    // 付款日期
    @TableField(exist = false)
    private LocalDateTime payDate;

    // 付款金额
    @TableField(exist = false)
    private BigDecimal payAmount;

    // 付款方式
    @TableField(exist = false)
    private Integer method;

    // 剩余金额
    @TableField(exist = false)
    private BigDecimal remainAmount;

}