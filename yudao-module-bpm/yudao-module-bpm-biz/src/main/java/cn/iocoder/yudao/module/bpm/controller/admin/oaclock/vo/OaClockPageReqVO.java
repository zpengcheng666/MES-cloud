package cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - OA 打卡分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OaClockPageReqVO extends PageParam {

    @Schema(description = "用户名", example = "王五")
    private String name;

    @Schema(description = "打卡类型", example = "1")
    private Integer type;
    @Schema(description = "打卡状态", example = "1")
    private Integer clockStatus;

    @Schema(description = "部门")
    private String dept;

    @Schema(description = "岗位")
    private String job;

    @Schema(description = "打卡时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] clockTime;

    @Schema(description = "打卡异常原因")
    private String reason;

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "27349")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
