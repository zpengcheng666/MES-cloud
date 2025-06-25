package com.miyu.module.pdm.api.projectAssessment;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.projectAssessment.dto.*;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomRespVO;
import com.miyu.module.pdm.dal.dataobject.combination.CombinationDO;
import com.miyu.module.pdm.dal.dataobject.device.DeviceDO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.*;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;
import com.miyu.module.pdm.dal.mysql.combination.CombinationMapper;
import com.miyu.module.pdm.dal.mysql.device.DeviceMapper;
import com.miyu.module.pdm.dal.mysql.feasibilityDetail.*;
import com.miyu.module.pdm.dal.mysql.material.MaterialMapper;
import com.miyu.module.pdm.service.feasibilityDetail.FeasibilityDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@Validated
public class TechnologyAssessmentApiImpl implements TechnologyAssessmentApi {


   @Resource
   private FeasibilityDetailService feasibilityDetailService;

   @Resource
   private FeasibilityDetailMapper feasibilityDetailMapper;

    @Resource
    private DemandDeviceMapper demandDeviceMapper;

    @Resource
    private DemandMaterialMapper demandMaterialMapper;

    @Resource
    private DemandCutterMapper demandCutterMapper;

    @Resource
    private DemandHiltMapper demandHiltMapper;

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private CombinationMapper combinationMapper;

    @Resource
    private MaterialMapper materialMapper;

    @Resource
    private DeviceTypeApi deviceTypeApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private DemandMeasureMapper demandMeasureMapper;


    @Override
    public CommonResult<TechnologyAssessmentRespDTO> getTechnologyAssement(String projectCode) {
        TechnologyAssessmentRespDTO technologyAssessmentRespDTO = new TechnologyAssessmentRespDTO();
        //1.获取零件
//        List<ProjPartBomRespVO> projPartBomRespVOS = feasibilityDetailMapper.selectPartList(projectCode, null, null, null);
        List<ProjPartBomRespVO> projPartBomRespVOS = feasibilityDetailMapper.selectPartListNew(projectCode, null, null, null, 0);
        List<ProjPartBomRespDTO> projPartBomRespDTOS = BeanUtils.toBean(projPartBomRespVOS, ProjPartBomRespDTO.class);
        technologyAssessmentRespDTO.setProjPartBomList(projPartBomRespDTOS);
        //key为零件版本号,value为图号
        Map<String,String> map = new HashMap<>();
        //有一个前提，就是每个项目下的零件的版本号都唯一
        for (ProjPartBomRespDTO projPartBomRespDTO : projPartBomRespDTOS) {
            map.put(projPartBomRespDTO.getPartVersionId(),projPartBomRespDTO.getPartNumber());
        }
        //2.获取设备刀具工装
        List<FeasibilityDetailRespDTO> feasibilityDetailList = getFeasibilityDetailList(projectCode,map);
        technologyAssessmentRespDTO.setFeasibilityDetailList(feasibilityDetailList);
        //key为ResourcesTypeId
        Map<String, FeasibilityDetailRespDTO> feasibilityMap = new HashMap<>();
        for (FeasibilityDetailRespDTO feasibilityDetailRespDTO : feasibilityDetailList) {
            feasibilityMap.put(feasibilityDetailRespDTO.getId(),feasibilityDetailRespDTO);
        }
        //3.获取采购刀具
        List<DemandCutterRespDTO> demandCutterList = getDemandCutterList(projectCode, map);
        demandCutterList.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
        technologyAssessmentRespDTO.setDemandCutterList(demandCutterList);

        //4.获取采购刀柄
        List<DemandHiltRespDTO> demandHiltList = getDemandHiltList(projectCode, map);
        demandHiltList.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
        technologyAssessmentRespDTO.setDemandHiltList(demandHiltList);
        //5.获取采购设备
        List<DemandDeviceRespDTO> demandDeviceList = getDemandDeviceList(projectCode, map);
        demandDeviceList.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
        technologyAssessmentRespDTO.setDemandDeviceList(demandDeviceList);
        //6.获取采购工装
        List<DemandMaterialRespDTO> demandMaterialList = getDemandMaterialList(projectCode, map);
        demandMaterialList.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
        technologyAssessmentRespDTO.setDemandMaterialList(demandMaterialList);

        //7.分出设备
        List<String> deviceIds = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 1).map(item -> item.getResourcesTypeId()).collect(Collectors.toList());
        List<FeasibilityDetailRespDTO> deviceFeasibility = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 1).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(deviceIds)) {
            List<DeviceTypeDataRespDTO> deviceDOS = deviceTypeApi.getDeviceTypeListByIds(deviceIds).getCheckedData();
//            List<DeviceDO> deviceDOS = deviceMapper.selectListByDeviceIds(deviceIds);
            Map<String, DeviceTypeDataRespDTO> stringDeviceDOMap = CollectionUtils.convertMap(deviceDOS, DeviceTypeDataRespDTO::getId);

            List<DeviceRespDTO> deviceRespDTOS = BeanUtils.toBean(deviceFeasibility, DeviceRespDTO.class, vo -> {
                BeanUtil.copyProperties(stringDeviceDOMap.get(vo.getResourcesTypeId()), vo);
            });
            deviceRespDTOS.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
            technologyAssessmentRespDTO.setDeviceList(deviceRespDTOS);
        }
        //8.分出刀具
        List<String> cutterIds = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 2).map(item -> item.getResourcesTypeId()).collect(Collectors.toList());
        List<FeasibilityDetailRespDTO> combinationFeasibility = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 2).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(cutterIds)) {
            List<CombinationDO> combinationDOS = combinationMapper.selectListByCombinationIds(cutterIds);
            Map<String, CombinationDO> stringCombinationDOMap = CollectionUtils.convertMap(combinationDOS, CombinationDO::getId);
            List<CombinationRespDTO> combinationRespDTOS = BeanUtils.toBean(combinationFeasibility, CombinationRespDTO.class, vo -> {
                BeanUtil.copyProperties(stringCombinationDOMap.get(vo.getResourcesTypeId()), vo);
            });
            combinationRespDTOS.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
            technologyAssessmentRespDTO.setCombinationList(combinationRespDTOS);
        }
        //9.分出工装
        List<String> materialIds = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 3).map(item -> item.getResourcesTypeId()).collect(Collectors.toList());
        List<FeasibilityDetailRespDTO> materialFeasibility = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 3).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(materialIds)) {
            //工装从mcc获取
            List<MaterialConfigRespDTO> materialDOS = materialMCCApi.getMaterialConfigList(materialIds).getCheckedData();
//            List<MaterialDO> materialDOS = materialMapper.selectListByMaterialIds(materialIds);
            Map<String, MaterialConfigRespDTO> stringMaterialDOMap = CollectionUtils.convertMap(materialDOS, MaterialConfigRespDTO::getId);
            List<MaterialRespDTO> materialRespDTOS = BeanUtils.toBean(materialFeasibility, MaterialRespDTO.class, vo -> {
                BeanUtil.copyProperties(stringMaterialDOMap.get(vo.getResourcesTypeId()), vo);
            });
            materialRespDTOS.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
            technologyAssessmentRespDTO.setMaterialList(materialRespDTOS);
        }
        //10.分出量具
        List<String> measureIds = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 4).map(item -> item.getResourcesTypeId()).collect(Collectors.toList());
        List<FeasibilityDetailRespDTO> measureFeasibility = feasibilityDetailList.stream().filter(item -> item.getResourcesType() == 4).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(materialIds)) {
            //量具也从mcc获取
            List<MaterialConfigRespDTO> measureDOS = materialMCCApi.getMaterialConfigList(measureIds).getCheckedData();
            Map<String, MaterialConfigRespDTO> stringMaterialConfigRespDTOMap = CollectionUtils.convertMap(measureDOS, MaterialConfigRespDTO::getId);
            List<MeasureRespDTO> measureRespDTOS = BeanUtils.toBean(measureFeasibility, MeasureRespDTO.class, vo -> {
                BeanUtil.copyProperties(stringMaterialConfigRespDTOMap.get(vo.getResourcesTypeId()), vo);
            });
            measureRespDTOS.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
            technologyAssessmentRespDTO.setMeasureList(measureRespDTOS);
        }


        //11.获取量具(采购建议)
        List<DemandMeasureRespDTO> demandMeasureList = getDemandMeasureList(projectCode, map);
        demandMeasureList.sort((a,b)->{return a.getPartNumber().compareTo(b.getPartNumber());});
        technologyAssessmentRespDTO.setDemandMeasureList(demandMeasureList);


        return success(technologyAssessmentRespDTO);
    }

    //获取设备,刀具,工装,不分类
    public List<FeasibilityDetailRespDTO> getFeasibilityDetailList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<FeasibilityDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FeasibilityDetailDO::getProjectCode,projectCode);
        List<FeasibilityDetailDO> feasibilityDetailDOS = feasibilityDetailMapper.selectList(wrapper);
        List<FeasibilityDetailRespDTO> feasibilityDetailRespDTOS = BeanUtils.toBean(feasibilityDetailDOS, FeasibilityDetailRespDTO.class, vo->{
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setPurchaseType(0);
        });
        return feasibilityDetailRespDTOS;
    }
    //获取采购刀具
    public List<DemandCutterRespDTO> getDemandCutterList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<DemandCutterDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandCutterDO::getProjectCode,projectCode);
        List<DemandCutterDO> demandCutterDOS = demandCutterMapper.selectList(wrapper);
        List<DemandCutterRespDTO> demandCutterRespDTOS = BeanUtils.toBean(demandCutterDOS, DemandCutterRespDTO.class, vo -> {
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setResourcesType(4);
            vo.setResourcesTypeId(vo.getId());
            vo.setPurchaseType(1);
        });
        return demandCutterRespDTOS;
    }
    //获取采购刀柄(采购刀具和刀柄合起来应该才是一个完整刀具)
    public List<DemandHiltRespDTO> getDemandHiltList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<DemandHiltDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandHiltDO::getProjectCode,projectCode);
        List<DemandHiltDO> demandHiltDOS = demandHiltMapper.selectList(wrapper);
        List<DemandHiltRespDTO> demandHiltRespDTOS = BeanUtils.toBean(demandHiltDOS, DemandHiltRespDTO.class, vo -> {
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setResourcesType(5);
            vo.setResourcesTypeId(vo.getId());
            vo.setPurchaseType(1);
        });
        return demandHiltRespDTOS;
    }
    //获取采购设备
    public List<DemandDeviceRespDTO>  getDemandDeviceList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<DemandDeviceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandDeviceDO::getProjectCode,projectCode);
        List<DemandDeviceDO> demandDeviceDOS = demandDeviceMapper.selectList(wrapper);
        List<DemandDeviceRespDTO> demandDeviceRespDTOS = BeanUtils.toBean(demandDeviceDOS, DemandDeviceRespDTO.class, vo -> {
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setResourcesType(1);
            vo.setResourcesTypeId(vo.getId());
            vo.setPurchaseType(1);
        });
        return demandDeviceRespDTOS;
    }

    //获取采购工装
    public List<DemandMaterialRespDTO>  getDemandMaterialList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<DemandMaterialDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandMaterialDO::getProjectCode,projectCode);
        List<DemandMaterialDO> demandMaterialDOS = demandMaterialMapper.selectList(wrapper);
        List<DemandMaterialRespDTO> demandMaterialRespDTOS = BeanUtils.toBean(demandMaterialDOS, DemandMaterialRespDTO.class, vo -> {
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setResourcesType(3);
            vo.setResourcesTypeId(vo.getId());
            vo.setPurchaseType(1);
        });
        return demandMaterialRespDTOS;
    }

    //获取采购量具
    public List<DemandMeasureRespDTO> getDemandMeasureList(String projectCode, Map<String,String> map){
        LambdaQueryWrapper<DemandMeasureDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DemandMeasureDO::getProjectCode,projectCode);
        List<DemandMeasureDO> demandMeasureDOS = demandMeasureMapper.selectList(wrapper);
        List<DemandMeasureRespDTO> demandMaterialRespDTOS = BeanUtils.toBean(demandMeasureDOS, DemandMeasureRespDTO.class, vo -> {
            vo.setPartNumber(map.get(vo.getPartVersionId()));
            vo.setResourcesType(4);
            vo.setResourcesTypeId(vo.getId());
            vo.setPurchaseType(1);
        });
        return demandMaterialRespDTOS;
    }
}
