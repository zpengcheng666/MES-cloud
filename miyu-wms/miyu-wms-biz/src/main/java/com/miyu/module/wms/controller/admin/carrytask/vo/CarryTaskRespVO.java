package com.miyu.module.wms.controller.admin.carrytask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;
import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;

@Schema(description = "管理后台 - 搬运任务 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CarryTaskRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25712")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "任务编码")
    @ExcelProperty("任务编码")
    private String taskCode;

    @Schema(description = "任务状态(未开始、进行中、已完成、已取消)", example = "2")
    @ExcelProperty(value = "任务状态(未开始、进行中、已完成、已取消)", converter = DictConvert.class)
    @DictFormat("wms_carrying_task_status") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer taskStatus;

    @Schema(description = "任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "任务类型(入库搬运、出库搬运、库存移交、呼叫托盘)", converter = DictConvert.class)
    @DictFormat("wms_carrying_task_type") // TODO 代码优化：建议设置到对应的 DictTypeConstants 枚举类中
    private Integer taskType;

    @Schema(description = "任务内容")
    @ExcelProperty("任务内容")
    private String taskContent;

    @Schema(description = "优先级")
    @ExcelProperty("优先级")
    private Integer priority;

    @Schema(description = "任务描述", example = "随便")
    @ExcelProperty("任务描述")
    private String taskDescription;

    @Schema(description = "AGV ID", example = "25317")
    @ExcelProperty("AGV ID")
    private String agvId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Schema(description = "库存单集合")
    @ExcelProperty("库存单集合")
    private String orderIds;

}