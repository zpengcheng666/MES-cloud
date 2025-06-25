package com.miyu.module.ppm.controller.admin.shipping.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售发货分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ShippingPageReqVO extends PageParam {

    @Schema(description = "合同ID", example = "953")
    private String contractId;
    @Schema(description = "合作方ID", example = "953")
    private String companyId;
    @Schema(description = "发货单名称")
    private String name;
    @Schema(description = "发货单号")
    private String no;

    @Schema(description = "发货人")
    private String consigner;

    @Schema(description = "发货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignerDate;

    @Schema(description = "承运方式")
    private Integer deliveryMethod;

    @Schema(description = "承运人")
    private String deliveryBy;

    @Schema(description = "承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人电话")
    private String deliveryContact;

    @Schema(description = "收货人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", example = "2")
    private Integer shippingStatus;



    @Schema(description = "发货单类型1销售发货2外协发货3采购退货4委托加工退货", example = "2")
    private Integer shippingType;

    /***
     * 项目ID
     */
    private String projectId;


    private List<Integer> shippingStatuss;

}