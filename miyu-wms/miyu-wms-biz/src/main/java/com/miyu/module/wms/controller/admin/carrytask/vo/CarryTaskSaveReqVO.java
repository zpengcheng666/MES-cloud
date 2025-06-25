package com.miyu.module.wms.controller.admin.carrytask.vo;

import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 搬运任务新增/修改 Request VO")
@Data
public class CarryTaskSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25712")
    private String id;

    @Schema(description = "任务编码")
    private String taskCode;

    @Schema(description = "任务状态(未开始、进行中、已完成、已取消)", example = "2")
    private Integer taskStatus;

    @Schema(description = "任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)不能为空")
    private Integer taskType;

    @Schema(description = "任务内容")
    private String taskContent;

    @Schema(description = "任务描述", example = "随便")
    private String taskDescription;

    @Schema(description = "AGV ID", example = "25317")
    private String agvId;

    @Schema(description = "库存单集合")
    private String orderIds;

    @Schema(description = "搬运任务子表列表")
    private List<CarrySubTaskDO> carrySubTasks;

}
