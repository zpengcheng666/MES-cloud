package com.miyu.module.wms.service.materialconfigarea;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.wms.controller.admin.materialconfigarea.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.materialconfigarea.MaterialConfigAreaMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 物料类型关联库区配置 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class MaterialConfigAreaServiceImpl implements MaterialConfigAreaService {

    @Resource
    private MaterialConfigAreaMapper materialConfigAreaMapper;

    @Override
    public String createMaterialConfigArea(MaterialConfigAreaSaveReqVO createReqVO) {
        // 校验存在
        validateMaterialConfigAreaExistsInsertAndUpdate(createReqVO.getWarehouseAreaId(), createReqVO.getMaterialConfigId());
        // 插入
        MaterialConfigAreaDO materialConfigArea = BeanUtils.toBean(createReqVO, MaterialConfigAreaDO.class);
        materialConfigAreaMapper.insert(materialConfigArea);
        // 返回
        return materialConfigArea.getId();
    }

    @Override
    public void updateMaterialConfigArea(MaterialConfigAreaSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialConfigAreaExists(updateReqVO.getId());
        // 校验存在
        validateMaterialConfigAreaExistsInsertAndUpdate(updateReqVO.getWarehouseAreaId(), updateReqVO.getMaterialConfigId());
        // 更新
        MaterialConfigAreaDO updateObj = BeanUtils.toBean(updateReqVO, MaterialConfigAreaDO.class);
        materialConfigAreaMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterialConfigArea(String id) {
        // 校验存在
        validateMaterialConfigAreaExists(id);
        // 删除
        materialConfigAreaMapper.deleteById(id);
    }

    private void validateMaterialConfigAreaExists(String id) {
        if (materialConfigAreaMapper.selectById(id) == null) {
            throw exception(MATERIAL_CONFIG_AREA_NOT_EXISTS);
        }
    }

    private void validateMaterialConfigAreaExistsInsertAndUpdate(String warehouseAreaId, String materialConfigAreaId) {
        if (this.getMaterialConfigAreaByWarehouseAreaIdAndMaterialConfigId(warehouseAreaId, materialConfigAreaId) != null) {
            throw exception(MATERIAL_CONFIG_AREA_EXISTS);
        }
    }

    @Override
    public MaterialConfigAreaDO getMaterialConfigAreaByWarehouseAreaIdAndMaterialConfigId(String warehouseAreaId, String materialConfigId) {
        return materialConfigAreaMapper.selectOne(new LambdaQueryWrapper<MaterialConfigAreaDO>()
                .eq(MaterialConfigAreaDO::getWarehouseAreaId, warehouseAreaId)
                .eq(MaterialConfigAreaDO::getMaterialConfigId, materialConfigId));
    }


    @Override
    public MaterialConfigAreaDO getMaterialConfigArea(String id) {
        return materialConfigAreaMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialConfigAreaDO> getMaterialConfigAreaPage(MaterialConfigAreaPageReqVO pageReqVO) {
        return materialConfigAreaMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByMaterialConfigIdAndWarehouseId(String materialConfigId, String warehouseId) {
        return this.materialConfigAreaMapper.selectByMaterialConfigIdAndWarehouseId(materialConfigId,warehouseId,DictConstants.WMS_WAREHOUSE_AREA_TYPE_3);
    }

    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByWarehouseId(String warehouseId){
        return this.materialConfigAreaMapper.selectByWarehouseId(warehouseId,DictConstants.WMS_WAREHOUSE_AREA_TYPE_3);
    }

    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigCutterTransitAreaByWarehouseId(String warehouseId){
        return this.materialConfigAreaMapper.selectByWarehouseId(warehouseId,DictConstants.WMS_WAREHOUSE_AREA_TYPE_11);
    }


    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigTransitAreaByMaterialConfigIdsAndWarehouseId(Collection<String> materialConfigIds, String warehouseId){
        return this.materialConfigAreaMapper.selectByMaterialConfigIdsAndWarehouseId(materialConfigIds,warehouseId,DictConstants.WMS_WAREHOUSE_AREA_TYPE_3);
    }

    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(String materialConfigId, String warehouseId) {
        return this.materialConfigAreaMapper.selectByMaterialConfigIdAndWarehouseId(materialConfigId,warehouseId,DictConstants.WMS_WAREHOUSE_AREA_TYPE_1);
    }

    @Override
    public MaterialConfigAreaDO getMaterialConfigAreaByMaterialConfigIdAndLocationId(String materialConfigId, String locationId) {
        return materialConfigAreaMapper.selectByMaterialConfigIdAndLocationId(materialConfigId, locationId);
    }


    @Override
    public List<MaterialConfigAreaDO> getMaterialConfigAreaByMaterialConfigIdAndAreaIds(String materialConfigId, Collection<String> areaIds) {
        return materialConfigAreaMapper.selectByMaterialConfigIdAndAreaIds(materialConfigId, areaIds);
    }


    @Override
    public List<MaterialConfigAreaDO> getTrayMaterialConfigAreaByAreaId(String areaId) {
        return materialConfigAreaMapper.selectTrayMaterialConfigAreaByAreaId(areaId);
    }

    @Override
    public List<MaterialConfigAreaDO> getTrayMaterialConfigAreaByMaterialConfigId(String materialConfigId) {
        return materialConfigAreaMapper.selectTrayMaterialConfigAreaByMaterialConfigId(materialConfigId);
    }

    @Override
    public List<String> getMaterialConfigAreaByStartAreaIdAndEndAreaId(String startAreaId, String endAreaId, Collection<String> appointTrayConfigIds) {
        if(StringUtils.isAnyBlank(startAreaId)){
            return Collections.emptyList();
        }
        // 获取配置了抵达起始库区的托盘类型
        List<MaterialConfigAreaDO> startAreaList = this.getTrayMaterialConfigAreaByAreaId(startAreaId);
        if(CollectionUtils.isAnyEmpty(startAreaList)){
            return Collections.emptyList();
        }

        List<MaterialConfigAreaDO> targetAreaList;
        if(StringUtils.isNotBlank(endAreaId)){
            // 获取配置了抵达目标库区的托盘类型
            targetAreaList = this.getTrayMaterialConfigAreaByAreaId(endAreaId);
        } else {
            targetAreaList = null;
        }

        List<String> appointTrayConfigIdsFilter = null;
        if(CollectionUtils.isAnyEmpty(appointTrayConfigIds) && targetAreaList != null){
            // 没有指定托盘类型
            appointTrayConfigIdsFilter = startAreaList.stream().map(MaterialConfigAreaDO::getMaterialConfigId).filter(configId -> targetAreaList.stream().anyMatch(config -> config.getMaterialConfigId().equals(configId))).distinct().collect(Collectors.toList());
//            appointTrayConfigIdsFilter = startAreaList.stream().map(MaterialConfigAreaDO::getMaterialConfigId).filter(configId -> targetAreaList.stream().anyMatch(config -> config.getMaterialConfigId().equals(configId))).distinct().collect(Collectors.toList());

        }
        if(CollectionUtils.isAnyEmpty(appointTrayConfigIds) && targetAreaList == null){
            // 没有指定托盘类型 并且没有指定目标库区
            appointTrayConfigIdsFilter = startAreaList.stream().map(MaterialConfigAreaDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        }
        if(!CollectionUtils.isAnyEmpty(appointTrayConfigIds) && targetAreaList != null){
            // 过滤了 指定的托盘类型
            appointTrayConfigIdsFilter = appointTrayConfigIds.stream().filter(configId -> startAreaList.stream().anyMatch(config -> config.getMaterialConfigId().equals(configId))
                    && targetAreaList.stream().anyMatch(config -> config.getMaterialConfigId().equals(configId))).collect(Collectors.toList());
        }

        if(!CollectionUtils.isAnyEmpty(appointTrayConfigIds) && targetAreaList == null){
            // 过滤了 指定的托盘类型 并且没有指定目标库区
            appointTrayConfigIdsFilter = appointTrayConfigIds.stream().filter(configId -> startAreaList.stream().anyMatch(config -> config.getMaterialConfigId().equals(configId))).collect(Collectors.toList());
        }
        return appointTrayConfigIdsFilter;
    }

}