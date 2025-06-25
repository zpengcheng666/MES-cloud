package com.miyu.cloud.mcs.service.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordPageReqVO;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordRespVO;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordSaveReqVO;
import com.miyu.cloud.mcs.controller.admin.statistics.vo.ProcessingRecordVO;
import com.miyu.cloud.mcs.controller.admin.statistics.vo.StatisticQueryReqVO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.productionrecords.ProductionRecordsMapper;
import com.miyu.cloud.mcs.dal.mysql.receiptrecord.ReceiptRecordMapper;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.RECEIPT_RECORD_NOT_EXISTS;

/**
 * 生产单元签收记录 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private ProductionRecordsMapper productionRecordsMapper;
    @Resource
    private TechnologyRestService technologyRestService;


    @Override
    public Map<String, Long> getWorkerActualHoursList(StatisticQueryReqVO params) {
        Set<String> userIsSet = getOperatorList(params);
        LambdaQueryWrapper<ProductionRecordsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductionRecordsDO::getOperationBy, userIsSet);
        queryWrapper.gt(ProductionRecordsDO::getOperationTime, params.getBeginTime());
        queryWrapper.lt(ProductionRecordsDO::getOperationTime, params.getEndTime());
        queryWrapper.orderByAsc(ProductionRecordsDO::getOperationTime);
        List<ProductionRecordsDO> list = productionRecordsMapper.selectList(queryWrapper);
        Map<String, LocalDateTime> userNowTime = new HashMap<>();
        Map<String, Long> userActualHours = new HashMap<>();
        Map<String, String> userNowPlan = new HashMap<>();
        for (ProductionRecordsDO recordsDO : list) {
            if (recordsDO.getStepId() == null && !recordsDO.getNoStep()) continue;
            String operationBy = recordsDO.getOperationBy();
            String recordsId = recordsDO.getId();
            int type = recordsDO.getOperationType();
            LocalDateTime operationTime = recordsDO.getOperationTime();
            if (userNowTime.get(operationBy) == null) {
                if (type == MCS_STEP_PLAN_EVENT_TYPE_START || type == MCS_WORKSTATION_EVENT_TYPE_RECOVERY || type == MCS_WORKSTATION_EVENT_TYPE_CONTINUES) {
                    userNowTime.put(operationBy, operationTime);
                    userNowPlan.put(operationBy,recordsId);
                }
            } else {
                if (type == MCS_STEP_PLAN_EVENT_TYPE_FINISH || type == MCS_WORKSTATION_EVENT_TYPE_SUSPEND || type == MCS_WORKSTATION_EVENT_TYPE_INTERRUPTION) {
                    if (recordsId.equals(userNowPlan.get(operationBy))) {
                        LocalDateTime lastTime = userNowTime.get(operationBy);
                        Duration between = Duration.between(lastTime, operationTime);
                        long millis = between.toMillis();
                        Long aLong = userActualHours.get(operationBy);
                        if (aLong == null) {
                            aLong = millis;
                        } else {
                            aLong += millis;
                        }
                        userActualHours.put(operationBy, aLong);
                        userNowTime.put(operationBy, operationTime);
                    }
                    userNowTime.remove(operationBy);
                    userNowPlan.remove(operationBy);
                } else {
                    if (!recordsId.equals(userNowPlan.get(operationBy))) {
                        userNowTime.put(operationBy, operationTime);
                        userNowPlan.put(operationBy,recordsId);
                    }
                }
            }
        }
        return userActualHours;
    }

    @Override
    public Map<String, Integer> getWorkerEffectiveHoursList(StatisticQueryReqVO params) {
        Set<String> userIsSet = getOperatorList(params);
        LambdaQueryWrapper<ProductionRecordsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductionRecordsDO::getOperationBy, userIsSet);
        queryWrapper.gt(ProductionRecordsDO::getOperationTime, params.getBeginTime());
        queryWrapper.lt(ProductionRecordsDO::getOperationTime, params.getEndTime());
        queryWrapper.orderByAsc(ProductionRecordsDO::getOperationTime);
        List<ProductionRecordsDO> list = productionRecordsMapper.selectList(queryWrapper);

        Map<String, Map<String,Integer>> userTask = new HashMap<>();
        Map<String, Integer> planProgress = new HashMap<>();
        Map<String, ProductionRecordsDO> infoMap = new HashMap<>();
        for (ProductionRecordsDO recordsDO : list) {
            if (recordsDO.getStepId() == null && !recordsDO.getNoStep()) continue;
            String operationBy = recordsDO.getOperationBy();
            String stepId = recordsDO.getStepId();
            String processId = recordsDO.getProcessId();
            Integer taskProgress = recordsDO.getTaskProgress();
            String key = recordsDO.getId();
            if (stepId == null) {
                key = key + "-" + processId;
            } else {
                key = key + "-" +  stepId;
            }
            if (recordsDO.getOperationType() == MCS_WORKSTATION_EVENT_TYPE_CONTINUES) {
                planProgress.put(key, taskProgress);
                continue;
            }
            if (recordsDO.getOperationType() != MCS_STEP_PLAN_EVENT_TYPE_FINISH) continue;
            Map<String,Integer> taskMap = userTask.computeIfAbsent(operationBy, k -> new HashMap<>());
            Integer progress = taskMap.get(key);
            if (progress == null) progress = 0;
            progress += taskProgress;
            if (planProgress.containsKey(key)) {
                progress -= planProgress.get(key);
            }
            if (progress > 100) progress = 100;
            taskMap.put(key, progress);
            infoMap.put(key,recordsDO);
        }
        Map<String,ProcessPlanDetailRespDTO> technologyMap = new HashMap<>();
        Map<String, Integer> userActualHours = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> stringMapEntry : userTask.entrySet()) {
            String user = stringMapEntry.getKey();
            Map<String, Integer> map = stringMapEntry.getValue();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                int progress = entry.getValue();
                ProductionRecordsDO recordsDO = infoMap.get(key);
                if (!technologyMap.containsKey(recordsDO.getTechnologyId())) {
                    ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(recordsDO.getOrderId(), recordsDO.getTechnologyId());
                    technologyMap.put(recordsDO.getTechnologyId(), technology);
                }
                ProcessPlanDetailRespDTO technology = technologyMap.get(recordsDO.getTechnologyId());
                Optional<ProcedureRespDTO> first = technology.getProcedureList().stream().filter(item -> item.getId().equals(recordsDO.getProcessId())).findFirst();
                if (first.isPresent()) {
                    int processingTime = 0;
                    ProcedureRespDTO procedureRespDTO = first.get();
                    if (recordsDO.getStepId() == null) {
                        processingTime = procedureRespDTO.getProcessingTime() * progress / 100;
                    } else {
                        Optional<StepRespDTO> stepFirst = procedureRespDTO.getStepList().stream().filter(item -> item.getId().equals(recordsDO.getStepId())).findFirst();
                        if (stepFirst.isPresent()) {
                            StepRespDTO step = stepFirst.get();
                            processingTime = step.getProcessingTime() * progress / 100;
                        }
                    }
                    if (!userActualHours.containsKey(user)) {
                        userActualHours.put(user, processingTime);
                    } else {
                        int minute = userActualHours.get(user);
                        userActualHours.put(user, processingTime + minute);
                    }
                }
            }
        }
        return userActualHours;
    }

    @NotNull
    private Set<String> getOperatorList(StatisticQueryReqVO params) {
        LambdaQueryWrapper<ProductionRecordsDO> queryWrapperUser = new LambdaQueryWrapper<>();
        queryWrapperUser.select(ProductionRecordsDO::getOperationBy);
        queryWrapperUser.groupBy(ProductionRecordsDO::getOperationBy);
        queryWrapperUser.lt(ProductionRecordsDO::getOperationTime, params.getEndTime());
        queryWrapperUser.gt(ProductionRecordsDO::getOperationTime, params.getBeginTime());
        Page<ProductionRecordsDO> myPage = new Page<>(params.getPageNo(), params.getPageSize());
        List<ProductionRecordsDO> productionRecordsDOS = productionRecordsMapper.selectList(myPage, queryWrapperUser);
        return productionRecordsDOS.stream().map(ProductionRecordsDO::getOperationBy).collect(Collectors.toSet());
    }

    @Override
    public List<ProcessingRecordVO> getWorkerProcessingRecords(StatisticQueryReqVO params) {
        LambdaQueryWrapper<ProductionRecordsDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ProductionRecordsDO::getOperationBy, params.getWorkerId());
        queryWrapper.gt(ProductionRecordsDO::getOperationTime, params.getBeginTime());
        queryWrapper.lt(ProductionRecordsDO::getOperationTime, params.getEndTime());
        queryWrapper.orderByAsc(ProductionRecordsDO::getOperationTime);
        List<ProductionRecordsDO> list = productionRecordsMapper.selectList(queryWrapper);
        for (ProductionRecordsDO recordsDO : list) {

        }
        return null;
    }
}
