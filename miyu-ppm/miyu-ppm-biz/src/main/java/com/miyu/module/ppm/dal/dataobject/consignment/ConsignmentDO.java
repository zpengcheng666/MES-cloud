package com.miyu.module.ppm.dal.dataobject.consignment;

import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 采购收货 DO
 *
 * @author 芋道源码
 */
@TableName("ppm_consignment")
@KeySequence("ppm_consignment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 收货单
     */
    private String no;

    /***
     * 收货单名称
     */
    private String name;
    /***
     * 项目ID
     */
    private String projectId;
    /**
     * 收货人
     */
    private String consignedBy;
    /**
     * 收货日期
     */
    private LocalDateTime consignedDate;
    /**
     * 收货人联系方式
     */
    private String consignedContact;
    /**
     * 发货人
     */
    private String consigner;
    /**
     * 发货人联系方式
     */
    private String consignerContact;
    /**
     * 发货日期
     */
    private LocalDateTime consignerDate;
    /**
     * 承运方式
     *
     * 枚举 {@link TODO transport_way 对应的类}
     */
    private Integer deliveryMethod;
    /**
     * 承运单号
     */
    private String deliveryNumber;
    /**
     * 承运人
     */
    private String deliveryBy;
    /**
     * 承运人电话
     */
    private String deliveryContact;
    /**
     * 流程实例编号
     */
    private String processInstanceId;
    /**
     * 审批结果
     *
     * 枚举 {@link TODO crm_audit_status 对应的类}
     */
    private Integer status;

    /**
     *  状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废
     *
     * 枚举 {@link ConsignmentStatusEnum}
     */
    private Integer consignmentStatus;

    /***
     * 收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货
     */
    private Integer consignmentType;
    /***
     * 单子类型  1正常单  2 补单
     */
    private Integer type;
    /**
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link TODO return_type 对应的类}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;

    private String companyId;


    @TableField(exist = false)
    private Integer contractType;
    @TableField(exist = false)
    private String contractName;
    @TableField(exist = false)
    private String contractNo;

}