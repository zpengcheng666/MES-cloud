package com.miyu.module.ppm.dal.dataobject.contractinvoice;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 购销合同发票 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract_invoice")
@KeySequence("pd_contract_invoice_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractInvoiceDO extends BaseDO {

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
     * 业务类型1采购 2销售
     */
    private Integer businessType;
    /**
     * 类型，普票、专票、收据等
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal amount;
    /**
     * 开具时间
     */
    private LocalDateTime invoiceDate;
    /**
     * 票据代码
     */
    private String invoiceNumber;
    /**
     * 票据代码2
     */
    private String invoiceNumber2;

    /**
     * 附件地址
     */
    private String fileUrl;

    /**
     * 审批状态
     */
    private Integer status;

    /**
     * 工作流编号
     */
    private String processInstanceId;


    @TableField(exist = false)
    private String contractNumber;

    @TableField(exist = false)
    private String contractName;

}