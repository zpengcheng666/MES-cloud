package com.miyu.cloud.mcs.controller.admin.orderform.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 生产订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderFormSelectListRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "订单状态")
    private List<Integer> status;


}
