package com.miyu.module.wms.dal.mysql.materialstorage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStoragePageReqVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 物料储位 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface MaterialStorageMapper extends BaseMapperX<MaterialStorageDO> {

    default PageResult<MaterialStorageDO> selectPage(MaterialStoragePageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MaterialStorageDO::getMaterialStockId)
                .select(MaterialStockDO::getBarCode)
                .selectAll(MaterialStorageDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(MaterialStorageDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialStorageDO::getStorageCode, reqVO.getStorageCode())
                .eqIfPresent(MaterialStorageDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(MaterialStorageDO::getValid, reqVO.getValid())
                .orderByDesc(MaterialStorageDO::getId));
    }

    default List<MaterialStorageDO> selectOccupyMaterialStockListByTrayIds(List<String> trayIds) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getStorageId, MaterialStorageDO::getId)
                .in(MaterialStorageDO::getMaterialStockId, trayIds)
                .isNotNull(MaterialStockDO::getStorageId)
                .ne(MaterialStockDO::getStorageId, "")
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStorageDO> selectDetailMaterialStockListByTrayIds(List<String> trayIds){
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getStorageId, MaterialStorageDO::getId)
                .in(MaterialStorageDO::getMaterialStockId, trayIds)
                .selectAs(MaterialStockDO::getBarCode, MaterialStorageDO::getBarCode)
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }


    default List<MaterialStorageDO> selectFreeMaterialStockListByTrayId(String id) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getStorageId, MaterialStorageDO::getId)
                .eq(MaterialStorageDO::getMaterialStockId, id)
                .and(w -> w
                        .isNull(MaterialStockDO::getStorageId)
                        .or()
                        .eq(MaterialStockDO::getStorageId, "")
                )
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStorageDO> selectAllMaterialStorageListByContainerStockId(String containerStockId) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(MaterialStorageDO::getMaterialStockId, containerStockId)
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStorageDO> selectMaterialStorageListByContainerStockId(String containerStockId) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(MaterialStorageDO::getMaterialStockId, containerStockId)
                .eq(MaterialStorageDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStorageDO> selectMaterialStorageListByContainerStockIds(List<String> containerStockIds) {
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(MaterialStorageDO::getMaterialStockId, containerStockIds)
                .eq(MaterialStorageDO::getValid, DictConstants.INFRA_BOOLEAN_TINYINT_YES)
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

    default List<MaterialStorageDO> selectStorageByBarCode(String barCode){
        MPJLambdaWrapperX<MaterialStorageDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MaterialStorageDO::getMaterialStockId)
                .eq(MaterialStockDO::getBarCode, barCode)
                .selectAll(MaterialStorageDO.class);
        return selectList(wrapperX);
    }

}