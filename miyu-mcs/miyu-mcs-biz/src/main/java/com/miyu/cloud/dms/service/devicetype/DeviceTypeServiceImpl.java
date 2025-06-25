package com.miyu.cloud.dms.service.devicetype;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToUserDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToUserMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.enums.DictConstants;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.DictConstants.*;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.*;

/**
 * 设备类型 Service 实现类
 *
 * @author 王正浩
 */
@Service
@Validated
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private LedgerToUserMapper ledgerToUserMapper;

    private void check(DeviceTypeSaveReqVO data) {
        if (deviceTypeMapper.exists(new LambdaQueryWrapperX<DeviceTypeDO>().eq(true, DeviceTypeDO::getCode, data.getCode()).ne(data.getId() != null, DeviceTypeDO::getId, data.getId()))) {
            throw exception(DEVICE_TYPE_DUPLICATE_NAME);
        }
        if (StringUtils.isBlank(data.getCode())) {
            throw exception(DEVICE_TYPE_CODE_EMPTY);
        }
        if (StringUtils.isBlank(data.getName())) {
            throw exception(DEVICE_TYPE_NAME_EMPTY);
        }
        if (data.getEnable() == null) {
            throw exception(DEVICE_TYPE_ENABLE_EMPTY);
        }
    }

    @Override
    public String createDeviceType(DeviceTypeSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        DeviceTypeDO deviceType = BeanUtils.toBean(createReqVO, DeviceTypeDO.class);
        deviceTypeMapper.insert(deviceType);
        // 返回
        return deviceType.getId();
    }

    @Override
    public void updateDeviceType(DeviceTypeSaveReqVO updateReqVO) {
        // 校验存在
        validateDeviceTypeExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        DeviceTypeDO updateObj = BeanUtils.toBean(updateReqVO, DeviceTypeDO.class);
        deviceTypeMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeviceType(String id) {
        // 校验存在
        validateDeviceTypeExists(id);
        // 删除
        deviceTypeMapper.deleteById(id);
    }

    private void validateDeviceTypeExists(String id) {
        if (deviceTypeMapper.selectById(id) == null) {
            throw exception(DEVICE_TYPE_NOT_EXISTS);
        }
    }

    @Override
    public DeviceTypeDO getDeviceType(String id) {
        return deviceTypeMapper.selectById(id);
    }

    @Override
    public PageResult<DeviceTypeDO> getDeviceTypePage(DeviceTypePageReqVO pageReqVO) {
        return deviceTypeMapper.selectPage(pageReqVO);
    }

    @Override
    public List<DeviceTypeDO> listDeviceType(Integer type) {
        if (DictConstants.DMS_EQUIPMENT_STATION_3.equals(type)) {
            return deviceTypeMapper.selectList(new LambdaQueryWrapperX<DeviceTypeDO>()
                    .eq(DeviceTypeDO::getEnable, DMS_DEVICE_TYPE_ENABLE_1)
                    .in(DeviceTypeDO::getType, DMS_EQUIPMENT_STATION_1, DMS_EQUIPMENT_STATION_2));
        } else {
            return deviceTypeMapper.selectList(new LambdaQueryWrapperX<DeviceTypeDO>()
                    .eq(DeviceTypeDO::getEnable, DMS_DEVICE_TYPE_ENABLE_1)
                    .eqIfPresent(DeviceTypeDO::getType, type));
        }
    }

    @Override
    public List<CommonDevice> getLineTypeList(CommonDevice commonDevice) {
        LambdaQueryWrapperX<DeviceTypeDO> queryWrapperDT = new LambdaQueryWrapperX<>();
        queryWrapperDT.eq(DeviceTypeDO::getEnable, 1);
        queryWrapperDT.in(DeviceTypeDO::getType, 1,2);
        queryWrapperDT.likeIfPresent(DeviceTypeDO::getCode, commonDevice.getCode());
        queryWrapperDT.likeIfPresent(DeviceTypeDO::getName, commonDevice.getName());
        queryWrapperDT.eqIfPresent(DeviceTypeDO::getType, commonDevice.getType());
        List<DeviceTypeDO> typeList = deviceTypeMapper.selectList(queryWrapperDT);
        List<CommonDevice> list = BeanUtils.toBean(typeList, CommonDevice.class);

        LambdaQueryWrapperX<LedgerDO> queryWrapperL = new LambdaQueryWrapperX<>();
        queryWrapperL.and(qw -> qw.isNull(LedgerDO::getLintStationGroup).or().eq(LedgerDO::getLintStationGroup, ""));
        queryWrapperL.eqIfPresent(LedgerDO::getType, commonDevice.getType());
        List<LedgerDO> ledgerDOS = ledgerMapper.selectList(queryWrapperL);
        if (ledgerDOS.size() > 0) {
            Set<String> collect = ledgerDOS.stream().map(LedgerDO::getEquipmentStationType).collect(Collectors.toSet());
            List<DeviceTypeDO> list1 = deviceTypeMapper.selectBatchIds(collect);
            if (StringUtils.isNotBlank(commonDevice.getCode())) {
                list1 = list1.stream().filter(item -> item.getCode().contains(commonDevice.getCode())).collect(Collectors.toList());
            }
            if (StringUtils.isNotBlank(commonDevice.getName())) {
                list1 = list1.stream().filter(item -> item.getName().contains(commonDevice.getName())).collect(Collectors.toList());
            }
            list.addAll(BeanUtils.toBean(list1, CommonDevice.class));
        }
        return list;
    }

    /**
     *  根据设备类型id 匹配设备类型
     */
    @Override
    public List<DeviceTypeDataRespDTO> getDeviceTypeListByIds(Collection<String> ids) {
        if (ids.size() == 0) return new ArrayList<>();
        List<DeviceTypeDO> list = deviceTypeMapper.selectList(new QueryWrapper<DeviceTypeDO>().in("id", ids).eq("enable", 1));
        return BeanUtils.toBean(list, DeviceTypeDataRespDTO.class);
    }

    /**
     * 根据产线类型id 查询设备类型
     */
    @Override
    public List<DeviceTypeDataRespDTO> getDeviceTypeListByLineType(Collection<String> ids) {
        if (ids.size() == 0) return new ArrayList<>();
        List<CommonDevice> lineList = getUnitListByLineType(ids);
        Set<String> collect = lineList.stream().map(CommonDevice::getId).collect(Collectors.toSet());
        List<LedgerDO> ledgerList = getLedgerListByLineList(collect);
        Set<String> deviceTypeId = ledgerList.stream().map(LedgerDO::getEquipmentStationType).collect(Collectors.toSet());
        if (deviceTypeId.size() == 0) return new ArrayList<>();
        List<DeviceTypeDO> list = deviceTypeMapper.selectList(new QueryWrapper<DeviceTypeDO>().in("id", deviceTypeId).eq("enable", 1));
        return BeanUtils.toBean(list, DeviceTypeDataRespDTO.class);
    }

    /**
     * 根据类型查询 产线或设备类型
     * @param ids 设备类型/产线类型 id
     */
    @Override
    public List<CommonDevice> getUnitListByLineType(Collection<String> ids) {
        if (ids.size() == 0) return new ArrayList<>();
        QueryWrapper<LineStationGroupDO> queryWrapperU = new QueryWrapper<>();
        queryWrapperU.in("affiliation_device_type", ids);
        queryWrapperU.eq("enable", 1);
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList(queryWrapperU);
        QueryWrapper<DeviceTypeDO> queryWrapperDT = new QueryWrapper<>();
        queryWrapperDT.in("id", ids);
        queryWrapperDT.in("type", 0); //设备(单机)
        List<DeviceTypeDO> deviceTypeDOS = deviceTypeMapper.selectList(queryWrapperDT);
        List<CommonDevice> list = BeanUtils.toBean(lineStationGroupDOS, CommonDevice.class);
        if (deviceTypeDOS.size() > 0) {
            list.addAll(BeanUtils.toBean(deviceTypeDOS, CommonDevice.class));
        }
        return list;
    }

    @Override
    public List<CommonDevice> getAllUnitList() {
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList();
        List<CommonDevice> list = BeanUtils.toBean(lineStationGroupDOS, CommonDevice.class);
        QueryWrapper<LedgerDO> queryWrapperL = new QueryWrapper<>();
        queryWrapperL.isNull("lint_station_group");
        queryWrapperL.or().eq("lint_station_group", "");
        List<LedgerDO> ledgerDOS = ledgerMapper.selectList(queryWrapperL);
        if (list.size() > 0) {
            list.addAll(BeanUtils.toBean(ledgerDOS, CommonDevice.class));
        }
        return list;
    }

    /**
     * 通过 产线/单机设备类型 ids 查询设备
     */
    @Override
    public List<LedgerDO> getLedgerListByLineList(Collection<String> unitIds) {
        if (unitIds.size() == 0) return new ArrayList<>();
        QueryWrapper<LedgerDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("lint_station_group", unitIds);
        queryWrapper.or(qw -> qw.isNull("lint_station_group").or().eq("lint_station_group","")).in("equipment_station_type", unitIds);
        return ledgerMapper.selectList(queryWrapper);
    }

    @Override
    public List<DeviceTypeDO> getAllTypeList(CommonDevice commonDevice) {
        return deviceTypeMapper.selectList(new LambdaQueryWrapperX<DeviceTypeDO>()
                .eq(DeviceTypeDO::getEnable, DMS_DEVICE_TYPE_ENABLE_1)
                .likeIfPresent(DeviceTypeDO::getCode, commonDevice.getCode())
                .likeIfPresent(DeviceTypeDO::getName, commonDevice.getName())
                .eqIfPresent(DeviceTypeDO::getType, commonDevice.getType())
        );
    }

    @Override
    public List<CommonDevice> getDeviceListByUserId(String userId) {
        List<LedgerToUserDO> ledgerToUserDOS = ledgerToUserMapper.selectList(LedgerToUserDO::getUser, userId);
        Set<String> ids = ledgerToUserDOS.stream().map(LedgerToUserDO::getLedger).collect(Collectors.toSet());
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectBatchIds(ids);
        List<LedgerDO> ledgerDOS = ledgerMapper.selectBatchIds(ids);
        List<CommonDevice> commonDevices = BeanUtils.toBean(lineStationGroupDOS, CommonDevice.class);
        BeanUtils.toBean(ledgerDOS.get(0), CommonDevice.class);
        commonDevices.addAll(BeanUtils.toBean(ledgerDOS, CommonDevice.class));
        return commonDevices;
    }


    @Override
    public List<DeviceTypeDO> getDeviceTypeList() {
        return deviceTypeMapper.selectList(new LambdaQueryWrapperX<DeviceTypeDO>()
                .eq(DeviceTypeDO::getEnable, DMS_DEVICE_TYPE_ENABLE_1)
                .in(DeviceTypeDO::getType, DMS_EQUIPMENT_STATION_0, DMS_EQUIPMENT_STATION_1)
        );
    }

    @Override
    public DeviceTypeDO getDeviceTypeListByCode(String deviceTypeCode) {
        List<DeviceTypeDO> deviceTypeDOList = deviceTypeMapper.selectList(DeviceTypeDO::getCode, deviceTypeCode);
        return deviceTypeDOList.size() == 1 ? deviceTypeDOList.get(0) : null;
    }
}
