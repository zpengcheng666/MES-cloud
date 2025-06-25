package com.miyu.module.ppm.controller.admin.contractconsignment.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 外协发货分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractConsignmentPageReqVO extends PageParam {

    @Schema(description = "发货单编号")
    private String consignmentNo;

    @Schema(description = "发货单名称", example = "张三")
    private String consignmentName;

    @Schema(description = "合同ID", example = "13776")
    private String contractId;

    @Schema(description = "公司ID", example = "27667")
    private String companyId;

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "审批状态", example = "2")
    private Integer status;

    @Schema(description = "工作流编号", example = "13946")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4待发货5结束 9取消", example = "2")
    private Integer consignmentStatus;

}