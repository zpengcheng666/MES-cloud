package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 项目计划 DO
 *
 * @author 芋道源码
 */
@TableName("project_approval")
@KeySequence("project_approval_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithPlan extends BaseDO {

    /**
     * 项目id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 项目订单id
     */
    @TableField(exist = false)
    private String projectOrderId;

    /**
     * 物料编号
     */
    @TableField(exist = false)
    private String materialNumber;

    /**
     * 图号
     */
    @TableField(exist = false)
    private String partNumber;

    /**
     * 工件名称
     */
    @TableField(exist = false)
    private String partName;
    /**
     * 订单类型(0为外部销售订单，1为内部自制订单)
     */
    @TableField(exist = false)
    private Integer orderType;

    /**
     * 带料加工(是/否)
     */
    @TableField(exist = false)
    private Integer processType;

    /**
     * 加工状态
     */
    @TableField(exist = false)
    private String processCondition;

    /**
     * 数量
     */
    @TableField(exist = false)
    private Integer quantity;

    /**
     * 是否整单外协(0,否,1,是)
     */
    @TableField(exist = false)
    private Integer outsource;




    /**
     * 项目编码
     */
//    @TableField(exist = false)
    private String projectCode;
    /**
     * 项目名称
     */
//    @TableField(exist = false)
    private String projectName;
    /**
     * 项目id
     */
    @TableField(exist = false)
    private String projectId;

    /**
     * 负责人
     */
//    @TableField(exist = false)
    private Long responsiblePerson;

    /**
     * 审批结果
     */
//    @TableField(exist = false)
    private Integer status;




    /**
     * 工艺方案(id)
     */
    @TableField(exist = false)
    private String processScheme;
    /**
     * 工艺版本id
     */
    @TableField(exist = false)
    private String processVersionId;
    /**
     * 提醒
     */
    @TableField(exist = false)
    private String remindInfo;
    /**
     * 采购完成时间
     */
    @TableField(exist = false)
    private LocalDateTime purchaseCompletionTime;
    /**
     * 工艺准备完成时间
     */
    @TableField(exist = false)
    private LocalDateTime processPreparationTime;
    /**
     * 生产准备完成时间
     */
    @TableField(exist = false)
    private LocalDateTime productionPreparationTime;
    /**
     * 入库时间
     */
    @TableField(exist = false)
    private LocalDateTime warehousingTime;
    /**
     * 完成检验时间
     */
    @TableField(exist = false)
    private LocalDateTime checkoutCompletionTime;
    /**
     * 计划交付时间
     */
    @TableField(exist = false)
    private LocalDateTime planDeliveryTime;

    /**
     * 项目计划id
     */
    @TableField(exist = false)
    private String planId;

    /**
     * 项目计划审批状态
     */
    @TableField(exist = false)
    private String planStatus;

    /**
     * 流程实例的编号
     */
    @TableField(exist = false)
    private String processInstanceId;


}
