package com.miyu.module.pdm.api.processPlanDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.dto.*;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcessPlanDetailRespVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDetailDO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.*;
import com.miyu.module.pdm.dal.dataobject.stepCategory.CustomizedAttributeDO;
import com.miyu.module.pdm.dal.mysql.process.ProcedureDetailMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcedureFileMapper;
import com.miyu.module.pdm.dal.mysql.process.ProcedureMapper;
import com.miyu.module.pdm.dal.mysql.processPlanDetail.*;
import com.miyu.module.pdm.dal.mysql.stepCategory.CustomizedAttributeMapper;
import com.miyu.module.pdm.service.combination.CombinationService;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class ProcessPlanDetailApiImpl implements ProcessPlanDetailApi {

    @Resource
    private ProcessPlanDetailService processPlanDetailService;

    @Resource
    private ProcedureMapper procedureMapper;

    @Resource
    private StepMapper stepMapper;

    @Resource
    private ProcedureDetailMapper procedureDetailMapper;

    @Resource
    private DeviceTypeApi deviceTypeApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private CombinationService combinationService;

    @Resource
    private ProcedureFileMapper procedureFileMapper;

    @Resource
    private StepDetailMapper stepDetailMapper;

    @Resource
    private StepFileMapper stepFileMapper;

    @Resource
    private ProcessVersionNcMapper processVersionNcMapper;

    @Resource
    private CustomizedAttributeValMapper customizedAttributeValMapper;

    @Resource
    private CustomizedAttributeMapper customizedAttributeMapper;

    @Resource
    private NcMapper ncMapper;

    @Override
    public CommonResult<String> updateProcessStatus(String businessKey, Integer status) {
        processPlanDetailService.updateProcessInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<String> getProcessPlanDetail(String processVersionId) {
        ProcessPlanDetailRespVO processPlanDetailRespVO = processPlanDetailService.getProcess(processVersionId);
        ProcessPlanDetailRespNewDTO detailDTO = BeanUtils.toBean(processPlanDetailRespVO, ProcessPlanDetailRespNewDTO.class);
        List<ProcedureRespNewDTO> procedureList = new ArrayList<>();
        List<ProcedureDO> procedureDOList = procedureMapper.selectList(new LambdaQueryWrapperX<ProcedureDO>()
                .eq(ProcedureDO::getProcessVersionId, processVersionId)
                .orderByAsc(ProcedureDO::getProcedureNum));
        for(ProcedureDO procedureDO : procedureDOList) {
            ProcedureRespNewDTO respDTO = BeanUtils.toBean(procedureDO, ProcedureRespNewDTO.class);
            List<StepRespDTO> stepList = new ArrayList<>();
            List<StepDO> stepDOList = stepMapper.selectList(new LambdaQueryWrapperX<StepDO>()
                    .eq(StepDO::getProcessVersionId, processVersionId)
                    .eq(StepDO::getProcedureId, procedureDO.getId())
                    .orderByAsc(StepDO::getStepNum));
            for(StepDO stepDO : stepDOList) {
                StepRespDTO stepRespDTO = BeanUtils.toBean(stepDO, StepRespDTO.class);

                //处理工步关联资源(设备、刀具)------------------start
                List<String> deviceIds = new ArrayList<>();
                List<StepDetailRespDTO> stepDetailRespDTOList = new ArrayList<>();
                List<StepDetailDO> stepDetailDOList = stepDetailMapper.selectList(new LambdaQueryWrapperX<StepDetailDO>()
                        .eq(StepDetailDO::getProcessVersionId, processVersionId)
                        .eq(StepDetailDO::getProcedureId, stepDO.getProcedureId())
                        .eq(StepDetailDO::getStepId, stepDO.getId())
                        .eq(StepDetailDO::getResourcesType, 1));
                for(StepDetailDO stepDetailDO : stepDetailDOList) {
                    StepDetailRespDTO stepDetailRespDTO = BeanUtils.toBean(stepDetailDO, StepDetailRespDTO.class);
                    stepDetailRespDTOList.add(stepDetailRespDTO);
                    deviceIds.add(stepDetailDO.getResourcesTypeId());
                }
                if(deviceIds != null && deviceIds.size() > 0) {
                    CommonResult<List<DeviceTypeDataRespDTO>> list = deviceTypeApi.getDeviceTypeListByIds(deviceIds);
                    if(list != null && list.getData() != null) {
                        for(DeviceTypeDataRespDTO deviceTypeDataRespDTO : list.getData()) {
                            for(StepDetailRespDTO stepDetailRespDTO : stepDetailRespDTOList) {
                                if(deviceTypeDataRespDTO.getId().equals(stepDetailRespDTO.getResourcesTypeId())) {
                                    stepDetailRespDTO.setCode(deviceTypeDataRespDTO.getCode());
                                    stepDetailRespDTO.setName(deviceTypeDataRespDTO.getName());
                                    stepDetailRespDTO.setSpecification(deviceTypeDataRespDTO.getSpecification());
                                }
                            }
                        }
                    }
                }
                List<String> combinationIds = new ArrayList<>();
                List<StepDetailDO> stepDetailDOListCombination = stepDetailMapper.selectList(new LambdaQueryWrapperX<StepDetailDO>()
                        .eq(StepDetailDO::getProcessVersionId, processVersionId)
                        .eq(StepDetailDO::getProcedureId, stepDO.getProcedureId())
                        .eq(StepDetailDO::getStepId, stepDO.getId())
                        .eq(StepDetailDO::getResourcesType, 2));
                for(StepDetailDO stepDetailDO : stepDetailDOListCombination) {
                    StepDetailRespDTO stepDetailRespDTO = BeanUtils.toBean(stepDetailDO, StepDetailRespDTO.class);
                    stepDetailRespDTOList.add(stepDetailRespDTO);
                    combinationIds.add(stepDetailDO.getResourcesTypeId());
                }
                if(combinationIds != null && combinationIds.size() > 0) {
                    List<CombinationDO> list = combinationService.getCombinationListByCombinationIds(combinationIds);
                    for(CombinationDO combinationDO : list) {
                        for(StepDetailRespDTO stepDetailRespDTO : stepDetailRespDTOList) {
                            if(combinationDO.getId().equals(stepDetailRespDTO.getResourcesTypeId())) {
                                stepDetailRespDTO.setCutterGroupCode(combinationDO.getCutterGroupCode());
                                stepDetailRespDTO.setTaperTypeName(combinationDO.getTaperTypeName());
                                stepDetailRespDTO.setHiltMark(combinationDO.getHiltMark());
                            }
                        }
                    }
                }
                stepRespDTO.setResourceList(stepDetailRespDTOList);
                //处理工步关联资源(设备、刀具)------------------end

                //处理工步与数控程序关联表
                List<StepNcRespDTO> stepNcList = new ArrayList<>();
                List<ProcessVersionNcDO> processVersionNcDOList = processVersionNcMapper.selectList(new LambdaQueryWrapper<ProcessVersionNcDO>()
                        .eq(ProcessVersionNcDO::getProcessVersionId, processVersionId)
                        .eq(ProcessVersionNcDO::getProcedureId, stepDO.getProcedureId())
                        .eq(ProcessVersionNcDO::getStepId, stepDO.getId()));
                for(ProcessVersionNcDO processVersionNcDO : processVersionNcDOList) {
                    NcDO ncDO = ncMapper.selectOne(new LambdaQueryWrapper<NcDO>().eq(NcDO::getId,processVersionNcDO.getNcId()));
                    StepNcRespDTO stepNcRespDTO = BeanUtils.toBean(ncDO, StepNcRespDTO.class);
                    stepNcList.add(stepNcRespDTO);
                }
                stepRespDTO.setNcList(stepNcList);

                //处理工步与自定义属性关联表
                // 根据工步id取自定义属性
                List<CustomizedAttributeValDO> attrValueDOList = customizedAttributeValMapper.selectList(new LambdaQueryWrapperX<CustomizedAttributeValDO>()
                        .eq(CustomizedAttributeValDO::getObjectId, stepDO.getId()));
                // 取分类自定义属性
                List<CustomizedAttributeDO> attrDOList = customizedAttributeMapper.selectList(new LambdaQueryWrapperX<CustomizedAttributeDO>()
                        .eq(CustomizedAttributeDO::getCategoryId, stepDO.getCategoryId()));
                if(attrDOList != null && attrDOList.size() > 0) {
                    List<CustomizedAttributeValRespDTO> attrValueRespDTOList = BeanUtils.toBean(attrDOList, CustomizedAttributeValRespDTO.class);
                    if(attrValueDOList != null && attrValueDOList.size() > 0) {
                        for(CustomizedAttributeValRespDTO attrRestDTO : attrValueRespDTOList) {
                            for(CustomizedAttributeValDO attrValueDO : attrValueDOList) {
                                if(attrValueDO.getAttributeId().equals(attrRestDTO.getId())) {
                                    attrRestDTO.setAttrDefaultValue(attrValueDO.getAttributeValue());
                                }
                            }
                        }
                    }
                    stepRespDTO.setAttrValueList(attrValueRespDTOList);
                }

                stepList.add(stepRespDTO);
            }
            respDTO.setStepList(stepList);

            //处理工序关联资源(设备、工装)------------------start
            List<String> deviceIds = new ArrayList<>();
            List<ProcedureDetailRespDTO> procedureDetailRespDTOList = new ArrayList<>();
            List<ProcedureDetailDO> procedureDetailDOList = procedureDetailMapper.selectList(new LambdaQueryWrapperX<ProcedureDetailDO>()
                    .eq(ProcedureDetailDO::getProcessVersionId, processVersionId)
                    .eq(ProcedureDetailDO::getProcedureId, procedureDO.getId())
                    .eq(ProcedureDetailDO::getResourcesType, 1));
            for(ProcedureDetailDO procedureDetailDO : procedureDetailDOList) {
                ProcedureDetailRespDTO procedureDetailRespDTO = BeanUtils.toBean(procedureDetailDO, ProcedureDetailRespDTO.class);
                procedureDetailRespDTOList.add(procedureDetailRespDTO);
                deviceIds.add(procedureDetailDO.getResourcesTypeId());
            }
            if(deviceIds != null && deviceIds.size() > 0) {
                CommonResult<List<DeviceTypeDataRespDTO>> list = deviceTypeApi.getDeviceTypeListByIds(deviceIds);
                if(list != null && list.getData() != null) {
                    for(DeviceTypeDataRespDTO deviceTypeDataRespDTO : list.getData()) {
                        for(ProcedureDetailRespDTO procedureDetailRespDTO : procedureDetailRespDTOList) {
                            if(deviceTypeDataRespDTO.getId().equals(procedureDetailRespDTO.getResourcesTypeId())) {
                                procedureDetailRespDTO.setCode(deviceTypeDataRespDTO.getCode());
                                procedureDetailRespDTO.setName(deviceTypeDataRespDTO.getName());
                                procedureDetailRespDTO.setSpecification(deviceTypeDataRespDTO.getSpecification());
                            }
                        }
                    }
                }
            }
            List<String> materialIds = new ArrayList<>();
            List<ProcedureDetailDO> procedureDetailDOListMaterial = procedureDetailMapper.selectList(new LambdaQueryWrapperX<ProcedureDetailDO>()
                    .eq(ProcedureDetailDO::getProcessVersionId, processVersionId)
                    .eq(ProcedureDetailDO::getProcedureId, procedureDO.getId())
                    .eq(ProcedureDetailDO::getResourcesType, 3));
            for(ProcedureDetailDO procedureDetailDO : procedureDetailDOListMaterial) {
                ProcedureDetailRespDTO procedureDetailRespDTO = BeanUtils.toBean(procedureDetailDO, ProcedureDetailRespDTO.class);
                procedureDetailRespDTOList.add(procedureDetailRespDTO);
                materialIds.add(procedureDetailDO.getResourcesTypeId());
            }
            if(materialIds != null && materialIds.size() > 0) {
                CommonResult<List<MaterialConfigRespDTO>> list = materialMCCApi.getMaterialConfigList(materialIds);
                if(list != null && list.getData() != null) {
                    for(MaterialConfigRespDTO materialConfigRespDTO : list.getData()) {
                        for(ProcedureDetailRespDTO procedureDetailRespDTO : procedureDetailRespDTOList) {
                            if(materialConfigRespDTO.getId().equals(procedureDetailRespDTO.getResourcesTypeId())) {
                                procedureDetailRespDTO.setMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                                procedureDetailRespDTO.setMaterialName(materialConfigRespDTO.getMaterialName());
                                procedureDetailRespDTO.setMaterialSpecification(materialConfigRespDTO.getMaterialSpecification());
                            }
                        }
                    }
                }
            }
            respDTO.setResourceList(procedureDetailRespDTOList);
            //处理工序关联资源(设备、工装)------------------end

            //处理工序与草图关联表
            List<ProcedureFileRespDTO> procedureFileList = new ArrayList<>();
            List<ProcedureFileDO> procedureFileDOList = procedureFileMapper.selectList(new LambdaQueryWrapperX<ProcedureFileDO>()
                    .eq(ProcedureFileDO::getProcessVersionId, processVersionId)
                    .eq(ProcedureFileDO::getProcedureId, procedureDO.getId())
                    .orderByAsc(ProcedureFileDO::getSketchCode));
            for(ProcedureFileDO procedureFileDO : procedureFileDOList) {
                ProcedureFileRespDTO procedureFileRespDTO = BeanUtils.toBean(procedureFileDO, ProcedureFileRespDTO.class);
                procedureFileList.add(procedureFileRespDTO);
            }
            respDTO.setFileList(procedureFileList);

            procedureList.add(respDTO);
        }
        if(procedureList != null && procedureList.size() > 0) {
            detailDTO.setProcedureList(procedureList);
        }
        String resultJson = JsonUtils.toJsonString(detailDTO);
        System.out.println("resultJson:" + resultJson);
        return CommonResult.success(resultJson);
    }

    @Override
    public CommonResult<List<ProcedureRespDTO>> getProcedureListByProcessVersionId(String processVersionId) {
        List<ProcedureDO> procedureDOList = procedureMapper.selectList(new LambdaQueryWrapperX<ProcedureDO>()
                .eq(ProcedureDO::getProcessVersionId, processVersionId)
                .orderByAsc(ProcedureDO::getProcedureNum));
        List<ProcedureRespDTO> procedureList = BeanUtils.toBean(procedureDOList, ProcedureRespDTO.class);
        return success(procedureList);
    }

    //API接口的实现类
    @Override
    public CommonResult<List<ProcessPlanDetailRespDTO>> getProcessPlanList(String partNumber) {
        ProcessPlanDetailRespVO respVO = new ProcessPlanDetailRespVO();
        respVO.setPartNumber(partNumber);
        List<ProcessPlanDetailRespVO> resp = processPlanDetailService.getProcessPlanList(respVO);
        List<ProcessPlanDetailRespDTO> processPlanDetailRespDTOS = BeanUtils.toBean(resp, ProcessPlanDetailRespDTO.class);
        return CommonResult.success(processPlanDetailRespDTOS);
    }

    @Override
    public CommonResult<List<ProcedureRespDTO>> getProcedureListByIds(Collection<String> ids) {

        List<ProcedureDO> doList = procedureMapper.selectProcedureListByIds(ids);

        return CommonResult.success(BeanUtils.toBean(doList,ProcedureRespDTO.class));
    }
}

