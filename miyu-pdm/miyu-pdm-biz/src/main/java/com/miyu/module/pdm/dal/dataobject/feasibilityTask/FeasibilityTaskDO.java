package com.miyu.module.pdm.dal.dataobject.feasibilityTask;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@TableName("capp_feasibility_task")
@KeySequence("capp_feasibility_task_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
//@Builder生成一个私有的构造函数
@Builder
//生成一个无参的构造方法
@NoArgsConstructor
//生成一个包含全部字段的构造方法
@AllArgsConstructor
public class FeasibilityTaskDO extends BaseDO {

    private String id;

    private String projectCode;

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

}