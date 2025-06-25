package com.miyu.cloud.es.api.visit.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class VisitSaveResVO {

    @Schema(description = "访问记录ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22309")
    private String visitRecordId;

    @Schema(description = "访客姓名", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "访客姓名不能为空")
    private String name;

    @Schema(description = "访客车牌", requiredMode = Schema.RequiredMode.REQUIRED, example = "")
    @NotEmpty(message = "访客车牌不能为空")
    private String[] licenseNoList;

    @Schema(description = "访客签退时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "访客签退时间不能为空")
    private Long visitorCancelTime;

    @Schema(description = "访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "访问记录状态(1：创建访客访问记录 2：取消访客访问记录 3：签到)不能为空")
    private Integer status;

    @Schema(description = "访客单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "访客单位不能为空")
    private String company;

    @Schema(description = "来访目的", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "来访目的不能为空")
    private String cause;

    @Schema(description = "同行人数", requiredMode = Schema.RequiredMode.REQUIRED, example = "1411")
    @NotEmpty(message = "同行人数不能为空")
    private String followCount;

    @Schema(description = "计划开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划开始时间不能为空")
    private Long planBeginTime;

    @Schema(description = "计划结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "计划结束时间不能为空")
    private Long planEndTime;

    @Schema(description = "访客签到时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "访客签到时间不能为空")
    private Long visitorRecordTime;

    @Schema(description = "访客签到码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "访客签到码不能为空")
    private String visitorCheckCode;

    @Schema(description = "被访人tpId", requiredMode = Schema.RequiredMode.REQUIRED, example = "320")
    @NotEmpty(message = "被访人tpId不能为空")
    private String visitTpId;

    @Schema(description = "设备SN", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "设备SN不能为空")
    private String deviceSn;
}
