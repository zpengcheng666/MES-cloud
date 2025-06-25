package cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - OA 打卡新增/修改 Request VO")
@Data
public class OaClockSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22533")
    private Long id;

    @Schema(description = "用户名", example = "王五")
    private String name;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19374")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    @Schema(description = "打卡类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "打卡类型不能为空")
    private Integer type;
    @Schema(description = "打卡状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
//    @NotNull(message = "打卡类型不能为空")
    private Integer clockStatus;

    @Schema(description = "部门")
    private String dept;

    @Schema(description = "岗位")
    private String job;

    @Schema(description = "打卡时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "打卡时间不能为空")
    private LocalDateTime clockTime;

    @Schema(description = "打卡异常原因")
//    @NotEmpty(message = "打卡异常原因不能为空")
    private String reason;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
//    @NotNull(message = "审批结果不能为空")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "27349")
    private String processInstanceId;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
