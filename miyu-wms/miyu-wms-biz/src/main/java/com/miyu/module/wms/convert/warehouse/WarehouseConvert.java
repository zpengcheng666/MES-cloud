package com.miyu.module.wms.convert.warehouse;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.wms.controller.admin.warehouse.vo.WarehouseRespVO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface WarehouseConvert {

    WarehouseConvert INSTANCE = Mappers.getMapper(WarehouseConvert.class);

    default List<WarehouseRespVO> convertList(List<WarehouseDO> list, Map<Long, AdminUserRespDTO> userMap) {
        return CollectionUtils.convertList(list, warehouseDO ->
                {
                    WarehouseRespVO warehouseRespVO = BeanUtils.toBean(warehouseDO, WarehouseRespVO.class);
                    if (StringUtils.isNotBlank(warehouseDO.getUserId()))
                        MapUtils.findAndThen(userMap, Long.valueOf(warehouseDO.getUserId()), a -> warehouseRespVO.setNickName(a.getNickname()));
                    if (StringUtils.isNotBlank(warehouseDO.getCreator()))
                        MapUtils.findAndThen(userMap, Long.valueOf(warehouseDO.getCreator()), a -> warehouseRespVO.setCreator(a.getNickname()));
                    if (StringUtils.isNotBlank(warehouseDO.getUpdater()))
                        MapUtils.findAndThen(userMap, Long.valueOf(warehouseDO.getUpdater()), a -> warehouseRespVO.setUpdater(a.getNickname()));
                    return warehouseRespVO;
                });
                /*convert(warehouseDO,
                userMap.get(StringUtils.isBlank(warehouseDO.getUserId())?null:Long.valueOf(warehouseDO.getUserId())),
                userMap.get(StringUtils.isBlank(warehouseDO.getCreator())?null:Long.valueOf(warehouseDO.getCreator())),
                userMap.get(StringUtils.isBlank(warehouseDO.getUpdater())?null:Long.valueOf(warehouseDO.getUpdater()))));*/
    }

    default WarehouseRespVO convert(WarehouseDO warehouseDO, AdminUserRespDTO user, AdminUserRespDTO creator, AdminUserRespDTO updater) {
        WarehouseRespVO warehouseRespVO = BeanUtils.toBean(warehouseDO, WarehouseRespVO.class);
        if (user != null) {
            warehouseRespVO.setNickName(user.getNickname());
        }
        if (creator != null) {
            warehouseRespVO.setCreator(creator.getNickname());
        }
        if (updater != null) {
            warehouseRespVO.setUpdater(updater.getNickname());
        }
        return warehouseRespVO;
    }

}
