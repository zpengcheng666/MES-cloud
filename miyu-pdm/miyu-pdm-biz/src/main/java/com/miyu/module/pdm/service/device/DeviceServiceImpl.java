package com.miyu.module.pdm.service.device;

import cn.hutool.core.collection.CollUtil;
import com.miyu.module.pdm.controller.admin.device.vo.DeviceListReqVO;
import com.miyu.module.pdm.dal.dataobject.device.DeviceDO;
import com.miyu.module.pdm.dal.mysql.device.DeviceMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * PDM 设备-临时 Service 实现类
 *
 * @author Liuy
 */
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Override
    public List<DeviceDO> getDeviceList(DeviceListReqVO listReqVO) {
        return deviceMapper.selectList(listReqVO);
    }

    @Override
    public List<DeviceDO> getDeviceListByDeviceIds(Collection<String> deviceIds) {
        if (CollUtil.isEmpty(deviceIds)) {
            return Collections.emptyList();
        }
        return deviceMapper.selectListByDeviceIds(deviceIds);
    }

}
