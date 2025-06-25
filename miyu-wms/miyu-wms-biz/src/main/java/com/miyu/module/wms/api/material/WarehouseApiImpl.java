package com.miyu.module.wms.api.material;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.mateiral.WarehouseApi;
import com.miyu.module.wms.api.mateiral.dto.WarehouseAreaRespDTO;
import com.miyu.module.wms.api.mateiral.dto.WarehouseRespDTO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class WarehouseApiImpl implements WarehouseApi {

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private WarehouseAreaService warehouseAreaService;

    @Override
    public CommonResult<List<WarehouseRespDTO>> getWarehouseByCode(String code) {
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseByCode(code);
        return CommonResult.success(BeanUtils.toBean(warehouseList, WarehouseRespDTO.class));
    }


    @Override
    public CommonResult<List<WarehouseAreaRespDTO>> getWarehouseAreaByLocationIds(Collection<String> locationIds) {
        List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaByLocationIds(locationIds);
        return CommonResult.success(BeanUtils.toBean(warehouseAreaList, WarehouseAreaRespDTO.class));
    }

}
