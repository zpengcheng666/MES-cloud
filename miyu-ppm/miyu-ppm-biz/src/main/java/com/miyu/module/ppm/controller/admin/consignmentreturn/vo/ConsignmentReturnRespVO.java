package com.miyu.module.ppm.controller.admin.consignmentreturn.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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

@Schema(description = "管理后台 - 销售退货单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConsignmentReturnRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4930")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "退货单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("退货单编号")
    private String consignmentReturnNo;

    @Schema(description = "退货单名称", example = "王五")
    @ExcelProperty("退货单名称")
    private String consignmentReturnName;

    @Schema(description = "合同ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20431")
    @ExcelProperty("合同ID")
    private String contractId;

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

    @Schema(description = "退换货原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "不香")
    @ExcelProperty("退换货原因")
    private String returnReason;

    @Schema(description = "工作流编号", example = "7107")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审批状态")
    private Integer status;

    @Schema(description = "状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废", example = "1")
    @ExcelProperty(value = "状态  0已创建 1审批中 2待发货 3运输中 4 待入库 5结束 6审批不通过 7已作废", converter = DictConvert.class)
    @DictFormat("consignment_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer consignmentStatus;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    @ExcelProperty("附件地址")
    private String fileUrl;

    @Schema(description = "备注", example = "你猜")
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


    private String partyName;


    private String ContractNum;

    @ExcelProperty("退款单金额")
    private BigDecimal price;

}