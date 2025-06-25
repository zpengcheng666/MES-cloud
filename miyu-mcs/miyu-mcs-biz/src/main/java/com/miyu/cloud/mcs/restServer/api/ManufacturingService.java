package com.miyu.cloud.mcs.restServer.api;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerToUserDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerToUserMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.controller.admin.orderform.vo.OrderFormSaveReqVO;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchdetailcutting.BatchDetailCuttingDO;
import com.miyu.cloud.mcs.dal.dataobject.batchdetailnc.BatchDetailNcDO;
import com.miyu.cloud.mcs.dal.dataobject.batchdetailtool.BatchDetailToolDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;
import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchdetailcutting.BatchDetailCuttingMapper;
import com.miyu.cloud.mcs.dal.mysql.batchdetailnc.BatchDetailNcMapper;
import com.miyu.cloud.mcs.dal.mysql.batchdetailtool.BatchDetailToolMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecordstep.BatchRecordStepMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionapplication.DistributionApplicationMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.dal.mysql.productionrecords.ProductionRecordsMapper;
import com.miyu.cloud.mcs.dal.mysql.receiptrecord.ReceiptRecordMapper;
import com.miyu.cloud.mcs.dto.distribution.McsDistributionLocationDTO;
import com.miyu.cloud.mcs.dto.manufacture.*;
import com.miyu.cloud.mcs.dto.orderForm.McsOrderFormCreateDTO;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.cloud.mcs.dto.productionProcess.*;
import com.miyu.cloud.mcs.dto.resource.McsMaterialDeliveryDTO;
import com.miyu.cloud.mcs.restServer.service.encoding.EncodingService;
import com.miyu.cloud.mcs.restServer.service.inspection.InspectionSheetService;
import com.miyu.cloud.mcs.restServer.service.order.OrderRestService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.cloud.mcs.service.batchorder.BatchOrderService;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import com.miyu.cloud.mcs.service.orderform.OrderFormService;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.wms.api.mateiral.dto.CarryTrayStatusDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.api.order.dto.ProductionOrderReqDTO;
import com.miyu.module.wms.api.order.dto.ProductionOrderRespDTO;
import jodd.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.module.wms.enums.DictConstants.*;

@Service
@Transactional
public class ManufacturingService {

    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private TechnologyRestService technologyRestService;
    @Resource
    private BatchOrderService batchOrderService;
    @Resource
    private InspectionSheetService inspectionSheetService;
    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private OrderFormService orderFormService;
    @Resource
    private OrderRestService orderRestService;
    @Resource
    private EncodingService encodingService;

    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private DistributionApplicationMapper distributionApplicationMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;
    @Resource
    private ReceiptRecordMapper receiptRecordMapper;
    @Resource
    private BatchDemandRecordMapper batchDemandRecordMapper;
    @Resource
    private ProductionRecordsMapper productionRecordsMapper;
    @Resource
    private BatchDetailCuttingMapper batchDetailCuttingMapper;
    @Resource
    private BatchDetailToolMapper batchDetailToolMapper;
    @Resource
    private BatchDetailNcMapper batchDetailNcMapper;
    @Resource
    private LedgerToUserMapper ledgerToUserMapper;
    @Resource
    private BatchRecordStepMapper batchRecordStepMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    public CommonResult<?> orderFormCreate(McsOrderFormCreateDTO orderFormCreate) {
        String orderFormId = orderFormService.createOrderForm(BeanUtils.toBean(orderFormCreate, OrderFormSaveReqVO.class));
        return success(orderFormId);
    }

    public CommonResult<?> orderFormCancel(String orderNumber) {
        List<OrderFormDO> orderFormDOList = orderFormMapper.selectList(OrderFormDO::getOrderNumber, orderNumber);
        if (orderFormDOList.size() == 1) {
            OrderFormDO orderFormDO = orderFormDOList.get(0);
            if (orderFormDO.getIssued()) {
                orderFormService.orderCancel(orderFormDO.getId());
            } else {
                orderFormService.orderDelete(orderFormDO.getId());
            }
            return success("操作成功");
        } else {
            if (orderFormDOList.size() == 0) {
                return success("未找到子订单");
            } else {
                return CommonResult.error(5005, "订单编码重复");
            }
        }
    }

    public CommonResult<?> batchRecordStart(McsBatchRecordEventDTO batchRecord) {
        try {
            if (batchRecord.getDeviceUnitId() == null) {
                LedgerDO ledgerByIp = ledgerService.getLedgerByIp(batchRecord.getIp());
                batchRecord.setDeviceUnitId(ledgerByIp.getId());
            }
            batchRecordStart1(batchRecord);
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
        return CommonResult.success("开工事件接收成功");
    }

    //批次任务 开工
    private void batchRecordStart1(McsBatchRecordEventDTO batchRecord) {
        LocalDateTime now = batchRecord.getOperatingTime();
        String batchRecordId = batchRecord.getBatchRecordId();
        String barCode = batchRecord.getBarCode();
        String deviceUnitId = batchRecord.getDeviceUnitId();
        String operatorId = batchRecord.getOperatorId();
        String stepId = batchRecord.getStepId();
        int progress = batchRecord.getProgress() == null ? 0 : batchRecord.getProgress();
        if (StringUtil.isBlank(batchRecordId)) throw new ServiceException(5004, "批次任务Id为空");
        if (StringUtil.isBlank(barCode)) throw new ServiceException(5004, "物料条码编号为空");
        if (StringUtil.isBlank(deviceUnitId)) throw new ServiceException(5010,"设备为空未知");
        if (StringUtil.isBlank(operatorId)) throw new ServiceException(5010,"操作者为空");
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecord.getBatchRecordId());
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchOrderDO.getOrderId());
        LedgerDO ledgerDO = ledgerMapper.selectById(deviceUnitId);
        if (ledgerDO == null) throw new ServiceException(5010,"未找到设备:"+ deviceUnitId);
        if (batchOrderDO.getSubmitStatus() != MCS_BATCH_SUBMIT_STATUS_SUBMIT) throw new ServiceException(5016, "当前任务正在修改,不可开工!");
        if (!batchOrderDO.getBarCode().equals(barCode)) throw new ServiceException(5005,"当前零件不属于该任务:"+ barCode);
        //查找工步任务
        List<BatchRecordStepDO> stepDOList = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordId);
        BatchRecordStepDO stepPlan = null;
        if (stepId == null) {
            if (stepDOList.size() > 0) throw new ServiceException(5005,"当前任务包含工步任务,请传入工步id");
        } else {
            for (BatchRecordStepDO batchRecordStepDO : stepDOList) {
                if (stepId.equals(batchRecordStepDO.getStepId())) {
                    stepPlan = batchRecordStepDO;
                    break;
                }
            }
            if (stepPlan == null) throw new ServiceException(5004,"未找到当前工步任务");
        }
        if (stepPlan == null) {
            //无工步
            if (!batchRecordDO.getDeviceId().equals(deviceUnitId) && !batchRecordDO.getProcessingUnitId().equals(deviceUnitId)) {
                throw new ServiceException(5007, "当前任务不属于该设备");
            }
            if (!batchRecordDO.getStatus().equals(MCS_BATCH_RECORD_STATUS_ISSUED)) {
                switch (batchRecordDO.getStatus()) {
                    case MCS_BATCH_RECORD_STATUS_NEW: throw new ServiceException(5007, "当前任务未下发");
                    case MCS_BATCH_RECORD_STATUS_ONGOING: {
                        if (batchRecordDO.getCurrentOperator() != null && !batchRecordDO.getCurrentOperator().equals(operatorId)) {
                            //记录
                            this.saveProductionLog(batchRecordDO,stepPlan,batchRecord.getResourceList(), ledgerDO, batchRecordDO.getCurrentOperator(),MCS_WORKSTATION_EVENT_TYPE_INTERRUPTION,now, progress);
                            this.saveProductionLog(batchRecordDO,stepPlan,batchRecord.getResourceList(), ledgerDO, batchRecord.getOperatorId(),MCS_WORKSTATION_EVENT_TYPE_CONTINUES,now, progress);
                            return;
                        } else {
                            throw new ServiceException(5007, "当前任务已开始");
                        }
                    }
                    case MCS_BATCH_RECORD_STATUS_COMPLETED: throw new ServiceException(5007, "当前任务已完成");
                    case MCS_BATCH_RECORD_STATUS_RESCINDED: throw new ServiceException(5007, "当前任务已撤销");
                    default: throw new ServiceException(5007, "当前任务状态异常");
                }
            }
        } else {
            //含工步
            if (!stepPlan.getDefineDeviceId().equals(deviceUnitId)) {
                throw new ServiceException(5007, "当前任务不属于该设备");
            }
            if (!stepPlan.getStatus().equals(MCS_STEP_STATUS_NEW)) {
                switch (stepPlan.getStatus()) {
                    case MCS_STEP_STATUS_ONGOING: throw new ServiceException(5007, "当前任务已开始");
                    case MCS_STEP_STATUS_COMPLETED: throw new ServiceException(5007, "当前任务已完成");
                    case MCS_STEP_STATUS_RESCINDED: throw new ServiceException(5007, "当前任务已撤销");
                    default: throw new ServiceException(5007, "当前任务状态异常");
                }
            }
            stepPlan.setStatus(MCS_STEP_STATUS_ONGOING);
            stepPlan.setStartTime(now);
            batchRecordStepMapper.updateById(stepPlan);
            if (!batchRecordDO.getStatus().equals(MCS_BATCH_RECORD_STATUS_ISSUED) && !batchRecordDO.getStatus().equals(MCS_BATCH_RECORD_STATUS_ONGOING)) {
                throw new ServiceException(5007, "当前工序任务状态异常");
            }
        }

        //批次任务 状态
        if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_ISSUED) {
            batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_ONGOING);
            batchRecordDO.setStartTime(now);
        }
        batchRecordDO.setCurrentOperator(operatorId);
        batchRecordMapper.updateById(batchRecordDO);
        //批次订单
        if (batchOrderDO.getStatus() == MCS_BATCH_STATUS_ISSUED) {
            batchOrderDO.setStatus(MCS_BATCH_STATUS_ONGOING);
            batchOrderDO.setStartTime(now);
            batchOrderMapper.updateById(batchOrderDO);
        }
        //订单
        if (orderFormDO.getStatus() == MCS_ORDER_STATUS_NEW || orderFormDO.getStatus() == MCS_ORDER_STATUS_SUBMIT) {
            orderFormDO.setStatus(MCS_ORDER_STATUS_ONGOING);
            orderFormMapper.updateById(orderFormDO);
        }

        this.saveProductionLog(batchRecordDO,stepPlan,batchRecord.getResourceList(), ledgerDO, batchRecord.getOperatorId(),MCS_STEP_PLAN_EVENT_TYPE_START,now, progress);
    }

    public CommonResult<?> batchRecordEnd(McsBatchRecordEventDTO batchRecord) {
        try {
            if (batchRecord.getDeviceUnitId() == null) {
                if (batchRecord.getIp() == null) throw new ServiceException(5004,"设备与Ip皆为空");
                LedgerDO ledgerByIp = ledgerService.getLedgerByIp(batchRecord.getIp());
                batchRecord.setDeviceUnitId(ledgerByIp.getId());
            }
            batchRecordEnd1(batchRecord);
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
        return CommonResult.success("完工事件接收成功");
    }

    //批次任务 完工
    private void batchRecordEnd1(McsBatchRecordEventDTO batchRecord) {
        LocalDateTime now = LocalDateTime.now();
        String batchRecordId = batchRecord.getBatchRecordId();
        String barCode = batchRecord.getBarCode();
        String deviceUnitId = batchRecord.getDeviceUnitId();
        String operatorId = batchRecord.getOperatorId();
        String stepId = batchRecord.getStepId();
        int progress = batchRecord.getProgress() == null ? 100 : batchRecord.getProgress();
        if (StringUtil.isBlank(batchRecordId)) throw new ServiceException(5004, "批次任务Id为空");
        if (StringUtil.isBlank(barCode)) throw new ServiceException(5004, "物料条码编号为空");
        if (StringUtil.isBlank(deviceUnitId)) throw new ServiceException(5010,"设备为空未知");
        if (StringUtil.isBlank(operatorId)) throw new ServiceException(5010,"操作者为空");
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecord.getBatchRecordId());
        LedgerDO ledgerDO = ledgerMapper.selectById(deviceUnitId);
        if (ledgerDO == null) throw new ServiceException(5010,"未找到设备:"+ deviceUnitId);
        List<BatchRecordStepDO> stepDOList = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordId);
        BatchRecordStepDO stepPlan = null;
        if (stepId == null) {
            if (stepDOList.size() > 0) throw new ServiceException(5005,"当前任务包含工步任务,请传入工步id");
        } else {
            for (BatchRecordStepDO batchRecordStepDO : stepDOList) {
                if (stepId.equals(batchRecordStepDO.getStepId())) {
                    stepPlan = batchRecordStepDO;
                    break;
                }
            }
            if (stepPlan == null) throw new ServiceException(5004,"未找到当前工步任务");
        }
        if (progress != 100) {
            //临时停止/交班
            this.saveProductionLog(batchRecordDO, stepPlan, batchRecord.getResourceList(), ledgerDO, operatorId, MCS_WORKSTATION_EVENT_TYPE_INTERRUPTION, now, progress);
            return;
        }
        if (stepPlan == null) {
            //无工步
            if (!batchRecordDO.getDeviceId().equals(deviceUnitId) && !batchRecordDO.getProcessingUnitId().equals(deviceUnitId)) {
                throw new ServiceException(5007, "当前任务不属于该设备");
            }
            if (!batchRecordDO.getStatus().equals(MCS_BATCH_RECORD_STATUS_ONGOING)) {
                switch (batchRecordDO.getStatus()) {
                    case MCS_BATCH_RECORD_STATUS_NEW: throw new ServiceException(5007, "当前任务未下发");
                    case MCS_BATCH_RECORD_STATUS_ISSUED: throw new ServiceException(5007, "当前任务未开始");
                    case MCS_BATCH_RECORD_STATUS_COMPLETED: throw new ServiceException(5007, "当前任务已完成");
                    case MCS_BATCH_RECORD_STATUS_RESCINDED: throw new ServiceException(5007, "当前任务已撤销");
                    default: throw new ServiceException(5007, "当前任务状态异常");
                }
            }
        } else {
            //含工步
            if (!stepPlan.getDefineDeviceId().equals(deviceUnitId)) {
                throw new ServiceException(5007, "当前任务不属于该设备");
            }
            if (!stepPlan.getStatus().equals(MCS_STEP_STATUS_ONGOING)) {
                switch (stepPlan.getStatus()) {
                    case MCS_STEP_STATUS_NEW: throw new ServiceException(5007, "当前任务未开始");
                    case MCS_STEP_STATUS_COMPLETED: throw new ServiceException(5007, "当前任务已完成");
                    case MCS_STEP_STATUS_RESCINDED: throw new ServiceException(5007, "当前任务已撤销");
                    default: throw new ServiceException(5007, "当前任务状态异常");
                }
            }
            stepPlan.setStatus(MCS_STEP_STATUS_COMPLETED);
            stepPlan.setStartTime(now);
            batchRecordStepMapper.updateById(stepPlan);
            if (!batchRecordDO.getStatus().equals(MCS_BATCH_RECORD_STATUS_ONGOING)) {
                throw new ServiceException(5007, "当前工序任务状态异常");
            }
        }
        this.saveProductionLog(batchRecordDO, stepPlan, batchRecord.getResourceList(), ledgerDO, operatorId, MCS_STEP_PLAN_EVENT_TYPE_FINISH, now, progress);
        //任务完成后状态更新
        batchDetailFinishUpdate(batchRecordDO, now, false, false);
    }

    //工步任务完成后转态更新
    private void batchDetailFinishUpdate(BatchRecordDO batchRecordDO, LocalDateTime now, boolean outsourcing, boolean ignoredMaterialChange) {
        //批次任务
        List<BatchRecordStepDO> stepDOList = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        boolean recordFinish = stepDOList.stream().mapToInt(BatchRecordStepDO::getStatus).noneMatch(status -> status == MCS_STEP_STATUS_NEW || status == MCS_STEP_STATUS_ONGOING);
        //完成数量等于总数 批次任务完成
        if (recordFinish) {
            batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_COMPLETED);
            batchRecordDO.setEndTime(now);
            batchRecordMapper.updateById(batchRecordDO);
        }
        //批次
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        QueryWrapper<BatchRecordDO> queryWrapperBR = new QueryWrapper<>();
        queryWrapperBR.eq("batch_id", batchRecordDO.getBatchId());
        queryWrapperBR.in("status", MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        boolean batchFinish = batchRecordMapper.selectCount(queryWrapperBR) == 0;
        //未完成数量为 0 当前批次完成
        if (batchFinish) {
            batchOrderDO.setStatus(MCS_BATCH_STATUS_COMPLETED);
            batchOrderDO.setEndTime(now);
            batchOrderMapper.updateById(batchOrderDO);
        } else {
            if (batchOrderDO.getStatus() == MCS_BATCH_STATUS_ISSUED) {
                batchOrderDO.setStatus(MCS_BATCH_STATUS_ONGOING);
                batchOrderMapper.updateById(batchOrderDO);
            }
        }
        String nextProcessId = technologyRestService.getNextProcessIdCache(batchOrderDO.getOrderId(), batchOrderDO.getTechnologyId(), batchRecordDO.getProcessId());
        boolean partFinish = nextProcessId == null;
        //订单
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchOrderDO.getOrderId());
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getOrderId, batchOrderDO.getOrderId());
        boolean orderFinish = batchOrderDOList.stream().mapToInt(BatchOrderDO::getStatus).allMatch(status -> status == MCS_BATCH_STATUS_COMPLETED || status == MCS_BATCH_STATUS_RESCINDED);
        if (orderFinish) {
            if (orderFormDO.getStatus() != MCS_ORDER_STATUS_COMPLETED && orderFormDO.getStatus() != MCS_ORDER_STATUS_RESCINDED) {
                orderFormDO.setStatus(MCS_ORDER_STATUS_COMPLETED);
                orderFormDO.setCompletionTime(now);
                orderFormMapper.updateById(orderFormDO);
            }
        } else {
            if (orderFormDO.getStatus() == MCS_ORDER_STATUS_SUBMIT) {
                orderFormDO.setStatus(MCS_ORDER_STATUS_ONGOING);
                orderFormMapper.updateById(orderFormDO);
            }
        }
        //todo 测试临时屏蔽
        /*if (!ignoredMaterialChange) {
            //变更物料
            MaterialStockRespDTO material = warehouseRestService.getByBarCode(batchRecordDO.getBarCode());
            MaterialStockRespDTO materialStockRespDTOs = warehouseRestService.updateMaterialsByPlan(
                    material.getId(),
                    orderFormDO.getPartNumber(),
                    partFinish? null : batchRecordDO.getProcedureNum(),
                    material.getBatchNumber());
        }*/
        //下发下一步
        if (outsourcing || batchOrderDO.getSubmitStatus() == MCS_BATCH_SUBMIT_STATUS_SUBMIT) {
            if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED && batchRecordDO.getInspect() == MCS_DETAIL_INSPECT_STATUS_FINISH) {
                batchOrderService.nextRecordDistribution(batchRecordDO);
            }
        }
        //通知项目 物料变更
        //todo 测试临时屏蔽
        /*if (!ignoredMaterialChange) {
            orderProgressUpdateToPMS(batchRecordDO, orderFormDO);
        }*/
    }

    private void orderProgressUpdateToPMS(BatchRecordDO batchRecordDO, OrderFormDO orderFormDO) {
        ProcessPlanDetailRespDTO technologyByIdCache = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        List<ProcedureRespDTO> procedureList = technologyByIdCache.getProcedureList();
        boolean begin = false;
        ProcedureRespDTO process = null;
        for (ProcedureRespDTO procedureRespDTO : procedureList) {
            if (!begin) {
                begin = procedureRespDTO.getId().equals(batchRecordDO.getProcessId());
            } else {
                if (ProcedureRespDTO.isIgnoreProcedure(procedureRespDTO)) continue;
                process = procedureRespDTO;break;
            }
        }
        if (!begin) {
            throw new ServiceException(5004, "未找到工序");
        }
        if (process == null) {
            orderRestService.orderMaterialUpdate(batchRecordDO.getBarCode(), orderFormDO, "-1", PMS_MATERIAL_STATUS_COMPLETED);
        } else {
            Integer status = process.getIsOut() == 1 ? PMS_MATERIAL_STATUS_PENDING_PROCESSING : PMS_MATERIAL_STATUS_ONGOING;
            orderRestService.orderMaterialUpdate(batchRecordDO.getBarCode(), orderFormDO, process.getProcedureNum(), status);
        }
    }

    private void saveProductionLog(
            BatchRecordDO batchRecordDO,
            BatchRecordStepDO recordStepDO,
            List<McsStepResourceDTO> resourceList,
            LedgerDO device,
            String operatorId,
            Integer operationType,
            LocalDateTime now,
            Integer progress) {
        if (batchRecordDO == null) throw new ServiceException(5005, "日志参数错误,任务缺失");
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        ProcedureRespDTO process = technologyRestService.getProcessCache(orderFormDO.getId(), orderFormDO.getTechnologyId(), batchRecordDO.getProcessId());

        ProductionRecordsDO records = new ProductionRecordsDO();
        records.setOrderId(orderFormDO.getId());
        records.setBatchId(batchRecordDO.getBatchId());
        records.setBatchRecordId(batchRecordDO.getId());
        records.setTechnologyId(orderFormDO.getTechnologyId());
        records.setProcessId(process.getId());
        if (recordStepDO != null) {
            records.setProcessId(recordStepDO.getStepId());
            records.setStepName(recordStepDO.getStepName());
            records.setNoStep(false);
        } else {
            records.setNoStep(true);
        }
        records.setProcessingUnitId(device.getLintStationGroup());
        records.setEquipmentId(device.getId());
        records.setBarCode(batchRecordDO.getBarCode());
        records.setOperationType(operationType);
        records.setOperationTime(now);
        records.setOperationBy(operatorId);
        records.setTotality(batchRecordDO.getCount());
        records.setTaskProgress(progress);
        records.setOrderNumber(orderFormDO.getOrderNumber());
        records.setBatchNumber(batchOrderDO.getBatchNumber());
        records.setRecordNumber(batchRecordDO.getNumber());
        records.setProcessName(process.getProcedureName());
        if (records.getProcessingUnitId() != null) {
            LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(records.getProcessingUnitId());
            if (lineStationGroupDO != null) {
                records.setUnitName(lineStationGroupDO.getName());
            }
        }
        records.setDeviceName(device.getName());

        productionRecordsMapper.insert(records);

        if (resourceList != null && operationType == 2) {
            List<McsStepResourceDTO> tool = new ArrayList<>();
            List<McsStepResourceDTO> cutting = new ArrayList<>();
            List<McsStepResourceDTO> ncFile = new ArrayList<>();
            for (McsStepResourceDTO mcsStepResourceDTO : resourceList) {
                Integer materialType = mcsStepResourceDTO.getMaterialType();
                switch (materialType) {
                    case 3:
                    case 4:
                        tool.add(mcsStepResourceDTO);
                        break;
                    case 5:
                        cutting.add(mcsStepResourceDTO);
                        break;
                    case 6:
                        ncFile.add(mcsStepResourceDTO);
                        break;
                }
            }
            generateStepPlanTool(records, tool);
            generateStepPlanCutting(records, cutting);
            generateStepPlanNcFile(records, ncFile);
        }
    }

    private void generateStepPlanTool(ProductionRecordsDO records, List<McsStepResourceDTO> tool) {
        List<BatchDetailToolDO> addList = new ArrayList<>();
        for (McsStepResourceDTO resource : tool) {
            BatchDetailToolDO batchDetailToolDO = new BatchDetailToolDO(
                    null,records.getBatchRecordId(),records.getId(),resource.getMaterialConfigId(),records.getEquipmentId(),
                    records.getProcessId(),records.getStepName(),resource.getBarCode(),resource.getBatchNumber(),
                    resource.getMaterialNumber(),resource.getTotality());
            addList.add(batchDetailToolDO);
        }
        batchDetailToolMapper.insertBatch(addList);
    }

    private void generateStepPlanCutting(ProductionRecordsDO records, List<McsStepResourceDTO> cutting) {
        List<BatchDetailCuttingDO> addList = new ArrayList<>();
        for (McsStepResourceDTO resource : cutting) {
            BatchDetailCuttingDO batchDetailCuttingDO = new BatchDetailCuttingDO(
                    null,records.getBatchRecordId(),records.getId(),resource.getMaterialConfigId(),records.getEquipmentId(),
                    records.getProcessId(),records.getStepName(),resource.getBarCode(),resource.getBatchNumber(),
                    resource.getMaterialNumber(),resource.getTotality());
            addList.add(batchDetailCuttingDO);
        }
        batchDetailCuttingMapper.insertBatch(addList);
    }

    private void generateStepPlanNcFile(ProductionRecordsDO records, List<McsStepResourceDTO> ncFile) {
        List<BatchDetailNcDO> addList = new ArrayList<>();
        for (McsStepResourceDTO resource : ncFile) {
            BatchDetailNcDO batchDetailNcDO = new BatchDetailNcDO(
                    null,records.getBatchRecordId(),records.getId(),records.getEquipmentId(),
                    records.getProcessId(),records.getStepName(),resource.getNcName(),
                    resource.getVersion(),resource.getIndex());
            addList.add(batchDetailNcDO);
        }
        batchDetailNcMapper.insertBatch(addList);
    }

    public CommonResult<?> stepWorkstationEvent(McsWorkstationDTO workstationDTO) {
        LedgerDO device = ledgerMapper.selectById(workstationDTO.getDeviceId());
        LineStationGroupDO lineStationGroupDO = new LineStationGroupDO();
        if (device.getLintStationGroup() != null) {
            LineStationGroupDO line = lineStationGroupMapper.selectById(device.getLintStationGroup());
            if (line != null) lineStationGroupDO = line;
        }
        ProductionRecordsDO records = new ProductionRecordsDO();
        records.setEquipmentId(device.getId());
        records.setProcessingUnitId(lineStationGroupDO.getId());
        records.setDeviceName(device.getName());
        records.setUnitName(lineStationGroupDO.getName());
        int operationType = workstationDTO.getOperationType();
        records.setOperationType(operationType);
        records.setOperationTime(workstationDTO.getTime());
        records.setOperationBy(workstationDTO.getOperatorId());
        if (StringUtils.isNotBlank(workstationDTO.getBatchRecordId())) {
            BatchRecordDO batchRecordDO = batchRecordMapper.selectById(workstationDTO.getBatchRecordId());
            BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
            OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
            ProcedureRespDTO process = technologyRestService.getProcessCache(batchOrderDO.getOrderId(), batchOrderDO.getTechnologyId(), batchRecordDO.getProcessId());
            records.setOrderId(orderFormDO.getId());
            records.setBatchId(batchOrderDO.getId());
            records.setBatchRecordId(batchRecordDO.getId());
            records.setProcessId(batchRecordDO.getProcessId());
            records.setOrderNumber(orderFormDO.getOrderNumber());
            records.setBatchNumber(batchOrderDO.getBatchNumber());
            records.setProcessName(process.getProcedureName());
            records.setBarCode(batchRecordDO.getBarCode());
        }
        productionRecordsMapper.insert(records);
        String eventName = "";
        if (operationType == MCS_WORKSTATION_EVENT_TYPE_SUSPEND) eventName = "已暂停";
        if (operationType == MCS_WORKSTATION_EVENT_TYPE_RECOVERY) eventName = "已恢复";
        return CommonResult.success(eventName);
    }

    //配送校验完成
    public void deliveryCheck() {
        LambdaQueryWrapper<DistributionRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DistributionRecordDO::getStatus,MCS_DELIVERY_RECORD_STATUS_DELIVERY, MCS_DELIVERY_RECORD_STATUS_READY);
        List<DistributionRecordDO> distributionRecordDOS = distributionRecordMapper.selectList(queryWrapper);
        List<DistributionRecordDO> deliveryComplete = warehouseRestService.checkDeliveryComplete(distributionRecordDOS);
        deliveryCompleted(deliveryComplete);
    }

    //配送完成
    public void deliveryCompleted(List<DistributionRecordDO> distributionRecordDOS) {
        for (DistributionRecordDO distributionRecord : distributionRecordDOS) {
            if (distributionRecord.getStatus() == MCS_DELIVERY_RECORD_STATUS_DELIVERY || distributionRecord.getStatus() == MCS_DELIVERY_RECORD_STATUS_NEW) continue;
            distributionRecordMapper.updateById(distributionRecord);
            if (distributionRecord.getStatus() == MCS_DELIVERY_RECORD_STATUS_READY) continue;
            String applicationId = distributionRecord.getApplicationId();
            if (!StringUtil.isBlank(applicationId)) {
                //检查配送任务是否完成
                DistributionApplicationDO applicationDO = distributionApplicationMapper.selectById(applicationId);
                if (applicationDO != null) {
                    Long count = distributionRecordMapper.selectCount(new QueryWrapper<DistributionRecordDO>()
                            .eq("application_id", applicationDO.getId())
                            .in("status", MCS_DELIVERY_RECORD_STATUS_DELIVERY, MCS_DELIVERY_RECORD_STATUS_NEW));
                    if (count == 0) {
                        applicationDO.setStatus(MCS_DISTRIBUTION_APPLICATION_COMPLETED);
                        distributionApplicationMapper.updateById(applicationDO);
                    }
                }
            }
            if (distributionRecord.getStatus() == MCS_DELIVERY_RECORD_STATUS_COMPLETED) {
                //更新需求分拣结果状态
                if (!StringUtil.isBlank(distributionRecord.getDemandRecordId())) {
                    BatchDemandRecordDO batchDemandRecordDO = batchDemandRecordMapper.selectById(distributionRecord.getDemandRecordId());
                    if (batchDemandRecordDO.getStatus() == MCS_DEMAND_RECORD_STATUS_DELIVERY) {
                        batchDemandRecordDO.setStatus(MCS_DEMAND_RECORD_STATUS_COMPLETED);
                        batchDemandRecordMapper.updateById(batchDemandRecordDO);
                    }
                }
                String barCode = distributionRecord.getBarCode();
                MPJLambdaWrapperX<BatchRecordDO> queryWrapper = new MPJLambdaWrapperX<>();
                queryWrapper.eq(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED);
                queryWrapper.eq(BatchRecordDO::getProcesStatus, MCS_PROCES_STATUS_CURRENT);
                queryWrapper.eq(BatchRecordDO::getBarCode, barCode);
                queryWrapper.in(BatchRecordDO::getNeedDelivery, MCS_DETAIL_NEED_DELIVERY_STATUS_MOVE, MCS_DETAIL_NEED_DELIVERY_STATUS_OUT);
                List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
                if (batchRecordDOList.size() > 0) {
                    BatchRecordDO batchDetailDO = batchRecordDOList.get(0);
                    if (distributionRecord.getType() == (int)WMS_ORDER_TYPE_PRODUCE_MOVE || distributionRecord.getType() == (int)WMS_ORDER_TYPE_PRODUCE_OUT) {
                        //移库搬运
                        batchDetailDO.setNeedDelivery(MCS_DETAIL_NEED_DELIVERY_STATUS_NULL);
                    } else {
                        //入库搬运
                        batchDetailDO.setNeedDelivery(MCS_DETAIL_NEED_DELIVERY_STATUS_OUT);
                        //外协 目标位置为仓库
                        if (batchDetailDO.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
                            batchDetailDO.setNeedDelivery(MCS_DETAIL_NEED_DELIVERY_STATUS_NULL);
                        }
                    }
                    batchRecordMapper.updateById(batchDetailDO);
                }

                ReceiptRecordDO receiptRecord = new ReceiptRecordDO();
                receiptRecord.setApplicationId(applicationId);
                receiptRecord.setDistributionRecordId(distributionRecord.getId());
                String processingUnitId = distributionRecord.getProcessingUnitId();
                if (processingUnitId == null) {
                    receiptRecord.setProcessingUnitId(distributionRecord.getDeviceId());
                } else {
                    receiptRecord.setProcessingUnitId(processingUnitId);
                }
                receiptRecord.setDemandRecordId(distributionRecord.getDemandRecordId());
                receiptRecord.setMaterialUid(distributionRecord.getMaterialUid());
                receiptRecord.setCount(distributionRecord.getCount());
                receiptRecord.setResourceType(distributionRecord.getResourceType());
                receiptRecord.setResourceTypeCode(distributionRecord.getMaterialNumber());
                receiptRecord.setBarCode(barCode);
                receiptRecord.setBatchNumber(distributionRecord.getBatchNumber());
                receiptRecord.setBatch(distributionRecord.getBatch());
                receiptRecord.setCreator(distributionRecord.getOperatorId());
                receiptRecord.setCreateTime(distributionRecord.getTime());
                receiptRecordMapper.insert(receiptRecord);
            }
        }
    }

    //入库
    public CommonResult<?> putInStorage(String deviceId, List<McsBatchResourceDTO> resourceList) {
        List<DistributionRecordDO> list = new ArrayList<>();
        for (McsBatchResourceDTO resourceDTO : resourceList) {
            LambdaQueryWrapper<DistributionRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DistributionRecordDO::getMaterialUid, resourceDTO.getMaterialUid());
            queryWrapper.in(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_APPLIED, MCS_DELIVERY_RECORD_STATUS_DELIVERY);
            List<DistributionRecordDO> old = distributionRecordMapper.selectList(queryWrapper);
            if (old.size() > 0) continue;
            //找工件
            List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getBarCode, resourceDTO.getBarCode(), BatchOrderDO::getStatus, MCS_BATCH_STATUS_ONGOING);
            if (batchOrderDOList.size() > 0) {
                BatchOrderDO batchOrderDO = batchOrderDOList.get(0);
                //找任务
                List<BatchRecordDO> batchRecords = batchRecordMapper.selectList(new LambdaQueryWrapper<BatchRecordDO>()
                        .eq(BatchRecordDO::getBatchId, batchOrderDO.getId())
                        .like(BatchRecordDO::getDeviceId, deviceId)
                        .orderByDesc(BatchRecordDO::getId));
                if (batchRecords.size() > 0) {
                    BatchRecordDO batchRecordDO = batchRecords.get(0);
                    if (batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_COMPLETED && batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_RESCINDED) {
                        throw new ServiceException(5017, "当前零件未完成!");
                    }
                    if (batchRecordDO.getInspect() > MCS_DETAIL_INSPECT_STATUS_FINISH)
                        throw new ServiceException(5017, "当前零件未检查!");
                }
            }
            DistributionRecordDO recordDO = new DistributionRecordDO();
            recordDO.setMaterialUid(resourceDTO.getMaterialUid());
            recordDO.setNumber(encodingService.getDistributionCode());
            recordDO.setMaterialConfigId(resourceDTO.getMaterialConfigId());
            LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(deviceId);
            if (lineStationGroupDO == null) {
                LedgerDO ledgerDO = ledgerMapper.selectById(deviceId);
                if (ledgerDO == null) throw new ServiceException(5004, "未找到设备");
                recordDO.setProcessingUnitId(ledgerDO.getLintStationGroup());
                recordDO.setDeviceId(ledgerDO.getId());
            } else {
                recordDO.setProcessingUnitId(deviceId);
            }
            recordDO.setMaterialNumber(resourceDTO.getMaterialNumber());
            recordDO.setBatchNumber(resourceDTO.getBatchNumber());
            recordDO.setResourceType(WMS_MATERIAL_TYPE_WORKPIECE);
            recordDO.setBarCode(resourceDTO.getBarCode());
            recordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_DELIVERY);
            recordDO.setType(WMS_ORDER_TYPE_PRODUCE_IN);
            recordDO.setBatch(resourceDTO.getMaterialManage());
            recordDO.setCount(resourceDTO.getTotality());
            list.add(recordDO);
        }
        distributionRecordMapper.insertBatch(list);
        warehouseRestService.putInStorage(list);
        return CommonResult.success("已发起入库");
    }

    //根据工位Ip获取当前任务(工序任务)
    public CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstationIp(String workstationIp) {
        LedgerDO ledger = ledgerService.getLedgerByIp(workstationIp);
        return getBatchPlanByWorkstation(ledger.getId());
    }

    //根据工位/产线获取当前任务(工序任务)
    public CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstation(String workstationId) {
        if (StringUtils.isBlank(workstationId)) {
            throw new ServiceException(5005, "工位id为空");
        }
        List<McsBatchRecordDTO> result = new ArrayList<>();
        QueryWrapper<BatchRecordDO> queryWrapperBR = new QueryWrapper<>();
        queryWrapperBR.in("status", MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.and(qw -> qw.eq("processing_unit_id", workstationId).or().like("device_id", workstationId));
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapperBR);
        if (batchRecordDOList.size() == 0) return CommonResult.success(result);
        Set<String> batchIds = new HashSet<>();
        Set<String> orderIds = new HashSet<>();
        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            batchIds.add(batchRecordDO.getBatchId());
            orderIds.add(batchRecordDO.getOrderId());
        }
        Map<String,String> batchOrderMap = batchOrderMapper.selectBatchIds(batchIds).stream().collect(Collectors.toMap(BatchOrderDO::getId, BatchOrderDO::getTechnologyCode));
        Map<String,OrderFormDO> orderFormMap = orderFormMapper.selectBatchIds(orderIds).stream().collect(Collectors.toMap(OrderFormDO::getId, item -> item, (a,b)->b));

        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            McsBatchRecordDTO batchRecordDTO = new McsBatchRecordDTO();
            result.add(batchRecordDTO);
            String batchId = batchRecordDO.getBatchId();
            String orderId = batchRecordDO.getOrderId();
            OrderFormDO orderFormDO = orderFormMap.get(orderId);
            batchRecordDTO.setId(batchRecordDO.getId());
            batchRecordDTO.setNumber(batchRecordDO.getNumber());
            batchRecordDTO.setCount(batchRecordDO.getCount());
            batchRecordDTO.setPartNumber(orderFormDO.getPartNumber());
            batchRecordDTO.setTechnologyCode(batchOrderMap.get(batchId));
            batchRecordDTO.setTechnologyId(orderFormDO.getTechnologyId());
            batchRecordDTO.setProcessId(batchRecordDO.getProcessId());
            batchRecordDTO.setProcessNumber(batchRecordDO.getProcedureNum());
            batchRecordDTO.setStatus(batchRecordDO.getStatus());
            batchRecordDTO.setPlanStartTime(batchRecordDO.getPlanStartTime());
            batchRecordDTO.setPlanEndTime(batchRecordDO.getPlanEndTime());
            batchRecordDTO.setStartTime(batchRecordDO.getStartTime());
            batchRecordDTO.setEndTime(batchRecordDO.getEndTime());
            batchRecordDTO.setBarCode(batchRecordDO.getBarCode());

        }
        return CommonResult.success(result);
    }

    public CommonResult<List<McsOrderFormDTO>> getBatchPlanByProductionLine(String productionLine) {
        if (StringUtils.isBlank(productionLine)) {
            throw new ServiceException(5005, "产线id为空");
        }
        QueryWrapper<BatchRecordDO> queryWrapperBR = new QueryWrapper<>();
        queryWrapperBR.in("status", MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.eq("processing_unit_id", productionLine);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapperBR);
        Map<String, OrderFormDO> orderFormMap = new HashMap<>();
        Map<String, ProcessPlanDetailRespDTO> processTechnologyMap = new HashMap<>();
        Map<String, ProcessPlanDetailRespDTO> technologyMap = new HashMap<>();
        Map<String, List<BatchRecordDO>> recordMap = new HashMap<>();
        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            String orderId = batchRecordDO.getOrderId();
            if (!orderFormMap.containsKey(orderId)) {
                OrderFormDO orderFormDO = orderFormMapper.selectById(orderId);
                orderFormMap.put(orderId, orderFormDO);
            }
            OrderFormDO orderFormDO = orderFormMap.get(orderId);
            String technologyId = orderFormDO.getTechnologyId();
            String processId = batchRecordDO.getProcessId();
            if (!technologyMap.containsKey(technologyId)) {
                ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderId, technologyId);
                technologyMap.put(technologyId,technology);
            }
            ProcessPlanDetailRespDTO technology = technologyMap.get(technologyId);
            if (!processTechnologyMap.containsKey(processId)) {
                processTechnologyMap.put(processId, technology);
            }
            String order_process = orderId + "_" + processId;
            if (!recordMap.containsKey(order_process)) {
                recordMap.put(order_process, new ArrayList<>());
            }
            List<BatchRecordDO> recordDOS = recordMap.get(order_process);
            recordDOS.add(batchRecordDO);
        }
        List<McsOrderFormDTO> result = new ArrayList<>();
        Map<String,LedgerDO> deviceMap = new HashMap<>();
        for (Map.Entry<String, List<BatchRecordDO>> stringListEntry : recordMap.entrySet()) {
            String[] order_process = stringListEntry.getKey().split("_");
            String orderId = order_process[0];
            String processId = order_process[1];
            List<BatchRecordDO> list = stringListEntry.getValue();
            McsOrderFormDTO mcsOrderFormDTO = new McsOrderFormDTO();
            List<McsProcessRecordDTO> recordDTOS = new ArrayList<>();
            OrderFormDO orderFormDO = orderFormMap.get(orderId);
            List<String> barCodeList = new ArrayList<>(Arrays.asList(orderFormDO.getMaterialCode().split(",")));
            for (BatchRecordDO batchRecordDO : list) {
                List<BatchRecordStepDO> stepDOS = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
                McsProcessRecordDTO recordDTO = new McsProcessRecordDTO();
                recordDTOS.add(recordDTO);
                recordDTO.setNumber(batchRecordDO.getNumber());
                recordDTO.setBarCode(batchRecordDO.getBarCode());
                recordDTO.setPlanStartTime(batchRecordDO.getPlanStartTime());
                recordDTO.setPlanEndTime(batchRecordDO.getPlanEndTime());
                recordDTO.setDeviceIds(Arrays.asList(batchRecordDO.getDeviceId().split(",")));
                List<McsStepRecordDTO> stepRecordList = recordDTO.getStepRecordList();
                for (BatchRecordStepDO stepDO : stepDOS) {
                    McsStepRecordDTO stepRecordDTO = new McsStepRecordDTO();
                    String stepDeviceIds = stepDO.getDefineDeviceId();
                    stepRecordDTO.setDeviceId(stepDeviceIds);
                    StringBuilder deviceNumbers = new StringBuilder();
                    for (String stepDeviceId : stepDeviceIds.split(",")) {
                        if (deviceMap.containsKey(stepDeviceId)) {
                            LedgerDO ledgerDO = deviceMap.get(stepDeviceId);
                            deviceNumbers.append(ledgerDO.getCode());
                        } else {
                            LedgerDO ledgerDO = ledgerMapper.selectById(stepDeviceId);
                            deviceNumbers.append(ledgerDO.getCode());
                            deviceMap.put(stepDeviceId, ledgerDO);
                        }
                    }
                    stepRecordDTO.setDeviceNumber(deviceNumbers.toString());
                    stepRecordDTO.setStepNumber(stepDO.getStepOrder());
                    stepRecordDTO.setStepName(stepDO.getStepName());
                    stepRecordDTO.setPlanStartTime(stepDO.getPlanStartTime());
                    stepRecordDTO.setPlanEndTime(stepDO.getPlanEndTime());
                    stepRecordList.add(stepRecordDTO);
                }
            }
            ProcessPlanDetailRespDTO technology = processTechnologyMap.get(processId);
            McsPlanPartDTO mcsPlanPartDTO = BeanUtils.toBean(technology, McsPlanPartDTO.class);
            List<ProcedureRespDTO> procedureList = technology.getProcedureList();
            McsPlanProcessDTO processDTO = new McsPlanProcessDTO();
            for (ProcedureRespDTO procedureRespDTO : procedureList) {
                if (processId.equals(procedureRespDTO.getId())) {
                    processDTO = BeanUtils.toBean(procedureRespDTO, McsPlanProcessDTO.class);
                    processDTO.setPart(mcsPlanPartDTO);
                }
            }
            mcsOrderFormDTO.setPriority(orderFormDO.getPriority());
            mcsOrderFormDTO.setReceptionTime(orderFormDO.getReceptionTime());
            mcsOrderFormDTO.setDeliveryTime(orderFormDO.getDeliveryTime());
            mcsOrderFormDTO.setRecordList(recordDTOS);
            mcsOrderFormDTO.setBarCodeList(barCodeList);
            mcsOrderFormDTO.setProcessOrderNumber(orderFormDO.getOrderNumber() + "_" + processDTO.getProcedureNum());
            mcsOrderFormDTO.setToolCodeList(barCodeList);
            mcsOrderFormDTO.setProcessDTO(processDTO);
            mcsOrderFormDTO.setProcessNumber(processDTO.getProcedureNum());
            result.add(mcsOrderFormDTO);
        }
        return CommonResult.success(result);
    }

    //根据任务(批次)id 查询物料需求
    public CommonResult<List<McsBatchResourceDTO>> getBatchResourceByRecordId(String recordId, String workstationId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(recordId);
        QueryWrapper<BatchDemandRecordDO> queryWrapperBDR = new QueryWrapper<>();
        queryWrapperBDR.eq("order_id", batchRecordDO.getOrderId());
        //todo 筛选 + 检验齐全
        queryWrapperBDR.and(qw -> qw.eq("processing_unit_id", workstationId).or().like("device_id",workstationId));
        List<BatchDemandRecordDO> demandRecordDOS = batchDemandRecordMapper.selectList(queryWrapperBDR);
        List<McsBatchResourceDTO> result = BeanUtils.toBean(demandRecordDOS, McsBatchResourceDTO.class);
        return CommonResult.success(result);
    }

    //根据任务(批次)id 查询详细任务(零件)
    public CommonResult<McsBatchRecordDTO> getBatchDetailByRecordId(String recordId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(recordId);
        McsBatchRecordDTO result = getMcsBatchRecordDTO(batchRecordDO);
        return CommonResult.success(result);
    }

    @NotNull
    private McsBatchRecordDTO getMcsBatchRecordDTO(BatchRecordDO batchRecordDO) {
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
        McsBatchRecordDTO result = new McsBatchRecordDTO();
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        List<BatchRecordStepDO> batchRecordStepDOS = batchRecordStepMapper.selectList(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        List<McsStepRecordDTO> stepRecordDTOList = new ArrayList<>();
        for (BatchRecordStepDO batchRecordStepDO : batchRecordStepDOS) {
            McsStepRecordDTO stepRecordDTO = new McsStepRecordDTO();
            stepRecordDTO.setId(batchRecordStepDO.getId());
            stepRecordDTO.setBatchRecordId(batchRecordStepDO.getBatchRecordId());
            stepRecordDTO.setStepNumber(batchRecordStepDO.getStepOrder());
            stepRecordDTO.setStepId(batchRecordStepDO.getStepId());
            stepRecordDTO.setStepName(batchRecordStepDO.getStepName());
            stepRecordDTO.setDeviceId(batchRecordStepDO.getDefineDeviceId());
            stepRecordDTO.setDeviceNumber(batchRecordStepDO.getDefineDeviceNumber());
            stepRecordDTO.setPlanStartTime(batchRecordStepDO.getPlanStartTime());
            stepRecordDTO.setPlanEndTime(batchRecordStepDO.getPlanEndTime());
            stepRecordDTO.setStatus(batchRecordStepDO.getStatus());
            stepRecordDTO.setStartTime(batchRecordStepDO.getStartTime());
            stepRecordDTO.setEndTime(batchRecordStepDO.getEndTime());
            stepRecordDTOList.add(stepRecordDTO);
        }
        result.setId(batchRecordDO.getId());
        result.setNumber(batchRecordDO.getNumber());
        result.setCount(batchRecordDO.getCount());
        result.setPartNumber(orderFormDO.getPartNumber());
        result.setTechnologyCode(technology.getProcessCode());
        result.setTechnologyId(orderFormDO.getTechnologyId());
        result.setProcessId(batchRecordDO.getProcessId());
        result.setProcessNumber(batchRecordDO.getProcedureNum());
        result.setStatus(batchRecordDO.getStatus());
        result.setBarCode(batchRecordDO.getBarCode());
        result.setPlanStartTime(batchRecordDO.getPlanStartTime());
        result.setPlanEndTime(batchRecordDO.getPlanEndTime());
        result.setStartTime(batchRecordDO.getStartTime());
        result.setEndTime(batchRecordDO.getEndTime());
        result.setStepRecordList(stepRecordDTOList);
        return result;
    }

    //根据批次任务id 查询工序(包含工步)
    public CommonResult<McsPlanProcessDTO> getProcessByRecordId(String recordId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(recordId);
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(batchOrderDO.getOrderId(),batchOrderDO.getTechnologyId());
        McsPlanPartDTO mcsPlanPartDTO = BeanUtils.toBean(technology, McsPlanPartDTO.class);
        List<ProcedureRespDTO> procedureList = technology.getProcedureList();
        List<McsPlanProcessDTO> mcsPlanProcessDTOS = BeanUtils.toBean(procedureList, McsPlanProcessDTO.class);
        for (McsPlanProcessDTO mcsPlanProcessDTO : mcsPlanProcessDTOS) {
            if (batchRecordDO.getProcessId().equals(mcsPlanProcessDTO.getId())) {
                return CommonResult.success(mcsPlanProcessDTO.setPart(mcsPlanPartDTO));
            }
        }
        return CommonResult.error(5004, "未找到相应工序");
    }

    //根据工位是否暂停 暂停true
    public CommonResult<Boolean> getWorkstationPause(String workstationId) {
        Page<ProductionRecordsDO> page = new Page<>(0,1);
        QueryWrapper<ProductionRecordsDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("equipment_id", workstationId);
        queryWrapper.in("operation_type", MCS_WORKSTATION_EVENT_TYPE_SUSPEND, MCS_WORKSTATION_EVENT_TYPE_RECOVERY);
        queryWrapper.orderByDesc("id");
        List<ProductionRecordsDO> productionRecordsDOS = productionRecordsMapper.selectList(page, queryWrapper);
        if (productionRecordsDOS.size() == 0) return CommonResult.success(false);
        int type = productionRecordsDOS.get(0).getOperationType();
        return CommonResult.success(type == MCS_WORKSTATION_EVENT_TYPE_SUSPEND);
    }

    //根据项目号(批量) 查询零件加工情况
    public CommonResult<List<McsProjectOrderDTO>> getPartRecordByProjectNumber(List<String> projectNumbers) {
        List<McsProjectOrderDTO> projectOrderDTOS = new ArrayList<>();
        for (String projectNumber : projectNumbers) {
            List<OrderFormDO> orderFormDOList = orderFormMapper.selectList(OrderFormDO::getProjectNumber, projectNumber);
            if (orderFormDOList.size() == 0) continue;
            for (OrderFormDO orderFormDO : orderFormDOList) {
                McsProjectOrderDTO projectOrderDTO = new McsProjectOrderDTO();
                projectOrderDTOS.add(projectOrderDTO);
                projectOrderDTO.setId(orderFormDO.getId());
                projectOrderDTO.setOrderNumber(orderFormDO.getOrderNumber());
                projectOrderDTO.setProjectNumber(orderFormDO.getProjectNumber());
                projectOrderDTO.setPartVersionId(orderFormDO.getPartVersionId());
                projectOrderDTO.setPartNumber(orderFormDO.getPartNumber());
                projectOrderDTO.setTechnologyId(orderFormDO.getTechnologyId());
                projectOrderDTO.setOrderType(orderFormDO.getOrderType());
                projectOrderDTO.setPriority(orderFormDO.getPriority());
                projectOrderDTO.setCount(orderFormDO.getCount());
                projectOrderDTO.setReceptionTime(orderFormDO.getReceptionTime());
                projectOrderDTO.setDeliveryTime(orderFormDO.getDeliveryTime());
                projectOrderDTO.setCompletionTime(orderFormDO.getCompletionTime());
                projectOrderDTO.setResponsiblePerson(orderFormDO.getResponsiblePerson());
                projectOrderDTO.setStatus(orderFormDO.getStatus());
                projectOrderDTO.setIsBatch(orderFormDO.getIsBatch());
                projectOrderDTO.setMaterialCode(orderFormDO.getMaterialCode());
                projectOrderDTO.setProcesStatus(orderFormDO.getProcesStatus());
                projectOrderDTO.setAidMill(orderFormDO.getAidMill());
                List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getOrderId, orderFormDO.getId());
                List<McsBatchRecordDTO> partList = new ArrayList<>();
                for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                    partList.add(getMcsBatchRecordDTO(batchRecordDO));
                }
                projectOrderDTO.setPartList(partList);
            }
        }
        return CommonResult.success(projectOrderDTOS);
    }

    //根据物料id集合 查询零件当前任务
    public CommonResult<Map<String,McsBatchRecordDTO>> getCurrentPlanByMaterialIds(List<String> materialIds) {
        Map<String, McsBatchRecordDTO> result = new HashMap<>();
        List<MaterialStockRespDTO> materialList = warehouseRestService.getByIds(materialIds);
        for (MaterialStockRespDTO material : materialList) {
            LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchRecordDO::getBarCode, material.getBarCode());
            queryWrapper.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
            queryWrapper.orderByAsc(BatchRecordDO::getProcedureNum);
            List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
            for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                McsBatchRecordDTO mcsBatchRecordDTO = getMcsBatchRecordDTO(batchRecordDO);
                result.put(material.getId(), mcsBatchRecordDTO);
                break;
            }
        }
        return CommonResult.success(result);
    }

    //根据物料id 查询加工记录
    public CommonResult<Map<String, List<McsBatchRecordDTO>>> getRecordsByBarCodeList(List<String> barCodeList) {
        Map<String, List<McsBatchRecordDTO>> result = new HashMap<>();
        for (String barCode : barCodeList) {
            List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getBarCode, barCode);
            List<McsBatchRecordDTO> list = new ArrayList<>();
            for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                McsBatchRecordDTO mcsBatchRecordDTO = getMcsBatchRecordDTO(batchRecordDO);
                list.add(mcsBatchRecordDTO);
            }
            result.put(barCode, list);
        }
        return CommonResult.success(result);
    }

    /**
     * 检验单生成
     * @param recordId 批次任务id
     * @param schemeId 检验方案ID
     */
    public CommonResult<?> createInspectionSheet(String recordId, String barCode, String schemeId, String userId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(recordId);
        if (!batchRecordDO.getBarCode().equals(barCode)) {
            throw new ServiceException(5005, "任务与物料不匹配");
        }
        int inspect = batchRecordDO.getInspect();
        if (inspect == MCS_DETAIL_INSPECT_STATUS_WAITING) {
            return CommonResult.success("已生成检验单");
        } else if (inspect == MCS_DETAIL_INSPECT_STATUS_FINISH) {
            return CommonResult.success("检验已完成");
        }
        batchRecordDO.setInspect(MCS_DETAIL_INSPECT_STATUS_WAITING);
        batchRecordMapper.updateById(batchRecordDO);
        MaterialStockRespDTO material = warehouseRestService.getByBarCode(barCode);
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
        inspectionSheetService.createInspectionSheet(batchRecordDO, material, schemeId, userId, orderFormDO.getFirst());
        return CommonResult.success("操作成功");
    }

    /**
     * 接收检验结果
     * @param batchRecordId 批次零件id
     * @param qualified 结果 合格:true, 不合格:false
     */
    public CommonResult<?> setInspectionSheetResult(String batchRecordId, Boolean qualified) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecordId);
        batchRecordDO.setInspect(MCS_DETAIL_INSPECT_STATUS_FINISH);
        if (!qualified) {
            batchRecordDO.setStatus(MCS_DEMAND_RECORD_STATUS_UNQUALIFIED);
        }
        batchRecordMapper.updateById(batchRecordDO);
        if (qualified) {
            batchOrderService.nextRecordDistribution(batchRecordDO);
        }
        return CommonResult.success("操作成功");
    }

    public CommonResult<List<McsDistributionLocationDTO>> getOrderReqLocation(List<String> applicationNumbers) {
        LambdaQueryWrapper<DistributionRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DistributionRecordDO::getNumber, applicationNumbers);
        List<DistributionRecordDO> distributionRecordDOS = distributionRecordMapper.selectList(queryWrapper);
        if (distributionRecordDOS.size() == 0) {
            throw new ServiceException(5004, "未找到配送需求");
        }
        List<McsDistributionLocationDTO> result = new ArrayList<>();
        for (DistributionRecordDO distributionRecordDO : distributionRecordDOS) {
            String resourceType = distributionRecordDO.getResourceType();
            McsDistributionLocationDTO location = new McsDistributionLocationDTO();
            location.setMaterialId(distributionRecordDO.getMaterialUid());
            location.setStartLocation(distributionRecordDO.getStartLocationId());
            location.setTargetLocation(distributionRecordDO.getTargetLocationId());
            location.setStartWarehouseId(distributionRecordDO.getStartWarehouseId());
            location.setTargetWarehouseId(distributionRecordDO.getTargetWarehouseId());
            result.add(location);
        }
        return CommonResult.success(result);
    }

    public CommonResult<List<String>> getLocationByUser(String userId) {
        List<LedgerToUserDO> ledgerToUserDOS = ledgerToUserMapper.selectList(LedgerToUserDO::getUser, userId);
        if (ledgerToUserDOS.size() == 0) return CommonResult.success(null);
        Set<String> deviceIds = ledgerToUserDOS.stream().map(LedgerToUserDO::getLedger).collect(Collectors.toSet());
        if (deviceIds.size() == 0) return CommonResult.success(null);
        Set<String> collect = deviceIds.stream().map(item -> ledgerService.getToolLocationByDevice(item)).filter(Objects::nonNull).collect(Collectors.toSet());
        return CommonResult.success(new ArrayList<>(collect));
    }

    public CommonResult<?> outsourcingStart(McsOutsourcingPlanDTO outsourcingPlanDTO) {
        String orderNumber = outsourcingPlanDTO.getOrderNumber();
        String outsourcingId = outsourcingPlanDTO.getOutsourcingId();
        String aidMill = outsourcingPlanDTO.getAidMill();
        List<String> barCodeList = Arrays.asList(outsourcingPlanDTO.getMaterialCodeList().split(","));
        OrderFormDO orderFormDO = orderFormMapper.selectOne(OrderFormDO::getOrderNumber, orderNumber);
        ProcessPlanDetailRespDTO technologyByIdCache = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        List<ProcedureRespDTO> procedureList = technologyByIdCache.getProcedureList();
        Optional<ProcedureRespDTO> first = procedureList.stream().filter(item -> item.getProcedureNum().equals(outsourcingPlanDTO.getProcessNumber())).findFirst();
        if (!first.isPresent()) {
            throw new ServiceException(5004, "未找到当前工序");
        }
        ProcedureRespDTO process = first.get();
        LambdaQueryWrapper<BatchOrderDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BatchOrderDO::getOrderId, orderFormDO.getId());
        queryWrapper1.in(BatchOrderDO::getBarCode, barCodeList);
        List<BatchOrderDO> batchRecordDOList = batchOrderMapper.selectList(queryWrapper1);
        if (batchRecordDOList.size() != barCodeList.size()) {
            throw new ServiceException(5005, "条码查询,任务数量异常");
        } else {
            for (BatchOrderDO batchOrderDO : batchRecordDOList) {
                String aidMill1 = batchOrderDO.getAidMill();
                if (aidMill1 == null) {
                    batchOrderDO.setAidMill(aidMill);
                } else {
                    batchOrderDO.setAidMill(aidMill1+","+aidMill);
                }
                batchOrderDO.setOutsourcingId(outsourcingId);
                if (batchOrderDO.getStatus() == MCS_BATCH_STATUS_ISSUED || batchOrderDO.getStatus() == MCS_BATCH_STATUS_CAN_BE_ISSUED) {
                    batchOrderDO.setStatus(MCS_BATCH_STATUS_ONGOING);
                }
                batchOrderMapper.updateById(batchOrderDO);

                LambdaQueryWrapper<BatchRecordDO> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
                queryWrapper2.eq(BatchRecordDO::getBarCode, batchOrderDO.getBarCode());
                queryWrapper2.orderByAsc(BatchRecordDO::getNumber);
                List<BatchRecordDO> batchRecordDOs = batchRecordMapper.selectList(queryWrapper2);
                BatchRecordDO batchRecordDO = batchRecordDOs.get(0);
                batchRecordDO.setOutsourcingId(outsourcingId);
                batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_ONGOING);
                batchRecordDO.setProcesStatus(MCS_PROCES_STATUS_OUTSOURCING);
                batchRecordDO.setAidMill(aidMill);
                if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_ISSUED) {
                    batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_ONGOING);
                }
                batchRecordMapper.updateById(batchRecordDO);
            }
            if (orderFormDO.getStatus() == MCS_ORDER_STATUS_SUBMIT) {
                orderFormMapper.updateById(orderFormDO.setStatus(MCS_ORDER_STATUS_ONGOING));
            }
        }
        return CommonResult.success("操作成功");
    }

    public CommonResult<?> outsourcingFinish(McsOutsourcingPlanDTO outsourcingPlanDTO) {
        String outsourcingId = outsourcingPlanDTO.getOutsourcingId();
        String processNumber = outsourcingPlanDTO.getProcessNumber();
        String orderNumber = outsourcingPlanDTO.getOrderNumber();
        OrderFormDO orderFormDO = orderFormMapper.selectOne(OrderFormDO::getOrderNumber, orderNumber);
        List<String> barCodeList = Arrays.asList(outsourcingPlanDTO.getMaterialCodeList().split(","));
        String aidMill = outsourcingPlanDTO.getAidMill();
        LambdaQueryWrapper<BatchOrderDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BatchOrderDO::getOutsourcingId, outsourcingId);
        queryWrapper1.in(BatchOrderDO::getBarCode, barCodeList);
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(queryWrapper1);
        LambdaQueryWrapper<BatchRecordDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BatchRecordDO::getOutsourcingId, outsourcingId);
        queryWrapper2.in(BatchRecordDO::getBarCode, barCodeList);
        queryWrapper2.orderByAsc(BatchRecordDO::getNumber);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper2);
        String processId = batchRecordDOList.get(0).getProcessId();
        ProcessPlanDetailRespDTO technologyByIdCache = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        //已完成工序id集合
        List<String> processIdList = new ArrayList<>();
        boolean processBegin = false;
        for (ProcedureRespDTO process : technologyByIdCache.getProcedureList()) {
            if (processBegin) {
                if (ProcedureRespDTO.isIgnoreProcedure(process)) continue;
                processIdList.add(process.getId());
            } else if (processId.equals(process.getId())) {
                processBegin = true;
                processIdList.add(process.getId());
            }
            if (processNumber.equals(process.getProcedureNum())) break;
        }
        if (processIdList.size() == 0) {
            throw new ServiceException(5004, "未找到完成工序");
        }
        for (BatchOrderDO batchOrderDO : batchOrderDOList) {
            LambdaQueryWrapper<BatchRecordDO> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
            queryWrapper3.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
            Long count = batchRecordMapper.selectCount(queryWrapper3);
            if (count > 0) {
                if (batchOrderDO.getStatus() == MCS_BATCH_STATUS_ISSUED || batchOrderDO.getStatus() == MCS_BATCH_STATUS_CAN_BE_ISSUED) {
                    batchOrderDO.setStatus(MCS_BATCH_STATUS_ONGOING);
                }
            } else {
                batchOrderDO.setStatus(MCS_BATCH_STATUS_COMPLETED);
            }
            batchOrderMapper.selectById(batchOrderDO);
            LambdaQueryWrapper<BatchRecordDO> queryWrapper4 = new LambdaQueryWrapper<>();
            queryWrapper4.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
            queryWrapper4.in(BatchRecordDO::getProcessId, processIdList);
            queryWrapper4.orderByAsc(BatchRecordDO::getProcedureNum);
            List<BatchRecordDO> batchRecords1 = batchRecordMapper.selectList(queryWrapper4);
            for (BatchRecordDO batchRecordDO : batchRecords1) {
                batchRecordDO.setAidMill(aidMill);
                batchRecordDO.setOutsourcingId(outsourcingId);
                batchRecordDO.setProcesStatus(MCS_PROCES_STATUS_OUTSOURCING);
                batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_COMPLETED);
                batchRecordMapper.updateById(batchRecordDO);
                this.batchDetailFinishUpdate(batchRecordDO, LocalDateTime.now(), true, true);
            }
            orderProgressUpdateToPMS(batchRecords1.get(batchRecords1.size() - 1),orderFormDO);
        }
        //零件清除外协状态
        LambdaUpdateWrapper<BatchOrderDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(BatchOrderDO::getOutsourcingId, outsourcingId);
        updateWrapper.set(BatchOrderDO::getAidMill, null);
        updateWrapper.set(BatchOrderDO::getOutsourcingId, null);
        batchOrderMapper.update(updateWrapper);
        return CommonResult.success("操作成功");
    }

    public CommonResult<?> inspectionCheck() {
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_COMPLETED, BatchRecordDO::getInspect, 99);
        List<BatchRecordDO> batchDetailDOListTest = batchRecordMapper.selectList(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_COMPLETED, BatchRecordDO::getInspect, 1234);
        for (BatchRecordDO batchRecordDO : batchDetailDOListTest) {
            setInspectionSheetResult(batchRecordDO.getId(), true);
        }
        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            InspectionSheetSchemeMaterialRespDTO finishedInspection = inspectionSheetService.getFinishedInspection(batchRecordDO.getNumber());
            if (finishedInspection == null) continue;
            //专检结果
            Integer specInspectionResult = finishedInspection.getSpecInspectionResult();
            if (specInspectionResult != null)
                setInspectionSheetResult(batchRecordDO.getId(), specInspectionResult == 1);
            Integer mutualInspectionResult = finishedInspection.getMutualInspectionResult();
            if (mutualInspectionResult != null)
                setInspectionSheetResult(batchRecordDO.getId(), mutualInspectionResult == 1);
            Integer inspectionResult = finishedInspection.getInspectionResult();
            if (inspectionResult != null)
                setInspectionSheetResult(batchRecordDO.getId(), inspectionResult == 1);
        }
        return CommonResult.success("操作成功");
    }

    /**
     * 零件自动配送 任务目标位置查询
     * @return
     */
    public CommonResult<?> carryTaskCheck() {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED);
        queryWrapper.eq(BatchRecordDO::getProcesStatus, MCS_PROCES_STATUS_CURRENT);
        queryWrapper.isNotNull(BatchRecordDO::getBarCode);
        queryWrapper.in(BatchRecordDO::getNeedDelivery, MCS_DETAIL_NEED_DELIVERY_STATUS_MOVE, MCS_DETAIL_NEED_DELIVERY_STATUS_OUT);
        queryWrapper.orderByAsc(BatchRecordDO::getPlanStartTime);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);

        LambdaQueryWrapper<DistributionRecordDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_APPLIED, MCS_DELIVERY_RECORD_STATUS_DELIVERY);
        queryWrapper1.select(DistributionRecordDO::getBarCode);
        List<String> barCodeIgnoreList = distributionRecordMapper.selectObjs(queryWrapper1);

        if (batchRecordDOList.size() == 0) {
            warehouseRestService.autoProductionDispatch(new ArrayList<>());
            return CommonResult.success("操作成功");
        }
        List<ProductionOrderReqDTO> reqDTOList = new ArrayList<>();
        Map<String,BatchRecordDO> batchRecordDOMap = new HashMap<>();
        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
            String barCode = batchRecordDO.getBarCode();
            if (barCodeIgnoreList.contains(barCode)) continue;
            reqDTOList.addAll(getMaterialTargetLocationListByDetail(batchRecordDO, batchRecordDOMap));
        }
        List<ProductionOrderRespDTO> respDTOS = warehouseRestService.autoProductionDispatch(reqDTOList);
        wmsCreateDistributionRecord(respDTOS, batchRecordDOMap);
        return CommonResult.success("操作成功");
    }

    /**
     * 根据 工步 任务 查询 当前零件 下一工序目标位置
     * @param batchRecordDO 工步 任务
     * @return List<ProductionOrderReqDTO>
     */
    private List<ProductionOrderReqDTO> getMaterialTargetLocationListByDetail(BatchRecordDO batchRecordDO, Map<String,BatchRecordDO> batchRecordDOMap) {
        List<ProductionOrderReqDTO> reqList = new ArrayList<>();
        String barCode = batchRecordDO.getBarCode();
        String deviceId = batchRecordDO.getDeviceId();
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        queryWrapper.orderByAsc(BatchRecordStepDO::getStepOrder);
        List<BatchRecordStepDO> recordStepDOS = batchRecordStepMapper.selectList(queryWrapper);
        if (recordStepDOS.size() != 0) {
            deviceId = recordStepDOS.get(0).getDefineDeviceId();
        }
        boolean carryFlag = false;
        if (batchRecordDO.getProcesStatus() == MCS_PROCES_STATUS_CURRENT) {
            LedgerDO ledgerDO = ledgerMapper.selectById(deviceId);
            if (ledgerDO.getNeedMaterials() == null || !ledgerDO.getNeedMaterials()) return reqList;
            String warehouseId = ledgerService.getWarehouseByDevice(deviceId);
            if (warehouseId == null) {
                throw new ServiceException(5005, "设备仓库为空" + deviceId);
            }
            String location = ledgerService.getToolLocationByDevice(deviceId);
            if (location == null) {
                throw new ServiceException(5005, "设备签收位置为空" + deviceId);
            }
            ProductionOrderReqDTO reqDTO = new ProductionOrderReqDTO();
            reqDTO.setQuantity(batchRecordDO.getCount());
            reqDTO.setBarCode(barCode);
            reqDTO.setIsNeedMaterial(true);
            reqDTO.setTargetLocationId(location);
            reqDTO.setTargetWarehouseId(warehouseId);
            batchRecordDOMap.put(location, batchRecordDO);
            reqList.add(reqDTO);
            carryFlag = true;
        }
        if (!carryFlag && batchRecordDO.getNeedDelivery() == MCS_DETAIL_NEED_DELIVERY_STATUS_MOVE) {
            //无可移库位置 则生成入库任务
            ProductionOrderReqDTO reqDTO = new ProductionOrderReqDTO();
            reqDTO.setQuantity(batchRecordDO.getCount());
            reqDTO.setBarCode(barCode);
            reqDTO.setIsNeedMaterial(false);
            batchRecordDOMap.put(barCode, batchRecordDO);
            reqList.add(reqDTO);
        }
        return reqList;
    }

    /**
     * 根据条码查询 当前零件 当前工序目标位置
     * @param barCode 物料条码
     * @return
     */
    public CommonResult<List<ProductionOrderReqDTO>> getTargetLocationListByBarCode(String barCode) {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED);
        queryWrapper.eq(BatchRecordDO::getProcesStatus, MCS_PROCES_STATUS_CURRENT);
        queryWrapper.eq(BatchRecordDO::getBarCode, barCode);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
        if (batchRecordDOList.size() > 1) {
            throw new ServiceException(5005, "该零件绑定下发任务数量异常!"+barCode);
        } else if (batchRecordDOList.size() == 0){
            ProductionOrderReqDTO reqDTO = new ProductionOrderReqDTO();
            reqDTO.setBarCode(barCode);
            reqDTO.setIsNeedMaterial(false);
            return CommonResult.success(Collections.singletonList(reqDTO));
        } else {
            List<ProductionOrderReqDTO> list = getMaterialTargetLocationListByDetail(batchRecordDOList.get(0), new HashMap<>());
            return CommonResult.success(list);
        }
    }

    /**
     * 仓储创建生产搬运结果处理
     * @param respDTOS 返回值 成功搬运工件集合
     * @param batchRecordDOMap 工步任务map key:barCode, value:BatchDetailDO
     */
    private void wmsCreateDistributionRecord(List<ProductionOrderRespDTO> respDTOS, Map<String,BatchRecordDO> batchRecordDOMap) {
        if (respDTOS == null) return;
        for (ProductionOrderRespDTO respDTO : respDTOS) {
            String barCode = respDTO.getBarCode();
            BatchRecordDO batchRecordDO;
            if (respDTO.getTargetLocationId() == null) {
                batchRecordDO = batchRecordDOMap.get(barCode);
            } else {
                batchRecordDO = batchRecordDOMap.get(respDTO.getTargetLocationId());
            }
            if (respDTO.getOrderNumber() != null) {
                creatDistributionRecord(barCode,respDTO,batchRecordDO);
            } else {
                //无单号 则无需搬运
                batchRecordDO.setNeedDelivery(MCS_DETAIL_NEED_DELIVERY_STATUS_NULL);
                batchRecordMapper.updateById(batchRecordDO);
            }
        }
    }

    private void creatDistributionRecord(String barCode, ProductionOrderRespDTO respDTO, BatchRecordDO batchRecordDO) {
        MaterialStockRespDTO resourceDTO = warehouseRestService.getByBarCodeIgnoreTenantId(barCode);
        String deviceId = batchRecordDO.getDeviceId();
        DistributionRecordDO recordDO = new DistributionRecordDO();
        recordDO.setMaterialUid(resourceDTO.getId());
        recordDO.setBatchRecordId(batchRecordDO.getId());
        recordDO.setNumber(respDTO.getOrderNumber());
        recordDO.setMaterialConfigId(resourceDTO.getMaterialConfigId());
        LedgerDO ledgerDO = ledgerMapper.selectById(deviceId);
        recordDO.setProcessingUnitId(ledgerDO.getLintStationGroup());
        recordDO.setDeviceId(ledgerDO.getId());
        recordDO.setMaterialNumber(resourceDTO.getMaterialNumber());
        recordDO.setBatchNumber(resourceDTO.getBatchNumber());
        recordDO.setBarCode(resourceDTO.getBarCode());
        recordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_DELIVERY);
        recordDO.setType(respDTO.getOrderType());
        recordDO.setResourceType(WMS_MATERIAL_TYPE_WORKPIECE);
        recordDO.setBatch(resourceDTO.getMaterialManage());
        recordDO.setCount(resourceDTO.getTotality());
        recordDO.setTargetWarehouseId(respDTO.getTargetWarehouseId());
        recordDO.setTargetLocationId(respDTO.getTargetLocationId());
        distributionRecordMapper.insert(recordDO);
    }

    //一生多加工 编码添加
    public CommonResult<String> createNewMaterialInManufacture(McsSplitProcessingProcedure splitProcessingProcedure) {
        String batchRecordId = splitProcessingProcedure.getBatchRecordId();
        String workstationId = splitProcessingProcedure.getWorkstationId();
        String barCode = splitProcessingProcedure.getBarCode();
        String location = null;
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecordId);
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
        LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(workstationId);
        if (lineStationGroupDO == null) {
            LedgerDO ledgerDO = ledgerMapper.selectById(workstationId);
            if (ledgerDO == null) throw new ServiceException(5004, "未找到设备");
            location = ledgerService.getToolLocationByDevice(workstationId);
        } else {
            location = lineStationGroupDO.getLocation();
        }
        warehouseRestService.createNewMaterialInManufacture(barCode, location);
        orderRestService.createMaterialInOrder(orderFormDO,barCode,batchRecordDO.getProcedureNum());

        return CommonResult.success("list");
    }

    public void setMaterialCarryReadyStatusCache() {
        Map<String, CarryTrayStatusDTO> map = warehouseRestService.getMaterialCarryReadyStatusList();
        redisTemplate.opsForValue().set(MCS_CARRY_READY_STATUS_REDIS, map);
    }

    public void setCarryStatisticsCache() {
        List<OrderReqDTO> notCompleteOrder = warehouseRestService.getNotCompleteOrder();
        Map<String,List<OrderReqDTO>> orderCacheL = new HashMap<>();
        Map<String,List<OrderReqDTO>> orderCacheD = new HashMap<>();
        for (OrderReqDTO reqDTO : notCompleteOrder) {
            String targetWarehouseId = reqDTO.getTargetWarehouseId();
            if (targetWarehouseId != null){
                if (!orderCacheL.containsKey(targetWarehouseId)) {
                    orderCacheL.put(targetWarehouseId, new ArrayList<>());
                }
                orderCacheL.get(targetWarehouseId).add(reqDTO);
            }
            String signLocationId = reqDTO.getSignLocationId();
            if (signLocationId != null) {
                if (!orderCacheD.containsKey(signLocationId)) {
                    orderCacheD.put(signLocationId, new ArrayList<>());
                }
                orderCacheD.get(signLocationId).add(reqDTO);
            }
        }
        redisTemplate.opsForValue().set(MCS_DISTRIBUTION_STATUS_REDIS_LINE, orderCacheL);
        redisTemplate.opsForValue().set(MCS_DISTRIBUTION_STATUS_REDIS_DEVICE, orderCacheD);
    }

    public List<OrderReqDTO> getDistributionByDeviceId(String deviceId) {
        String locationId = ledgerService.getToolLocationByDevice(deviceId);
        Map<String, List<OrderReqDTO>> map = (Map<String, List<OrderReqDTO>>) redisTemplate.opsForValue().get(MCS_DISTRIBUTION_STATUS_REDIS_DEVICE);
        if (map == null || !map.containsKey(locationId))
            return new ArrayList<>();
        else return map.get(locationId);
    }

    public List<OrderReqDTO> getCarryByWarehouseLocation(String warehouseId) {
        Map<String, List<OrderReqDTO>> map = (Map<String, List<OrderReqDTO>>) redisTemplate.opsForValue().get(MCS_DISTRIBUTION_STATUS_REDIS_LINE);
        if (map == null || !map.containsKey(warehouseId))
            return new ArrayList<>();
        else return map.get(warehouseId);
    }

    public CommonResult<Map<String,Boolean>> getMaterialCarryReadyStatusMap(List<String> barCodeList) {
        Map<String, Boolean> map = new HashMap<>();
        if (barCodeList.size() == 0) return CommonResult.success(map);
        Map<String, CarryTrayStatusDTO> list = warehouseRestService.getMaterialCarryReadyStatusList();
        for (Map.Entry<String, CarryTrayStatusDTO> entry : list.entrySet()) {
            if (barCodeList.contains(entry.getKey())) {
                if (entry.getValue().getStatus() == 1) continue;
                map.put(entry.getKey(), entry.getValue().getStatus() == 3);
            }
        }
        return CommonResult.success(map);
    }

    public McsMaterialDeliveryDTO getCarryReadyByWarehouseLocation(String warehouseId) {
        if (StringUtils.isNotBlank(warehouseId)) {
            Map<String, CarryTrayStatusDTO> list = warehouseRestService.getMaterialCarryReadyStatusList();
            for (Map.Entry<String, CarryTrayStatusDTO> entry : list.entrySet()) {
                CarryTrayStatusDTO dto = entry.getValue();
                if (warehouseId.equals(dto.getStartWarehouseId())) {
                    McsMaterialDeliveryDTO result = new McsMaterialDeliveryDTO();
                    result.setMaterialCode(entry.getKey());
                    int status = dto.getStatus();
                    if (status == 2) {
                        result.setStatus(3);
                    } else if (status == 3) {
                        result.setStatus(4);
                    }
                    return result;
                }
            }
        }
        return null;
    }

    public void batchRecordStartBatch(List<McsBatchRecordEventDTO> eventDTOList) {
        for (McsBatchRecordEventDTO eventDTO : eventDTOList) {
            CommonResult<?> commonResult = this.batchRecordStart(eventDTO);
            if (!commonResult.isSuccess()) {
                throw new ServiceException(commonResult.getCode(), commonResult.getMsg());
            }
        }
    }

    public CommonResult<?> batchRecordEndBatch(List<McsBatchRecordEventDTO> eventDTOList) {
        for (McsBatchRecordEventDTO eventDTO : eventDTOList) {
            CommonResult<?> commonResult = this.batchRecordEnd(eventDTO);
            if (!commonResult.isSuccess()) {
                throw new ServiceException(commonResult.getCode(), commonResult.getMsg());
            }
        }
        return CommonResult.success("完工事件接收成功");
    }

    public CommonResult<?> getDeviceListByLineCode(String lineCode) {
        LineStationGroupDO line = lineStationGroupService.getOneByCode(lineCode);
        if (line == null) return CommonResult.success(null);
        List<LedgerDO> ledgerDOList = ledgerMapper.selectList(LedgerDO::getLintStationGroup, line.getId());
        return CommonResult.success(ledgerDOList);
    }

    public void cleanAbandonedCarryTask() {
        LambdaQueryWrapper<DistributionRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(DistributionRecordDO::getApplicationId);
        queryWrapper.eq(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW);
        List<DistributionRecordDO> list = distributionRecordMapper.selectList(queryWrapper);
        Set<String> recordIdSet = list.stream().map(DistributionRecordDO::getBatchRecordId).collect(Collectors.toSet());
        LambdaQueryWrapper<BatchRecordDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(BatchRecordDO::getId, recordIdSet);
        queryWrapper1.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_COMPLETED, MCS_BATCH_RECORD_STATUS_RESCINDED);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper1);
        Set<String> collect = batchRecordDOList.stream().map(BatchRecordDO::getId).collect(Collectors.toSet());
        Set<String> distributionIds = list.stream().filter(item -> collect.contains(item.getBatchRecordId())).map(DistributionRecordDO::getId).collect(Collectors.toSet());
        LambdaUpdateWrapper<DistributionRecordDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DistributionRecordDO::getId, distributionIds);
        updateWrapper.set(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_CLOSE);
        distributionRecordMapper.update(updateWrapper);
    }

    public CommonResult<String> getDeviceCodeByLocationId(String locationId) {
        return ledgerService.getDeviceCodeByLocationId(locationId);
    }
}
