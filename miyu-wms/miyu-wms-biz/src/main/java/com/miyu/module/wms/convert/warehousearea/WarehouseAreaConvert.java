package com.miyu.module.wms.convert.warehousearea;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.controller.admin.warehousearea.vo.WarehouseAreaRespVO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseAreaConvert {

    WarehouseAreaConvert INSTANCE = Mappers.getMapper(WarehouseAreaConvert.class);

    default List<WarehouseAreaRespVO> convertList(List<WarehouseAreaDO> list, Map<String, WarehouseDO> warehouseMap) {
        return CollectionUtils.convertList(list, warehouseAreaDO ->
                {
                    WarehouseAreaRespVO warehouseAreaRespVO = BeanUtils.toBean(warehouseAreaDO, WarehouseAreaRespVO.class);
                    if (StringUtils.isNotBlank(warehouseAreaRespVO.getWarehouseId()))
                        MapUtils.findAndThen(warehouseMap, warehouseAreaDO.getWarehouseId(), a -> warehouseAreaRespVO.setWarehouseCode(a.getWarehouseCode()));
                    return warehouseAreaRespVO;
                });

    }


}
