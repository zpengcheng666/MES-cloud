package com.miyu.cloud.mcs.dto.externalManufacture;

import com.miyu.cloud.mcs.dto.productionProcess.McsPlanStepNcDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class McsFurnacePlanDTO {

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
    //设备编码
    @Schema(description = "设备名称", example = "abc")
    private String deviceName;
    //状态
    @Schema(description = "状态", example = "0:新建;1:进行中;2:已完成;3:已撤销")
    private Integer status;
    //工步文件Url
    @Schema(description = "工步文件Url", example = "['localhost:80/download/xxx/xxx.xx']")
    private List<McsPlanStepNcDTO> ncList;
    @Schema(description = "当前批次所有任务及计划", example = "")
    private List<Part> batchOrderList;

    public void addBatchOrderList(String barCode, String deviceNumber, LocalDateTime planStartTime) {
        if (this.batchOrderList == null) {
            batchOrderList = new ArrayList<>();
        }
        Part part = new Part();
        part.setMaterialCode(barCode);
        part.setDeviceNumber(deviceNumber);
        part.setPlanStartTime(planStartTime);
        batchOrderList.add(part);
    }

    class Part {
        @Schema(description = "物料编码", example = "BC-1234")
        private String materialCode;
        @Schema(description = "计划开工时间")
        private LocalDateTime planStartTime;
        @Schema(description = "计划加工设备编码", example = "abc")
        private String deviceNumber;

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public LocalDateTime getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(LocalDateTime planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getDeviceNumber() {
            return deviceNumber;
        }

        public void setDeviceNumber(String deviceNumber) {
            this.deviceNumber = deviceNumber;
        }
    }
}
