package cn.iocoder.yudao.module.pms.api.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - pms 立项表,项目立项相关 Response VO")
@Data
public class PmsApprovalDto {

    @Schema(description = "立项id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10226")
    private String id;

    @Schema(description = "项目编码")
    private String projectCode;

    @Schema(description = "项目名称", example = "芋艿")
    private String projectName;

    @Schema(description = "自研/销售 (父部门id)", example = "2")
    private Integer projectType;

//    @Schema(description = "项目合同，如销售项目则可以填写销售合同")
//    private String projectContract;

    @Schema(description = "项目预算(万元)")
    private BigDecimal budget;

    @Schema(description = "客户(企业ID)")
    private String projectClient;

    @Schema(description = "项目简述", example = "你猜")
    private String description;

    @Schema(description = "预计开始时间")
    private LocalDateTime prestartTime;

    @Schema(description = "预计结束时间")
    private LocalDateTime preendTime;

    @Schema(description = "负责人(人员ID)")
    private Long responsiblePerson;
    private String responsiblePersonName;

    @Schema(description = "项目经理(ID)")
    private Long projectManager;
    private String projectManagerName;

    @Schema(description = "是否需要评估")
    private Integer needsAssessment;

    @Schema(description = "战略评估")
    private Integer strategy;

    @Schema(description = "技术评估")
    private Integer technology;

    @Schema(description = "产能评估")
    private Integer capacity;

    @Schema(description = "成本评估")
    private Integer cost;

    @Schema(description = "项目技术材料")
    private String technicalMaterials;

    @Schema(description = "创建者")
    private String creator;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "客户联系人(id)", example = "20907")
    private Long contactId;

}
