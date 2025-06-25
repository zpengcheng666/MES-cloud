package com.miyu.module.ppm.dal.dataobject.contract;

import lombok.*;

import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 购销合同 DO
 *
 * @author Zhangyunfei
 */
@TableName("pd_contract")
@KeySequence("pd_contract_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 类型(采购、销售)
     */
    private Integer type;
    /**
     * 合同编号
     */
    private String number;
    /**
     * 合同名称
     */
    private String name;

    /**
     * 项目ID
     */
    private String projectId;

    /**
     * 合同方
     */
    private String party;
    /**
     * 签约人
     */
    private String contact;

    /**
     * 签约时间
     */
    private LocalDate signingDate;
    /**
     * 签约地点
     */
    private String signingAddress;
    /**
     * 签约部门
     */
    private String department;
    /**
     * 我方签约人
     */
    private String selfContact;
    /**
     * 是否增值税
     */
    private Integer vat;
    /**
     * 币种
     */
    private Integer currency;
    /**
     * 交货方式
     */
    private Integer delivery;

    /**
     * 合同状态
     */
    private Integer contractStatus;

    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 采购员
     */
    private String purchaser;
    /**
     * 备注
     */
    private String remark;

    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 合同分类
     */
    private Integer contractType;

    /**
     * 合同方
     */
    @TableField(exist = false)
    private String partyName;

    /**
     * 签约人
     */
    @TableField(exist = false)
    private String contactName;


}