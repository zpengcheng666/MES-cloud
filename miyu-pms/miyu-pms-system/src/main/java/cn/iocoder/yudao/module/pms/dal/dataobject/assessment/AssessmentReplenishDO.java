package cn.iocoder.yudao.module.pms.dal.dataobject.assessment;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 评审子表，评审补充 DO
 *
 * @author 芋道源码
 */
@TableName("project_assessment_replenish")
@KeySequence("project_assessment_replenish_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentReplenishDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 审核人id
     */
    private String auditor;
    /**
     * 审核日期
     */
    private LocalDateTime auditDate;
    /**
     * 评审类型(不是id,是普通字符串)
     */
    private String assessmentType;
    /**
     * 评审表id
     */
    private String assessmentId;
    /**
     * 审批建议
     */
    private String suggestion;

    @TableField(exist = false)
    private String username;

}
