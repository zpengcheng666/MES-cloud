package com.miyu.module.ppm.dal.dataobject.shippingreturn;

import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import lombok.*;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 销售退货单 DO
 *
 * @author miyudmA
 */
@TableName("dm_shipping_return")
@KeySequence("dm_shipping_return_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingReturnDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;


    /**
     * 退货单编号
     */
    private String shippingReturnNo;
    /**
     * 退货单名称
     */
    private String shippingReturnName;
//    /**
//     * 发货单
//     */
//    private String shippingId;
    /**
     * 合同ID
     */
    private String contractId;
    /***
     * 项目ID
     */
    private String projectId;
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
     * 枚举 {@link ShippingReturnTypeEnum}
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
     * 状态  0已创建 1审批中 2 待签收 3待入库 4结束 8 审批失败 9作废
     * 枚举 {@link }
     */
    private Integer shippingStatus;
    /**
     * 附件地址
     */
    private String fileUrl;
    /**
     * 备注
     */
    private String remark;

}