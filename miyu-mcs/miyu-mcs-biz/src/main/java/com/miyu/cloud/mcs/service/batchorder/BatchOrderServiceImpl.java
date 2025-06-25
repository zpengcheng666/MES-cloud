package com.miyu.cloud.mcs.service.batchorder;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.restServer.service.order.OrderRestService;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import com.miyu.cloud.mcs.service.batchrecordstep.BatchRecordStepService;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;

import com.miyu.cloud.mcs.controller.admin.batchorder.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 批次级订单 Service 实现类
 *
 * @author miyu
 */
@Slf4j
@Service
@Validated
@Transactional
public class BatchOrderServiceImpl implements BatchOrderService {

    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;

    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private OrderRestService orderRestService;
    @Resource
    private BatchRecordStepService batchRecordStepService;

    @Override
    public String createBatchOrder(BatchOrderSaveReqVO createReqVO) {
        // 插入
        BatchOrderDO batchOrder = BeanUtils.toBean(createReqVO, BatchOrderDO.class);
        batchOrderMapper.insert(batchOrder);
        // 返回
        return batchOrder.getId();
    }

    @Override
    public void updateBatchOrder(BatchOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchOrderExists(updateReqVO.getId());
        // 更新
        BatchOrderDO updateObj = BeanUtils.toBean(updateReqVO, BatchOrderDO.class);
        batchOrderMapper.updateById(updateObj);
        if (updateObj.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
            OrderFormDO orderFormDO = orderFormMapper.selectById(updateObj.getOrderId());
            if (updateReqVO.getStatus() == MCS_BATCH_STATUS_ONGOING) {
                if (orderFormDO.getStatus() != MCS_ORDER_STATUS_COMPLETED && orderFormDO.getStatus() != MCS_ORDER_STATUS_ONGOING && orderFormDO.getStatus() != MCS_ORDER_STATUS_RESCINDED) {
                    orderFormDO.setStatus(MCS_ORDER_STATUS_ONGOING);
                    orderFormMapper.updateById(orderFormDO);
                }
            } else if (updateReqVO.getStatus() == MCS_BATCH_STATUS_COMPLETED) {
                if (orderFormDO.getStatus() != MCS_ORDER_STATUS_COMPLETED && orderFormDO.getStatus() != MCS_ORDER_STATUS_RESCINDED) {
                    List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getOrderId, updateObj.getOrderId());
                    boolean flag = true;
                    for (BatchOrderDO batchOrderDO : batchOrderDOList) {
                        if (batchOrderDO.getStatus() != MCS_BATCH_STATUS_COMPLETED) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        orderFormDO.setCompletionTime(updateObj.getEndTime());
                        orderFormDO.setStatus(MCS_ORDER_STATUS_COMPLETED);
                        orderFormMapper.updateById(orderFormDO);
                    }
                }
            }
        }
    }

    @Override
    public void deleteBatchOrder(String id) {
        // 校验存在
        validateBatchOrderExists(id);
        // 删除
        batchOrderMapper.deleteById(id);
    }

    private void validateBatchOrderExists(String id) {
        if (batchOrderMapper.selectById(id) == null) {
            throw exception(BATCH_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public PageResult<BatchOrderDO> getBatchOrderPage(BatchOrderPageReqVO pageReqVO) {
        return batchOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public BatchOrderDO getBatchOrder(String id) {
        return batchOrderMapper.selectById(id);
    }

    /**
     * 物理删除
     */
    @Override
    public void deleteByIdPhy(String id) {
        batchOrderMapper.deleteByIdPhy(id);
    }

    /**
     * 物理删除
     */
    @Override
    public void deleteBatchIdsPhy(Collection<String> ids) {
        QueryWrapper<BatchOrderDO> wrapper = new QueryWrapper<BatchOrderDO>().in("id", ids);
        batchOrderMapper.deleteBatchIdsPhy(wrapper);
    }


    @Override
    public void batchSubmit(List<BatchOrderDO> batchOrderDOList) {
        for (BatchOrderDO batchOrderDO : batchOrderDOList) {
            batchSubmit(batchOrderDO);
        }
    }

    private void batchSubmit(BatchOrderDO batchOrderDO) {
        //信息完整性校验
        batchOrderDO.setSubmitStatus(MCS_BATCH_SUBMIT_STATUS_SUBMIT);
        List<BatchRecordDO> batchRecordDOS = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;
        for (BatchRecordDO batchRecordDO : batchRecordDOS) {
            if (batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_NEW) throw new ServiceException(5004, batchRecordDO.getNumber() + "订单状态异常");
            if (batchRecordDO.getProcesStatus() != MCS_PROCES_STATUS_OUTSOURCING) {
                if (StringUtil.isBlank(batchRecordDO.getProcessingUnitId())) {
                    throw new ServiceException(5007, batchRecordDO.getNumber() + ":生产单元/工位为空");
                }
                if (StringUtil.isBlank(batchRecordDO.getDeviceId())) {
                    throw new ServiceException(5007, batchRecordDO.getNumber() + ":设备/工位为空");
                }
            }
            if (batchRecordDO.getPlanStartTime() == null) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":计划开始时间为空");
            }
            if (batchRecordDO.getPlanEndTime() == null) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":计划结束时间为空");
            }
            if (beginTime == null || batchRecordDO.getPlanStartTime().isBefore(beginTime)) {
                beginTime = batchRecordDO.getPlanStartTime();
            }
            if (endTime == null || batchRecordDO.getPlanEndTime().isAfter(endTime)) {
                endTime = batchRecordDO.getPlanEndTime();
            }
            batchRecordStepService.updateByRecord(batchRecordDO);
        }
        //更新 时间 状态
        batchOrderDO.setPlanStartTime(beginTime);
        batchOrderDO.setPlanEndTime(endTime);
        batchOrderDO.setStatus(MCS_BATCH_STATUS_PREPARATION);
        batchOrderMapper.updateById(batchOrderDO);
        //后续批次
        List<BatchOrderDO> batchOrderList = batchOrderMapper.selectList(BatchOrderDO::getPreBatchId, batchOrderDO.getId());
        for (BatchOrderDO nextBatch : batchOrderList) {
            nextBatch.setFirstBatchId(batchOrderDO.getFirstBatchId());
            batchSubmit(nextBatch);
        }
    }

    /**
     * 订单下发
     */
    @Override
    public void batchIssuance(String batchId) {
        //更改批次订单状态
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchId);
        if (batchOrderDO.getPreBatchId() != null) throw new ServiceException(5008,"该订单不可手动下发");
        if (batchOrderDO.getStatus() != MCS_BATCH_STATUS_CAN_BE_ISSUED) throw new ServiceException(5004,"当前状态不可下发");
        batchOrderDO.setStatus(MCS_BATCH_STATUS_ISSUED);
        batchOrderMapper.updateById(batchOrderDO);
        //下发首个任务 生成详情
        List<BatchRecordDO> firstRecords = batchRecordMapper.selectList(new QueryWrapper<BatchRecordDO>().eq("batch_id", batchId).isNull("pre_record_id"));
        if (firstRecords.size() == 0) throw new ServiceException(5009,"未找到可下发任务");
        if (firstRecords.size() > 1) throw new ServiceException(5009,"可下发任务数量异常");
        BatchRecordDO batchRecordDO = firstRecords.get(0);
        batchRecordService.batchRecordDistribution(batchRecordDO.getId());
        //2025/1/16修改 同批次可选料->任务指定物料
        batchRecordService.bindMaterial(batchOrderDO);
        if (batchRecordDO.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
            orderRestService.outsourcingBegin(batchOrderDO);
        }
    }

    @Override
    public List<BatchOrderDO> getBatchOrderListByOrderId(String orderId) {
        return batchOrderMapper.selectBatchList(orderId);
    }
    @Override
    public List<BatchOrderDO> getBatchOrderListByOrderIds(List<String> orderIds) {
        return batchOrderMapper.selectBatchListByOrderIds(orderIds);
    }

    @Override
    public List<BatchOrderDO> list(Wrapper<BatchOrderDO> wrapper) {
        return batchOrderMapper.selectList(wrapper);
    }

    @Override
    public void batchSuspend(String id) {
        batchOrderMapper.updateById(new BatchOrderDO().setId(id).setSubmitStatus(MCS_BATCH_SUBMIT_STATUS_SUSPEND));
    }

    @Override
    public void batchContinue(String id) {
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(id);
        //校验信息完整性  更新时间
        checkAndUpdateBatchOrder(batchOrderDO);
        //下发 因暂停影响的任务
        if (batchOrderDO.getStatus() == MCS_BATCH_STATUS_ONGOING) {
            QueryWrapper<BatchRecordDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("batch_id", batchOrderDO.getId());
            queryWrapper.isNull("pre_record_id");
            List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
            batchRecordRecovery(batchRecordDOList.get(0));
        }
    }

    private void checkAndUpdateBatchOrder(BatchOrderDO batchOrderDO) {
        batchOrderDO.setSubmitStatus(MCS_BATCH_SUBMIT_STATUS_SUBMIT);
        batchOrderMapper.updateById(batchOrderDO);
        //校验信息完整性
        List<BatchRecordDO> batchRecordDOS = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
        LocalDateTime beginTime = null;
        LocalDateTime endTime = null;
        LocalDateTime realEndTime = null;
        //
        boolean recordFinish = true;
        for (BatchRecordDO batchRecordDO : batchRecordDOS) {
            //已完成的任务 不允许更改 记录时间跳过
            if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED) {
                if (realEndTime == null || (batchRecordDO.getEndTime() != null && batchRecordDO.getEndTime().isAfter(realEndTime))) {
                    realEndTime = batchRecordDO.getEndTime();
                }
                continue;
            }
            if (StringUtil.isBlank(batchRecordDO.getProcessingUnitId())) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":生产单元/工位为空");
            }
            if (StringUtil.isBlank(batchRecordDO.getDeviceId())) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":设备/工位为空");
            }
            if (batchRecordDO.getPlanStartTime() == null) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":计划开始时间为空");
            }
            if (batchRecordDO.getPlanEndTime() == null) {
                throw new ServiceException(5007, batchRecordDO.getNumber() + ":计划结束时间为空");
            }
            if (beginTime == null || batchRecordDO.getPlanStartTime().isBefore(beginTime)) {
                beginTime = batchRecordDO.getPlanStartTime();
            }
            if (endTime == null || batchRecordDO.getPlanEndTime().isAfter(endTime)) {
                endTime = batchRecordDO.getPlanEndTime();
            }
            batchRecordStepService.updateByRecord(batchRecordDO);
            int status = batchRecordDO.getStatus();
            if (status == MCS_BATCH_RECORD_STATUS_NEW || status == MCS_BATCH_RECORD_STATUS_ISSUED || status == MCS_BATCH_RECORD_STATUS_ONGOING) {
                recordFinish = false;
            }
        }
        //更新 时间 状态
        batchOrderDO.setPlanStartTime(beginTime);
        batchOrderDO.setPlanEndTime(endTime);
        if (recordFinish) {
            batchOrderDO.setStatus(MCS_BATCH_STATUS_COMPLETED);
            if (realEndTime != null) {
                batchOrderDO.setEndTime(realEndTime);
            }
        }
        batchOrderMapper.updateById(batchOrderDO);
        List<BatchOrderDO> batchOrderDOS = batchOrderMapper.selectList(BatchOrderDO::getPreBatchId, batchOrderDO.getId());
        for (BatchOrderDO batchOrder : batchOrderDOS) {
            checkAndUpdateBatchOrder(batchOrder);
        }
    }

    //递归查找可恢复的 批次任务
    private void batchRecordRecovery(BatchRecordDO batchRecord) {
        if (batchRecord.getStatus() == MCS_BATCH_RECORD_STATUS_NEW) {
            //校验前置工序是否完成
            String preRecordId = batchRecord.getPreRecordId();
            if (preRecordId != null) {
                BatchRecordDO preRecord = batchRecordMapper.selectById(preRecordId);
                if (preRecord.getStatus() != MCS_BATCH_RECORD_STATUS_COMPLETED) return;
                if (preRecord.getInspect() != 0) return;
            }
            recordDistribution(Collections.singletonList(batchRecord));
        }
    }

    /**
     * 下发后续任务
     */
    @Override
    public void nextRecordDistribution(BatchRecordDO finishRecord) {
        //通过任务查下一步工序选定的设备类型
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getPreRecordId, finishRecord.getId());
        if (batchRecordDOList.size() == 0) return;
        BatchRecordDO nextBatchRecord = batchRecordDOList.get(0);
        if (nextBatchRecord.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED || nextBatchRecord.getStatus() == MCS_BATCH_RECORD_STATUS_RESCINDED)
            throw new ServiceException(5004, "下一任务状态异常" + nextBatchRecord.getId());
        recordDistribution(Collections.singletonList(nextBatchRecord));
    }

    private void recordDistribution(List<BatchRecordDO> nextRecordList) {
        for (BatchRecordDO recordDO : nextRecordList) {
            BatchOrderDO distributeBatch = batchOrderMapper.selectById(recordDO.getBatchId());
            if (recordDO.getStatus() == MCS_BATCH_RECORD_STATUS_NEW) {
                recordDO.setStatus(MCS_BATCH_RECORD_STATUS_ISSUED);
                recordDO.setBarCode(distributeBatch.getBarCode());
                batchRecordMapper.updateById(recordDO);
                if (distributeBatch.getStatus() == MCS_BATCH_STATUS_CAN_BE_ISSUED || distributeBatch.getStatus() == MCS_BATCH_STATUS_NEW) {
                    distributeBatch.setStatus(MCS_BATCH_STATUS_ISSUED);
                    batchOrderMapper.updateById(distributeBatch);
                }
            }
        }
    }
}
