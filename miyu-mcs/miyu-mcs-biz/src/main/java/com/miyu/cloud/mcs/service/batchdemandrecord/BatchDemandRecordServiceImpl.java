package com.miyu.cloud.mcs.service.batchdemandrecord;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.service.distributionrecord.DistributionRecordService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 需求分拣详情 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class BatchDemandRecordServiceImpl implements BatchDemandRecordService {

    @Resource
    private BatchDemandRecordMapper batchDemandRecordMapper;


    @Resource
    private DistributionRecordService distributionRecordService;

    @Override
    public String createBatchDemandRecord(BatchDemandRecordSaveReqVO createReqVO) {
        // 插入
        BatchDemandRecordDO batchDemandRecord = BeanUtils.toBean(createReqVO, BatchDemandRecordDO.class);
        batchDemandRecordMapper.insert(batchDemandRecord);
        // 返回
        return batchDemandRecord.getId();
    }

    @Override
    public void updateBatchDemandRecord(BatchDemandRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchDemandRecordExists(updateReqVO.getId());
        // 更新
        BatchDemandRecordDO updateObj = BeanUtils.toBean(updateReqVO, BatchDemandRecordDO.class);
        batchDemandRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchDemandRecord(String id) {
        // 校验存在
        validateBatchDemandRecordExists(id);
        // 删除
        batchDemandRecordMapper.deleteById(id);
    }

    private void validateBatchDemandRecordExists(String id) {
        if (batchDemandRecordMapper.selectById(id) == null) {
            throw exception(BATCH_DEMAND_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public BatchDemandRecordDO getBatchDemandRecord(String id) {
        return batchDemandRecordMapper.selectById(id);
    }

    @Override
    public PageResult<BatchDemandRecordDO> getBatchDemandRecordPage(BatchDemandRecordPageReqVO pageReqVO) {
        return batchDemandRecordMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BatchDemandRecordDO> list(Wrapper<BatchDemandRecordDO> wrapper) {
        return batchDemandRecordMapper.selectList(wrapper);
    }

    @Override
    public void demandRecordRevoke(String id) {
        BatchDemandRecordDO demandRecordDO = batchDemandRecordMapper.selectById(id);
        int status = demandRecordDO.getStatus();
        if (status == MCS_DEMAND_RECORD_STATUS_APPLIED) {
            distributionRecordService.recordRevokeByDemandRecord(id);
        }
        demandRecordDO.setStatus(MCS_DEMAND_RECORD_STATUS_RESCINDED);
        batchDemandRecordMapper.updateById(demandRecordDO);
    }
}
