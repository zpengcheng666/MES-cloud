package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentReplenishDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 项目评审新增/修改 Request VO")
@Data
public class AssessmentSaveReqVO {

    @Schema(description = "评审id", requiredMode = Schema.RequiredMode.REQUIRED, example = "20190")
    private String id;

    @Schema(description = "项目id", example = "17002")
    private String projectId;

    @Schema(description = "发起原因(新立项/修改)", example = "1")
    private Integer reasonType;

    @Schema(description = "类型(技术、采购、检验)", example = "1")
    private Integer assessmentType;

    @Schema(description = "状态(未开启、评估中、评估结束)", example = "1")
    private Integer assessmentStatus;

    @Schema(description = "评估人")
    private String assessmentPerson;

    @Schema(description = "评估说明")
    private String instruction;

    @Schema(description = "结论")
    private Integer conclusion;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "预计完成时间")
    private LocalDateTime prefinishTime;

    @Schema(description = "审核人id")
    private String auditor;

    @Schema(description = "审核日期")
    private LocalDateTime auditDate;

    @Schema(description = "审核意见")
    private String auditOpinion;

    @Schema(description = "流程实例的编号", example = "11289")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "技术评估状态", example = "1")
    private Integer technologyAssessmentStatus;

    @Schema(description = "产能评估状态", example = "2")
    private Integer capacityAssessmentStatus;

    @Schema(description = "成本评估状态", example = "1")
    private Integer costAssessmentStatus;

    @Schema(description = "成本评估状态", example = "1")
    private Integer strategyAssessmentStatus;

    @Schema(description = "评审子表，评审补充列表")
    private List<AssessmentReplenishDO> assessmentReplenishs;

    @Schema(description = "战略评估")
    private Integer strategy;

    @Schema(description = "技术评估")
    private Integer technology;

    @Schema(description = "产能评估")
    private Integer capacity;

    @Schema(description = "成本评估")
    private Integer cost;

    @Schema(description = "技术部长")
    private Long technologyAuditor;

    @Schema(description = "制造部长")
    private Long capacityAuditor;

    @Schema(description = "财务部长")
    private Long costAuditor;

    @Schema(description = "战略部长")
    private Long strategyAuditor;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;



}
