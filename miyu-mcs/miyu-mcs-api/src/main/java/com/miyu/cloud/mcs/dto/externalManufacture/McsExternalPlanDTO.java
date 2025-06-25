package com.miyu.cloud.mcs.dto.externalManufacture;

import com.miyu.cloud.mcs.dto.productionProcess.McsPlanProcessDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanStepNcDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class McsExternalPlanDTO {

    //物料唯一码
    @Schema(description = "物料编码", example = "BC-1234")
    private String materialCode;
    //任务编码(工序任务)
    @Schema(description = "工序任务编码", example = "OR123")
    private String orderNumber;
    //任务id(工序任务)
    @Schema(description = "工序任务id", example = "123456")
    private String orderId;
    //工序序号
    @Schema(description = "工序序号", example = "15")
    private String processNumber;
    //工序名称
    @Schema(description = "工序名称", example = "加工")
    private String processName;
    //工步编号
    @Schema(description = "工步编号", example = "15.01")
    private String stepNumber;
    //工步名称
    @Schema(description = "工步名称", example = "加工装夹")
    private String stepName;
    //设备编码
    @Schema(description = "设备编码", example = "abc")
    private String deviceNumber;
    //状态
    @Schema(description = "状态", example = "0:新建;1:进行中;2:已完成;3:已撤销")
    private Integer status;
    //托盘规格(大规格/小规格)
    @Schema(description = "托盘规格(大规格/小规格)", example = "1200X1211")
    private String trayModel;
    //报告类型
    @Schema(description = "报告类型", example = "csv,xlsx,xls,pdf")
    private String reportType;
    //报告路径
    @Schema(description = "报告路径")
    private String reportPath;
    //工步文件Url
    @Schema(description = "工步文件Url", example = "['localhost:80/download/xxx/xxx.xx']")
    private List<McsPlanStepNcDTO> ncList;

    //工序信息
    @Schema(description = "工序信息")
    private McsPlanProcessDTO processDTO;

}
