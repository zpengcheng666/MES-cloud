package com.miyu.module.dc.service.device;

import java.util.*;
import javax.validation.*;

import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.module.dc.controller.admin.device.vo.*;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

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
    void createDevice(@Valid DeviceSaveReqVO createReqVO);

    /**
     * 更新设备
     *
     * @param updateReqVO 更新信息
     */
    void updateDevice(@Valid DeviceUpdateReqVO updateReqVO);

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

    /**
     * 获取设备列表
     * @return
     */
    List<DeviceDO> getDeviceList();

    /**
     * 获取设备监控分页
     */
    PageResult<DeviceOfflineRespVO> getDeviceOfflinePage(DevicePageReqVO pageReqVO);

    /**
     * 获取当前数据采集未存在设备
     */
    List<LedgerDataResDTO> getDeviceListByCollet(String id);

}