package com.miyu.cloud.mcs.service.distributionrecord;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 物料配送申请详情 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class DistributionRecordServiceImpl implements DistributionRecordService {

    @Resource
    private DistributionRecordMapper distributionRecordMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;

    @Resource
    private WarehouseRestService warehouseRestService;

    @Override
    public String createDistributionRecord(DistributionRecordSaveReqVO createReqVO) {
        // 插入
        DistributionRecordDO distributionRecord = BeanUtils.toBean(createReqVO, DistributionRecordDO.class);
        distributionRecordMapper.insert(distributionRecord);
        // 返回
        return distributionRecord.getId();
    }

    @Override
    public void updateDistributionRecord(DistributionRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateDistributionRecordExists(updateReqVO.getId());
        // 更新
        DistributionRecordDO updateObj = BeanUtils.toBean(updateReqVO, DistributionRecordDO.class);
        distributionRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteDistributionRecord(String id) {
        // 校验存在
        validateDistributionRecordExists(id);
        // 删除
        distributionRecordMapper.deleteById(id);
    }

    private void validateDistributionRecordExists(String id) {
        if (distributionRecordMapper.selectById(id) == null) {
            throw exception(DISTRIBUTION_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public DistributionRecordDO getDistributionRecord(String id) {
        return distributionRecordMapper.selectById(id);
    }

    @Override
    public PageResult<DistributionRecordDO> getDistributionRecordPage(DistributionRecordPageReqVO pageReqVO) {
        return distributionRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<DistributionRecordRespVO> getDistributionRecordPageAll(DistributionRecordPageReqVO pageReqVO) {
        PageResult<DistributionRecordRespVO> data = BeanUtils.toBean(distributionRecordMapper.selectPage(pageReqVO), DistributionRecordRespVO.class);
        Set<String> unitIds = new HashSet<>();
        Set<String> deviceIds = new HashSet<>();
        Set<String> batchRecordIds = new HashSet<>();
        for (DistributionRecordRespVO distributionRecordDO : data.getList()) {
            if (StringUtils.isNotBlank(distributionRecordDO.getProcessingUnitId())) {
                unitIds.add(distributionRecordDO.getProcessingUnitId());
            }
            if (StringUtils.isNotBlank(distributionRecordDO.getDeviceId())) {
                deviceIds.add(distributionRecordDO.getDeviceId());
            }
            if (StringUtils.isNotBlank(distributionRecordDO.getBatchRecordId())) {
                batchRecordIds.add(distributionRecordDO.getBatchRecordId());
            }
        }
        Map<String, String> unitNameMap = new HashMap<>();
        Map<String, String> deviceNameMap = new HashMap<>();
        Map<String, String> batchRecordNameMap = new HashMap<>();
        if (unitIds.size() > 0) {
            unitNameMap = lineStationGroupMapper.selectBatchIds(unitIds).stream().collect(Collectors.toMap(LineStationGroupDO::getId, LineStationGroupDO::getName, (a,b)->b));
        }
        if (deviceIds.size() > 0) {
            deviceNameMap = ledgerMapper.selectBatchIds(deviceIds).stream().collect(Collectors.toMap(LedgerDO::getId, LedgerDO::getName, (a, b)->b));
        }
        if (batchRecordIds.size() > 0) {
            batchRecordNameMap = batchRecordMapper.selectBatchIds(batchRecordIds).stream().collect(Collectors.toMap(BatchRecordDO::getId, BatchRecordDO::getNumber, (a, b)->b));
        }
        for (DistributionRecordRespVO distributionRecordDO : data.getList()) {
            if (StringUtils.isNotBlank(distributionRecordDO.getProcessingUnitId())) {
                distributionRecordDO.setUnitName(unitNameMap.get(distributionRecordDO.getProcessingUnitId()));
            }
            if (StringUtils.isNotBlank(distributionRecordDO.getDeviceId())) {
                distributionRecordDO.setDeviceName(deviceNameMap.get(distributionRecordDO.getDeviceId()));
            }
            if (StringUtils.isNotBlank(distributionRecordDO.getBatchRecordId())) {
                distributionRecordDO.setBatchRecordNumber(batchRecordNameMap.get(distributionRecordDO.getBatchRecordId()));
            }
        }
        return data;
    }

    @Override
    public List<DistributionRecordDO> listByApplication(String applicationId) {
        return distributionRecordMapper.selectList(DistributionRecordDO::getApplicationId, applicationId);
    }

    @Override
    public void recordRevokeByDemandRecord(String demandRecordId) {
        List<DistributionRecordDO> recordList = distributionRecordMapper.selectList(DistributionRecordDO::getDemandRecordId, demandRecordId);
        recordList = recordList.stream().filter(
                item -> item.getStatus() == MCS_DELIVERY_RECORD_STATUS_NEW
                        || item.getStatus() == MCS_DELIVERY_RECORD_STATUS_REJECT
                        || item.getStatus() == MCS_DELIVERY_RECORD_STATUS_APPLIED).collect(Collectors.toList());
        warehouseRestService.distributionRecordRevoke(recordList);
        recordList.forEach(item -> item.setStatus(MCS_DELIVERY_RECORD_STATUS_CLOSE));
        distributionRecordMapper.updateBatch(recordList);
    }

    @Override
    public void recordRevokeById(String id) {
        DistributionRecordDO distributionRecordDO = distributionRecordMapper.selectById(id);
        if (distributionRecordDO.getStatus() != MCS_DELIVERY_RECORD_STATUS_NEW
                && distributionRecordDO.getStatus() != MCS_DELIVERY_RECORD_STATUS_REJECT
                && distributionRecordDO.getStatus() != MCS_DELIVERY_RECORD_STATUS_APPLIED) {
            throw new ServiceException(5005, "当前配送任务状态不可撤销");
        }
        warehouseRestService.distributionRecordRevoke(Collections.singletonList(distributionRecordDO));
        distributionRecordMapper.updateById(distributionRecordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_CLOSE));
    }
}
