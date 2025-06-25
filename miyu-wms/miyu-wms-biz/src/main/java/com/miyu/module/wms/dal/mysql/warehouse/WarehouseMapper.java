package com.miyu.module.wms.dal.mysql.warehouse;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.warehouse.vo.*;

/**
 * 仓库表 Mapper
 *
 * @author Qianjy
 */
@Mapper
public interface WarehouseMapper extends BaseMapperX<WarehouseDO> {

    default PageResult<WarehouseDO> selectPage(WarehousePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarehouseDO>()
                .betweenIfPresent(WarehouseDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(WarehouseDO::getWarehouseCode, reqVO.getWarehouseCode())
                .eqIfPresent(WarehouseDO::getWarehouseNature, reqVO.getWarehouseNature())
                .eqIfPresent(WarehouseDO::getWarehouseType, reqVO.getWarehouseType())
                .eqIfPresent(WarehouseDO::getWarehouseState, reqVO.getWarehouseState())
                .eqIfPresent(WarehouseDO::getUserId, reqVO.getUserId())
                .orderByDesc(WarehouseDO::getId));
    }

    default WarehouseDO selectByLocationId(String locationId){
        MPJLambdaWrapperX<WarehouseDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.rightJoin(WarehouseAreaDO.class, WarehouseAreaDO::getWarehouseId, WarehouseDO::getId)
                .rightJoin(WarehouseLocationDO.class, WarehouseLocationDO::getWarehouseAreaId, WarehouseAreaDO::getId)
                .selectAll(WarehouseDO.class);

        return selectOne(wrapperX.eq(WarehouseLocationDO::getId, locationId));
    }
}