package com.miyu.module.ppm.controller.admin.shippingreturn.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购退货单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingReturnPageReqVO extends PageParam {

//    @Schema(description = "发货单", example = "28859")
//    private String shippingId;
    @Schema(description = "退货单编号")
    private String shippingReturnNo;
    @Schema(description = "退货单名称")
    private String shippingReturnName;

    @Schema(description = "合同ID", example = "27069")
    private String contractId;

    private String projectId;

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

    @Schema(description = "退换货原因", example = "不对")
    private String returnReason;

    @Schema(description = "工作流编号", example = "1220")
    private String processInstanceId;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2 待退货 3运输中 4结束 9取消", example = "2")
    private Integer shippingStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}