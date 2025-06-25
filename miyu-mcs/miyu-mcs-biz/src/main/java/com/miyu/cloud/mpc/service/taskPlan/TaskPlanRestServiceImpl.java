package com.miyu.cloud.mpc.service.taskPlan;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.miyu.cloud.mcs.enums.DictConstants.*;

@Slf4j
@Service
@Validated
@Transactional
public class TaskPlanRestServiceImpl implements TaskPlanRestService {

    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;

    //质检工序 质检单生产
    @Override
    public List<BatchRecordDO> checkBatchRecordNeedInspect() {
        List<BatchRecordDO> list = new ArrayList<>();
        DeviceTypeDO DLMIS = deviceTypeMapper.selectOne(DeviceTypeDO::getCode, "DLMIS");
        List<LedgerDO> inspectList = ledgerMapper.selectList(LedgerDO::getEquipmentStationType, DLMIS.getId());
        for (LedgerDO ledgerDO : inspectList) {
            //查询任务
            LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED);
            queryWrapper.eq(BatchRecordDO::getDeviceId, ledgerDO.getId());
            queryWrapper.notIn(BatchRecordDO::getInspect, 0, 99, 1234);
            List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
            if (batchRecordDOList.size() == 0) continue;
            list.addAll(batchRecordDOList);
        }
        return list;
    }
}
