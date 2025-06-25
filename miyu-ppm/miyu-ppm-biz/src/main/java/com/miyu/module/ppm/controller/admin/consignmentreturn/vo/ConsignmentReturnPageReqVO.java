package com.miyu.module.ppm.controller.admin.consignmentreturn.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售退货单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsignmentReturnPageReqVO extends PageParam {

    @Schema(description = "退货单编号")
    private String consignmentReturnNo;

    @Schema(description = "退货单名称", example = "王五")
    private String consignmentReturnName;

    @Schema(description = "合同ID", example = "20431")
    private String contractId;

    @Schema(description = "退货人")
    private String consigner;

    @Schema(description = "退货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignerDate;

    @Schema(description = "接收人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", example = "1")
    private Integer returnType;

    @Schema(description = "退换货原因", example = "不香")
    private String returnReason;

    @Schema(description = "工作流编号", example = "7107")
    private String processInstanceId;

    @Schema(description = "审批状态", example = "2")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废", example = "1")
    private Integer consignmentStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}