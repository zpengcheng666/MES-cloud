package com.miyu.module.ppm.controller.admin.shipping.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

import static com.miyu.module.ppm.enums.DictTypeConstants.*;

@Schema(description = "管理后台 - 销售发货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShippingRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22734")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同ID")
    private String contractId;


    @Schema(description = "合作商ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合作商ID")
    private String companyId;



    @Schema(description = "发货人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货人")
    private String consigner;

    @Schema(description = "发货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "承运方式")
    @ExcelProperty("承运方式")
    @DictFormat(TRANSPORT_METHOD_1)
    private Integer deliveryMethod;

    @Schema(description = "承运人")
    @ExcelProperty("承运人")
    private String deliveryBy;

    @Schema(description = "承运单号")
    @ExcelProperty("承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人电话")
    @ExcelProperty("承运人电话")
    private String deliveryContact;

    @Schema(description = "收货人")
    @ExcelProperty("收货人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @ExcelProperty("收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    @ExcelProperty("收货人联系方式")
    private String consignedContact;


    @Schema(description = "工作流编号", example = "1043")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @DictFormat(DM_AUDIT_STATUS)
    private Integer status;


    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    @ExcelProperty("附件地址")
    private String fileUrl;
    @Schema(description = "发货单")
    @ExcelProperty("发货单")
    private String no;
    @Schema(description = "发货单名称")
    @ExcelProperty("发货单名称")
    private String name;


    @Schema(description = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", example = "2")
    @ExcelProperty(value = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", converter = DictConvert.class)
    @DictFormat(SHIPPING_STATUS)
    private Integer shippingStatus;



    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同编号")
    private String contractNo;


    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同名称")
    private String contractName;
    @Schema(description = "合作方", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合作方")
    private String companyName;


    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;


    /**
     * 处理方式 1返修2换货3退货退款4仅退款
     *
     * 枚举 {@link ShippingReturnTypeEnum}
     */
    private Integer returnType;
    /**
     * 退换货原因
     */
    private String returnReason;

    /***
     * 项目ID
     */
    private String projectId;
    private String projectName;
    private String projectCode;
}