package com.miyu.module.ppm.controller.admin.purchaseconsignment.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

import static com.miyu.module.ppm.enums.DictTypeConstants.*;

@Schema(description = "管理后台 - 采购收货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseConsignmentRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6249")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "收货单")
    @ExcelProperty("收货单")
    private String no;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14173")
    @ExcelProperty("合同ID")
    private String contractId;

    @Schema(description = "收货人")
    @ExcelProperty("收货人")
    private String consignedBy;

    @Schema(description = "收货日期")
    @ExcelProperty("收货日期")
    private LocalDateTime consignedDate;

    @Schema(description = "收货人联系方式")
    @ExcelProperty("收货人联系方式")
    private String consignedContact;

    @Schema(description = "发货人")
    @ExcelProperty("发货人")
    private String consigner;

    @Schema(description = "发货人联系方式")
    @ExcelProperty("发货人联系方式")
    private String consignerContact;

    @Schema(description = "发货日期")
    @ExcelProperty("发货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "承运方式")
    @ExcelProperty(value = "承运方式", converter = DictConvert.class)
    @DictFormat(TRANSPORT_METHOD) // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer deliveryMethod;

    @Schema(description = "承运单号")
    @ExcelProperty("承运单号")
    private String deliveryNumber;

    @Schema(description = "承运人")
    @ExcelProperty("承运人")
    private String deliveryBy;

    @Schema(description = "承运人电话")
    @ExcelProperty("承运人电话")
    private String deliveryContact;

    @Schema(description = "创建日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建日期")
    private LocalDateTime createTime;

    @Schema(description = "流程实例编号", example = "18623")
    @ExcelProperty("流程实例编号")
    private String processInstanceId;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "审批结果", converter = DictConvert.class)
    @DictFormat(AUDIT_STATUS) // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", example = "2")
    @ExcelProperty(value = "状态  0已创建 1审批中 2 待出库 3待发货4运输中 5结束 9取消", converter = DictConvert.class)
    @DictFormat(PURCHASECONSIGNMENT_STATUS)
    private Integer consignmentStatus;

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同编号")
    private String contractNo;

    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合同名称")
    private String contractName;


    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    private Integer contractType;


    private Integer type;


    @Schema(description = "合作方", requiredMode = Schema.RequiredMode.REQUIRED, example = "953")
    @ExcelProperty("合作方")
    private String companyName;

    @Schema(description = "收货单名称")
    @ExcelProperty("收货单名称")
    private String name;

    private String projectId;
    private String projectName;
    private String projectCode;


    @Schema(description = "处理方式 1返修2换货3退货退款4仅退款", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer returnType;

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不对")
    private String returnReason;

}