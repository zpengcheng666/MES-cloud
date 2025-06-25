package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 项目评审 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssessmentRespVO {
    @Schema(description = "评审id", example = "17002")
    @ExcelProperty("评审id")
    private String id;

    @Schema(description = "项目id", example = "17002")
    @ExcelProperty("项目id")
    private String projectId;

    @Schema(description = "项目编码", example = "sssswqs")
    @ExcelProperty("项目编码")
    private String projectCode;

    @Schema(description = "发起原因(新立项/修改)", example = "1")
    @ExcelProperty(value = "发起原因(新立项/修改)", converter = DictConvert.class)
    @DictFormat("pms_assessment_reason") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer reasonType;

    @Schema(description = "类型(技术、采购、检验)", example = "1")
    @ExcelProperty(value = "类型(技术、采购、检验)", converter = DictConvert.class)
    @DictFormat("pms_assessment_category") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer assessmentType;

    @Schema(description = "状态(未开启、评估中、评估结束)", example = "1")
    @ExcelProperty(value = "状态(未开启、评估中、评估结束)", converter = DictConvert.class)
    @DictFormat("pms_assessment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer assessmentStatus;

    @Schema(description = "评估人")
    @ExcelProperty("评估人")
    private String assessmentPerson;

    @Schema(description = "评估说明")
    @ExcelProperty("评估说明")
    private String instruction;

    @Schema(description = "结论")
    @ExcelProperty(value = "结论", converter = DictConvert.class)
    @DictFormat("pms_assessment_conclusion") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer conclusion;

    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "预计完成时间")
    @ExcelProperty("预计完成时间")
    private LocalDateTime prefinishTime;

    @Schema(description = "审核意见")
    @ExcelProperty("审核意见")
    private String auditOpinion;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "技术评估状态", example = "1")
    @ExcelProperty(value = "技术评估状态", converter = DictConvert.class)
    @DictFormat("pms_assessment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer technologyAssessmentStatus;

    @Schema(description = "产能评估状态", example = "2")
    @ExcelProperty(value = "产能评估状态", converter = DictConvert.class)
    @DictFormat("pms_assessment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer capacityAssessmentStatus;

    @Schema(description = "成本评估状态", example = "1")
    @ExcelProperty(value = "成本评估状态", converter = DictConvert.class)
    @DictFormat("pms_assessment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer costAssessmentStatus;

    @Schema(description = "成本评估状态", example = "1")
    @ExcelProperty(value = "成本评估状态", converter = DictConvert.class)
    @DictFormat("pms_assessment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer strategyAssessmentStatus;

    @Schema(description = "技术部长")
    private Long technologyAuditor;

    @Schema(description = "制造部长")
    private Long capacityAuditor;

    @Schema(description = "财务部长")
    private Long costAuditor;

    @Schema(description = "战略部长")
    private Long strategyAuditor;

    private String projectName;

    @Schema(description = "流程实例的编号", example = "11289")
    @ExcelProperty(value = "流程实例的编号", converter = DictConvert.class)
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    @ExcelProperty(value = "审批结果", converter = DictConvert.class)
    private Integer status;

    @Schema(description = "项目预算", example = "1")
    private BigDecimal budget;

}
