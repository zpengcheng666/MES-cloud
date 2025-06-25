package com.miyu.module.pdm.dal.dataobject.processDetailTask;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("capp_process_detail_task")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
//@Builder生成一个私有的构造函数
@Builder
//生成一个无参的构造方法
@NoArgsConstructor
//生成一个包含全部字段的构造方法
@AllArgsConstructor
public class ProcessDetailTaskDO extends BaseDO {

    private String id;

    private String projectCode;

    private String partVersionId;

    private String processVersionId;

    private String procedureId;

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

//    private String taskId;
}
