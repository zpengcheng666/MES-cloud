package com.miyu.module.pdm.api.projectAssessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "RPC 服务 - 技术评审相关数据")
@Data
public class TechnologyAssessmentRespDTO {
    //项目编号
    private String projectCode;
    //评审id
    private String assessmentId;
    //采购设备
    private List<DemandDeviceRespDTO> demandDeviceList;
    //采购刀具
    private List<DemandCutterRespDTO> demandCutterList;
    //采购刀柄
    private List<DemandHiltRespDTO> demandHiltList;
    //采购工装
    private List<DemandMaterialRespDTO> demandMaterialList;
    //设备刀具工装
    private List<FeasibilityDetailRespDTO> feasibilityDetailList;
    //相关零件
    private List<ProjPartBomRespDTO> projPartBomList;
    //设备
    private List<DeviceRespDTO> deviceList;
    //刀具
    private List<CombinationRespDTO> combinationList;
    //工装
    private List<MaterialRespDTO> materialList;
    //采购量具
    private List<DemandMeasureRespDTO> demandMeasureList;
    //量具
    private List<MeasureRespDTO> measureList;


}
