package com.miyu.cloud.mpc.dto;

import com.miyu.cloud.mcs.dto.productionProcess.McsPlanStepNcDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class FurnacePlanStartDTO {

    //工序任务编码集合
    @Schema(description = "工序任务编码集合", example = "OR123,1,2,3")
    private String orderNumber;
    //工序名称
    @Schema(description = "工序名称", example = "加工")
    private String processName;
    //工步NC程序列表
    @Schema(description = "工步NC程序列表")
    private List<McsPlanStepNcDTO> ncList;
    //设备编码
    @Schema(description = "工序名称", example = "加工")
    private String deviceNumber;
}
