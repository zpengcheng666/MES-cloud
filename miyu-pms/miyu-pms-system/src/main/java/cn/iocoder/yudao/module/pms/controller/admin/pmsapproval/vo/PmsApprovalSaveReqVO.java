package cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Schema(description = "管理后台 - pms 立项表,项目立项相关新增/修改 Request VO")
@Data
public class PmsApprovalSaveReqVO {

    @Schema(description = "立项id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10226")
    private String id;

    @Schema(description = "项目编码")
    @DiffLogField(name = "项目编码")
    private String projectCode;

    @Schema(description = "项目名称", example = "芋艿")
    @DiffLogField(name = "项目名称")
    private String projectName;

    @Schema(description = "自研/销售 (父部门id)", example = "2")
    @DiffLogField(name = "自研/销售")
    private Integer projectType;

    @Schema(description = "项目合同，如销售项目则可以填写销售合同")
    @DiffLogField(name = "项目合同")
    private String projectContract;

    @Schema(description = "项目预算(万元)")
    @DiffLogField(name = "项目预算")
    private BigDecimal budget;

    @Schema(description = "客户(企业ID)")
    @DiffLogField(name = "客户")
    private String projectClient;

    @Schema(description = "项目状态")
    @DiffLogField(name = "项目状态")
    private Long projectStatus;

    @Schema(description = "项目简述", example = "你猜")
    @DiffLogField(name = "项目简述")
    private String description;

    @Schema(description = "预计开始时间")
    @DiffLogField(name = "预计开始时间")
    private LocalDateTime prestartTime;

    @Schema(description = "预计结束时间")
    @DiffLogField(name = "预计结束时间")
    private LocalDateTime preendTime;

    @Schema(description = "负责人(人员ID)")
    @DiffLogField(name = "负责人")
    private Long responsiblePerson;

    @Schema(description = "项目经理(ID)")
    @DiffLogField(name = "项目经理")
    private Long projectManager;

    @Schema(description = "是否需要评估")
    @DiffLogField(name = "是否需要评估")
    private Integer needsAssessment;

    @Schema(description = "战略评估")
    @DiffLogField(name = "战略评估")
    private Integer strategy;

    @Schema(description = "技术评估")
    @DiffLogField(name = "技术评估")
    private Integer technology;

    @Schema(description = "产能评估")
    @DiffLogField(name = "产能评估")
    private Integer capacity;

    @Schema(description = "成本评估")
    @DiffLogField(name = "成本评估")
    private Integer cost;

    @Schema(description = "项目技术材料")
    @DiffLogField(name = "项目技术材料")
    private String technicalMaterials;

    @Schema(description = "客户联系人(id)", example = "20907")
    @DiffLogField(name = "客户联系人")
    private Long contactId;

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "1824")
    private String processInstanceId;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

    //订单详细,这里没用订单，直接用订单详细
    //private List<OrderListDO> orderLists;
    //项目订单
    private PmsOrderDO order;

    private List<PmsOrderDO> orderLists;

}
