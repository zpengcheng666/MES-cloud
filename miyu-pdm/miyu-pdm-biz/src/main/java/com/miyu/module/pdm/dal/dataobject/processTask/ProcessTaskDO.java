package com.miyu.module.pdm.dal.dataobject.processTask;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@TableName("capp_process_task")
@KeySequence("capp_process_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
//@Builder生成一个私有的构造函数
@Builder
//生成一个无参的构造方法
@NoArgsConstructor
//生成一个包含全部字段的构造方法
@AllArgsConstructor
public class ProcessTaskDO extends BaseDO {
    private String id;

    private String projectCode;

    private String partNumber;

    private String partName;

    private String processCondition;

    private Date processPreparationTime;

    private String partVersionId;

    private String reviewedBy;

    private String reviewer;

    private LocalDateTime deadline;

    private String status;

    private String taskMessageId;

    private String creator;

    private String updater;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String processInstanceId;

    private Integer approvalStatus;

    @TableField(exist = false)
    private String one;

    @TableField(exist = false)
    private String three;

    @TableField(exist = false)
    private String two;

    @TableField(exist = false)
    private String five;

}
