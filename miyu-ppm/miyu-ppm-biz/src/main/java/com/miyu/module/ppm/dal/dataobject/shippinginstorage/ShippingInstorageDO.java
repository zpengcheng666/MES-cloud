package com.miyu.module.ppm.dal.dataobject.shippinginstorage;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售订单入库 DO
 *
 * @author 上海弥彧
 */
@TableName("dm_shipping_instorage")
@KeySequence("dm_shipping_instorage_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingInstorageDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 入库单名称
     */
    private String name;
    /**
     * 入库单
     */
    private String no;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 公司ID
     */
    private String companyId;
    /**
     * 项目ID
     */
    private String projectId;
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
     * 状态  状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 9已作废
     *
     * 枚举 {@link TODO wms_in_state 对应的类}
     */
    private Integer shippingInstorageStatus;

}