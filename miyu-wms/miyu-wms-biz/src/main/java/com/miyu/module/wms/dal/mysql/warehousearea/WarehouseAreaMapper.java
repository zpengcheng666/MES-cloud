package com.miyu.module.wms.dal.mysql.warehousearea;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.warehousearea.vo.*;

import java.util.Collection;
import java.util.List;

/**
 * 库区 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface WarehouseAreaMapper extends BaseMapperX<WarehouseAreaDO> {

    default PageResult<WarehouseAreaDO> selectPage(WarehouseAreaPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarehouseAreaDO>()
                .betweenIfPresent(WarehouseAreaDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(WarehouseAreaDO::getAreaName, reqVO.getAreaName())
                .eqIfPresent(WarehouseAreaDO::getAreaCode, reqVO.getAreaCode())
                .eqIfPresent(WarehouseAreaDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(WarehouseAreaDO::getAreaProperty, reqVO.getAreaProperty())
                .eqIfPresent(WarehouseAreaDO::getAreaChannels, reqVO.getAreaChannels())
                .eqIfPresent(WarehouseAreaDO::getAreaGroup, reqVO.getAreaGroup())
                .eqIfPresent(WarehouseAreaDO::getAreaLayer, reqVO.getAreaLayer())
                .eqIfPresent(WarehouseAreaDO::getAreaSite, reqVO.getAreaSite())
                .eqIfPresent(WarehouseAreaDO::getAreaType, reqVO.getAreaType())
                .orderByDesc(WarehouseAreaDO::getId));
    }

    default WarehouseAreaDO getWarehouseAreaByLocationId(String locationId){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId);
        wrapperX.eq(WarehouseLocationDO::getId, locationId);
        wrapperX.selectAs(WarehouseLocationDO::getId, WarehouseAreaDO::getJoinLocationId);
        wrapperX.selectAll(WarehouseAreaDO.class);
        return selectOne(wrapperX);
    }

    default WarehouseAreaDO getWarehouseAreaByLocationCode(String locationCode){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId);
        wrapperX.eq(WarehouseLocationDO::getLocationCode, locationCode);
        wrapperX.selectAs(WarehouseLocationDO::getId, WarehouseAreaDO::getJoinLocationId);
        wrapperX.selectAll(WarehouseAreaDO.class);
        return selectOne(wrapperX);
    }


    default List<WarehouseAreaDO> getWarehouseAreaByLocationIds(Collection<String> locationIds){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId)
                .in(WarehouseLocationDO::getId, locationIds)
                .selectAs(WarehouseLocationDO::getId, WarehouseAreaDO::getJoinLocationId)
                .selectAll(WarehouseAreaDO.class);
        return selectList(wrapperX);
    }

    default WarehouseAreaDO selectByMaterialStockId(String materialStockId){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId)
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getLocationId, WarehouseLocationDO::getId)
                .eq(MaterialStockDO::getId, materialStockId)
                .selectAll(WarehouseAreaDO.class);
        return selectOne(wrapperX);
    }

    default List<WarehouseAreaDO> selectByMaterialStockIds(Collection<String> materialStockIds){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId)
                .leftJoin(MaterialStockDO.class, MaterialStockDO::getLocationId, WarehouseLocationDO::getId)
                .in(MaterialStockDO::getId, materialStockIds)
                .selectAs(MaterialStockDO::getId, WarehouseAreaDO::getMaterialStockId)
                .selectAll(WarehouseAreaDO.class);
        return selectList(wrapperX);
    }


    default List<WarehouseAreaDO> selectWarehouseAreaInWarehouseByAreaIdForCall(String areaId){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inSql(WarehouseAreaDO::getWarehouseId, "select warehouse_id from wms_warehouse_area where id = " + areaId)
                .and(warehouseArea -> warehouseArea.eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3)
                .selectAll(WarehouseAreaDO.class);
        return selectList(wrapperX);
    }

    default List<WarehouseAreaDO> selectSelectableWarehouseAreaList(Collection<Integer> warehouseTypes, Collection<Integer> areaTypes){
        MPJLambdaWrapperX<WarehouseAreaDO> wrapperX = new MPJLambdaWrapperX<>();
        return selectList(wrapperX.leftJoin(WarehouseDO.class, WarehouseDO::getId, WarehouseAreaDO::getWarehouseId)
                        .in(WarehouseDO::getWarehouseType, warehouseTypes)
                        .in(WarehouseAreaDO::getAreaType,areaTypes));
        /*return selectList(wrapperX.leftJoin(WarehouseDO.class, WarehouseDO::getId, WarehouseAreaDO::getWarehouseId)
                .and(w->w
                    .eq(WarehouseDO::getWarehouseType, wmsWarehouseType1)
                    .eq(WarehouseAreaDO::getAreaType, wmsWarehouseAreaType1))
                .or(w->w
                    .eq(WarehouseDO::getWarehouseType, wmsWarehouseType2)
                    .eq(WarehouseAreaDO::getAreaType, wmsWarehouseAreaType1))
                .or(w->w
                    .eq(WarehouseDO::getWarehouseType, wmsWarehouseType3)
                    .eq(WarehouseAreaDO::getAreaType, wmsWarehouseAreaType10))
                .selectAll(WarehouseAreaDO.class));*/

    }

    default List<WarehouseAreaDO> selectWarehouseAreaByWarehouseIdAndAreaTypes(String warehouseId, Collection<String> areaTypes){
        LambdaQueryWrapperX<WarehouseAreaDO> wrapperX = new LambdaQueryWrapperX<>();
            wrapperX.eqIfPresent(WarehouseAreaDO::getWarehouseId, warehouseId);
            wrapperX.inIfPresent(WarehouseAreaDO::getAreaType, areaTypes);
        return selectList(wrapperX);
    }
}