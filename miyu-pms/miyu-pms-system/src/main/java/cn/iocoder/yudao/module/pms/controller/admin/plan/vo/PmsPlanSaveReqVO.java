package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 项目计划新增/修改 Request VO")
@Data
public class PmsPlanSaveReqVO {
    @Schema(description = "项目计划id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19639")
    private String id;

    @Schema(description = "项目订单编号(项目订单ID)", example = "15863")
    @DiffLogField(name = "项目订单编号")
    private String projectOrderId;

    @Schema(description = "项目id", example = "14878")
    @DiffLogField(name = "项目id")
    private String projectId;

    @Schema(description = "项目编码")
    @DiffLogField(name = "项目编码")
    private String projectCode;

    @Schema(description = "项目名称")
    @DiffLogField(name = "项目名称")
    private String projectName;

    @Schema(description = "备注", example = "你猜")
    @DiffLogField(name = "备注")
    private String remark;

    @Schema(description = "物料编码	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    @DiffLogField(name = "物料编码")
    private String materialNumber;

    @Schema(description = "图号")
    @DiffLogField(name = "图号")
    private String partNumber;

    @Schema(description = "工件名称", example = "芋艿")
    @DiffLogField(name = "工件名称")
    private String partName;

    @Schema(description = "订单类型(0为外部销售订单，1为内部自制订单)", example = "2")
    @DiffLogField(name = "订单类型")
    private Integer orderType;

    @Schema(description = "外协数量(向外委派)，默认为0，项目评审时，产能评估填写，为0就是不用外协")
    private Integer outSourceAmount;

    @Schema(description = "工序外协数量")
    private Integer stepOutSourceAmount;

    @Schema(description = "数量")
    @DiffLogField(name = "数量")
    private Integer quantity;

    @Schema(description = "带料加工(是/否)", example = "1")
    @DiffLogField(name = "带料加工")
    private Integer processType;

    @Schema(description = "工艺方案(id)")
    @DiffLogField(name = "工艺方案")
    private String processScheme;

    @Schema(description = "提醒")
    @DiffLogField(name = "提醒")
    private String remindInfo;

    @Schema(description = "采购完成时间")
    @DiffLogField(name = "采购完成时间")
    private LocalDateTime purchaseCompletionTime;

    @Schema(description = "工艺准备完成时间")
    @DiffLogField(name = "工艺准备完成时间")
    private LocalDateTime processPreparationTime;

    @Schema(description = "生产准备完成时间")
    @DiffLogField(name = "生产准备完成时间")
    private LocalDateTime productionPreparationTime;

    @Schema(description = "入库时间")
    @DiffLogField(name = "入库时间")
    private LocalDateTime warehousingTime;

    @Schema(description = "完成检验时间")
    @DiffLogField(name = "完成检验时间")
    private LocalDateTime checkoutCompletionTime;

    @Schema(description = "计划交付时间")
    @DiffLogField(name = "计划交付时间")
    private LocalDateTime planDeliveryTime;

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "30024")
    private String processInstanceId;

    @Schema(description = "项目计划子表，产品计划完善列表")
    private List<PlanItemDO> planItems;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

    @Schema(description = "负责人")
    @DiffLogField(name = "负责人")
    private Long responsiblePerson;

    @Schema(description = "加工状态")
    @DiffLogField(name = "加工状态")
    private String processCondition;

    @Schema(description = "工艺版本id")
    @DiffLogField(name = "工艺版本id")
    private String processVersionId;

}
