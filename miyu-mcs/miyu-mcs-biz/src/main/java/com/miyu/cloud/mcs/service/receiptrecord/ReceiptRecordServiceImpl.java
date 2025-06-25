package com.miyu.cloud.mcs.service.receiptrecord;

import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionapplication.DistributionApplicationMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.receiptrecord.ReceiptRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 生产单元签收记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class ReceiptRecordServiceImpl implements ReceiptRecordService {

    @Resource
    private ReceiptRecordMapper receiptRecordMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;

    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private LedgerMapper ledgerMapper;

    @Override
    public String createReceiptRecord(ReceiptRecordSaveReqVO createReqVO) {
        // 插入
        ReceiptRecordDO receiptRecord = BeanUtils.toBean(createReqVO, ReceiptRecordDO.class);
        receiptRecordMapper.insert(receiptRecord);
        // 返回
        return receiptRecord.getId();
    }

    @Override
    public void updateReceiptRecord(ReceiptRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateReceiptRecordExists(updateReqVO.getId());
        // 更新
        ReceiptRecordDO updateObj = BeanUtils.toBean(updateReqVO, ReceiptRecordDO.class);
        receiptRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteReceiptRecord(String id) {
        // 校验存在
        validateReceiptRecordExists(id);
        // 删除
        receiptRecordMapper.deleteById(id);
    }

    private void validateReceiptRecordExists(String id) {
        if (receiptRecordMapper.selectById(id) == null) {
            throw exception(RECEIPT_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public ReceiptRecordDO getReceiptRecord(String id) {
        return receiptRecordMapper.selectById(id);
    }

    @Override
    public PageResult<ReceiptRecordRespVO> getReceiptRecordPage(ReceiptRecordPageReqVO pageReqVO) {
        PageResult<ReceiptRecordRespVO> data = BeanUtils.toBean(receiptRecordMapper.selectPage(pageReqVO), ReceiptRecordRespVO.class);
        Set<String> distributionIdSet = new HashSet<>();
        Set<String> unitIdSet = new HashSet<>();
        for (ReceiptRecordRespVO receiptRecordRespVO : data.getList()) {
            distributionIdSet.add(receiptRecordRespVO.getDistributionRecordId());
            unitIdSet.add(receiptRecordRespVO.getProcessingUnitId());
        }
        List<DistributionRecordDO> applicationDOList = new ArrayList<>();
        List<CommonDevice> allUnitList = new ArrayList<>();
        if (distributionIdSet.size() > 0) {
            applicationDOList = distributionRecordMapper.selectBatchIds(distributionIdSet);
        }
        if (unitIdSet.size() > 0) {
            allUnitList = BeanUtils.toBean(lineStationGroupMapper.selectList(), CommonDevice.class);
            allUnitList.addAll(BeanUtils.toBean(ledgerMapper.selectList(), CommonDevice.class));
        }
        Map<String, String> unitMap = allUnitList.stream().filter(item -> unitIdSet.contains(item.getId())).collect(Collectors.toMap(CommonDevice::getId, CommonDevice::getName, (a, b) -> b));
        Map<String, String> applicationDOMap = applicationDOList.stream().collect(Collectors.toMap(DistributionRecordDO::getId, DistributionRecordDO::getNumber, (a, b) -> b));
        for (ReceiptRecordRespVO receiptRecord : data.getList()) {
            receiptRecord.setApplicationNumber(applicationDOMap.get(receiptRecord.getDistributionRecordId()));
            receiptRecord.setUnitName(unitMap.get(receiptRecord.getProcessingUnitId()));
        }
        return data;
    }

}
