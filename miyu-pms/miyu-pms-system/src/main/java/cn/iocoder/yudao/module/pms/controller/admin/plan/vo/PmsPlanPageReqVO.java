package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PmsPlanPageReqVO extends PageParam {
    @Schema(description = "项目订单编号(项目订单ID)", example = "15863")
    private String projectOrderId;

    @Schema(description = "项目id", example = "14878")
    private String projectId;

    @Schema(description = "项目编码")
    private String projectCode;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称", example = "芋艿")
    private String partName;

    @Schema(description = "订单类型(0为外部销售订单，1为内部自制订单)", example = "2")
    private Integer orderType;

    @Schema(description = "外协数量(向外委派)，默认为0，项目评审时，产能评估填写，为0就是不用外协")
    private Integer outSourceAmount;
    @Schema(description = "工序外协数量")
    private Integer stepOutSourceAmount;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "带料加工(是/否)", example = "1")
    private Integer processType;

    @Schema(description = "加工状态")
    private String processCondition;

    @Schema(description = "工艺方案(id)")
    private String processScheme;

    @Schema(description = "提醒")
    private String remindInfo;

    @Schema(description = "采购完成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] purchaseCompletionTime;

    @Schema(description = "工艺准备完成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] processPreparationTime;

    @Schema(description = "生产准备完成时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] productionPreparationTime;

    @Schema(description = "入库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] warehousingTime;

    @Schema(description = "完成检验时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] checkoutCompletionTime;

    @Schema(description = "计划交付时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planDeliveryTime;

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "30024")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "负责人")
    private Long responsiblePerson;

    @Schema(description = "工艺版本id")
    private String processVersionId;

}
