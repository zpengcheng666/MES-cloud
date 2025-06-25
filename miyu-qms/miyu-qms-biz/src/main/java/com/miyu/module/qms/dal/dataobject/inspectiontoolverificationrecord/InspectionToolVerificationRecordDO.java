package com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 检验工具校准记录 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_inspection_tool_verification_record")
@KeySequence("qms_inspection_tool_verification_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InspectionToolVerificationRecordDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 库存主键
     */
    private String stockId;
    /**
     * 工具主键
     */
    private String toolId;
    /**
     * 送检时间
     */
    private LocalDateTime verificationDateBegin;

    /**
     *  实际送检时间
     */
    private LocalDateTime verificationDateBeginAct;

    /**
     * 完成时间
     */
    private LocalDateTime verificationDateEnd;

    /**
     * 送检结果
     */
    private String remark;

    /**
     * 状态
     */
    private Integer status;

    @TableField(exist = false)
    private String toolName;

}
