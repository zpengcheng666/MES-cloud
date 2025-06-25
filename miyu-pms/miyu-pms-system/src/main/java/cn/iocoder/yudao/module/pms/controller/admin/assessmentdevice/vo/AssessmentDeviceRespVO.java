package cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 评审子表，关联的设备 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssessmentDeviceRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22651")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "项目编号")
    @ExcelProperty("项目编号")
    private String projectCode;

    @Schema(description = "制造资源类型：1设备 2刀具 3工装", example = "2")
    @ExcelProperty("制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源id(设备、刀具、工装等)", example = "14499")
    @ExcelProperty("制造资源id(设备、刀具、工装等)")
    private String resourcesTypeId;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer amount;;
//    private Integer quantity;
    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Integer purchaseAmount;

    @Schema(description = "估值", example = "2100")
    @ExcelProperty("估值")
    private BigDecimal predictPrice;

    @Schema(description = "损耗", example = "2100")
    @ExcelProperty("损耗")
    private BigDecimal wastage;

    @Schema(description = "加工时间")
    @ExcelProperty("加工时间")
    private Long processingTime;

    @Schema(description = "评审表id", example = "3310")
    @ExcelProperty("评审表id")
    private String assessmentId;

    @Schema(description = "零部件图号")
    @ExcelProperty("零部件图号")
    private String partNumber;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "审批建议")
    @ExcelProperty("审批建议")
    private String suggestion;

}
