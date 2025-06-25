package cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval;

import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * pms 立项表,项目立项相关 DO
 *
 * @author 芋道源码
 */
@TableName("project_approval")
@KeySequence("project_approval_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmsApprovalDO extends BaseDO {

    /**
     * 立项id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 自研/销售 (父部门id)
     *
     * 枚举 {@link TODO pms_project_type 对应的类}
     */
    private Integer projectType;
    /**
     * 项目合同，如销售项目则可以填写销售合同
     */
    private String projectContract;
    /**
     * 项目预算(万元)
     */
    private BigDecimal budget;
    /**
     * 客户(企业ID)
     */
    private String projectClient;
    /**
     * 项目简述
     */
    private String description;
    /**
     * 预计开始时间
     */
    private LocalDateTime prestartTime;
    /**
     * 预计结束时间
     */
    private LocalDateTime preendTime;
    /**
     * 负责人(人员ID)
     */
    private Long responsiblePerson;
    /**
     * 项目经理(ID)
     */
    private Long projectManager;
    /**
     * 是否需要评估
     *
     * 枚举 {@link TODO pms_assessment_type 对应的类}
     */
    private Integer needsAssessment;
    /**
     * 战略评估
     *
     * 枚举 {@link TODO pms_assessment_type 对应的类}
     */
    private Integer strategy;
    /**
     * 技术评估
     *
     * 枚举 {@link TODO pms_assessment_type 对应的类}
     */
    private Integer technology;
    /**
     * 产能评估
     *
     * 枚举 {@link TODO pms_assessment_type 对应的类}
     */
    private Integer capacity;
    /**
     * 成本评估
     *
     * 枚举 {@link TODO pms_assessment_type 对应的类}
     */
    private Integer cost;
    /**
     * 项目技术材料
     */
    private String technicalMaterials;
    /**
     * 客户联系人(id)
     */
    private Long contactId;

    /**
     * 项目状态
     * 0，未开始，1待审批，2，待评审，3准备中，4生产中，5出库完成，6已结束，7异常终止
     */
    private Long projectStatus;

    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    //项目订单列表
    @TableField(exist = false)
    private List<PmsOrderDO> orderList;

    //项目订单列表
    @TableField(exist = false)
    private List<AssessmentDO> assessmentList;

}
