package com.miyu.module.pdm.service.device;

import com.miyu.module.pdm.controller.admin.device.vo.DeviceListReqVO;
import com.miyu.module.pdm.dal.dataobject.device.DeviceDO;

import java.util.Collection;
import java.util.List;

/**
 * PDM 设备-临时 Service 接口
 *
 * @author Liuy
 */
public interface DeviceService {

    /**
     * 获得设备列表
     *
     * @param listReqVO 查询条件
     * @return 设备列表
     */
    List<DeviceDO> getDeviceList(DeviceListReqVO listReqVO);

    /**
     * 获得指定设备id的设备列表
     *
     * @param deviceIds 设备id数组
     * @return 设备列表
     */
    List<DeviceDO> getDeviceListByDeviceIds(Collection<String> deviceIds);

}
