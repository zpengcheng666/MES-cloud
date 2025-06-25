package com.miyu.cloud.mcs.controller.admin.orderform.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 生产订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderFormRespVO {

    @Schema(description = "ID", example = "1")
    private String id;

    @Schema(description = "主计划id")
    private String projectPlanId;

    @Schema(description = "项目号")
    @ExcelProperty("项目号")
    private String projectNumber;

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "零件图号", example = "李四")
    @ExcelProperty("零件图号")
    private String partNumber;
    private String partVersionId;

    @Schema(description = "订单类型", example = "1")
    @ExcelProperty("订单类型")
    private Integer orderType;

    @Schema(description = "工艺规程版本Id", example = "123")
    private String technologyId;

    @Schema(description = "优先级")
    @ExcelProperty("优先级")
    private Integer priority;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2822")
    @ExcelProperty("数量")
    private Integer count;

    @Schema(description = "接收时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("接收时间")
    private LocalDateTime receptionTime;

    @Schema(description = "交付时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("交付时间")
    private LocalDateTime deliveryTime;

    @Schema(description = "完成时间")
    @ExcelProperty("完成时间")
    private LocalDateTime completionTime;

    private String responsiblePerson;

    @Schema(description = "负责人")
    @ExcelProperty("负责人")
    private String responsiblePersonName;

    @Schema(description = "订单状态")
    private Integer status;

    @Schema(description = "批量管理")
    private Boolean isBatch;

    @Schema(description = "物料编码集合")
    private String materialCode;

    @Schema(description = "加工状态(1本厂加工 2整单外协)")
    private Integer procesStatus;

    @Schema(description = "外协厂家")
    private String aidMill;

    @Schema(description = "下发状态")
    private Boolean issued;

    @Schema(description = "排产状态")
    private Integer schedulingStatus;

    @Schema(description = "首件")
    private Boolean first;
}
