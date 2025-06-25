package com.miyu.module.wms.api.warehouse;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class WarehouseLocationApiImpl implements WarehouseLocationApi {

    @Resource
    private WarehouseLocationService warehouseLocationService;

    @Override
    public CommonResult<List<WarehouseLocationRespDTO>> getWarehouseLocationByIds(Collection<String> ids) {
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationListByIds(ids);
        return CommonResult.success(BeanUtils.toBean(warehouseLocationList, WarehouseLocationRespDTO.class));
    }
}
