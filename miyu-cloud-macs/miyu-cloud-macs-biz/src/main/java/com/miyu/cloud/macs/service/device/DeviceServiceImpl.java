package com.miyu.cloud.macs.service.device;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.dal.mysql.collector.CollectorMapper;
import com.miyu.cloud.macs.dal.mysql.door.DoorMapper;
import com.miyu.cloud.macs.restServer.entity.MacsRestCollector;
import com.miyu.cloud.macs.restServer.entity.MacsRestDevice;
import com.miyu.cloud.macs.restServer.entity.MacsRestDoor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.device.vo.*;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.device.DeviceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 设备 Service 实现类
 *
 * @author 芋道源码
 */
@Slf4j
@Service
@Validated
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;
    @Resource
    private CollectorMapper collectorMapper;
    @Resource
    private DoorMapper doorMapper;

    @Override
    public String createDevice(DeviceSaveReqVO createReqVO) {
        // 插入
        DeviceDO device = BeanUtils.toBean(createReqVO, DeviceDO.class);
        deviceMapper.insert(device);
        // 返回
        return device.getId();
    }

    @Override
    public void updateDevice(DeviceSaveReqVO updateReqVO) {
        // 校验存在
        validateDeviceExists(updateReqVO.getId());
        // 更新
        DeviceDO updateObj = BeanUtils.toBean(updateReqVO, DeviceDO.class);
        deviceMapper.updateById(updateObj);
    }

    @Override
    public void deleteDevice(String id) {
        // 校验存在
        validateDeviceExists(id);
        // 删除
        deviceMapper.deleteById(id);
    }

    private void validateDeviceExists(String id) {
        if (deviceMapper.selectById(id) == null) {
            throw exception(DEVICE_NOT_EXISTS);
        }
    }

    @Override
    public DeviceDO getDevice(String id) {
        return deviceMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceDO> getDevicePage(DevicePageReqVO pageReqVO) {
        return deviceMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceDO> getDeviceList(DevicePageReqVO listReqVO) {
        return deviceMapper.selectList();
    }

    @Override
    public DeviceDO getById(String deviceId) {
        return deviceMapper.selectById(deviceId);
    }

    @Override
    public List<DeviceDO> getAllAvailableDevices() {
        QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper();
        queryWrapper.eq("enable_status", 1);
        return deviceMapper.selectList(queryWrapper);
    }

    /**
     * 整理封装参数 排除不可用办公设备 返回值不为null 参数可能为null
     * @param deviceCodes 设备编码集合
     * @return MacsRestDevice
     */
    @Override
    public List<MacsRestDevice> getListForConnection(List<String> deviceCodes) {
        if (deviceCodes == null || deviceCodes.size() == 0) return new ArrayList<>();
        List<MacsRestDevice> result = new ArrayList<>();
        for (String deviceCode : deviceCodes) {
            QueryWrapper<DeviceDO> queryWrapper = new QueryWrapper<DeviceDO>().eq("code", deviceCode).eq("enable_status", 1);
            List<DeviceDO> devices = deviceMapper.selectList(queryWrapper);
            if (devices.size() == 0) continue;
            if (devices.size() > 1) {
                log.error("设备查询,'"+deviceCode+"'有多个结果");
                throw new RuntimeException("设备查询,'"+deviceCode+"'有多个结果");
            }
            DeviceDO device = devices.get(0);
            MacsRestDevice restDevice = new MacsRestDevice(device);
            result.add(restDevice);
            List<CollectorDO> collectorList = collectorMapper.selectList(new QueryWrapper<CollectorDO>().eq("device_id", device.getId()));
            Map<String, MacsRestDoor> doorsMap = new HashMap<>();
            for (CollectorDO collector : collectorList) {
                String doorId = collector.getDoorId();
                MacsRestDoor restDoor = doorsMap.get(doorId);
                if (restDoor == null) {
                    DoorDO door = doorMapper.selectById(doorId);
                    restDoor = new MacsRestDoor(door);
                    doorsMap.put(doorId, restDoor);
                }
                restDoor.collectorsAdd(new MacsRestCollector(collector));
            }
            for (MacsRestDoor restDoor : doorsMap.values()) {
                restDevice.addDoor(restDoor);
            }
        }
        return result;
    }

    @Override
    public int update(UpdateWrapper<DeviceDO> deviceWrapper) {
        return deviceMapper.update(deviceWrapper);
    }
}
