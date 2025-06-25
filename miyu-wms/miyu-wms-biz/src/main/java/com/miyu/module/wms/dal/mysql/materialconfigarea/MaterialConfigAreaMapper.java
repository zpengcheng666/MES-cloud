package com.miyu.module.wms.dal.mysql.materialconfigarea;

import java.util.*;

import cn.hutool.core.lang.Dict;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.materialconfigarea.vo.*;
import org.springframework.util.CollectionUtils;

/**
 * 物料类型关联库区配置 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface MaterialConfigAreaMapper extends BaseMapperX<MaterialConfigAreaDO> {

    default PageResult<MaterialConfigAreaDO> selectPage(MaterialConfigAreaPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId,MaterialConfigAreaDO::getWarehouseAreaId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId,MaterialConfigAreaDO::getMaterialConfigId)
                .select(WarehouseAreaDO::getAreaCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MaterialConfigAreaDO.class);

        return selectPage(reqVO, wrapperX
                .betweenIfPresent(MaterialConfigAreaDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialConfigAreaDO::getWarehouseAreaId, reqVO.getWarehouseAreaId())
                .eqIfPresent(MaterialConfigAreaDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .orderByDesc(MaterialConfigAreaDO::getId));
    }

    default MaterialConfigAreaDO selectByMaterialConfigIdAndLocationId(String materialConfigId, String locationId){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId,MaterialConfigAreaDO::getWarehouseAreaId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId,WarehouseAreaDO::getId)
                .eq(WarehouseLocationDO::getId,locationId)
                .eq(MaterialConfigAreaDO::getMaterialConfigId,materialConfigId)
                .and(w -> w.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3)
                .selectAll(MaterialConfigAreaDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialConfigAreaDO> selectByMaterialConfigIdsAndLocationId(List<String> materialConfigIds, String locationId){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId,MaterialConfigAreaDO::getWarehouseAreaId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId,WarehouseAreaDO::getId)
                .eq(WarehouseLocationDO::getId,locationId)
                .in(MaterialConfigAreaDO::getMaterialConfigId,materialConfigIds)
                .and(w -> w.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3)
                .selectAll(MaterialConfigAreaDO.class);
        return selectList(wrapperX);
    }


    default MaterialConfigAreaDO selectByMaterialConfigIdAndAreaId(String materialConfigId, String areaId){
        return selectOne(MaterialConfigAreaDO::getMaterialConfigId, materialConfigId, MaterialConfigAreaDO::getWarehouseAreaId, areaId);
    }

    default List<MaterialConfigAreaDO> selectByMaterialConfigIdAndAreaIds(String materialConfigId, Collection<String> areaIds){
        return selectList(new LambdaQueryWrapperX<MaterialConfigAreaDO>()
                .eq(MaterialConfigAreaDO::getMaterialConfigId, materialConfigId)
                .in(MaterialConfigAreaDO::getWarehouseAreaId, areaIds));
    }


    default List<MaterialConfigAreaDO> selectTrayMaterialConfigAreaListByMaterialConfigIdsAndAreaIds(List<String> materialConfigIds,List<String> areaIds) {
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialConfigAreaDO::getMaterialConfigId)
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)
                .eq(MaterialConfigDO::getMaterialContainer, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .in(MaterialConfigAreaDO::getWarehouseAreaId, areaIds)
                .selectAll(MaterialConfigAreaDO.class);
        if(!CollectionUtils.isEmpty(materialConfigIds)){
            wrapperX.in(MaterialConfigAreaDO::getMaterialConfigId, materialConfigIds);
        }
        return selectList(wrapperX);
    }

    default List<MaterialConfigAreaDO> selectByMaterialConfigIdAndWarehouseId(String materialConfigId, String warehouseId, Integer areaType){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, MaterialConfigAreaDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getWarehouseId, warehouseId)
                .eq(MaterialConfigAreaDO::getMaterialConfigId, materialConfigId)
                .and(w -> w.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, areaType);
        return selectList(wrapperX);
    }

    default List<MaterialConfigAreaDO> selectByWarehouseId(String warehouseId, Integer areaType){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, MaterialConfigAreaDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getWarehouseId, warehouseId)
                .and(w -> w.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, areaType);
        return selectList(wrapperX);
    }



    default List<MaterialConfigAreaDO> selectByMaterialConfigIdsAndWarehouseId(Collection<String> materialConfigIds, String warehouseId, Integer areaType){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, MaterialConfigAreaDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getWarehouseId, warehouseId)
                .in(MaterialConfigAreaDO::getMaterialConfigId, materialConfigIds)
                .and(w -> w.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, areaType);
        return selectList(wrapperX);
    }



    default List<MaterialConfigAreaDO> selectTrayMaterialConfigAreaByAreaId(String areaId){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialConfigAreaDO::getMaterialConfigId)
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)
                .eq(MaterialConfigAreaDO::getWarehouseAreaId, areaId)
                .selectAll(MaterialConfigAreaDO.class);
        return selectList(wrapperX);
    }


    default List<MaterialConfigAreaDO> selectTrayMaterialConfigAreaByMaterialConfigId(String materialConfigId){
        MPJLambdaWrapperX<MaterialConfigAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialConfigAreaDO::getMaterialConfigId)
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)
                .eq(MaterialConfigAreaDO::getMaterialConfigId, materialConfigId)
                .selectAll(MaterialConfigAreaDO.class);
        return selectList(wrapperX);
    }

}