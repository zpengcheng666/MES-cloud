package com.miyu.cloud.mcs.dto.manufacture;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批次任务
 */
@Data
public class McsBatchRecordDTO {

    //批次任务id
    @Schema(description = "批次任务id")
    private String id;
    //生产单号
    @Schema(description = "生产单号")
    private String number;
    //数量
    @Schema(description = "数量")
    private Integer count;
    //零件图号
    @Schema(description = "零件图号")
    private String partNumber;
    //工艺规程号
    @Schema(description = "工艺规程号")
    private String technologyCode;
    //工艺id
    @Schema(description = "工艺id")
    private String technologyId;
    //工序id
    @Schema(description = "工序id")
    private String processId;
    //工序编号
    @Schema(description = "工序编号")
    private String processNumber;
    //状态
    @Schema(description = "状态")
    private Integer status;

    //物料条码
    @Schema(description = "物料条码")
    private String barCode;
    //预计开始时间
    @Schema(description = "预计开始时间")
    private LocalDateTime planStartTime;
    //预计结束时间
    @Schema(description = "预计结束时间")
    private LocalDateTime planEndTime;
    //开始时间
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    //结束时间
    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    List<McsStepRecordDTO> stepRecordList;
}
