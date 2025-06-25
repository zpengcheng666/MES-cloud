package com.miyu.cloud.dms.api.devicetype;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class DeviceTypeApiImpl implements DeviceTypeApi {
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private LedgerService ledgerService;

    @Override
    public CommonResult<List<DeviceTypeDataRespDTO>> getEnableList(CommonDevice commonDevice) {
        List<DeviceTypeDO> list = deviceTypeService.getAllTypeList(commonDevice);
        return success(BeanUtils.toBean(list, DeviceTypeDataRespDTO.class));
    }

    @Override
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeListByIds(Collection<String> ids) {
        return success(deviceTypeService.getDeviceTypeListByIds(ids));
    }

    @Override
    public CommonResult<List<CommonDevice>> getLineTypeList(CommonDevice commonDevice) {
        return success(deviceTypeService.getLineTypeList(commonDevice));
    }

    @Override
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeListByLineType(Collection<String> ids) {
        return success(deviceTypeService.getDeviceTypeListByLineType(ids));
    }

    @Override
    public CommonResult<List<CommonDevice>> getDeviceListByUserId(String userId) {
        return success(deviceTypeService.getDeviceListByUserId(userId));
    }

    @Override
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeList() {
        return success(BeanUtils.toBean(deviceTypeService.getDeviceTypeList(), DeviceTypeDataRespDTO.class));
    }
}
