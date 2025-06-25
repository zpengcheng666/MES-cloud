package com.miyu.module.ppm.dal.dataobject.shipping;

import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
/**
 * 销售发货 DO
 *
 * @author 芋道源码
 */
@TableName("dm_shipping")
@KeySequence("dm_shipping_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDO extends BaseDO {

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
     * 合作方ID
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
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
    /**
     * 审批状态
     *
     * 枚举 {@link DMAuditStatusEnum}
     */
    private Integer status;

    /***
     * 附件地址
     */
    private String fileUrl;
    /***
     * 发货单
     */
    private String no;
    /***
     * 发货单名称
     */
    private String name;

    /**
     * 状态 0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9作废8审批失败
     * 枚举 {@link ShippingStatusEnum}
     */
    private Integer shippingStatus;


    /***
     * 项目ID
     */
    private String projectId;


    /***
     * 发货单类型
     * 1销售发货2外协发货3采购退货4委托加工退货
     */
    private Integer shippingType;


    /**
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link ShippingReturnTypeEnum}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;

}