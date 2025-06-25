package com.miyu.cloud.mcs.dal.dataobject.problemreport;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 问题上报 DO
 *
 * @author 王正浩
 */
@TableName("mcs_problem_report")
@KeySequence("mcs_problem_report_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemReportDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 工位id
     */
    private String stationId;
    /**
     * 问题类型
     * <p>
     * 枚举 {@link TODO mcs_problem_report_type 对应的类}
     */
    private Integer type;
    /**
     * 上报id
     */
    private String reportId;
    /**
     * 状态
     */
    private String status;
    /**
     * 问题描述
     */
    private String content;

}