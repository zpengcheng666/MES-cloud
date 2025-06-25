package cn.iocoder.yudao.module.bpm.controller.admin.oasupply.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - OA 物品领用 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OaSupplyRespVO {

    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "560")
    @ExcelProperty("请假表单主键")
    private Long id;

    @Schema(description = "申请人部门", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请人部门")
    private String dept;

    @Schema(description = "申请人", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请人")
    private String applicant;

    @Schema(description = "申请部门领导", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("申请部门领导")
    private String leader;

    @Schema(description = "仓库管理员")
    @ExcelProperty("仓库管理员")
    private String warehouseman;

    @Schema(description = "申请原因", example = "不好")
    @ExcelProperty("申请原因")
    private String reason;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "28355")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
