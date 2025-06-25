package com.miyu.module.ppm.dal.dataobject.contractconsignment;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 外协发货 DO
 *
 * @author 上海弥彧
 */
@TableName("ppm_contract_consignment")
@KeySequence("ppm_contract_consignment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractConsignmentDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 发货单编号
     */
    private String consignmentNo;
    /**
     * 发货单名称
     */
    private String consignmentName;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 发货人
     */
    private String consigner;
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
     * 承运人
     */
    private String deliveryBy;
    /**
     * 承运单号
     */
    private String deliveryNumber;
    /**
     * 承运人电话
     */
    private String deliveryContact;
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
     * 审批状态
     *
     * 枚举 {@link TODO ppm_audit_status 对应的类}
     */
    private Integer status;
    /**
     * 工作流编号
     */
    private String processInstanceId;
    /**
     * 附件地址
     */
    private String fileUrl;
    /**
     * 状态  0已创建 1审批中 2 待出库 3出库中4待发货5结束 9取消
     *
     * 枚举 {@link TODO shipping_status 对应的类}
     */
    private Integer consignmentStatus;

}