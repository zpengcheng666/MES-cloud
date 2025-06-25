package cn.iocoder.yudao.module.bpm.dal.dataobject.oameeting;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * OA 会议申请 DO
 *
 * @author 芋道源码
 */
@TableName("bpm_oa_meeting")
@KeySequence("bpm_oa_meeting_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OaMeetingDO extends BaseDO {

    /**
     * 请假表单主键
     */
    @TableId
    private Long id;
    /**
     * 会议主题
     */
    private String title;
    /**
     * 会议日期
     */
    private LocalDateTime mDate;
    /**
     * 参会人员
     */
    private String staff;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 会议室
     */
    private String mroom;
    /**
     * 描述
     */
    private String description;
    /**
     * 文件
     */
    private String document;
    /**
     * 审批结果
     */
    private Integer status;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}
