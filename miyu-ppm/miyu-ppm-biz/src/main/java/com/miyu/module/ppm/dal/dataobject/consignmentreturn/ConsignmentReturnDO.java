package com.miyu.module.ppm.dal.dataobject.consignmentreturn;

import groovy.transform.Field;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售退货单 DO
 *
 * @author 芋道源码
 */
@TableName("ppm_consignment_return")
@KeySequence("ppm_consignment_return_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsignmentReturnDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 退货单编号
     */
    private String consignmentReturnNo;
    /**
     * 退货单名称
     */
    private String consignmentReturnName;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 退货人
     */
    private String consigner;
    /**
     * 退货日期
     */
    private LocalDateTime consignerDate;
    /**
     * 接收人
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
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link TODO return_type 对应的类}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;
    /**
     * 工作流编号
     */
    private String processInstanceId;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 状态  0已创建 1审批中 2待出库 3待确认 4 运输中 5结束 6审批不通过 7已作废
     *
     * 枚举 {@link TODO consignment_status 对应的类}
     */
    private Integer consignmentStatus;
    /**
     * 附件地址
     */
    private String fileUrl;
    /**
     * 备注
     */
    private String remark;

    @TableField(exist = false)
    private String contractName;

    @TableField(exist = false)
    private String partyName;

    @TableField(exist = false)
    private String ContractNum;

}