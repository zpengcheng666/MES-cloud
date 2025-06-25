package com.miyu.cloud.dms.controller.admin.calendarshift.vo;

import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 班次类型新增/修改 Request VO")
@Data
public class ShiftTypeSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25449")
    private String id;

    @Schema(description = "班次名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "班次名称不能为空")
    private String name;

    @Schema(description = "基础日历id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25449")
    private String bcid;

    @Schema(description = "基础日历名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String basicName;

    @Schema(description = "班次时间列表")
    private List<ShiftTimeDO> shiftTimes;

}
