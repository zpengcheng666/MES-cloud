package com.miyu.module.ppm.controller.admin.shippinginstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;

@Schema(description = "管理后台 - 销售订单入库新增/修改 Request VO")
@Data
public class ShippingInstorageSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4395")
    private String id;

    @Schema(description = "入库单名称", example = "张三")
    private String name;

    @Schema(description = "入库单", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单不能为空")
    private String no;

    @Schema(description = "合同ID", example = "9732")
    private String contractId;

    @Schema(description = "公司ID", example = "15246")
    private String companyId;

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5007")
    @NotEmpty(message = "项目ID不能为空")
    private String projectId;

    @Schema(description = "发货人", requiredMode = Schema.RequiredMode.REQUIRED)

    private String consigner;

    @Schema(description = "发货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime consignerDate;

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
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "工作流编号", example = "28382")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态  状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 9已作废", example = "1")
    private Integer shippingInstorageStatus;

    @Schema(description = "销售订单入库明细列表")
    private List<ShippingInstorageDetailDO> shippingInstorageDetails;

}