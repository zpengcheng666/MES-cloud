package cn.iocoder.yudao.module.bpm.controller.admin.oameeting.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - OA 会议申请 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OaMeetingRespVO {

    @Schema(description = "请假表单主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "14144")
    @ExcelProperty("请假表单主键")
    private Long id;

    @Schema(description = "会议主题")
    @ExcelProperty("会议主题")
    private String title;

//    @Schema(description = "会议日期")
//    @ExcelProperty("会议日期")
//    private LocalDate mDate;

    @Schema(description = "参会人员")
    @ExcelProperty("参会人员")
    private String staff;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "会议室")
    @ExcelProperty("会议室")
    private String mroom;

    @Schema(description = "描述", example = "你说的对")
    @ExcelProperty("描述")
    private String description;

    @Schema(description = "文件")
    @ExcelProperty("文件")
    private String document;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审批结果", example = "2")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "1824")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

}
