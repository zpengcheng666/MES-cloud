package com.miyu.cloud.mcs.service.batchrecord;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.*;
import com.miyu.cloud.mcs.service.batchrecordstep.BatchRecordStepService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.batchrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.BATCH_RECORD_NOT_EXISTS;

/**
 * 批次工序任务 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
@Transactional
public class BatchRecordServiceImpl implements BatchRecordService {

    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private BatchRecordStepService batchRecordStepService;

    @Resource
    private TechnologyRestService technologyRestService;

    @Override
    public String createBatchRecord(BatchRecordSaveReqVO createReqVO) {
        // 插入
        BatchRecordDO batchRecord = BeanUtils.toBean(createReqVO, BatchRecordDO.class);
        batchRecordMapper.insert(batchRecord);
        // 返回
        return batchRecord.getId();
    }

    @Override
    public void updateBatchRecord(BatchRecordSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchRecordExists(updateReqVO.getId());
        // 更新
        BatchRecordDO updateObj = BeanUtils.toBean(updateReqVO, BatchRecordDO.class);
        batchRecordMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchRecord(String id) {
        // 校验存在
        validateBatchRecordExists(id);
        // 删除
        batchRecordMapper.deleteById(id);
    }

    private void validateBatchRecordExists(String id) {
        if (batchRecordMapper.selectById(id) == null) {
            throw exception(BATCH_RECORD_NOT_EXISTS);
        }
    }

    @Override
    public BatchRecordDO getBatchRecord(String id) {
        return batchRecordMapper.selectById(id);
    }

    @Override
    public PageResult<BatchRecordDO> getBatchRecordPage(BatchRecordPageReqVO pageReqVO) {
        return batchRecordMapper.selectPage(pageReqVO);
    }

    //生成新批次任务
    @Override
    public void createBatchRecordByBatch(BatchOrderDO batchOrderDO) {
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(batchOrderDO.getTechnologyId(),batchOrderDO.getTechnologyId());
        String beginProcessId = batchOrderDO.getBeginProcessId();
        List<BatchRecordDO> alreadyExistsRecords = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
        Map<String,String> alreadyExistsRecordIdMap = alreadyExistsRecords.stream().collect(Collectors.toMap(BatchRecordDO::getProcessId, BatchRecordDO::getId, (a,b)->b));
        boolean beginFlag = beginProcessId == null;
        List<ProcedureRespDTO> procedureList = technology.getProcedureList();
        String preId = null;
        BatchRecordDO firstBatchRecord = null;
        for (ProcedureRespDTO procedure : procedureList) {
            String procedureNum = procedure.getProcedureNum();
            String processId = procedure.getId();
            if (!beginFlag) {
                if (beginProcessId.equals(processId)) beginFlag = true;
                else continue;
            }
            if (ProcedureRespDTO.isIgnoreProcedure(procedure)) continue;
            //已存在 跳过生成
            if (alreadyExistsRecordIdMap.containsKey(processId)) {
                preId = alreadyExistsRecordIdMap.get(processId);
                continue;
            }
            //生成
            List<ProcedureDetailRespDTO> resourceList = procedure.getResourceList();
            List<String> unitTypeIdList = new ArrayList<>();
            for (ProcedureDetailRespDTO resource : resourceList) {
                int resourcesType = resource.getResourcesType();
                if (PROCESS_RESOURCES_TYPE_DEVICE == resourcesType) {
                    unitTypeIdList.add(resource.getResourcesTypeId());
                }
            }
            String unitTypeIds = String.join(",", unitTypeIdList);

            BatchRecordDO batchRecordDO = new BatchRecordDO();
            batchRecordDO.setOrderId(batchOrderDO.getOrderId());
            batchRecordDO.setBatchId(batchOrderDO.getId());
            batchRecordDO.setProcessId(processId);
            batchRecordDO.setNumber(batchOrderDO.getBatchNumber() + "_" + procedureNum);
            batchRecordDO.setUnitTypeIds(unitTypeIds);
            batchRecordDO.setProcedureNum(procedureNum);
            batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_NEW);
            batchRecordDO.setCount(batchOrderDO.getCount());
            batchRecordDO.setIsBatch(batchOrderDO.getIsBatch());
            batchRecordDO.setPreRecordId(preId);
            batchRecordDO.setBarCode(preId);
            batchRecordDO.setInspect(procedure.getInspectStatus());
            batchRecordDO.setNeedDelivery(MCS_DETAIL_NEED_DELIVERY_STATUS_MOVE);
            batchRecordDO.setProcesStatus(procedure.getIsOut() == 1 ? MCS_PROCES_STATUS_OUTSOURCING : MCS_PROCES_STATUS_CURRENT);
            batchRecordDO.setAidMill(batchOrderDO.getAidMill());
            batchRecordMapper.insert(batchRecordDO);
            if (firstBatchRecord == null) firstBatchRecord = batchRecordDO;
            preId = batchRecordDO.getId();
            batchRecordStepService.createBatchDetailStepByRecord(batchRecordDO, procedure.getStepList());
        }
    }

    @Override
    public void deleteByIdPhy(String id){
        batchRecordMapper.deleteByIdPhy(id);
    }

    @Override
    public void deleteBatchIdsPhy(Collection<String> deleteIds) {
        if (deleteIds.size() == 0) return;
        QueryWrapper<BatchRecordDO> wrapper = new QueryWrapper<>();
        wrapper.in("id", deleteIds);
        batchRecordMapper.deleteBatchIdsPhy(wrapper);
        batchRecordStepService.deleteByRecordIdsPhy(deleteIds);
    }

    //删除当前及后续批次任务 (物理删除,已下发逻辑删除)
    @Override
    public void deleteBatchRecordSelfAndAfter(BatchRecordDO batchRecordDO) {
        Set<String> deleteIds = new HashSet<>();
        getNextRecordIdsToAggregate(batchRecordDO, deleteIds);
        deleteBatchIdsPhy(deleteIds);
    }

    private void getNextRecordIdsToAggregate(BatchRecordDO batchRecord, Collection<String> deleteIds) {
        if (batchRecord.getStatus() == MCS_BATCH_RECORD_STATUS_NEW) {
            //新增 物理删除
            deleteIds.add(batchRecord.getId());
        } else {
            //下发后 逻辑删除
            batchRecordMapper.deleteById(batchRecord);
        }
        //逻辑删除 已生成的详情
        List<BatchRecordDO> nextRecord = batchRecordMapper.selectList(BatchRecordDO:: getPreRecordId, batchRecord.getId());
        for (BatchRecordDO batchRecordDO : nextRecord) {
            getNextRecordIdsToAggregate(batchRecordDO, deleteIds);
        }
    }

    @Override
    public List<BatchRecordDO> getListByBatchId(String batchId) {
        return batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchId);
    }

    @Override
    public List<BatchRecordDO> list(Wrapper<BatchRecordDO> wrapper) {
        return batchRecordMapper.selectList(wrapper);
    }

    /**
     * 手动(首任务)下发批次详情任务  生成详情
     */
    @Override
    public void batchRecordDistribution(String batchRecordId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecordId);
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        //状态更改
        batchRecordDO.setStatus(MCS_BATCH_RECORD_STATUS_ISSUED);
        batchRecordDO.setBarCode(batchOrderDO.getBarCode());
        batchRecordMapper.updateById(batchRecordDO);
    }

    @Override
    public List<BatchRecordRespVO> getBatchRecordByUnitForDelivery(String unitId) {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(unitId);
        if (lineStationGroupDO != null) {
            queryWrapper.eq(BatchRecordDO::getProcessingUnitId, unitId);
        } else {
            queryWrapper.like(BatchRecordDO::getDeviceId, unitId);
        }
        queryWrapper.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
        List<BatchRecordRespVO> recordRespVOList = BeanUtils.toBean(batchRecordDOList, BatchRecordRespVO.class);
        Set<String> orderIds = batchRecordDOList.stream().map(BatchRecordDO::getOrderId).collect(Collectors.toSet());
        if (orderIds.size() > 0) {
            List<OrderFormDO> orderFormDOS = orderFormMapper.selectBatchIds(orderIds);
            Map<String, String> map = orderFormDOS.stream().collect(Collectors.toMap(OrderFormDO::getId, OrderFormDO::getPartNumber, (a, b) -> b));
            for (BatchRecordRespVO batchRecordRespVO : recordRespVOList) {
                batchRecordRespVO.setPartNumber( map.get(batchRecordRespVO.getOrderId()));
            }
        }
        return recordRespVOList;
    }

    @Override
    public List<ProcedureRespDTO> getBeforeProcessListByRecordId(String recordId) {
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(recordId);
        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(batchRecordDO.getBatchId());
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(batchOrderDO.getOrderId(),batchOrderDO.getTechnologyId());
        String processId = batchRecordDO.getProcessId();
        List<ProcedureRespDTO> procedureList= technology.getProcedureList();
        String procedureNum = procedureList.stream().filter(item -> processId.equals(item.getId())).findFirst().get().getProcedureNum();
        int number = Integer.parseInt(procedureNum);
        return procedureList.stream().filter(item -> number >= Integer.parseInt(item.getProcedureNum())).collect(Collectors.toList());
    }

    private BatchRecordDO getLastBatchRecordInBatch(BatchOrderDO batchOrderDO) {
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
        if (batchRecordDOList.size() == 0) throw new ServiceException(5004, "任务异常,当前批次下无任务" + batchOrderDO.getId());
        BatchRecordDO batchRecordDO = batchRecordDOList.get(0);
        Map<String, BatchRecordDO> map = batchRecordDOList.stream().filter(item -> item.getPreRecordId() != null).collect(Collectors.toMap(BatchRecordDO::getPreRecordId, item -> item, (a, b) -> b));
        while (true) {
            if (map.containsKey(batchRecordDO.getId())) {
                batchRecordDO = map.get(batchRecordDO.getId());
            } else {
                return batchRecordDO;
            }
        }
    }

    @Override
    public BatchRecordDO getFirstRecordByBatchId(String id) {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getBatchId, id);
        queryWrapper.isNull(BatchRecordDO::getPreRecordId);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
        if (batchRecordDOList.size() == 0) {
            throw new ServiceException(5004, "当前批次内容为空");
        } else if (batchRecordDOList.size() > 1) {
            throw new ServiceException(5005, "起始工序任务数量异常");
        }
        return batchRecordDOList.get(0);
    }

    @Override
    public List<BatchRecordDO> listByIds(Collection<String> batchRecordIds) {
        return batchRecordMapper.selectBatchIds(batchRecordIds);
    }

    @Override
    public void bindMaterial(BatchOrderDO batchOrderDO) {
        String barCode = batchOrderDO.getBarCode();
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
        queryWrapper.isNull(BatchRecordDO::getPreRecordId);
        List<BatchRecordDO> recordDOS = batchRecordMapper.selectList(queryWrapper);
        BatchRecordDO batchRecordDO = recordDOS.get(0);
        batchRecordDO.setBarCode(barCode);
        batchRecordMapper.updateById(batchRecordDO);
    }

    @Override
    public Collection<BatchRecordDeviceTypeReqVO> getDeviceByOrderId(List<String> orderIdList) {
        List<OrderFormDO> orderFormDOList = orderFormMapper.selectBatchIds(orderIdList);
        Set<String> typeIdList = new HashSet<>();
        List<LedgerDO> ledgerList = new ArrayList<>();
        Set<String> technologyIdSet = new HashSet<>();
        for (OrderFormDO orderFormDO : orderFormDOList) {
            String technologyId = orderFormDO.getTechnologyId();
            if (technologyIdSet.contains(technologyId)) continue;
            technologyIdSet.add(technologyId);
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), technologyId);
            for (ProcedureRespDTO process : technology.getProcedureList()) {
                if (ProcedureRespDTO.isIgnoreProcedure(process)) continue;
                if (process.getStepList() == null || process.getStepList().size() == 0) {
                    for (ProcedureDetailRespDTO detailRespDTO : process.getResourceList()) {
                        if (detailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) {
                            String typeId = detailRespDTO.getResourcesTypeId();
                            if (typeIdList.contains(typeId)) continue;
                            typeIdList.add(typeId);
                            ledgerList.addAll(ledgerMapper.selectList(LedgerDO::getEquipmentStationType, typeId));
                        }
                    }
                } else {
                    Set<String> lineSet = new HashSet<>();
                    String lineTypeId = null;
                    for (ProcedureDetailRespDTO detailRespDTO : process.getResourceList()) {
                        if (detailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) {
                            lineTypeId = detailRespDTO.getResourcesTypeId();
                            List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList(LineStationGroupDO::getAffiliationDeviceType, lineTypeId);
                            for (LineStationGroupDO lineStationGroupDO : lineStationGroupDOS) {
                                lineSet.add(lineStationGroupDO.getId());
                            }
                        }
                    }
                    if (lineSet.size() == 0) continue;
                    for (StepRespDTO stepRespDTO : process.getStepList()) {
                        for (StepDetailRespDTO detailRespDTO : stepRespDTO.getResourceList()) {
                            if (detailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) {
                                String typeId = detailRespDTO.getResourcesTypeId();
                                if (typeIdList.contains(typeId)) continue;
                                if (typeIdList.contains(typeId + lineTypeId)) continue;
                                typeIdList.add(typeId + lineTypeId);
                                LambdaQueryWrapper<LedgerDO> queryWrapper = new LambdaQueryWrapper<>();
                                queryWrapper.in(LedgerDO::getLintStationGroup, lineSet);
                                queryWrapper.eq(LedgerDO::getEquipmentStationType, typeId);
                                ledgerList.addAll(ledgerMapper.selectList(queryWrapper));
                            }
                        }
                    }
                }
            }
        }
        Map<String,BatchRecordDeviceTypeReqVO> typeMap = new HashMap<>();
        for (LedgerDO ledgerDO : ledgerList) {
            String parentId = ledgerDO.getLintStationGroup() != null ? ledgerDO.getLintStationGroup() : ledgerDO.getEquipmentStationType();
            BatchRecordDeviceTypeReqVO unit;
            if (typeMap.containsKey(parentId)) {
                unit = typeMap.get(parentId);
            } else {
                if (ledgerDO.getLintStationGroup() != null) {
                    unit = BeanUtils.toBean(lineStationGroupMapper.selectById(ledgerDO.getLintStationGroup()), BatchRecordDeviceTypeReqVO.class);
                } else {
                    unit = BeanUtils.toBean(deviceTypeMapper.selectById(ledgerDO.getEquipmentStationType()), BatchRecordDeviceTypeReqVO.class);
                }
                if (unit == null) continue;
                typeMap.put(parentId, unit);
            }
            unit.getChildren().add(ledgerDO);
        }
        return typeMap.values();
    }

    @Override
    public BatchRecordDO getBatchRecordByNumber(String number) {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getNumber, number);
        queryWrapper.orderByDesc(BatchRecordDO::getId);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
        return batchRecordDOList.size() > 0 ? batchRecordDOList.get(0) : null;
    }
}
