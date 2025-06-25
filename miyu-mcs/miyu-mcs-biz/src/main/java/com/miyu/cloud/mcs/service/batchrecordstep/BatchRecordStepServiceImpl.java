package com.miyu.cloud.mcs.service.batchrecordstep;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.batchrecordstep.BatchRecordStepMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 工步计划 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class BatchRecordStepServiceImpl implements BatchRecordStepService {

    @Resource
    private BatchRecordStepMapper batchRecordStepMapper;
    @Resource
    private LedgerMapper ledgerMapper;

    @Override
    public String createBatchRecordStep(BatchRecordStepSaveReqVO createReqVO) {
        // 插入
        BatchRecordStepDO batchRecordStep = BeanUtils.toBean(createReqVO, BatchRecordStepDO.class);
        batchRecordStepMapper.insert(batchRecordStep);
        // 返回
        return batchRecordStep.getId();
    }

    @Override
    public void updateBatchRecordStep(BatchRecordStepSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchRecordStepExists(updateReqVO.getId());
        // 更新
        BatchRecordStepDO updateObj = BeanUtils.toBean(updateReqVO, BatchRecordStepDO.class);
        batchRecordStepMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchRecordStep(String id) {
        // 校验存在
        validateBatchRecordStepExists(id);
        // 删除
        batchRecordStepMapper.deleteById(id);
    }

    private void validateBatchRecordStepExists(String id) {
        if (batchRecordStepMapper.selectById(id) == null) {
            throw exception(BATCH_RECORD_STEP_NOT_EXISTS);
        }
    }

    @Override
    public BatchRecordStepDO getBatchRecordStep(String id) {
        return batchRecordStepMapper.selectById(id);
    }

    @Override
    public PageResult<BatchRecordStepDO> getBatchRecordStepPage(BatchRecordStepPageReqVO pageReqVO) {
        return batchRecordStepMapper.selectPage(pageReqVO);
    }

    @Override
    public void deleteByRecordIdsPhy(Collection<String> deleteIds) {
        if (deleteIds.size() == 0) return;
        QueryWrapper<BatchRecordStepDO> wrapper = new QueryWrapper<>();
        wrapper.in("batch_record_id", deleteIds);
        batchRecordStepMapper.deleteByRecordIdsPhy(wrapper);
    }

    @Override
    public void createBatchDetailStepByRecord(BatchRecordDO batchRecordDO, List<StepRespDTO> stepList) {
        if (stepList == null || stepList.size() == 0) return;
        List<BatchRecordStepDO> add = new ArrayList<>();
        for (StepRespDTO stepRespDTO : stepList) {
            BatchRecordStepDO batchRecordStepDO = new BatchRecordStepDO();
            batchRecordStepDO.setBatchRecordId(batchRecordDO.getId());
            batchRecordStepDO.setStepId(stepRespDTO.getId());
            batchRecordStepDO.setStepName(stepRespDTO.getStepName());
            batchRecordStepDO.setStepOrder(stepRespDTO.getStepNum());
            batchRecordStepDO.setStatus(MCS_STEP_STATUS_NEW);
            String deviceTypeIds = null;
            for (StepDetailRespDTO stepDetailRespDTO : stepRespDTO.getResourceList()) {
                if (stepDetailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) {
                    if (deviceTypeIds == null) {
                        deviceTypeIds = stepDetailRespDTO.getResourcesTypeId();
                    } else {
                        deviceTypeIds += deviceTypeIds + "," + stepDetailRespDTO.getResourcesTypeId();
                    }
                }
            }
            batchRecordStepDO.setDeviceTypeId(deviceTypeIds);
            add.add(batchRecordStepDO);
        }
        batchRecordStepMapper.insertBatch(add);
    }

    //临时方法 根据工序任务信息更新工步信息
    @Override
    public void updateByRecord(BatchRecordDO batchRecordDO) {
        List<BatchRecordStepDO> stepDOS = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        if (stepDOS.size() == 0) return;
        if (batchRecordDO.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) return;
        String[] split = batchRecordDO.getDeviceId().split(",");
        List<LedgerDO> ledgerDOS = ledgerMapper.selectBatchIds(Arrays.asList(split));
        for (BatchRecordStepDO stepDO : stepDOS) {
            List<String> deviceTypeIdList = Arrays.asList(stepDO.getDeviceTypeId().split(","));
            Set<String> set = ledgerDOS.stream().filter(item -> deviceTypeIdList.contains(item.getEquipmentStationType())).map(LedgerDO::getId).collect(Collectors.toSet());
            List<LedgerDO> ledgerDOList = ledgerMapper.selectBatchIds(set);
            stepDO.setDefineDeviceId(String.join(",",set));
            stepDO.setDefineDeviceNumber(ledgerDOList.stream().map(LedgerDO::getCode).collect(Collectors.joining(",")));
            batchRecordStepMapper.updateById(stepDO);
        }
    }

    @Override
    public List<BatchRecordStepDO> list(Wrapper<BatchRecordStepDO> queryWrapper) {
        return batchRecordStepMapper.selectList(queryWrapper);
    }
}
