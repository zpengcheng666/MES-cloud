package com.miyu.cloud.mcs.dal.dataobject.batchorderapsresult;

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
 * 排产结果 DO
 *
 * @author 上海弥彧
 */
@TableName("mcs_batch_order_aps_result")
@KeySequence("mcs_batch_order_aps_result_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchOrderApsResultDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 排产开始时间
     */
    private LocalDateTime startTime;
    /**
     * 排产结果
     */
    private String apsContent;
    /**
     * 状态:1新创建,2已提交,3已通过,4作废
     */
    private Integer status;
    /**
     * 提交时间
     */
    private LocalDateTime submitedTime;
    /**
     * 通过时间
     */
    private LocalDateTime acceptedTime;
    /**
     * 审批人
     */
    private String acceptedBy;
    /**
     * 作废时间
     */
    private LocalDateTime nullifiedTime;
    /**
     * 作废人
     */
    private String nullifiedBy;
    /**
     * 排产方式
     */
    private String planPriority;
    /**
     * 排产结果
     */
    private String apsConfig;
    /**
     * 所属部门(这个没用吧)
     */
    private String sysOrgCode;

}
