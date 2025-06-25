package cn.iocoder.yudao.module.pms.dal.dataobject.notifymessage;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 简易版,项目计划提醒用 DO
 *
 * @author 上海弥彧
 */
@TableName("project_notify_message")
@KeySequence("project_notify_message_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotifyMessageDO extends BaseDO {

    /**
     * 用户ID
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 模版发送人名称
     */
    private String templateNickname;
    /**
     * 模版内容
     */
    private String templateContent;
    /**
     * 通知类型
     */
    private Integer type;
    /**
     * 是否已读
     */
    private Boolean readStatus;
    /**
     * 阅读时间
     */
    private LocalDateTime readTime;

}
