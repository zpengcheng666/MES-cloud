package cn.iocoder.yudao.module.bpm.dal.dataobject.oaclock;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * OA 打卡 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_clock")
@KeySequence("bpm_oa_clock_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaClockDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户名
     */
    private String name;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 打卡类型
     *
     * 枚举 {@link TODO bpm_oa_clock_type 对应的类}
     */
    private Integer type;
    /**
     * 打卡状态类型
     *
     * 枚举 {@link TODO bpm_oa_clock_status 对应的类}
     */
    private Integer clockStatus;
    /**
     * 部门
     */
    private String dept;
    /**
     * 岗位
     */
    private String job;
    /**
     * 打卡时间
     */
    private LocalDateTime clockTime;
    /**
     * 打卡异常原因
     */
    private String reason;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}
