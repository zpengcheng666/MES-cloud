package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目计划 DO
 *
 * @author 芋道源码
 */
@TableName("project_plan")
@KeySequence("project_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsPlanDO extends BaseDO {

    /**
     * 项目计划id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目订单编号(项目订单ID)
     */
    private String projectOrderId;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 备注
     */
    private String remark;
    /**
     * 物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。
     */
    private String materialNumber;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 工件名称
     */
    private String partName;
    /**
     * 订单类型(0为外部销售订单，1为内部自制订单)
     *
     * 枚举 {@link TODO pms_order_type 对应的类}
     */
    private Integer orderType;
    /**
     * 外协数量(向外委派)，默认为0，项目评审时，产能评估填写，为0就是不用外协
     */
    private Integer outSourceAmount;
    /**
     * 工序外协
     */
    private Integer stepOutSourceAmount;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 带料加工(是/否)
     *
     * 枚举 {@link TODO pms_process_type 对应的类}
     */
    private Integer processType;
    /**
     * 加工状态
     */
    private String processCondition;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 工艺方案(id)
     */
    private String processScheme;
    /**
     * 提醒
     */
    private String remindInfo;
    /**
     * 采购完成时间
     */
    private LocalDateTime purchaseCompletionTime;
    /**
     * 工艺准备完成时间
     */
    private LocalDateTime processPreparationTime;
    /**
     * 生产准备完成时间
     */
    private LocalDateTime productionPreparationTime;
    /**
     * 入库时间
     */
    private LocalDateTime warehousingTime;
    /**
     * 完成检验时间
     */
    private LocalDateTime checkoutCompletionTime;
    /**
     * 计划交付时间
     */
    private LocalDateTime planDeliveryTime;

    private String projectName;

    /**
     * 负责人
     */
    private Long responsiblePerson;

    /**
     * 工艺版本id
     */
    private String processVersionId;

    @TableField(exist = false)
    private Long projectStatus;

    /**
     * 工艺进度
     */
    @TableField(exist = false)
    private Integer schemeProcess;

    /**
     * 工艺可选排序
     */
    @TableField(exist = false)
    private Integer schemeOrderBy;

    /**
     * 整单外协(1,外协，0,不外协 )
     */
    @TableField(exist = false)
    private Integer outsource;

}
