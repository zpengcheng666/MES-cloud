package com.miyu.module.ppm.controller.admin.contractconsignment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 外协发货 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractConsignmentRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "28867")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "发货单编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货单编号")
    private String consignmentNo;

    @Schema(description = "发货单名称", example = "张三")
    @ExcelProperty("发货单名称")
    private String consignmentName;

    @Schema(description = "合同ID", example = "13776")
    @ExcelProperty("合同ID")
    private String contractId;
    private String contractNo;
    private String contractName;

    @Schema(description = "公司ID", example = "27667")
    @ExcelProperty("公司ID")
    private String companyId;
    private String companyName;

    @Schema(description = "发货人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货人")
    private String consigner;

    @Schema(description = "发货日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发货日期")
    private LocalDateTime consignerDate;

    @Schema(description = "承运方式")
    @ExcelProperty(value = "承运方式", converter = DictConvert.class)
    @DictFormat("transport_way") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
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

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty(value = "审批状态", converter = DictConvert.class)
    @DictFormat("ppm_audit_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer status;

    @Schema(description = "工作流编号", example = "13946")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    @ExcelProperty("附件地址")
    private String fileUrl;

    @Schema(description = "状态  0已创建 1审批中 2 待出库 3出库中4待发货5结束 9取消", example = "2")
    @ExcelProperty(value = "状态  0已创建 1审批中 2 待出库 3出库中4待发货5结束 9取消", converter = DictConvert.class)
    @DictFormat("shipping_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer consignmentStatus;

}