package com.miyu.cloud.macs.service.accessRecords;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import com.miyu.cloud.macs.dal.mysql.collector.CollectorMapper;
import com.miyu.cloud.macs.dal.mysql.device.DeviceMapper;
import com.miyu.cloud.macs.dal.mysql.door.DoorMapper;
import com.miyu.cloud.macs.dal.mysql.region.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import com.miyu.cloud.macs.controller.admin.accessRecords.vo.*;
import com.miyu.cloud.macs.dal.dataobject.accessRecords.AccessRecordsDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.accessRecords.AccessRecordsMapper;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 通行记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AccessRecordsServiceImpl implements AccessRecordsService {

    @Resource
    private AccessRecordsMapper accessRecordsMapper;
    @Autowired
    private CollectorMapper collectorMapper;
    @Autowired
    private DoorMapper doorMapper;
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private RegionMapper regionMapper;

    @Override
    public String createAccessRecords(AccessRecordsSaveReqVO createReqVO) {
        // 插入
        AccessRecordsDO accessRecords = BeanUtils.toBean(createReqVO, AccessRecordsDO.class);
        accessRecordsMapper.insert(accessRecords);
        // 返回
        return accessRecords.getId();
    }

    @Override
    public void updateAccessRecords(AccessRecordsSaveReqVO updateReqVO) {
        // 校验存在
        validateAccessRecordsExists(updateReqVO.getId());
        // 更新
        AccessRecordsDO updateObj = BeanUtils.toBean(updateReqVO, AccessRecordsDO.class);
        accessRecordsMapper.updateById(updateObj);
    }

    @Override
    public void deleteAccessRecords(String id) {
        // 校验存在
        validateAccessRecordsExists(id);
        // 删除
        accessRecordsMapper.deleteById(id);
    }

    private void validateAccessRecordsExists(String id) {
        if (accessRecordsMapper.selectById(id) == null) {
            throw exception(ACCESS_RECORDS_NOT_EXISTS);
        }
    }

    @Override
    public AccessRecordsDO getAccessRecords(String id) {
        return accessRecordsMapper.selectById(id);
    }

    @Override
    public PageResult<AccessRecordsDO> getAccessRecordsPage(AccessRecordsPageReqVO pageReqVO) {
        return accessRecordsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<AccessRecordsDO> list(QueryWrapper<AccessRecordsDO> wrapper) {
        return accessRecordsMapper.selectList(wrapper);
    }

    @Override
    public void add(AccessRecordsDO records, AccessRecordsDO.InstructType validate, String msg) {
        records.setId(null);
        CollectorDO collector = records.getCollector();
        if (records.getCollectorId() != null) {
            if (records.getCollector() == null) {
                collector =collectorMapper.selectById(records.getCollectorId());
                records.setCollector(collector);
            }
            if (records.getDoorId() == null) {
                DoorDO door = doorMapper.selectById(collector.getDoorId());
                records.setDoor(door);
            }
            if (records.getDeviceId() == null) {
                DeviceDO device = deviceMapper.selectById(collector.getDeviceId());
                records.setDevice(device);
            }
        }
        if (records.getDoorId() != null) {
            if (records.getDoor() == null) {
                records.setDoor(doorMapper.selectById(records.getDoorId()));
            }
            if (records.getRegionId() == null) {
                RegionDO region = regionMapper.selectById(records.getDoor().getRegionId());
                records.setRegion(region);
            }
            if (records.getDeviceId() == null) {
                DeviceDO device = deviceMapper.selectById(records.getDoor().getDeviceId());
                records.setDevice(device);
            }
        }
        switch (validate) {
            case validate: records.setAction(0); break;
            case open: records.setAction(3); break;
            case close: records.setAction(4); break;
            case general:
                if (collector != null) {
                    if ("0".equals(collector.getLocationCode())) records.setAction(2);
                    else if ("1".equals(collector.getLocationCode())) records.setAction(1);
                    else records.setAction(-1);
                    break;
                }
            default: records.setAction(-1);
        }
        records.setMessage(msg);
        accessRecordsMapper.insert(records);
    }
}
