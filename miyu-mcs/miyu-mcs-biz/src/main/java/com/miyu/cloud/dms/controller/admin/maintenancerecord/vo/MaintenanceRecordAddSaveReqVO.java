package com.miyu.cloud.dms.controller.admin.maintenancerecord.vo;

import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordToSparePartDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 设备保养维护记录添加 Request VO")
@Data
public class MaintenanceRecordAddSaveReqVO {

    @Schema(description = "记录id")
    private String id;

    @Schema(description = "完成状态", example = "2")
    private Integer status;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "保养内容")
    private String content;

    @Schema(description = "开始维护时间")
    private LocalDateTime startTime;

    @Schema(description = "结束维护时间")
    private LocalDateTime endTime;

    @Schema(description = "使用备件")
    private List<MaintenanceRecordToSparePartDO> spareParts;

}

