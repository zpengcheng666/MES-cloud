package com.miyu.module.wms.dal.mysql.warehouselocation;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationPageReqVO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 库位 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface WarehouseLocationMapper extends BaseMapperX<WarehouseLocationDO> {

    default PageResult<WarehouseLocationDO> selectPage(WarehouseLocationPageReqVO reqVO) {
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .select(WarehouseAreaDO::getAreaCode)
                .selectAll(WarehouseLocationDO.class);

        wrapperX.betweenIfPresent(WarehouseLocationDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(WarehouseLocationDO::getLocationCode, reqVO.getLocationCode())
                .eqIfPresent(WarehouseLocationDO::getWarehouseAreaId, reqVO.getWarehouseAreaId())
                .eqIfPresent(WarehouseLocationDO::getLocked, reqVO.getLocked())
                .eqIfPresent(WarehouseLocationDO::getValid, reqVO.getValid())
                .orderByDesc(WarehouseLocationDO::getId);


        return selectPage(reqVO, wrapperX);

       /* return this.selectJoinWarehouseAreaPage(reqVO, new LambdaQueryWrapperX<WarehouseLocationDO>()
                .betweenIfPresent(WarehouseLocationDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WarehouseLocationDO::getLocationCode, reqVO.getLocationCode())
                .eqIfPresent(WarehouseLocationDO::getWarehouseAreaId, reqVO.getWarehouseAreaId())
                .orderByDesc(WarehouseLocationDO::getId));*/
    }

    default List<WarehouseLocationDO> getLocationListByMaterialConfigIdAndWarehouseIdAndAreaProperty(String containerMaterialConfigId, String warehouseId, Integer areaProperty) {
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.rightJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getWarehouseId, warehouseId)
                .eq(WarehouseAreaDO::getAreaProperty, areaProperty)
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3)
                .rightJoin(MaterialConfigAreaDO.class, MaterialConfigAreaDO::getWarehouseAreaId, WarehouseAreaDO::getId)
                .eq(MaterialConfigAreaDO::getMaterialConfigId, containerMaterialConfigId)
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO);
        return selectList(wrapperX);
    }

    default List<WarehouseLocationDO> selectAvailableLocationListByAreaIds(Collection<String> areaIds) {
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .in(WarehouseLocationDO::getWarehouseAreaId, areaIds)
                .select(WarehouseAreaDO::getWarehouseId)
                .selectAll(WarehouseLocationDO.class);
        return selectList(wrapperX);
    }


    default List<WarehouseLocationDO> selectAvailableNoLockedLocationListByAreaIds(List<String> areaIds){
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .in(WarehouseLocationDO::getWarehouseAreaId, areaIds);
        return selectList(wrapperX);
    }

    default List<WarehouseLocationDO> selectAvailableTransitLocation() {
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .and(w -> w
                    .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3).or()
                    .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_11))
                .and(w -> w
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO))
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .select(WarehouseAreaDO::getWarehouseId)
                .selectAll(WarehouseLocationDO.class);
        return selectList(wrapperX);
    }

    /**
     * 通过仓库id获得库位
     *
     * @param warehouseId 仓库id
     * @return
     */
    default List<WarehouseLocationDO> getLocationListByWarehouseId(String warehouseId) {
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eqIfExists(WarehouseAreaDO::getWarehouseId, warehouseId)
                .selectAll(WarehouseLocationDO.class);
        return selectList(wrapperX);
    }

    default WarehouseLocationDO getWarehouseLocationAndAreaTypeById(String locationId){
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .select(WarehouseAreaDO::getAreaType)
                .eq(WarehouseLocationDO::getId, locationId)
                .selectAll(WarehouseLocationDO.class);
        return selectOne(wrapperX);
    }

    default List<WarehouseLocationDO> selectWarehouseLocationListByIds(Collection<String> locationIds){
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .in(WarehouseLocationDO::getId, locationIds)
                .select(WarehouseAreaDO::getAreaType)
                .selectAll(WarehouseLocationDO.class);
        return selectList(wrapperX);
    }

    default List<WarehouseLocationDO> getWarehouseLocationByWarehouseType(Integer warehouseType){
        MPJLambdaWrapperX<WarehouseLocationDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, WarehouseAreaDO::getWarehouseId)
                .eq(WarehouseDO::getWarehouseType, warehouseType)
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1)
                .selectAs(WarehouseDO::getId, WarehouseLocationDO::getWarehouseId)
                .select(WarehouseDO::getWarehouseName)
                .selectAll(WarehouseLocationDO.class);
        return selectList(wrapperX);
    }
}