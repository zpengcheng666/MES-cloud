package cn.iocoder.yudao.module.bpm.controller.admin.oaclock.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - OA 打卡 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OaClockRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22533")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "用户名", example = "王五")
    @ExcelProperty("用户名")
    private String name;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "19374")
    @ExcelProperty("用户id")
    private Long userId;

    @Schema(description = "打卡类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "打卡类型", converter = DictConvert.class)
    @DictFormat("bpm_oa_clock_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer type;

    @Schema(description = "打卡状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "打卡状态", converter = DictConvert.class)
    @DictFormat("bpm_oa_clock_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer clockStatus;

    @Schema(description = "部门")
    @ExcelProperty("部门")
    private String dept;

    @Schema(description = "岗位")
    @ExcelProperty("岗位")
    private String job;

    @Schema(description = "打卡时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("打卡时间")
    private LocalDateTime clockTime;

    @Schema(description = "打卡异常原因")
    @ExcelProperty("打卡异常原因")
    private String reason;

    @Schema(description = "审批结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("审批结果")
    private Integer status;

    @Schema(description = "流程实例的编号", example = "27349")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
