package cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo;

import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - pms 立项表,项目立项相关 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PmsApprovalRespVO {

    @Schema(description = "立项id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10226")
    @ExcelProperty("立项id")
    private String id;

    @Schema(description = "项目编码")
    @ExcelProperty("项目编码")
    private String projectCode;

    @Schema(description = "项目名称", example = "芋艿")
    @ExcelProperty("项目名称")
    private String projectName;

    @Schema(description = "自研/销售 (父部门id)", example = "2")
    @ExcelProperty(value = "自研/销售 (父部门id)", converter = DictConvert.class)
    @DictFormat("pms_project_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer projectType;

    @Schema(description = "项目合同，如销售项目则可以填写销售合同")
    @ExcelProperty("项目合同，如销售项目则可以填写销售合同")
    private String projectContract;

    @Schema(description = "项目预算(万元)")
    @ExcelProperty("项目预算(万元)")
    private BigDecimal budget;

    @Schema(description = "客户(企业ID)")
    @ExcelProperty("客户(企业ID)")
    private String projectClient;
    /** 客户名 */
    private String projectClientName;

    @Schema(description = "项目简述", example = "你猜")
    @ExcelProperty("项目简述")
    private String description;

    @Schema(description = "预计开始时间")
    @ExcelProperty("预计开始时间")
    private LocalDateTime prestartTime;

    @Schema(description = "预计结束时间")
    @ExcelProperty("预计结束时间")
    private LocalDateTime preendTime;

    @Schema(description = "负责人(人员ID)")
    @ExcelProperty("负责人(人员ID)")
    private Long responsiblePerson;
    private String responsiblePersonName;

    @Schema(description = "项目经理(ID)")
    @ExcelProperty("项目经理(ID)")
    private Long projectManager;
    private String projectManagerName;

    @Schema(description = "是否需要评估")
    @ExcelProperty(value = "是否需要评估", converter = DictConvert.class)
    @DictFormat("pms_assessment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer needsAssessment;

    @Schema(description = "战略评估")
    @ExcelProperty(value = "战略评估", converter = DictConvert.class)
    @DictFormat("pms_assessment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer strategy;

    @Schema(description = "技术评估")
    @ExcelProperty(value = "技术评估", converter = DictConvert.class)
    @DictFormat("pms_assessment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer technology;

    @Schema(description = "产能评估")
    @ExcelProperty(value = "产能评估", converter = DictConvert.class)
    @DictFormat("pms_assessment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer capacity;

    @Schema(description = "成本评估")
    @ExcelProperty(value = "成本评估", converter = DictConvert.class)
    @DictFormat("pms_assessment_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer cost;

    @Schema(description = "项目技术材料")
    @ExcelProperty("项目技术材料")
    private String technicalMaterials;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "客户联系人(id)", example = "20907")
    @ExcelProperty("客户联系人(id)")
    private Long contactId;

    @Schema(description = "客户联系人", example = "20907")
    @ExcelProperty("客户联系人")
    private String contactName;

    @Schema(description = "项目状态", example = "2")
    @ExcelProperty("项目状态")
    private Long projectStatus;

    @Schema(description = "审批结果", example = "2")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "1824")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    /** 项目订单 */
    private List<PmsOrderDO> orderList;

    /** 项目订单 */
    private List<AssessmentDO> assessmentList;

    /** 项目计划 */
    private List<PmsPlanDO> planList;

    /** 项目订单 */
    private PmsOrderDO pmsorder;

}
