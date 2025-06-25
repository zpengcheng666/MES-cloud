package com.miyu.module.wms.dal.mysql.materialstock;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.*;

/**
 * 物料库存 Mapper
 *
 * @author Qianjy
 */
@Mapper
public interface MaterialStockMapper extends BaseMapperX<MaterialStockDO> {

    /*default PageResult<MaterialStockDO> selectPage(MaterialStockPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialTypeId)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MaterialStockDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStockDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStockDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eqIfPresent(MaterialStockDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(MaterialStockDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(MaterialStockDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(MaterialStockDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(MaterialStockDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(MaterialStockDO::getBindType, reqVO.getBindType())
                .orderByDesc(MaterialStockDO::getId));
    }*/

    default PageResult<MaterialStockDO> selectPage(MaterialStockPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
//                .leftJoin(MaterialStockDO.class,"mstock1",MaterialStockDO:: getId,MaterialStockDO :: getMaterialId)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialTypeName)
                .select(MaterialStorageDO::getStorageCode)
                .select(WarehouseLocationDO::getLocationCode)
//                .selectAs("mstock1.bar_code",MaterialStockDO::getStockBarcode)
                .selectAll(MaterialStockDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStockDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStockDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(MaterialStockDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(MaterialStockDO::getBatchNumber, reqVO.getBatchNumber())
//                .eqIfPresent(MaterialStockDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(MaterialStockDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(MaterialStockDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(MaterialStockDO::getRootLocationId, reqVO.getRootLocationId())
                .eqIfPresent(MaterialStockDO::getBindType, reqVO.getBindType())
                .eqIfPresent(MaterialStockDO::getId, reqVO.getId())
//                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .orderByDesc(MaterialStockDO::getId));
    }


    default List<MaterialStockDO> getMaterialStockContainerList(Integer isContainer) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialConfigDO::getMaterialContainer, isContainer)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(MaterialConfigDO::getMaterialNumber, MaterialStockDO::getMaterialNumber)
                .selectAs(MaterialConfigDO::getMaterialName, MaterialStockDO::getMaterialName)
                .selectAll(MaterialStockDO.class)
                .orderByDesc(MaterialStockDO::getId);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockListIncludingStorageByTrayId(String trayId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStorageDO::getMaterialStockId, trayId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialContainer)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialStorageDO::getStorageCode)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }


    default List<MaterialStockDO> getMaterialStockListByContainerId(String materialStockId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStorageDO::getMaterialStockId, materialStockId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialContainer)
                .select(MaterialConfigDO::getMaterialType)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockListByContainerIds(Collection<String> materialStockIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialStorageDO::getMaterialStockId, materialStockIds)
                .select(MaterialConfigDO::getMaterialContainer)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialManage)
                .select(MaterialStorageDO::getStorageCode)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockListByLocationId(String locationId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getLocationId, locationId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockListByLocationIds(Collection<String> locationIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialStockDO::getLocationId, locationIds)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialLayer)
                .select(MaterialConfigDO::getMaterialRow)
                .select(MaterialConfigDO::getMaterialCol)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockListByRootLocationIds(Collection<String> rootLocationIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialStockDO::getRootLocationId, rootLocationIds)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialLayer)
                .select(MaterialConfigDO::getMaterialRow)
                .select(MaterialConfigDO::getMaterialCol)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectExistsMaterialConfigStockList(){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialLayer)
                .select(MaterialConfigDO::getMaterialRow)
                .select(MaterialConfigDO::getMaterialCol)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }


    default List<MaterialStockDO> selectSimpleMaterialStockListByLocationIds(Collection<String> locationIds){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialStockDO::getLocationId, locationIds);
        return selectList(wrapperX);
    }


    default List<MaterialStockDO> getMaterialStockListAndContainerByLocationId(String locationId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getLocationId, locationId)
                .select(MaterialConfigDO::getMaterialContainer)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default MaterialStockDO selectMaterialStockByStorageId(String storageId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.rightJoin(MaterialStorageDO.class, MaterialStorageDO::getMaterialStockId, MaterialStockDO::getId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStorageDO::getId, storageId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }


    default List<MaterialStockDO> selectMaterialStockByStorageIds(Collection<String> storageIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.rightJoin(MaterialStorageDO.class, MaterialStorageDO::getMaterialStockId, MaterialStockDO::getId)
                .in(MaterialStorageDO::getId, storageIds)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(MaterialStorageDO::getId, MaterialStockDO::getOwnStorageId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default MaterialStockDO selectMaterialStockById(String materialStockId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(MaterialStockDO::getId, materialStockId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAs(WarehouseAreaDO::getAreaType, MaterialStockDO::getAtAreaType)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getContainerConfigIds)
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockByLocationId(String locationId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getLocationId, locationId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialContainer)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }


    default MaterialStockDO selectMaterialStockAndMaterialTypeById(String materialStockId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getId, materialStockId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialType)
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByMaterialConfigIds(Collection<String> materialConfigIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(MaterialStockDO::getMaterialConfigId, materialConfigIds)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    /**
     * 根据托盘id 查询所有在 自动库区 存储库区 有效 未锁定库位上的托盘
     *
     * @param materialStockIds
     * @return
     */
    default List<MaterialStockDO> selectTrayStockListInAutoStockAreaByMaterialStockIds(List<String> materialStockIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO)
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1)
                .in(MaterialStockDO::getId, materialStockIds)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListAndLocationByIds(List<String> materialStockIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(MaterialStockDO::getId, materialStockIds)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(WarehouseLocationDO::getLocationCode)
                .select(MaterialStorageDO::getStorageCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByMaterialConfigIdsAndLockedZero(Collection<String> materialConfigIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .in(MaterialStockDO::getMaterialConfigId, materialConfigIds)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(WarehouseLocationDO::getWarehouseAreaId)
                .select(MaterialConfigDO::getMaterialNumber)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectEmptyTrayStockListByMaterialConfigIds(Collection<String> trayConfigIds, String callAreaId) {
        if (trayConfigIds == null || trayConfigIds.isEmpty()) {
            return Collections.emptyList();
        }
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .in(MaterialConfigDO::getId, trayConfigIds)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)//未锁定
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)//有效
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)//托盘
                .and(w -> w
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO).or()//自动库区
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO)//半自动库区
                )
                .and(w -> w
                        .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1).or()//存储区
                        .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3).or()//接驳区
                        .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_11)//接驳区
                )
                .selectAs(WarehouseAreaDO::getWarehouseId, MaterialStockDO::getAtWarehouseId)
                .selectAs(WarehouseAreaDO::getAreaType, MaterialStockDO::getAtAreaType)
                .selectAs(WarehouseAreaDO::getId, MaterialStockDO::getAtAreaId)
                .select(WarehouseLocationDO::getLocationName)
                .selectAll(MaterialStockDO.class);
        if(StringUtils.isNotBlank(callAreaId)){
            wrapperX.ne(WarehouseAreaDO::getId, callAreaId);
        }
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByMaterialStockIds(Collection<String> materialStockIdSet) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX1 = new MPJLambdaWrapperX<>();
        wrapperX1.leftJoin(InWarehouseDetailDO.class, InWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(InWarehouseDetailDO::getMaterialStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(InWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAll(MaterialStockDO.class);
        MPJLambdaWrapperX<MaterialStockDO> wrapperX2 = new MPJLambdaWrapperX<>();
        wrapperX2.leftJoin(OutWarehouseDetailDO.class, OutWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(OutWarehouseDetailDO::getMaterialStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(OutWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAll(MaterialStockDO.class);
        MPJLambdaWrapperX<MaterialStockDO> wrapperX3 = new MPJLambdaWrapperX<>();
        wrapperX3.leftJoin(MoveWarehouseDetailDO.class, MoveWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(MoveWarehouseDetailDO::getMaterialStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(MoveWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAll(MaterialStockDO.class);

        List<MaterialStockDO> result = new ArrayList<>();
        result.addAll(selectList(wrapperX1));
        result.addAll(selectList(wrapperX2));
        result.addAll(selectList(wrapperX3));
        return result;
    }

    default List<MaterialStockDO> selectChooseStockListByMaterialStockIds(Collection<String> materialStockIdSet) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX1 = new MPJLambdaWrapperX<>();
        wrapperX1.leftJoin(InWarehouseDetailDO.class, InWarehouseDetailDO::getChooseStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(InWarehouseDetailDO::getInState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(InWarehouseDetailDO::getChooseStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(InWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAs(InWarehouseDetailDO::getQuantity, MaterialStockDO::getOrderQuantity)
                .selectAs(InWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getRealStockId)
                .selectAll(MaterialStockDO.class);
        MPJLambdaWrapperX<MaterialStockDO> wrapperX2 = new MPJLambdaWrapperX<>();
        wrapperX2.leftJoin(OutWarehouseDetailDO.class, OutWarehouseDetailDO::getChooseStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(OutWarehouseDetailDO::getChooseStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(OutWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAs(OutWarehouseDetailDO::getQuantity, MaterialStockDO::getOrderQuantity)
                .selectAs(OutWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getRealStockId)
                .selectAll(MaterialStockDO.class);
        MPJLambdaWrapperX<MaterialStockDO> wrapperX3 = new MPJLambdaWrapperX<>();
        wrapperX3.leftJoin(MoveWarehouseDetailDO.class, MoveWarehouseDetailDO::getChooseStockId, MaterialStockDO::getId)
                .and(w -> w
                        .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_4)
                        .ne(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_5)
                )
                .in(MoveWarehouseDetailDO::getChooseStockId, materialStockIdSet)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAs(MoveWarehouseDetailDO::getId, MaterialStockDO::getOrderId)
                .selectAs(MoveWarehouseDetailDO::getQuantity, MaterialStockDO::getOrderQuantity)
                .selectAs(MoveWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getRealStockId)
                .selectAll(MaterialStockDO.class);

        List<MaterialStockDO> result = new ArrayList<>();
        result.addAll(selectList(wrapperX1));
        result.addAll(selectList(wrapperX2));
        result.addAll(selectList(wrapperX3));
        return result;
    }



/*    @Select("SELECT\n" +
            "\tms.*,\n" +
            "\tmc.material_number,\n" +
            "\tmc.material_code,\n" +
            "\twa.warehouse_id AS 'atWarehouseId' \n" +
            "FROM\n" +
            "\twms_material_stock ms\n" +
            "\tLEFT JOIN wms_material_config mc ON ms.material_config_id = mc.id\n" +
            "\tLEFT JOIN wms_warehouse_location wl ON ms.location_id = wl.id\n" +
            "\tLEFT JOIN wms_warehouse_area wa ON wa.id = wl.warehouse_area_id \n" +
            "WHERE\n" +
            "\tmc.material_type = 2 \n" +
            "\tAND wl.id = #{locationId}\n" +
            "\tAND ms.deleted = 0")
    MaterialStockDO selectMaterialStockByLocationId(String locationId);*/

    default List<MaterialStockDO> selectMaterialStockByLocationIds(Collection<String> locationIds) {
        return selectList(new MPJLambdaWrapperX<MaterialStockDO>()
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(WarehouseLocationDO::getId, locationIds)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialTypeName)
                .select(MaterialConfigDO::getMaterialStorage)
                .selectAs(WarehouseAreaDO::getWarehouseId, MaterialStockDO::getAtWarehouseId)
                .selectAll(MaterialStockDO.class));
    }

  /*  @Select("SELECT\n" +
            "\tms.*,\n" +
            "\tmstor.storage_name,\n" +
            "\tmstor.storage_code,\n" +
            "\tmc.material_number,\n" +
            "\tmc.material_code,\n" +
            "\twl.location_name,\n" +
            "\twl.location_code \n" +
            "FROM\n" +
            "\twms_material_stock ms\n" +
            "\tLEFT JOIN wms_material_config mc ON ms.material_config_id = mc.id\n" +
            "\tLEFT JOIN wms_warehouse_location wl ON ms.location_id = wl.id\n" +
            "\tLEFT JOIN wms_warehouse_area wa ON wa.id = wl.warehouse_area_id\n" +
            "\tLEFT JOIN wms_material_storage mstor ON ms.storage_id = mstor.id \n" +
            "WHERE\n" +
            "\tmc.material_type = 2 \n" +
            "\tAND wl.locked = 0 \n" +
            "\tAND wl.valid = 1 \n" +
            "\tAND ms.deleted = 0 \n" +
            "\tAND (\n" +
            "\t\t( wa.area_property = 2 AND wa.area_type = 1 ) \n" +
            "\tOR ( wa.area_property IN ( 2, 3 ) AND wa.area_type = 3 ) \n" +
            "\t)")
    List<MaterialStockDO> getEmptyTrayListByWareHouseArea();
*/

    default List<MaterialStockDO> getEmptyTrayListByWareHouseArea() {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .eq(MaterialConfigDO::getMaterialType, DictConstants.WMS_MATERIAL_TYPE_TP)
                .eq(WarehouseLocationDO::getLocked, DictConstants.INFRA_BOOLEAN_TINYINT_NO)
                .eq(WarehouseLocationDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .and(w -> w
                        .eq(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO)
                        .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1)
                        .or()
                        .in(WarehouseAreaDO::getAreaProperty, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_HALF_AUTO, DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO)
                        .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_3)
                )

                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialStorageDO::getStorageName)
                .select(MaterialStorageDO::getStorageCode)
                .select(WarehouseLocationDO::getLocationName)
                .select(WarehouseLocationDO::getLocationCode)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }
/*    @Select("SELECT\n" +
            "\tms.*,\n" +
            "\tmstor.storage_name,\n" +
            "\tmstor.storage_code,\n" +
            "\tmc.material_number,\n" +
            "\tmc.material_code,\n" +
            "\tmc.material_type \n" +
            "FROM\n" +
            "\twms_material_stock ms\n" +
            "\tLEFT JOIN wms_out_warehouse_detail owd ON ms.id = owd.material_stock_id\n" +
            "\tLEFT JOIN wms_material_config mc ON ms.material_config_id = mc.id\n" +
            "\tLEFT JOIN wms_material_storage mstor ON ms.storage_id = mstor.id \n" +
            "WHERE\n" +
            "\towd.out_state = 1 \n" +
            "\tAND ms.deleted = 0 \n" +
            "\tAND owd.warehouse_id = #{warehouseId}")
    List<MaterialStockDO> getMaterialStockByOutWarehouseDetail(String warehouseId);*/

    default MaterialStockDO selectMaterialStockByStorageCode(String storageCode) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStorageDO.class, MaterialStorageDO::getMaterialStockId, MaterialStockDO::getId)
                .eq(MaterialStorageDO::getStorageCode, storageCode)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialStockDO> selectAllMaterialStockByLocationId(String locationId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .eq(WarehouseLocationDO::getId, locationId)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialType)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }


    default List<MaterialStockDO> getMaterialStockByOutWarehouseDetail(String warehouseId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(OutWarehouseDetailDO.class, OutWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(OutWarehouseDetailDO::getOutState, DictConstants.WMS_ORDER_DETAIL_STATUS_1)
                .eq(OutWarehouseDetailDO::getTargetWarehouseId, warehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class)
                .select(MaterialStorageDO::getStorageCode)
                .select(MaterialStorageDO::getStorageCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialTypeName);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> getMaterialStockByMoveWarehouseDetail(String warehouseId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MoveWarehouseDetailDO.class, MoveWarehouseDetailDO::getMaterialStockId, MaterialStockDO::getId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(MoveWarehouseDetailDO::getMoveState, DictConstants.WMS_ORDER_DETAIL_STATUS_1)
                .eq(MoveWarehouseDetailDO::getTargetWarehouseId, warehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class)
                .select(MaterialStorageDO::getStorageCode)
                .select(MaterialStorageDO::getStorageCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialTypeName);
        return selectList(wrapperX);
    }


    default List<MaterialStockDO> getMaterialStockListByWarehouseId(String warehouseId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, WarehouseAreaDO::getWarehouseId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(WarehouseDO::getId, warehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialConfigDO::getMaterialType, Arrays.asList(DictConstants.WMS_MATERIAL_TYPE_TP, DictConstants.WMS_MATERIAL_TYPE_GZ))
                .select(WarehouseLocationDO::getLocationCode)
                .select(WarehouseLocationDO::getLocationName)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialType)
                .selectAs(WarehouseDO::getId, MaterialStockDO::getAtWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    /*
     * 通过仓库获取物料库存
     * 约束：
     *   物料存在
     *   物料类型为托盘或工装
     *   所属库区为 存储区
     */
    default List<MaterialStockDO> getMaterialStockListByWarehouseIdAndAreaTypeEqContainer(String warehouseId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, WarehouseAreaDO::getWarehouseId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(WarehouseDO::getId, warehouseId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1) //库区类型：存储区
                .in(MaterialConfigDO::getMaterialType, Arrays.asList(DictConstants.WMS_MATERIAL_TYPE_TP, DictConstants.WMS_MATERIAL_TYPE_GZ))
                .select(WarehouseLocationDO::getLocationCode)
                .select(WarehouseLocationDO::getLocationName)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialType)
                .selectAs(WarehouseDO::getId, MaterialStockDO::getAtWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    /*
     * 根据储位id查询该储位是否空闲(未被任何库存占用)
     */
    default Boolean materialStrongIsFree(String id) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX
                .eq(MaterialStockDO::getIsExists, true)
                .eq(MaterialStockDO::getStorageId, id);
        return selectList(wrapperX).isEmpty();
    }

    /**
     * 批量查询，包括已逻辑删除的库存
     *
     * @param ids
     * @return
     */
    /*@Select("<script>" +
            "SELECT s.*,c.default_warehouse_id FROM wms_material_stock s LEFT JOIN  wms_material_config c ON c.id=s.material_config_id WHERE s.id IN " +
            "<foreach collection='ids' item='id' index='index' open='(' separator=',' close=')'>#{id}</foreach>" +
            "</script>")*/
    default List<MaterialStockDO> selectBatchIncludeDeletedByIds(@Param("ids") Collection<String> ids) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(MaterialStockDO::getId, ids)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockAndDefaultWarehouseIdByIds(Collection<String> ids) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .in(MaterialStockDO::getId, ids)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .selectAs(MaterialConfigDO::getStatus, MaterialStockDO::getApprovalStatus)
                .select(WarehouseLocationDO::getLocationName)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByCheckAreaIdAndMaterialConfigIds(String checkAreaId, List<String> materialConfigIds) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        if (!CollectionUtils.isAnyEmpty(materialConfigIds)) {
            wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                    .in(MaterialConfigDO::getId, materialConfigIds);
        }

        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .eq(WarehouseAreaDO::getId, checkAreaId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByBarCodes(Collection<String> barCodes) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .in(MaterialStockDO::getBarCode, barCodes)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialManage)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getContainerConfigIds)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default MaterialStockDO selectMaterialStockByBarCode(String barCode) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .eq(MaterialStockDO::getBarCode, barCode)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialManage)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getContainerConfigIds)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialStockDO> selectAllMaterialStockListByBarCodes(Collection<String> barCodes) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .in(MaterialStockDO::getBarCode, barCodes)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialManage)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getContainerConfigIds)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default MaterialStockDO selectMaterialStockByCheckContainerId(String checkContainerId) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.rightJoin(CheckContainerDO.class, CheckContainerDO::getContainerStockId, MaterialStockDO::getId)
                .eq(CheckContainerDO::getId, checkContainerId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .selectAll(MaterialStockDO.class);
        return selectOne(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockByLocationCode(String locationCode) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getLocationId)
                .eq(WarehouseLocationDO::getLocationCode, locationCode)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialType)
                .select(MaterialConfigDO::getMaterialContainer)
                .select(MaterialConfigDO::getDefaultWarehouseId)
                .selectAs(WarehouseLocationDO::getLocked, MaterialStockDO::getLocationLocked)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }


    /**
     * 查询物料在存储区上的库存
     * @param materialNumber
     * @return
     */
    default PageResult<MaterialStockDO> selectMaterialStockAtStorageAreaByMaterialNumber(MaterialStockPageReqVO reqVO){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getRootLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(WarehouseAreaDO::getAreaType, DictConstants.WMS_WAREHOUSE_AREA_TYPE_1)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialCode)
                .select(MaterialStorageDO::getStorageCode)
                .selectAs(WarehouseLocationDO::getLocationCode, MaterialStockDO::getRootLocationCode)
                .selectAll(MaterialStockDO.class);

        if(StringUtils.isNotBlank(reqVO.getMaterialNumber())){
            wrapperX.eq(MaterialConfigDO::getMaterialNumber, reqVO.getMaterialNumber());
        }
        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStockDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStockDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(MaterialStockDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(MaterialStockDO::getBatchNumber, reqVO.getBatchNumber())
//                .eqIfPresent(MaterialStockDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(MaterialStockDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(MaterialStockDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(MaterialStockDO::getBindType, reqVO.getBindType())
                .eqIfPresent(MaterialStockDO::getId, reqVO.getId())
//                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .orderByDesc(MaterialStockDO::getId));
    }

    default List<MaterialStockDO> selectMaterialStockListLocationByIds(Collection<String> ids){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(WarehouseLocationDO.class, WarehouseLocationDO::getId, MaterialStockDO::getRootLocationId)
                .leftJoin(MaterialStorageDO.class, MaterialStorageDO::getId, MaterialStockDO::getStorageId)
                .eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
                .in(MaterialStockDO::getId, ids)
                .select(MaterialStorageDO::getStorageCode)
                .selectAs(WarehouseLocationDO::getLocationCode, MaterialStockDO::getRootLocationCode)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialStockListByIds(Collection<String> ids){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
//        wrapperX.eq(MaterialStockDO::getIsExists, true) // 仅查询存在的库存
        wrapperX.in(MaterialStockDO::getId, ids);
        return selectList(wrapperX);
    }

    default List<MaterialStockDO> selectMaterialsAndConfigByIds(Collection<String> ids){
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .select(MaterialConfigDO::getMaterialCode)
                .selectAll(MaterialStockDO.class);
        return selectList(wrapperX);
    }
}
