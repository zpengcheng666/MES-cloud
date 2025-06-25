package com.miyu.module.pdm.dal.dataobject.toolingApply;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("pdm_tooling_apply")
@KeySequence("pdm_tooling_apply_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolingApplyDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private Long categoryId;

    private String toolingCode;

    private String toolingNumber;

    private String toolingName;

    private String toolingVersion;

    private String applyName;

    private Integer quantity;

    private Integer toolingStyle;

    private Integer processType;

    private Integer applyReason;

    private LocalDateTime requireTime;

    private String equipmentName;

    private String equipmentNumber;

    private String technicalRequirement;

    private String customizedIndex;

    private Integer status;

    private String processInstanceId;

    private Integer approvalStatus;

    private String applyId;

    @TableField(exist = false)
    private String name;
}
