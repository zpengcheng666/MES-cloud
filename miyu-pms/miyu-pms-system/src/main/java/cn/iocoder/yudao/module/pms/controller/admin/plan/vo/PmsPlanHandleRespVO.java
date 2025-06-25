package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 项目计划")
@Data
@ToString(callSuper = true)
public class PmsPlanHandleRespVO {
    @Schema(description = "id", example = "15863")
    private String id;

    @Schema(description = "项目计划id", example = "15863")
    private String planId;

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

    @Schema(description = "审批结果", example = "2")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "30024")
    private String processInstanceId;

    @Schema(description = "负责人")
    private Long responsiblePerson;

    @Schema(description = "工艺版本id")
    private String processVersionId;
    /**
     * 物料比
     */
    //private Integer property;
    private Integer groupSise;
    /**
     * 总计需求，计算物料比后的需求
     */
    private Integer totalRequire;
    /**
     * 需求物料
     */
    private Integer require;
    /** 带料加工 */
    private Integer outsource;
    /** 剩余库存 */
    private Integer inventory;
    /** 采购数量 */
    private Integer purchaseAmount;

    /** 采购单数量 */
    private Integer hasPurchase;

    private List<PlanMaterialStock> materialCodeList;
}
