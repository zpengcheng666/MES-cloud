package com.miyu.module.pdm.controller.admin.feasibilityTask.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableField;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目编号列表 Request VO")
@Data
@ToString(callSuper = true)
public class FeasibilityTaskReqVO {
    @Schema(description = "评估任务id", example = "20041")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零件号")
    private String partVersionId;

    @Schema(description = "零件图号", example = "A220")
    private String partNumber;

    @Schema(description = "评估状态")
    private String status;

    @Schema(description = "负责人")
    private String reviewedBy;

    @Schema(description = "负责人Id")
    private String reviewer;

    @Schema(description = "任务Id", example = "20041")
    private String taskId;

    @Schema(description = "评估截止时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "描述。。。。")
    @DiffLogField(name="截止时间")
    private LocalDateTime deadline;

    @Schema(description = "流程实例的编号", example = "20041")
    private String processInstanceId;

    @Schema(description = "流程审批状态", example = "1")
    private Integer approvalStatus;

    @TableField
    @Schema(description = "零部件版本id数组", example = "1")
    List<String> partVersionIdArr;

    @TableField
    @Schema(description = "任务号数组", example = "1")
    List<String> taskIdArr;
}
