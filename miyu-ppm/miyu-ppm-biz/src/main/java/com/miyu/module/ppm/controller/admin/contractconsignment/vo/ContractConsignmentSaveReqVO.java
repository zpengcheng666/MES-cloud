package com.miyu.module.ppm.controller.admin.contractconsignment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;

@Schema(description = "管理后台 - 外协发货新增/修改 Request VO")
@Data
public class ContractConsignmentSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28867")
    private String id;

    @Schema(description = "发货单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发货单编号不能为空")
    private String consignmentNo;

    @Schema(description = "发货单名称", example = "张三")
    private String consignmentName;

    @Schema(description = "合同ID", example = "13776")
    private String contractId;

    @Schema(description = "公司ID", example = "27667")
    private String companyId;

    @Schema(description = "发货人", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发货人不能为空")
    private String consigner;

    @Schema(description = "发货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "发货日期不能为空")
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

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer status;

    @Schema(description = "工作流编号", example = "13946")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4待发货5结束 9取消", example = "2")
    private Integer consignmentStatus;

    @Schema(description = "外协发货单详情列表")
    private List<ContractConsignmentDetailDO> contractConsignmentDetails;

}