package com.miyu.cloud.macs.service.device;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.device.vo.*;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.macs.restServer.entity.MacsRestDevice;

/**
 * 设备 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceService {

    /**
     * 创建设备
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDevice(@Valid DeviceSaveReqVO createReqVO);

    /**
     * 更新设备
     *
     * @param updateReqVO 更新信息
     */
    void updateDevice(@Valid DeviceSaveReqVO updateReqVO);

    /**
     * 删除设备
     *
     * @param id 编号
     */
    void deleteDevice(String id);

    /**
     * 获得设备
     *
     * @param id 编号
     * @return 设备
     */
    DeviceDO getDevice(String id);

    /**
     * 获得设备分页
     *
     * @param pageReqVO 分页查询
     * @return 设备分页
     */
    PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO);

    List<DeviceDO> getDeviceList(DevicePageReqVO listReqVO);

    DeviceDO getById(String deviceId);

    List<DeviceDO> getAllAvailableDevices();

    List<MacsRestDevice> getListForConnection(List<String> deviceCodes);

    int update(UpdateWrapper<DeviceDO> deviceWrapper);
}
