package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目计划子表，物料采购计划中的设备采购 DO
 *
 * @author 上海弥彧
 */
@TableName("project_plan_demand_cutter")
@KeySequence("project_plan_demand_cutter_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDemandCutterDO extends BaseDO {

    /**
     * 项目计划id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目计划id
     */
    private String projectPlanId;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer amount;
    /**
     * 采购数量
     */
    private Integer purchaseAmount;
    /**
     * 制造资源类型：1设备 2刀具 3工装
     */
    private Integer resourcesType;
    /**
     * 制造资源id(设备、刀具、工装等)
     */
    private String resourcesTypeId;
    /**
     * 零部件版本id
     */
    private String partVersionId;
    /**
     * 刀具长度
     */
    private String length;
    /**
     * 刃长
     */
    private String bladeLength;
    /**
     * 刃数
     */
    private String bladeNum;
    /**
     * 直径
     */
    private String diameter;
    /**
     * R角
     */
    private String rrAngle;
    /**
     * 缩径
     */
    private String reducingDiameter;
    /**
     * 备注
     */
    private String description;
    /**
     * 采购标记(0为未发起采购,1为已采购)
     */
    private Integer purchaseMark;
    /**
     * 采购时间
     */
    private LocalDateTime purchaseTime;
    /**
     * 预估单价
     */
    private BigDecimal predictPrice;

}
