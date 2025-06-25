package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 项目计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PmsPlanRespVO {
    @Schema(description = "项目计划id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19639")
    @ExcelProperty("项目计划id")
    private String id;

    @Schema(description = "项目订单编号(项目订单ID)", example = "15863")
    @ExcelProperty("项目订单编号(项目订单ID)")
    private String projectOrderId;

    @Schema(description = "项目id", example = "14878")
    @ExcelProperty("项目id")
    private String projectId;

    @Schema(description = "项目编码")
    @ExcelProperty("项目编码")
    private String projectCode;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    @ExcelProperty("物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    @ExcelProperty("图号")
    private String partNumber;

    @Schema(description = "工件名称", example = "芋艿")
    @ExcelProperty("工件名称")
    private String partName;

    @Schema(description = "订单类型(0为外部销售订单，1为内部自制订单)", example = "2")
    @ExcelProperty(value = "订单类型(0为外部销售订单，1为内部自制订单)")
    private Integer orderType;

    @Schema(description = "外协数量(向外委派)，默认为0，项目评审时，产能评估填写，为0就是不用外协")
    @ExcelProperty("外协数量(向外委派)，默认为0，项目评审时，产能评估填写，为0就是不用外协")
    private Integer outSourceAmount;

    @Schema(description = "工序外协数量")
    private Integer stepOutSourceAmount;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer quantity;

    @Schema(description = "带料加工(是/否)", example = "1")
    @ExcelProperty(value = "带料加工(是/否)")
    private Integer processType;

    @Schema(description = "加工状态")
    @ExcelProperty("加工状态")
    private String processCondition;

    @Schema(description = "工艺方案(id)")
    @ExcelProperty("工艺方案(id)")
    private String processScheme;

    @Schema(description = "提醒")
    @ExcelProperty("提醒")
    private String remindInfo;

    @Schema(description = "采购完成时间")
    @ExcelProperty("采购完成时间")
    private LocalDateTime purchaseCompletionTime;

    @Schema(description = "工艺准备完成时间")
    @ExcelProperty("工艺准备完成时间")
    private LocalDateTime processPreparationTime;

    @Schema(description = "生产准备完成时间")
    @ExcelProperty("生产准备完成时间")
    private LocalDateTime productionPreparationTime;

    @Schema(description = "入库时间")
    @ExcelProperty("入库时间")
    private LocalDateTime warehousingTime;

    @Schema(description = "完成检验时间")
    @ExcelProperty("完成检验时间")
    private LocalDateTime checkoutCompletionTime;

    @Schema(description = "计划交付时间")
    @ExcelProperty("计划交付时间")
    private LocalDateTime planDeliveryTime;

    @Schema(description = "审批结果", example = "2")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "30024")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /** 项目名称 */
    private String projectName;

    @Schema(description = "负责人")
    private Long responsiblePerson;

    @Schema(description = "工艺版本id")
    private String processVersionId;

    @Schema(description = "工艺可选排序,1可选，0不可选")
    private Integer schemeOrderBy;

}
