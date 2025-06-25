package com.miyu.module.ppm.controller.admin.shippingreturn.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 采购退货单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ShippingReturnRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "25075")
    @ExcelProperty("主键")
    private String id;

//    @Schema(description = "发货单", requiredMode = Schema.RequiredMode.REQUIRED, example = "28859")
//    @ExcelProperty("发货单")
//    private String shippingId;

    @Schema(description = "退货单编号")
    @ExcelProperty("退货单编号")
    private String shippingReturnNo;
    @Schema(description = "退货单名称")
    @ExcelProperty("退货单名称")
    private String shippingReturnName;


    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27069")
    @ExcelProperty("合同ID")
    private String contractId;
    private String projectId;

    @Schema(description = "退货人")
    @ExcelProperty("退货人")
    private String consigner;

    @Schema(description = "退货日期")
    @ExcelProperty("退货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "接收人")
    @ExcelProperty("接收人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @ExcelProperty("收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    @ExcelProperty("收货人联系方式")
    private String consignedContact;

    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "处理方式 1返修2换货3退货退款4仅退款", converter = DictConvert.class)
    @DictFormat("return_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    @ExcelProperty("退换货原因")
    private String returnReason;

    @Schema(description = "工作流编号", example = "1220")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("审批状态")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2 待退货 3运输中 4结束 9取消", example = "2")
    @ExcelProperty("状态  0已创建 1审批中 2 待退货 3运输中 4结束 9取消")
    private Integer shippingStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    @ExcelProperty("附件地址")
    private String fileUrl;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同编号")
    private String contractNo;


    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同名称")
    private String contractName;
    @Schema(description = "合作方", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合作方")
    private String companyName;

    @ExcelProperty("退货单金额")
    private BigDecimal price;
}