package com.miyu.module.wms.dal.mysql.stockactive;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物料操作 Mapper
 *
 * @author Qianjy
 */
@Mapper
public interface StockActiveMapper extends BaseMapperX<MaterialStockDO> {


    default PageResult<MaterialStockDO> getOnshelfPage(MaterialStockPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        // 上架 只查询库区 为 暂存位 接驳位 和收货位 的库存
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class,WarehouseLocationDO:: getId,MaterialStockDO :: getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .in(WarehouseAreaDO::getAreaType, reqVO.getWarehouseAreaTypes())
                .select(MaterialConfigDO::getMaterialNumber)
                .select(WarehouseLocationDO::getLocationCode)
                .selectAll(MaterialStockDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStockDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStockDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(MaterialStockDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(MaterialStockDO::getBatchNumber, reqVO.getBatchNumber())
//                .eqIfPresent(MaterialStockDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(MaterialStockDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(MaterialStockDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(MaterialStockDO::getBindType, reqVO.getBindType())
                .orderByDesc(MaterialStockDO::getId));
    }


    default PageResult<MaterialStockDO> getOffshelfPage(MaterialStockPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialStockDO> wrapperX = new MPJLambdaWrapperX<>();
        // 上架 只查询库区 为 暂存位 接驳位 和收货位 的库存
        wrapperX.leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .leftJoin(WarehouseLocationDO.class,WarehouseLocationDO:: getId,MaterialStockDO :: getLocationId)
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, WarehouseLocationDO::getWarehouseAreaId)
                .in(WarehouseAreaDO::getAreaType, reqVO.getWarehouseAreaTypes())
                .select(MaterialConfigDO::getMaterialNumber)
                .select(WarehouseLocationDO::getLocationCode)
                .selectAll(MaterialStockDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStockDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStockDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(MaterialStockDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(MaterialStockDO::getBatchNumber, reqVO.getBatchNumber())
//                .eqIfPresent(MaterialStockDO::getMaterialId, reqVO.getMaterialId())
                .eqIfPresent(MaterialStockDO::getStorageId, reqVO.getStorageId())
                .eqIfPresent(MaterialStockDO::getLocationId, reqVO.getLocationId())
                .eqIfPresent(MaterialStockDO::getBindType, reqVO.getBindType())
                .orderByDesc(MaterialStockDO::getId));
    }


}
