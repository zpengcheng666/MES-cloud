package com.miyu.module.pdm.service.feasibilityDetail;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.*;

import javax.validation.Valid;
import java.util.List;

/**
 *技术评估 Service 接口
 */
public interface FeasibilityDetailService {

    /**
     * 通过选中项目获取零件列表
     */
    List<ProjPartBomRespVO> getProjPartBomListByProjectCode(ProjPartBomReqVO reqVO);

    ProjPartBomRespVO getPartDetailByTaskId(String id);

    FeasibilityResultDO getFeasibilityResult(FeasibilityResultReqVO reqVO);

    String createFeasibilityResult(@Valid FeasibilityResultSaveReqVO createReqVO);

    void updateFeasibilityResult(@Valid FeasibilityResultSaveReqVO updateReqVO);

    /**
     * 保存选中资源信息
     */
    void saveSelectedResource(ResourceSelectedReqVO reqVO);

    /**
     * 删除选中资源信息
     */
    void deleteSelectedDevice(FeasibilityDetailReqVO reqVO);

    /**
     * 更改设备预估工时
     */
    void updateProcessingTime(FeasibilityDetailReqVO reqVO);

    List<FeasibilityDetailRespVO> getResourceListByPart(ResourceSelectedReqVO reqVO);

    List<DemandDeviceRespVO> getDemandDeviceList(DemandDeviceReqVO reqVO);

    String createDemandDevcie(DemandDeviceReqVO reqVO);

    void updateDemandDevice(DemandDeviceReqVO reqVO);

    void deleteDemandDevice(String id);

    DemandDeviceDO getDemandDevice(String id);
//
    List<DemandMaterialRespVO> getDemandMaterialList(DemandMaterialReqVO reqVO);

    String createDemandMaterial(DemandMaterialReqVO reqVO);

    void updateDemandMaterial(DemandMaterialReqVO reqVO);

    void deleteDemandMaterial(String id);

    DemandMaterialDO getDemandMaterial(String id);

    List<DemandMeasureRespVO> getDemandMeasureList(DemandMeasureReqVO reqVO);

    String createDemandMeasure(DemandMeasureReqVO reqVO);

    void updateDemandMeasure(DemandMeasureReqVO reqVO);

    void deleteDemandMeasure(String id);

    DemandMeasureDO getDemandMeasure(String id);

    List<DemandCutterRespVO> getDemandCutterList(DemandCutterReqVO reqVO);

    String createDemandCutter(DemandCutterReqVO reqVO);

    void updateDemandCutter(DemandCutterReqVO reqVO);

    void deleteDemandCutter(String id);

    DemandCutterDO getDemandCutter(String id);

    List<DemandHiltRespVO> getDemandHiltList(DemandHiltReqVO reqVO);

    String createDemandHilt(DemandHiltReqVO reqVO);

    void updateDemandHilt(DemandHiltReqVO reqVO);

    void deleteDemandHilt(String id);

    DemandHiltDO getDemandHilt(String id);

    QuotaPerPartDO getQuotaPerPart(QuotaPerPartReqVO reqVO);

    String createQuotaPerPart(@Valid QuotaPerPartSaveReqVO createReqVO);

    void updateQuotaPerPart(@Valid QuotaPerPartSaveReqVO updateReqVO);

    void updateFeasibilityTaskStatus(@Valid FeasibilityTaskReqVO updateReqVO);

    void startProcessInstance(@Valid FeasibilityTaskReqVO updateReqVO);

    void updateProcessInstanceStatus(String id,Integer status);

    List<ProjPartBomRespVO> getPartListByProjectCodeNew(ProjPartBomReqVO reqVO);

    ProjPartBomRespVO getPartDetailByTaskIdNew(String id);

    void updateProjectstatus(String projectCode);

    int deleteMeesage(String projectCode,String partNumber,String partName,String processCondition);
}
