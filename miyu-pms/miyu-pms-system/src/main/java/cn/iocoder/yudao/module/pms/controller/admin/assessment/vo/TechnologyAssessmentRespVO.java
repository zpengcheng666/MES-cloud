package cn.iocoder.yudao.module.pms.controller.admin.assessment.vo;

import com.miyu.module.pdm.api.projectAssessment.dto.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "技术评审相关数据RespVO")
@Data
public class TechnologyAssessmentRespVO {
    //项目编号
    private String projectCode;
    //评审id
    private String assessmentId;
    //采购设备
    private List<DemandDeviceRespVO> demandDeviceList;
    //采购刀具
    private List<DemandCutterRespVO> demandCutterList;
    //采购刀柄
    private List<DemandHiltRespVO> demandHiltList;
    //采购工装
    private List<DemandMaterialRespVO> demandMaterialList;
    //设备
    private List<DeviceRespVO> deviceList;
    //刀具
    private List<CombinationRespVO> combinationList;
    //工装
    private List<MaterialRespVO> materialList;

}
