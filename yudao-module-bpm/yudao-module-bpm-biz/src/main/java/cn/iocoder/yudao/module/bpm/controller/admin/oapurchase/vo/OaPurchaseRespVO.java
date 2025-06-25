package cn.iocoder.yudao.module.bpm.controller.admin.oapurchase.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - OA 采购申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OaPurchaseRespVO {

    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "26216")
    @ExcelProperty("请假表单主键")
    private Long id;

    @Schema(description = "申请人部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请人部门")
    private String dept;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请人")
    private String applicant;

    @Schema(description = "采购人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("采购人")
    private String purchaseAgent;

    @Schema(description = "供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("供应商")
    private String supplier;

    @Schema(description = "供应商编码")
    @ExcelProperty("供应商编码")
    private String supplyCode;

    @Schema(description = "联系人")
    @ExcelProperty("联系人")
    private String contact;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String tel;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "21999")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
