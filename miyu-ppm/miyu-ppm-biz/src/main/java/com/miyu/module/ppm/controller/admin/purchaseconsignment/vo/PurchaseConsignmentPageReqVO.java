package com.miyu.module.ppm.controller.admin.purchaseconsignment.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购收货分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseConsignmentPageReqVO extends PageParam {

    @Schema(description = "合同ID", example = "14173")
    private String contractId;

    @Schema(description = "收货人")
    private String consignedBy;

    @Schema(description = "收货单")
    private String no;

    @Schema(description = "收货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "发货人")
    private String consigner;

    @Schema(description = "发货人联系方式")
    private String consignerContact;

    @Schema(description = "发货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] consignerDate;

    @Schema(description = "承运方式")
    private Integer deliveryMethod;

    @Schema(description = "承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人")
    private String deliveryBy;

    @Schema(description = "承运人电话")
    private String deliveryContact;

    @Schema(description = "创建日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "流程实例编号", example = "18623")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    /**
     * 状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消
     *
     * 枚举 {@link ConsignmentStatusEnum}
     */
    private Integer consignmentStatus;

    /***
     * 收货单名称
     */
    private String name;


    /***
     * 收货单类型1采购收货2外协收获3外协原材料退货4委托加工收货5销售退货
     */
    private Integer consignmentType;
    private List<Integer> consignmentTypes;

    private List<Integer> consignmentStatuss;
}