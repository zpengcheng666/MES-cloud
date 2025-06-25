package cn.iocoder.yudao.module.pms.dal.dataobject.assessment;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目评审 DO
 *
 * @author 芋道源码
 */
@TableName("project_assessment")
@KeySequence("project_assessment_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDO extends BaseDO {

    /**
     * 评审id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 发起原因(新立项/修改)
     *
     * 枚举 {@link TODO pms_assessment_reason 对应的类}
     */
    private Integer reasonType;
    /**
     * 类型(技术、采购、检验)
     *
     * 枚举 {@link TODO pms_assessment_category 对应的类}
     */
    private Integer assessmentType;
    /**
     * 状态(未开启、评估中、评估结束)
     *
     * 枚举 {@link TODO pms_assessment_status 对应的类}
     */
    private Integer assessmentStatus;
    /**
     * 评估人
     */
    private String assessmentPerson;
    /**
     * 评估说明
     */
    private String instruction;
    /**
     * 结论
     *
     * 枚举 {@link TODO pms_assessment_conclusion 对应的类}
     */
    private Integer conclusion;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 预计完成时间
     */
    private LocalDateTime prefinishTime;
    /**
     * 审核人id
     */
    private String auditor;
    /**
     * 审核日期
     */
    private LocalDateTime auditDate;
    /**
     * 审核意见
     */
    private String auditOpinion;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 技术评估状态
     *
     * 枚举 {@link TODO pms_assessment_status 对应的类}
     */
    private Integer technologyAssessmentStatus;
    /**
     * 产能评估状态
     *
     * 枚举 {@link TODO pms_assessment_status 对应的类}
     */
    private Integer capacityAssessmentStatus;
    /**
     * 成本评估状态
     *
     * 枚举 {@link TODO pms_assessment_status 对应的类}
     */
    private Integer costAssessmentStatus;
    /**
     * 成本评估状态
     *
     * 枚举 {@link TODO pms_assessment_status 对应的类}
     */
    private Integer strategyAssessmentStatus;

    @TableField(exist = false)
    private String projectName;

    /** 技术部长 */
    private Long technologyAuditor;
    /** 制造部长 */
    private Long capacityAuditor;
    /** 财务部长 */
    private Long costAuditor;
    /** 战略部长 */
    private Long strategyAuditor;

}
