package com.miyu.module.ppm.controller.admin.shippingreturn.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;

@Schema(description = "管理后台 - 采购退货单新增/修改 Request VO")
@Data
public class ShippingReturnSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25075")
    private String id;

    @Schema(description = "退货单编号")
    private String shippingReturnNo;
    @Schema(description = "退货单名称")
    private String shippingReturnName;

//    @Schema(description = "发货单", requiredMode = Schema.RequiredMode.REQUIRED, example = "28859")
//    @NotEmpty(message = "发货单不能为空")
//    private String shippingId;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27069")

    private String contractId;


    @NotEmpty(message = "项目不能为空")
    private String projectId;

    @Schema(description = "退货人")
    private String consigner;

    @Schema(description = "退货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "接收人")
    private String consignedBy;

    @Schema(description = "收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    private String consignedContact;

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "处理方式 1返修2换货3退货退款4仅退款不能为空")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    @NotEmpty(message = "退换货原因不能为空")
    private String returnReason;

    @Schema(description = "工作流编号", example = "1220")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2 待退货 3运输中 4结束 9取消", example = "2")
    private Integer shippingStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "销售退货单详情列表")
    @NotEmpty(message = "销售退货单详情不能为空")
    private List<ShippingReturnDetailDO> shippingReturnDetails;

}