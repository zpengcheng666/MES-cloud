package com.miyu.module.ppm.controller.admin.consignmentinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 收货产品查询 Request VO")
@Data
public class ConsignmentInfoQueryReqVO {


    @Schema(description = "状态  状态  0已创建 1审批中 2待签收 3 入库中4待质检5质检中 6结束 7审批不通过 8已作废9待确认", example = "2")
    private Integer consignmentStatus;

    @Schema(description = "收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货", example = "2")
    private List<Integer> consignmentType;


    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private String creator;
}