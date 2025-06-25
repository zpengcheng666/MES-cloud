package cn.iocoder.yudao.module.pms.dal.dataobject.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目计划子表，产品计划完善 DO
 *
 * @author 芋道源码
 */
@TableName("project_plan_item")
@KeySequence("project_plan_item_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanItemDO extends BaseDO {

    /**
     * 项目子计划id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目计划id
     */
    private String projectPlanId;

    /**
     * 订单id
     */
    private String projectOrderId;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 物料牌号(物料类型)
     */
    private String materialNumber;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 计划数量(小于等于订单数量)
     */
    private Integer quantity;
    /**
     * 工艺方案id
     */
    private String processScheme;
    /**
     * 计划类型(加工1,外协2,工序外协3)
     */
    private Integer planType;



    private String projectId;
    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    private List<String> materialCodeList;

    /**
     * 负责人
     */
    private Long responsiblePerson;
    /**
     * 订单编码,生码
     */
    private String orderNumber;
    /**
     * 首件标识,1为首件,0正常(不是1就都正常)
     */
    private Integer firstMark;

    /**
     * 子订单数量
     */
    private Integer count;

}
