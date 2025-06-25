package com.miyu.module.ppm.controller.admin.shippinginstoragereturn.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售订单入库退货分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingInstorageReturnPageReqVO extends PageParam {

    @Schema(description = "入库单名称", example = "王五")
    private String name;

    @Schema(description = "入库单")
    private String no;

    @Schema(description = "合同ID", example = "20825")
    private String contractId;

    @Schema(description = "公司ID", example = "16936")
    private String companyId;

    @Schema(description = "项目ID", example = "23951")
    private String projectId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "审批状态", example = "2")
    private Integer status;

    @Schema(description = "工作流编号", example = "262")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4发货确认5结束 9取消 8审批失败", example = "2")
    private Integer shippingInstorageReturnStatus;

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

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", example = "2")
    private Integer returnType;

    @Schema(description = "退换货原因", example = "不喜欢")
    private String returnReason;

}