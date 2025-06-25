package com.miyu.cloud.dms.service.ledger;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerPageReqVO;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToLocationDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToLocationMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.DictConstants.DMS_LOCATION_TYPE_CUTTING;
import static com.miyu.cloud.dms.enums.DictConstants.DMS_LOCATION_TYPE_MATERIAL;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.LEDGER_DUPLICATE_NAME;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.LEDGER_NOT_EXISTS;

/**
 * 设备台账 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class LedgerServiceImpl implements LedgerService {

    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private LedgerToLocationMapper ledgerToLocationMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    private void check(LedgerSaveReqVO data) {
        if (ledgerMapper.exists(new LambdaQueryWrapperX<LedgerDO>().eq(true, LedgerDO::getCode, data.getCode()).ne(data.getId() != null, LedgerDO::getId, data.getId()))) {
            throw exception(LEDGER_DUPLICATE_NAME);
        }
//        if (StringUtils.isEmpty(data.getCode())) {
//            throw exception(LEDGER_CODE_EMPTY);
//        }
//        if (StringUtils.isEmpty(data.getName())) {
//            throw exception(LEDGER_NAME_EMPTY);
//        }
//        if (data.getType() == null) {
//            throw exception(LEDGER_TYPE_EMPTY);
//        }
//        if (data.getStatus() == null) {
//            throw exception(LEDGER_STATUS_EMPTY);
//        }
//        if (StringUtils.isEmpty(data.getSuperintendent())) {
//            throw exception(LEDGER_SUPERINTENDENT_EMPTY);
//        }
    }

    @Override
    public String createLedger(LedgerSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        LedgerDO ledger = BeanUtils.toBean(createReqVO, LedgerDO.class);
        ledgerMapper.insert(ledger);
        // 返回
        return ledger.getId();
    }

    @Override
    public void updateLedger(LedgerSaveReqVO updateReqVO) {
        // 校验存在
        validateLedgerExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        LedgerDO updateObj = BeanUtils.toBean(updateReqVO, LedgerDO.class);
        ledgerMapper.updateById(updateObj);
        //更新空值
        if (updateObj.getLintStationGroup() == null) {
            UpdateWrapper<LedgerDO> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", updateObj.getId());
            wrapper.set("lint_station_group", null);
            ledgerMapper.update(null, wrapper);
        }
    }

    @Override
    public void deleteLedger(String id) {
        // 校验存在
        validateLedgerExists(id);
        // 删除
        ledgerMapper.deleteById(id);
    }

    private void validateLedgerExists(String id) {
        if (ledgerMapper.selectById(id) == null) {
            throw exception(LEDGER_NOT_EXISTS);
        }
    }

    @Override
    public LedgerDO getLedger(String id) {
        return ledgerMapper.selectById(id);
    }

    @Override
    public PageResult<LedgerDO> getLedgerPage(LedgerPageReqVO pageReqVO) {
        return ledgerMapper.selectPage(pageReqVO);
    }

    @Override
    public List<LedgerDO> getLedgerList() {
        return ledgerMapper.selectList();
    }

    @Override
    public List<LedgerDO> getUnitListByRecordIds(Collection<String> recordIds) {
        return ledgerMapper.selectList(new QueryWrapper<LedgerDO>().in("lint_station_group", recordIds));
    }

    @Override
    public LedgerDO getLedgerByIp(String ip) {
        return ledgerMapper.selectOne(new LambdaQueryWrapperX<LedgerDO>().eq(LedgerDO::getIp, ip));
    }

    @Override
    public List<LedgerDO> list(Wrapper<LedgerDO> wrapper) {
        return ledgerMapper.selectList(wrapper);
    }

    @Override
    public List<LedgerDO> listByIds(Collection<String> deviceIds) {
        return ledgerMapper.selectBatchIds(deviceIds);
    }

    @Override
    public List<LedgerDO> getLedgerListByIds(Collection<String> deviceIds) {
        return ledgerMapper.selectBatchIds(deviceIds);
    }

    /**
     * 通过设备id 查询仓库
     *
     * @param deviceId 设备id(不包含产线)
     * @return 仓库id
     */
    @Override
    public String getWarehouseByDevice(String deviceId) {
        LedgerDO ledgerDO = ledgerMapper.selectById(deviceId);
        return ledgerDO.getLocationId();
    }

    /**
     * 通过设备id 查询刀具库位
     *
     * @param deviceId 设备id(不包含产线)
     * @return 库位id
     */
    @Override
    public String getCuttingLocationByDevice(String deviceId) {
        List<LedgerToLocationDO> ledgerToLocationDOS = ledgerToLocationMapper.selectList(LedgerToLocationDO::getLedger, deviceId, LedgerToLocationDO::getType, DMS_LOCATION_TYPE_CUTTING);
        if (ledgerToLocationDOS.size() > 1) {
            throw new ServiceException(5005, "设备刀具库位重复");
        }
        if (ledgerToLocationDOS.size() == 0) {
            return null;
        } else {
            return ledgerToLocationDOS.get(0).getLocation();
        }
    }

    @Override
    public Map<String,String> getCuttingLocationListByDeviceIds(Collection<String> deviceIds) {
        LambdaQueryWrapper<LedgerToLocationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LedgerToLocationDO::getType, DMS_LOCATION_TYPE_CUTTING);
        queryWrapper.in(LedgerToLocationDO::getLedger, deviceIds);
        List<LedgerToLocationDO> ledgerToLocationDOS = ledgerToLocationMapper.selectList(queryWrapper);
        return ledgerToLocationDOS.stream().collect(Collectors.toMap(LedgerToLocationDO::getLedger, LedgerToLocationDO::getLocation));
    }

    @Override
    public String getToolLocationByDevice(String deviceId) {
        List<LedgerToLocationDO> ledgerToLocationDOS = ledgerToLocationMapper.selectList(LedgerToLocationDO::getLedger, deviceId, LedgerToLocationDO::getType, DMS_LOCATION_TYPE_MATERIAL);
        if (ledgerToLocationDOS.size() > 1) {
            throw new ServiceException(5005, "设备资源库位重复");
        }
        if (ledgerToLocationDOS.size() == 0) {
            return null;
        } else {
            return ledgerToLocationDOS.get(0).getLocation();
        }
    }

    @Override
    public List<LedgerDO> getByLineTypeAndDeviceType(Collection<String> unitTypeSet, Collection<String> typeTypeSet) {
        LambdaQueryWrapper<LineStationGroupDO> queryWrapperLine = new LambdaQueryWrapperX<>();
        queryWrapperLine.in(LineStationGroupDO::getAffiliationDeviceType, unitTypeSet);
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList(queryWrapperLine);
        Set<String> lineSet = lineStationGroupDOS.stream().map(LineStationGroupDO::getId).collect(Collectors.toSet());
        List<DeviceTypeDO> deviceTypeDOList = deviceTypeMapper.selectBatchIds(unitTypeSet);
        Set<String> typeSet = deviceTypeDOList.stream().map(DeviceTypeDO::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<LedgerDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.and(qw -> {
            if (lineSet.size() > 0) {
                qw.in(LedgerDO::getLintStationGroup, lineSet);
                if (typeSet.size() > 0) {
                    qw.or(qw1 -> qw1.isNull(LedgerDO::getLintStationGroup).in(LedgerDO::getEquipmentStationType,typeSet));
                }
            } else if (typeSet.size() > 0) {
                qw.and(qw1 -> qw1.isNull(LedgerDO::getLintStationGroup).in(LedgerDO::getEquipmentStationType,typeSet));
            }
        });
        if (typeTypeSet != null && typeTypeSet.size() > 0) {
            queryWrapper.in(LedgerDO::getEquipmentStationType, typeTypeSet);
        }
        return ledgerMapper.selectList(queryWrapper);
    }

    @Override
    public LedgerDO getLedgerByNumber(String deviceNumber) {
        List<LedgerDO> ledgerDOList = ledgerMapper.selectList(LedgerDO::getCode, deviceNumber);
        return ledgerDOList.size() == 1 ? ledgerDOList.get(0) : null;
    }

    @Override
    public int save(LedgerDO ledgerDO) {
        return ledgerMapper.insert(ledgerDO);
    }

    @Override
    public int UpdateById(LedgerDO ledgerDO) {
        return ledgerMapper.updateById(ledgerDO);
    }

    @Override
    public CommonResult<String> getDeviceCodeByLocationId(String locationId) {
        List<LedgerDO> ledgerDOList = ledgerMapper.selectDeviceCodeByLocationId(locationId);
        if(ledgerDOList.size() != 1){
            return CommonResult.error(600, "传入刀具库位未绑定机床编码，请先绑定机床编码");
        }
        return CommonResult.success(ledgerDOList.get(0).getCode());
    }
}
