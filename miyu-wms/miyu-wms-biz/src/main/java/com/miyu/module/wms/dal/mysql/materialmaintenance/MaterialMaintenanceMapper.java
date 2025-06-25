package com.miyu.module.wms.dal.mysql.materialmaintenance;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialmaintenance.MaterialMaintenanceDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.materialmaintenance.vo.*;

/**
 * 物料维护记录 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface MaterialMaintenanceMapper extends BaseMapperX<MaterialMaintenanceDO> {

    default PageResult<MaterialMaintenanceDO> selectPage(MaterialMaintenancePageReqVO reqVO) {
        MPJLambdaWrapperX<MaterialMaintenanceDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(MaterialStockDO.class, MaterialStockDO::getId, MaterialMaintenanceDO::getMaterialStockId)
                .leftJoin(MaterialConfigDO.class, MaterialConfigDO::getId, MaterialStockDO::getMaterialConfigId)
                .select(MaterialStockDO::getBarCode)
                .select(MaterialConfigDO::getMaterialNumber)
                .select(MaterialConfigDO::getMaterialName)
                .selectAll(MaterialMaintenanceDO.class);
        return selectPage(reqVO, wrapperX
                .betweenIfPresent(MaterialMaintenanceDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialMaintenanceDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(MaterialMaintenanceDO::getType, reqVO.getType())
                .orderByDesc(MaterialMaintenanceDO::getId));
    }

}