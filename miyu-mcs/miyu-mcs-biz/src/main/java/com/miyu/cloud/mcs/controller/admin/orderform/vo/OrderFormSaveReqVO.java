package com.miyu.cloud.mcs.controller.admin.orderform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.miyu.cloud.mcs.enums.DictConstants.MCS_ORDER_STATUS_NEW;

@Schema(description = "管理后台 - 生产订单新增/修改 Request VO")
@Data
public class OrderFormSaveReqVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "主计划id")
    private String projectPlanId;

    @Schema(description = "项目号")
    private String projectNumber;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单编号不能为空")
    private String orderNumber;

    @Schema(description = "工艺规程版本Id", example = "123")
    private String technologyId;

    @Schema(description = "零件图号", example = "李四")
    private String partNumber;
    private String partVersionId;

    @Schema(description = "订单类型", example = "1")
    private Integer orderType;

    @Schema(description = "优先级")
    private Integer priority;

    @Schema(description = "起始工序号")
    private String beginProcessNumber;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2822")
    @NotNull(message = "数量不能为空")
    private Integer count;

    @Schema(description = "接收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "接收时间不能为空")
    private LocalDateTime receptionTime;

    @Schema(description = "交付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "交付时间不能为空")
    private LocalDateTime deliveryTime;

    @Schema(description = "完成时间")
    private LocalDateTime completionTime;

    @Schema(description = "负责人")
    private String responsiblePerson;

    @Schema(description = "订单状态")
    private Integer status = MCS_ORDER_STATUS_NEW;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "物料编码集合")
    private String materialCode;

    @Schema(description = "加工状态(1本厂加工 2整单外协)")
    private Integer procesStatus = 1;

    @Schema(description = "外协厂家")
    private String aidMill;

    @Schema(description = "下发状态")
    private Boolean issued = false;

    @Schema(description = "排产状态")
    private Integer schedulingStatus = 0;

    @Schema(description = "首件")
    private Boolean first;

}
