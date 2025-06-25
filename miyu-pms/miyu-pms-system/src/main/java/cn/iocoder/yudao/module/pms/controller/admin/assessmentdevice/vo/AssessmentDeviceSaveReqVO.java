package cn.iocoder.yudao.module.pms.controller.admin.assessmentdevice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 评审子表，关联的设备新增/修改 Request VO")
@Data
public class AssessmentDeviceSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22651")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "制造资源类型：1设备 2刀具 3工装", example = "2")
    private Integer resourcesType;

    @Schema(description = "制造资源id(设备、刀具、工装等)", example = "14499")
    private String resourcesTypeId;

    @Schema(description = "数量")
    private Integer amount;;
//    private Integer quantity;

    @Schema(description = "估值", example = "2100")
    private BigDecimal predictPrice;

    @Schema(description = "加工时间")
    private Long processingTime;

    @Schema(description = "评审表id", example = "3310")
    private String assessmentId;

    @Schema(description = "零部件图号")
    private String partNumber;

    @Schema(description = "审批建议")
    private String suggestion;

}
