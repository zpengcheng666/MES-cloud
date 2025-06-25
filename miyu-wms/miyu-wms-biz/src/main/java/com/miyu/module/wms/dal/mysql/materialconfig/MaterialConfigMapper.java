package com.miyu.module.wms.dal.mysql.materialconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.materialconfig.vo.*;
import org.apache.ibatis.annotations.Select;

/**
 * 物料类型 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface MaterialConfigMapper extends BaseMapperX<MaterialConfigDO> {

    default List<MaterialConfigDO> selectList(MaterialConfigListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MaterialConfigDO>()
                .betweenIfPresent(MaterialConfigDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(MaterialConfigDO::getMaterialCode, reqVO.getMaterialCode())
                .likeIfPresent(MaterialConfigDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(MaterialConfigDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MaterialConfigDO::getMaterialManage, reqVO.getMaterialManage())
                .eqIfPresent(MaterialConfigDO::getMaterialOutRule, reqVO.getMaterialOutRule())
                .eqIfPresent(MaterialConfigDO::getMaterialStorage, reqVO.getMaterialStorage())
                .eqIfPresent(MaterialConfigDO::getMaterialContainer, reqVO.getMaterialContainer())
//                .eqIfPresent(MaterialConfigDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eqIfPresent(MaterialConfigDO::getDefaultWarehouseId, reqVO.getDefaultWarehouseId())
                .orderByDesc(MaterialConfigDO::getId));
    }
    default List<MaterialConfigDO> selectListByMPJLambdaWrapperX(MaterialConfigListReqVO reqVO) {
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(WarehouseAreaDO.class, WarehouseAreaDO::getId, MaterialConfigDO::getDefaultWarehouseId);
//                .leftJoin(MaterialConfigDO.class, "mt1",on->on.eq(MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialParentId))
//                .select( WarehouseAreaDO::getAreaCode).selectAs("mt1.material_number",MaterialConfigDO::getMaterialNumberParent).selectAll(MaterialConfigDO.class);
        return selectList(materialConfigWrapperX
                .betweenIfPresent(MaterialConfigDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(MaterialConfigDO::getMaterialCode, reqVO.getMaterialCode())
                .likeIfPresent(MaterialConfigDO::getMaterialName, reqVO.getMaterialName())
//                .eqIfPresent(MaterialConfigDO::getMaterialProperty, reqVO.getMaterialProperty())
                .eqIfPresent(MaterialConfigDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MaterialConfigDO::getMaterialManage, reqVO.getMaterialManage())
                .eqIfPresent(MaterialConfigDO::getMaterialOutRule, reqVO.getMaterialOutRule())
                .eqIfPresent(MaterialConfigDO::getMaterialStorage, reqVO.getMaterialStorage())
                .eqIfPresent(MaterialConfigDO::getMaterialContainer, reqVO.getMaterialContainer())
//                .eqIfPresent(MaterialConfigDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eqIfPresent(MaterialConfigDO::getDefaultWarehouseId, reqVO.getDefaultWarehouseId())
                .orderByDesc(MaterialConfigDO::getId)
        );
    }



    default PageResult<MaterialConfigDO> getMaterialConfigPage(MaterialConfigPageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialConfigDO> materialConfigWrapperX = new MPJLambdaWrapperX<MaterialConfigDO>();
        materialConfigWrapperX
                .leftJoin(WarehouseDO.class, WarehouseDO::getId, MaterialConfigDO::getDefaultWarehouseId)
//                .leftJoin(MaterialConfigDO.class, "mt1",on->on.eq(MaterialConfigDO:: getId,MaterialConfigDO :: getMaterialParentId))
                .selectAs(WarehouseDO::getWarehouseCode, MaterialConfigDO::getDefaultWarehouseCode)
//                .selectAs("mt1.material_number",MaterialConfigDO::getMaterialNumberParent)
                .selectAll(MaterialConfigDO.class);
        return selectPage(reqVO,materialConfigWrapperX
                .betweenIfPresent(MaterialConfigDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(MaterialConfigDO::getMaterialCode, reqVO.getMaterialCode())
                .likeIfPresent(MaterialConfigDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(MaterialConfigDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MaterialConfigDO::getMaterialManage, reqVO.getMaterialManage())
                .eqIfPresent(MaterialConfigDO::getMaterialOutRule, reqVO.getMaterialOutRule())
                .eqIfPresent(MaterialConfigDO::getMaterialStorage, reqVO.getMaterialStorage())
                .eqIfPresent(MaterialConfigDO::getMaterialContainer, reqVO.getMaterialContainer())
//                .eqIfPresent(MaterialConfigDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eqIfPresent(MaterialConfigDO::getDefaultWarehouseId, reqVO.getDefaultWarehouseId())
                .orderByDesc(MaterialConfigDO::getId));
    }

/*    @Select("SELECT\n" +
            "\twa.area_code,\n" +
            "\tmt.material_number AS materialNumberParent,\n" +
            "\tmt1.* \n" +
            "FROM\n" +
            "\twms_material_config mt\n" +
            "\tRIGHT JOIN wms_material_config mt1 ON mt1.material_parent_id = mt.id\n" +
            "\tLEFT JOIN wms_warehouse_area wa ON mt1.warehouse_area_id = wa.id")
    List<MaterialConfigDO> selectListMaterialConfigDO(MaterialConfigListReqVO reqVO);*/
	/*default MaterialConfigDO selectByMaterialParentIdAndMaterialNumber(String materialParentId, String materialNumber) {
	    return selectOne(MaterialConfigDO::getMaterialParentId, materialParentId, MaterialConfigDO::getMaterialNumber, materialNumber);
	}

    default Long selectCountByMaterialParentId(String materialParentId) {
        return selectCount(MaterialConfigDO::getMaterialParentId, materialParentId);
    }*/


    default MaterialConfigDO selectByMaterialStockId(String materialStockId){
        return selectOne(new MPJLambdaWrapperX<MaterialConfigDO>()
                .rightJoin(MaterialStockDO.class, MaterialStockDO::getMaterialConfigId, MaterialConfigDO::getId)
                .eq(MaterialStockDO::getId, materialStockId)
                .selectAll(MaterialConfigDO.class));
    }

/*    @Select("SELECT\n" +
            "\tmc.* \n" +
            "FROM\n" +
            "\twms_material_config mc\n" +
            "\tLEFT JOIN wms_material_stock ms ON mc.id = ms.material_config_id \n" +
            "WHERE\n" +
            "\tms.bar_code = #{barcode}\n" +
            "\tAND mc.deleted =0")
     MaterialConfigDO getMaterialConfigByBarcode(String barcode);*/


    default MaterialConfigDO getMaterialConfigByBarcode(String barcode){
        return selectOne(new MPJLambdaWrapperX<MaterialConfigDO>()
        .leftJoin(MaterialStockDO.class,MaterialStockDO::getMaterialConfigId,MaterialConfigDO::getId)
                .eq(MaterialStockDO::getBarCode,barcode));

    }

}
