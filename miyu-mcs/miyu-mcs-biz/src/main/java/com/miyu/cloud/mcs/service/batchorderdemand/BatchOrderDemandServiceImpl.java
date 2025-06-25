package com.miyu.cloud.mcs.service.batchorderdemand;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.BatchDemandResourceReqVO;
import com.miyu.cloud.mcs.controller.admin.distributionapplication.vo.DistributionApplicationEditVO;
import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.DistributionRecordRespVO;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandDTO;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandUptateDTO;
import com.miyu.cloud.mcs.restServer.service.encoding.EncodingService;
import com.miyu.cloud.mcs.restServer.service.order.OrderRestService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.*;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.batchorderdemand.BatchOrderDemandMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;
import static com.miyu.module.wms.enums.DictConstants.*;

/**
 * 批次订单需求 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class BatchOrderDemandServiceImpl implements BatchOrderDemandService {

    @Resource
    private BatchOrderDemandMapper batchOrderDemandMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private BatchDemandRecordMapper batchDemandRecordMapper;
    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;

    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private DistributionApplicationService distributionApplicationService;

    @Resource
    private EncodingService encodingService;
    @Resource
    private TechnologyRestService technologyRestService;
    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private OrderRestService orderRestService;
    @Resource
    private LedgerService ledgerService;

    @Override
    public String createBatchOrderDemand(BatchOrderDemandSaveReqVO createReqVO) {
        // 插入
        if (StringUtils.isBlank(createReqVO.getOrderId())) {
            BatchRecordDO batchRecordDO = batchRecordMapper.selectById(createReqVO.getBatchRecordId());
            createReqVO.setOrderId(batchRecordDO.getOrderId());
            createReqVO.setBatchId(batchRecordDO.getBatchId());
        }
        BatchOrderDemandDO batchOrderDemand = new BatchOrderDemandDO();
        batchOrderDemand.setOrderId(createReqVO.getOrderId());
        batchOrderDemand.setStatus(MCS_READY_STATUS_NOT_FULLY_PREPARED);
        batchOrderDemand.setRequirementType(createReqVO.getRequirementType());
        batchOrderDemand.setResourceTypeCode(createReqVO.getResourceTypeCode());
        batchOrderDemand.setResourceTypeId(createReqVO.getResourceTypeId());
        batchOrderDemand.setResourceType(createReqVO.getResourceType());
        batchOrderDemand.setTotal(createReqVO.getTotal());
        batchOrderDemandMapper.insert(batchOrderDemand);

        BatchOrderDO batchOrderDO = batchOrderMapper.selectById(createReqVO.getBatchId());

        List<BatchDemandRecordDO> recordAdd = new ArrayList<>();
        for (int i = 0; i < createReqVO.getTotal(); i++) {
            BatchDemandRecordDO demandRecord = new BatchDemandRecordDO();
            demandRecord.setOrderId(createReqVO.getOrderId());
            demandRecord.setBatchId(createReqVO.getBatchId());
            demandRecord.setBatchRecordId(createReqVO.getBatchRecordId());
            demandRecord.setProcessingUnitId(createReqVO.getProcessingUnitId());
            demandRecord.setDeviceId(createReqVO.getDeviceId());
            demandRecord.setDemandId(batchOrderDemand.getId());
            demandRecord.setBatchNumber(batchOrderDO.getBatchNumber());
            demandRecord.setTotality(batchOrderDO.getCount());
            demandRecord.setResourceType(createReqVO.getResourceType());
            demandRecord.setMaterialConfigId(createReqVO.getResourceTypeId());
            demandRecord.setMaterialNumber(createReqVO.getResourceTypeCode());
            demandRecord.setStatus(MCS_DEMAND_RECORD_STATUS_NEW);
            recordAdd.add(demandRecord);
        }
        batchDemandRecordMapper.insertBatch(recordAdd);
        // 返回
        return batchOrderDemand.getId();
    }

    @Override
    public void updateBatchOrderDemand(BatchOrderDemandSaveReqVO updateReqVO) {
        // 校验存在
        validateBatchOrderDemandExists(updateReqVO.getId());
        // 更新
        BatchOrderDemandDO updateObj = BeanUtils.toBean(updateReqVO, BatchOrderDemandDO.class);
        batchOrderDemandMapper.updateById(updateObj);
    }

    @Override
    public void deleteBatchOrderDemand(String id) {
        // 校验存在
        validateBatchOrderDemandExists(id);
        // 删除
        batchOrderDemandMapper.deleteById(id);
    }

    private void validateBatchOrderDemandExists(String id) {
        if (batchOrderDemandMapper.selectById(id) == null) {
            throw exception(BATCH_ORDER_DEMAND_NOT_EXISTS);
        }
    }

    @Override
    public BatchOrderDemandDO getBatchOrderDemand(String id) {
        return batchOrderDemandMapper.selectById(id);
    }

    @Override
    public PageResult<BatchOrderDemandDO> getBatchOrderDemandPage(BatchOrderDemandPageReqVO pageReqVO) {
        return batchOrderDemandMapper.selectPage(pageReqVO);
    }

    @Override
    public void createBatchOrderDemandByOrder(OrderFormDO orderFormDO) {
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(),orderFormDO.getTechnologyId());
        Long count = batchDemandRecordMapper.selectCount(new LambdaQueryWrapper<BatchDemandRecordDO>().eq(BatchDemandRecordDO::getOrderId, orderFormDO.getId()));
        if (count == 0) {
            // 备毛料
            createMaterialDemand(orderFormDO, technology);
            // 备其他资源(可重复使用物料)
            createResourcesDemand(orderFormDO, technology);
        }
    }

    @Override
    public void updateOrderFormDemand(List<String> orderIdList) {
        List<OrderFormDO> orderFormDOList = orderFormMapper.selectBatchIds(orderIdList);
        for (OrderFormDO orderFormDO : orderFormDOList) {
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(),orderFormDO.getTechnologyId());
            deleteOldDemand(orderFormDO);
            // 备毛料
            createMaterialDemand(orderFormDO, technology);
            // 备其他资源(可重复使用物料)
            createResourcesDemand(orderFormDO, technology);
        }
    }

    public void deleteOldDemand(OrderFormDO orderFormDO) {
        batchDemandRecordMapper.deleteBatchIdsPhy(new LambdaQueryWrapper<BatchDemandRecordDO>().eq(BatchDemandRecordDO::getOrderId, orderFormDO.getId()));
        List<BatchOrderDemandDO> batchOrderDemandDOS = batchOrderDemandMapper.selectList(new LambdaQueryWrapper<BatchOrderDemandDO>().eq(BatchOrderDemandDO::getOrderId, orderFormDO.getId()));
        for (BatchOrderDemandDO batchOrderDemandDO : batchOrderDemandDOS) {
            batchOrderDemandMapper.deleteCompletelyById(batchOrderDemandDO.getId());
        }
    }

    private void createMaterialDemand(OrderFormDO orderFormDO, ProcessPlanDetailRespDTO technology) {
        //所有批次的首个任务
        LambdaQueryWrapper<BatchOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchOrderDO::getOrderId, orderFormDO.getId());
        queryWrapper.isNull(BatchOrderDO::getPreBatchId);
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(queryWrapper);
        //需求
        String materialId = technology.getMaterialId();
        String materialCode = technology.getMaterialNumber();
        boolean isBatch = orderFormDO.getIsBatch();
        BatchOrderDemandDO base = new BatchOrderDemandDO();
        base.setOrderId(orderFormDO.getId());
        base.setStatus(MCS_READY_STATUS_NOT_FULLY_PREPARED);
        base.setRequirementType(MCS_DEMAND_TYPE_NORMAL);
        base.setResourceTypeCode(materialCode);
        base.setResourceTypeId(materialId);
        base.setResourceType(WMS_MATERIAL_TYPE_WORKPIECE);
        base.setTotal(orderFormDO.getCount());
        base.setMaterialCode(orderFormDO.getMaterialCode());
        base.setIsBatch(isBatch);
        batchOrderDemandMapper.insert(base);
        //自备物料
        boolean bringFlag = false;
        List<String> materialCodeList = new ArrayList<>();
        if (StringUtils.isNotBlank(orderFormDO.getMaterialCode())) {
            bringFlag = true;
            materialCodeList = new ArrayList<>(Arrays.asList(orderFormDO.getMaterialCode().split(",")));
        }
        //需求详情
        List<BatchDemandRecordDO> recordAdd = new ArrayList<>();
        for (BatchOrderDO batchOrderDO : batchOrderDOList) {
            BatchRecordDO batchRecordDO = batchRecordService.getFirstRecordByBatchId(batchOrderDO.getId());
            while (batchRecordDO.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
                List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getPreRecordId, batchRecordDO.getId());
                if (batchRecordDOList.size() == 0) {
                    batchRecordDO = null; break;
                } else {
                    batchRecordDO = batchRecordDOList.get(0);
                }
            }
            if (batchRecordDO == null) continue;
            BatchDemandRecordDO demandRecord = new BatchDemandRecordDO();
            demandRecord.setOrderId(batchOrderDO.getOrderId());
            demandRecord.setBatchId(batchOrderDO.getId());
            demandRecord.setBatchRecordId(batchRecordDO.getId());
            demandRecord.setProcessingUnitId(batchRecordDO.getProcessingUnitId());
            demandRecord.setDeviceId(batchRecordDO.getDeviceId());
            demandRecord.setDemandId(base.getId());
            demandRecord.setTotality(batchOrderDO.getCount());
            demandRecord.setBatch(isBatch ? 2 : 1);
            demandRecord.setResourceType(base.getResourceType());
            demandRecord.setMaterialConfigId(base.getResourceTypeId());
            demandRecord.setMaterialNumber(base.getResourceTypeCode());
            demandRecord.setStatus(MCS_DEMAND_RECORD_STATUS_NEW);
            demandRecord.setPlanStartTime(batchRecordDO.getPlanStartTime());
            if (bringFlag) {
                StringBuffer sb = new StringBuffer(materialCodeList.remove(0));
                for (int i = 1; i < batchOrderDO.getCount(); i++) {
                    sb.append(",");
                    sb.append(materialCodeList.remove(0));
                }
                demandRecord.setBarCode(sb.toString());
            }
            recordAdd.add(demandRecord);
        }
        batchDemandRecordMapper.insertBatch(recordAdd);
    }

    private void createResourcesDemand(OrderFormDO orderFormDO, ProcessPlanDetailRespDTO technology) {
        // 按资源类型id 统计所有需求
        Map<String,BatchOrderDemandOverviewVO> demandMap = createMaterialDemand1(orderFormDO, technology);
        // 根据资源需求Map 保存需求
        createMaterialDemand2(demandMap);
    }

    private Map<String,BatchOrderDemandOverviewVO> createMaterialDemand1(OrderFormDO orderFormDO, ProcessPlanDetailRespDTO technology) {
        Map<String,BatchOrderDemandOverviewVO> demandMap = new HashMap<>();
        List<ProcedureRespDTO> procedureList = technology.getProcedureList();
        for (ProcedureRespDTO process : procedureList) {
            if (ProcedureRespDTO.isIgnoreProcedure(process)) continue;
            //订单下 所有该工序的任务
            List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getOrderId, orderFormDO.getId(), BatchRecordDO::getProcessId, process.getId());
            batchRecordDOList = batchRecordDOList.stream().filter(item -> item.getProcesStatus() != MCS_PROCES_STATUS_OUTSOURCING).collect(Collectors.toList());
            //需求
            for (ProcedureDetailRespDTO resource : process.getResourceList()) {
                //工装
                if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_TOOL) {
                    String materialNumber = resource.getMaterialNumber();
                    String resourcesTypeId = resource.getResourcesTypeId();
                    BatchOrderDemandOverviewVO overview;
                    if (demandMap.containsKey(resourcesTypeId)) {
                        overview = demandMap.get(resourcesTypeId);
                    } else {
                        overview = new BatchOrderDemandOverviewVO();
                        demandMap.put(resourcesTypeId, overview);
                    }
                    overview.setResourcesType(WMS_MATERIAL_TYPE_TOOL);
                    overview.setResourcesTypeId(resourcesTypeId);
                    overview.setResourcesTypeCode(materialNumber);
                    overview.setOrderId(orderFormDO.getId());
                    for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                        overview.getBatchRecordDOList().add(batchRecordDO);
                    }
                }
            }
            for (StepRespDTO stepRespDTO : process.getStepList()) {
                List<String> deviceTypeIdList = stepRespDTO.getResourceList().stream().filter(item -> item.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE).map(StepDetailRespDTO::getResourcesTypeId).collect(Collectors.toList());
                for (StepDetailRespDTO resource : stepRespDTO.getResourceList()) {
                    if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_CUTTING) {
                        String resourcesTypeId = resource.getResourcesTypeId();
                        String cutterNum = resource.getCutternum();
                        BatchOrderDemandOverviewVO overview;
                        if (demandMap.containsKey(resourcesTypeId)) {
                            overview = demandMap.get(resourcesTypeId);
                        } else {
                            overview = new BatchOrderDemandOverviewVO();
                            demandMap.put(resourcesTypeId, overview);
                        }
                        overview.setResourcesType(WMS_MATERIAL_TYPE_CUTTING);
                        overview.setResourcesTypeId(resourcesTypeId);
                        overview.setResourcesTypeCode(cutterNum);
                        overview.setOrderId(orderFormDO.getId());
                        for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                            String deviceIds = batchRecordDO.getDeviceId();
                            LambdaQueryWrapper<LedgerDO> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper.in(LedgerDO::getEquipmentStationType, deviceTypeIdList);
                            queryWrapper.in(LedgerDO::getId, Arrays.asList(deviceIds.split(",")));
                            List<LedgerDO> ledgerDOS = ledgerMapper.selectList(queryWrapper);
                            Set<String> deviceSet = ledgerDOS.stream().map(LedgerDO::getId).collect(Collectors.toSet());
                            batchRecordDO.setDeviceId(String.join(",", deviceSet));
                            overview.getBatchRecordDOList().add(batchRecordDO);
                        }
                    }
                }
            }
        }
        return demandMap;
    }

    private void createMaterialDemand2(Map<String, BatchOrderDemandOverviewVO> demandMap) {
        for (BatchOrderDemandOverviewVO overview : demandMap.values()) {
            String orderId = overview.getOrderId();
            String resourcesTypeCode = overview.getResourcesTypeCode();
            String resourcesTypeId = overview.getResourcesTypeId();
            String resourcesType = overview.getResourcesType();
            List<BatchRecordDO> batchRecordDOList = overview.getBatchRecordDOList();

            Set<String> deviceIdSet = new HashSet<>();
            for (int i = 0; i < batchRecordDOList.size(); i++) {
                BatchRecordDO batchRecordDO = batchRecordDOList.get(i);
                String[] deviceIds = batchRecordDO.getDeviceId().split(",");
                Collections.addAll(deviceIdSet, deviceIds);
            }
            if (deviceIdSet.size() == 0) continue;
            BatchOrderDemandDO base = new BatchOrderDemandDO();
            base.setOrderId(orderId);
            base.setStatus(MCS_READY_STATUS_NOT_FULLY_PREPARED);
            base.setRequirementType(MCS_DEMAND_TYPE_NORMAL);
            base.setResourceTypeCode(resourcesTypeCode);
            base.setResourceTypeId(resourcesTypeId);
            base.setResourceType(resourcesType);
            base.setTotal(deviceIdSet.size());
            batchOrderDemandMapper.insert(base);

            // 新增集合
            List<BatchDemandRecordDO> recordAdd = new ArrayList<>();
            // 同一设备需要相同类型的资源 需求合并 value: deviceId+"_"+resourceTypeId
            Set<String> deviceResourceList = new HashSet<>();
            for (int i = 0; i < batchRecordDOList.size(); i++) {
                BatchRecordDO batchRecordDO = batchRecordDOList.get(i);
                String deviceIds = batchRecordDO.getDeviceId();
                List<String> deviceIdList = new ArrayList<>();
                for (String deviceId : deviceIds.split(",")) {
                    if (deviceResourceList.add(deviceId+"_"+resourcesTypeId)) {
                        deviceIdList.add(deviceId);
                    }
                }
                for (String deviceId : deviceIdList) {
                    BatchDemandRecordDO demandRecord = new BatchDemandRecordDO();
                    demandRecord.setOrderId(orderId);
                    demandRecord.setBatchId(batchRecordDO.getBatchId());
                    demandRecord.setBatchRecordId(batchRecordDO.getId());
                    demandRecord.setProcessingUnitId(batchRecordDO.getProcessingUnitId());
                    demandRecord.setDeviceId(deviceId);
                    demandRecord.setDemandId(base.getId());
                    demandRecord.setTotality(1);
                    demandRecord.setResourceType(resourcesType);
                    demandRecord.setMaterialConfigId(resourcesTypeId);
                    demandRecord.setMaterialNumber(resourcesTypeCode);
                    demandRecord.setStatus(MCS_DEMAND_RECORD_STATUS_NEW);
                    demandRecord.setPlanStartTime(batchRecordDO.getPlanStartTime());
                    recordAdd.add(demandRecord);
                }
            }
            batchDemandRecordMapper.insertBatch(recordAdd);
        }
    }

    @Override
    public List<LineStationGroupDO> getAllProcessingUnit() {
        List<LineStationGroupDO> lineStationGroupDOS = lineStationGroupMapper.selectList();
        QueryWrapper<LedgerDO> ledgerWrapper = new QueryWrapper<>();
        ledgerWrapper.isNull("lint_station_group");
        ledgerWrapper.or().eq("lint_station_group", "");
        List<LedgerDO> ledgerDOS = ledgerMapper.selectList(ledgerWrapper);
        List<LineStationGroupDO> result = BeanUtils.toBean(lineStationGroupDOS, LineStationGroupDO.class);
        result.addAll(BeanUtils.toBean(ledgerDOS, LineStationGroupDO.class));
        return result;
    }

    @Override
    public void resourceSortingSave(BatchOrderDemandSaveReqVO createReqVO) {
        BatchOrderDemandDO demandDO = batchOrderDemandMapper.selectById(createReqVO.getId());
        demandDO.setStatus(MCS_READY_STATUS_SORTING);
        batchOrderDemandMapper.updateById(demandDO);
        //覆盖原数据
        List<BatchDemandRecordDO> demandRecordDOS = batchDemandRecordMapper.selectList(BatchDemandRecordDO::getDemandId, demandDO.getId());
        List<BatchDemandResourceReqVO> batchDemandRecordList = createReqVO.getBatchDemandResourceList();
        //校验重复 未完成搬运的资源不可选中,批量的算数量(暂无)
        //统计选定当前设备的资源 map: key:demandRecordId value:资源
        Set<String> selectedIds = new HashSet<>();
        Map<String,BatchDemandResourceReqVO> demandResourceMap = new HashMap<>();
        List<BatchDemandResourceReqVO> otherResourceList = new ArrayList<>();
        for (BatchDemandResourceReqVO batchDemandResource : batchDemandRecordList) {
            selectedIds.add(batchDemandResource.getId());
            if (batchDemandResource.getDemandRecordId() != null) {
                demandResourceMap.put(batchDemandResource.getDemandRecordId(), batchDemandResource);
            } else {
                otherResourceList.add(batchDemandResource);
            }
        }
        if (selectedIds.size() > 0) {
            List<DistributionRecordDO> list = distributionRecordMapper.selectList(new QueryWrapper<DistributionRecordDO>().in("material_uid", selectedIds).in("status", MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_DELIVERY));
            if (list.size() > 0) {
                String codes = list.stream().map(DistributionRecordDO::getBarCode).collect(Collectors.joining(","));
                throw new ServiceException(5008,"物料选择重复,操作失败!" + codes);
            }
        }
        //更新
        for (BatchDemandRecordDO demandRecordDO : demandRecordDOS) {
            BatchDemandResourceReqVO resource;
            if (demandResourceMap.containsKey(demandRecordDO.getId())) {
                resource = demandResourceMap.get(demandRecordDO.getId());
            } else {
                resource = otherResourceList.remove(0);
            }
            demandRecordDO.setMaterialUid(resource.getId());
            demandRecordDO.setBarCode(resource.getBarCode());
            demandRecordDO.setBatchNumber(resource.getBatchNumber());
            demandRecordDO.setBatch(resource.getMaterialManage() == null ? 1 : resource.getMaterialManage());
            if (WMS_MATERIAL_TYPE_WORKPIECE.equals(demandRecordDO.getResourceType())) {
                //毛坯默认 搬运
                demandRecordDO.setDeliveryRequired(true);
            } else {
                demandRecordDO.setDeliveryRequired(resource.getDeliveryRequired());
            }
        }
        batchDemandRecordMapper.updateBatch(demandRecordDOS);
    }

    @Override
    public Map<String, Object> getOutOrderOtherMaterialsByConfigIds(List<String> singletonList, Map<String, Integer> locationAndCount) {
        Map<String,Object> result = new HashMap<>();
        List<MaterialStockLocationTypeDTO> list = warehouseRestService.getMaterialsByConfigIdsWithWarehouse(singletonList);
        List<Map<String,Object>> listW = new ArrayList<>();
        Set<String> keys = locationAndCount.keySet();
        Map<String, List<MaterialStockLocationTypeDTO>> materialMap = new HashMap<>();
//        keys.stream().collect(Collectors.toMap(key -> key, key -> new ArrayList<>(), (a, b) -> b))
        result.put("stock", listW);
        result.put("locationAndCount", locationAndCount);
        result.put("materialMap", materialMap);
        for (MaterialStockLocationTypeDTO material : list) {
            if (material.getWarehouseType() == (int) WMS_WAREHOUSE_TYPE_1 || material.getWarehouseType() == (int) WMS_WAREHOUSE_TYPE_2) {
                if (material.getAreaType() == (int) WMS_WAREHOUSE_AREA_TYPE_1) {
                    Map<String, Object> map = BeanUtils.toBean(material, HashMap.class);
                    map.put("deliveryRequired", true);
                    listW.add(map);
                }
            } else {
                String locationId = material.getLocationId();
                if (keys.contains(locationId)) {
                    if (!materialMap.containsKey(locationId)) {
                        materialMap.put(locationId, new ArrayList<>());
                    }
                    materialMap.get(locationId).add(material);

                }
            }
        }
        return result;
    }

    @Override
    public void completeBatchOrderDemand(BatchOrderDemandDO batchOrderDemand) {
        batchOrderDemand.setStatus(MCS_READY_STATUS_ALL_READY);
        batchOrderDemandMapper.updateById(batchOrderDemand);
        List<DistributionRecordDO> outboundApplication = createOutboundApplication(batchOrderDemand);
        if (WMS_MATERIAL_TYPE_GJ.equals(batchOrderDemand.getResourceType())) {
            updateOrderSelectBarCode(batchOrderDemand);
        }
        //判断所在批次任务 是否可下发
        checkBatchOrderReady(batchOrderDemand);
        for (DistributionRecordDO distributionRecordDO : outboundApplication) {
            encodingService.updateMaterialConfigCodeStatus(distributionRecordDO.getNumber());
        }
    }

    private void updateOrderSelectBarCode(BatchOrderDemandDO batchOrderDemand) {
        List<BatchDemandRecordDO> demandDeliveryList = batchDemandRecordMapper.selectList(BatchDemandRecordDO::getDemandId, batchOrderDemand.getId());
        if (demandDeliveryList.size() == 0) return;
        Set<String> barCodeSet = demandDeliveryList.stream().map(BatchDemandRecordDO::getBarCode).collect(Collectors.toSet());
        boolean check = orderRestService.checkMaterialUsageInfo(barCodeSet);
        if (!check) throw new ServiceException(5005, "当前选定的物料已被使用!");
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchOrderDemand.getOrderId());
        orderFormDO.setMaterialCode(String.join(",", barCodeSet));
        orderFormMapper.updateById(orderFormDO);
        for (BatchDemandRecordDO demandRecordDO : demandDeliveryList) {
            BatchOrderDO batchOrderDO = batchOrderMapper.selectById(demandRecordDO.getBatchId());
            batchOrderDO.setBarCode(demandRecordDO.getBarCode());
            batchOrderMapper.selectById(batchOrderDO);
        }
    }

    private List<DistributionRecordDO> createOutboundApplication(BatchOrderDemandDO batchOrderDemandDO) {
        List<BatchDemandRecordDO> demandDeliveryList = batchDemandRecordMapper.selectList(BatchDemandRecordDO::getDemandId, batchOrderDemandDO.getId(), BatchDemandRecordDO::getStatus, MCS_DEMAND_RECORD_STATUS_NEW);
        if (demandDeliveryList.size() == 0) return new ArrayList<>();
        List<BatchDemandRecordDO> outboundDemandList = new ArrayList<>();
        String resourceType = batchOrderDemandDO.getResourceType();
        boolean firstRecordOutsourcing = false;
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(
                new LambdaQueryWrapper<BatchRecordDO>()
                        .eq(BatchRecordDO::getOrderId, batchOrderDemandDO.getOrderId())
                        .isNull(BatchRecordDO::getPreRecordId));
        if (batchRecordDOList.size() > 0) {
            BatchRecordDO batchRecordDO = batchRecordDOList.get(0);
            if (batchRecordDO.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
                firstRecordOutsourcing = true;
            }
        }
        for (BatchDemandRecordDO demandRecordDO : demandDeliveryList) {
            if (WMS_MATERIAL_TYPE_WORKPIECE.equals(resourceType) && firstRecordOutsourcing) {
                demandRecordDO.setDeliveryRequired(false);
            }
            //如果在仓库里
            if (demandRecordDO.getDeliveryRequired() != null && demandRecordDO.getDeliveryRequired()) {
                outboundDemandList.add(demandRecordDO);
                demandRecordDO.setStatus(MCS_DEMAND_RECORD_STATUS_APPLIED);
            } else {
                demandRecordDO.setStatus(MCS_DEMAND_RECORD_STATUS_COMPLETED);
            }
            batchDemandRecordMapper.updateById(demandRecordDO);
        }
        List<DistributionRecordDO> outboundRecordList = new ArrayList<>();
        for (BatchDemandRecordDO demandRecord : outboundDemandList) {
            DistributionRecordDO recordDO = new DistributionRecordDO();
            recordDO.setNumber(encodingService.getDistributionCode());
            recordDO.setDemandRecordId(demandRecord.getId());
            recordDO.setBatchRecordId(demandRecord.getBatchRecordId());
            recordDO.setMaterialUid(demandRecord.getMaterialUid());
            recordDO.setMaterialConfigId(demandRecord.getMaterialConfigId());
            recordDO.setProcessingUnitId(demandRecord.getProcessingUnitId());
            recordDO.setDeviceId(demandRecord.getDeviceId());
            recordDO.setResourceType(demandRecord.getResourceType());
            recordDO.setMaterialNumber(demandRecord.getMaterialNumber());
            recordDO.setBatchNumber(demandRecord.getBatchNumber());
            recordDO.setBarCode(demandRecord.getBarCode());
            recordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_NEW);
            recordDO.setType(WMS_ORDER_TYPE_PRODUCE_OUT);
            recordDO.setCount(demandRecord.getTotality());
            recordDO.setBatch(demandRecord.getBatch());
            recordDO.setPlanStartTime(demandRecord.getPlanStartTime());
            outboundRecordList.add(recordDO);
        }
        if (outboundRecordList.size() == 0) return new ArrayList<>();
        distributionRecordMapper.insertBatch(outboundRecordList);
        warehouseRestService.outboundApplication(outboundRecordList);
        return outboundRecordList;
    }

    private void checkBatchOrderReady(BatchOrderDemandDO batchDemand) {
        String orderId = batchDemand.getOrderId();
        List<BatchOrderDemandDO> batchOrderDemandDOS = batchOrderDemandMapper.selectList(BatchOrderDemandDO::getOrderId, orderId);
        Set<String> demandIds = batchOrderDemandDOS.stream().filter(item -> item.getStatus() == MCS_READY_STATUS_NOT_FULLY_PREPARED || item.getStatus() == MCS_READY_STATUS_SORTING).map(BatchOrderDemandDO::getId).collect(Collectors.toSet());
        Set<String> batchIds = new HashSet<>();
        if (demandIds.size() > 0) {
            List<BatchDemandRecordDO> recordDOS = batchDemandRecordMapper.selectList(new LambdaQueryWrapper<BatchDemandRecordDO>().in(BatchDemandRecordDO::getDemandId, demandIds));
            batchIds = recordDOS.stream().map(BatchDemandRecordDO::getBatchId).collect(Collectors.toSet());
        }
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getOrderId, orderId);
        List<BatchOrderDO> updateList = new ArrayList<>();
        //更改任务齐备状态, 排除batchIds未齐备任务id
        for (BatchOrderDO batchOrderDO : batchOrderDOList) {
            if (batchIds.contains(batchOrderDO.getId())) continue;
            updateList.add(batchOrderDO.setStatus(MCS_BATCH_STATUS_READY));
        }
        //所有需求齐备
        if (demandIds.size() == 0) {
            OrderFormDO orderFormDO = orderFormMapper.selectById(orderId);
            String[] split = orderFormDO.getMaterialCode().split(",");
            for (int i = 0; i < batchOrderDOList.size(); i++) {
                BatchOrderDO batchOrderDO = batchOrderDOList.get(i);
                if (batchOrderDO.getPreBatchId() != null) continue;
                batchOrderDO.setStatus(MCS_BATCH_STATUS_CAN_BE_ISSUED);
                //只有一个任务
                batchOrderDO.setBarCode(split[i]);
            }
        }
        if (updateList.size() > 0) {
            batchOrderMapper.updateBatch(updateList);
        }
    }

    @Override
    public List<BatchOrderDemandDO> getListByOrderId(String orderId) {
        LambdaQueryWrapper<BatchOrderDemandDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchOrderDemandDO::getOrderId, orderId);
        return batchOrderDemandMapper.selectList(queryWrapper);
    }

    @Override
    public String createExtraDemand(BatchOrderDemandSaveReqVO createReqVO) {
        //额外生成的需求单
        createReqVO.setRequirementType(MCS_DEMAND_TYPE_COMPENSATE);
        //创建需求
        String demandId = createBatchOrderDemand(createReqVO);
        createReqVO.setId(demandId);
        //保存选定物料
        resourceSortingSave(createReqVO);
        //更新需求状态齐备
        BatchOrderDemandDO batchOrderDemandDO = batchOrderDemandMapper.selectById(demandId);
        batchOrderDemandDO.setStatus(MCS_READY_STATUS_ALL_READY);
        batchOrderDemandMapper.updateById(batchOrderDemandDO);
        //创建配送占用
        List<DistributionRecordDO> outboundApplication = createOutboundApplication(batchOrderDemandDO);
        //生产配送申请
        DistributionApplicationEditVO distributionApplicationEditVO = new DistributionApplicationEditVO();
        List<DistributionRecordRespVO> distributionRecordRespVOS = BeanUtils.toBean(outboundApplication, DistributionRecordRespVO.class);
        String distributionCode = encodingService.getDistributionCode();
        distributionApplicationEditVO.setApplicationNumber(distributionCode);
        distributionApplicationEditVO.setDeviceUnitId(createReqVO.getDeviceId());
        distributionApplicationEditVO.setBatchRecordIdList(Collections.singletonList(createReqVO.getBatchRecordId()));
        distributionApplicationEditVO.setDemandDeliveryList(distributionRecordRespVOS);
        String applicationId = distributionApplicationService.createApplication(distributionApplicationEditVO);
        //更新编码使用状态
        encodingService.updateMaterialConfigCodeStatus(distributionCode);
        //提交配送申请
        distributionApplicationService.submitApplication(applicationId);
        return demandId;
    }

    @Override
    public PageResult<McsCuttingToolDemandDTO> getCuttingToolDemandPage(PageParam pageReqVO) {
        PageResult<BatchDemandRecordDO> batchDemandRecordDOPageResult = batchDemandRecordMapper.selectPage(pageReqVO, new LambdaQueryWrapper<BatchDemandRecordDO>()
                .eq(BatchDemandRecordDO::getResourceType, WMS_MATERIAL_TYPE_CUTTING)
                .eq(BatchDemandRecordDO::getStatus, MCS_DEMAND_RECORD_STATUS_NEW)
                .isNull(BatchDemandRecordDO::getBarCode));
        PageResult<McsCuttingToolDemandDTO> result = new PageResult<>();
        result.setTotal(batchDemandRecordDOPageResult.getTotal());
        ArrayList<McsCuttingToolDemandDTO> list = new ArrayList<>();
        result.setList(list);
        List<BatchDemandRecordDO> demandList = batchDemandRecordDOPageResult.getList();
        if (demandList.size() == 0) return new PageResult<>(new ArrayList<>(), 0L);
        Set<String> configIdList = new HashSet<>();
        Set<String> deviceIdList = new HashSet<>();
        for (BatchDemandRecordDO batchDemandRecordDO : demandList) {
            configIdList.add(batchDemandRecordDO.getMaterialConfigId());
            deviceIdList.add(batchDemandRecordDO.getDeviceId());
        }
        Map<String, String> cuttingLocationMap = ledgerService.getCuttingLocationListByDeviceIds(deviceIdList);
        Map<String, MaterialConfigRespDTO> materialConfigMap = warehouseRestService.getMaterialConfigById(configIdList);
        List<WarehouseLocationRespDTO> warehouseLocationList = warehouseRestService.getWarehouseLocationByIds(cuttingLocationMap.values());
        Map<String, String> locationMap = warehouseLocationList.stream().collect(Collectors.toMap(WarehouseLocationRespDTO::getId, WarehouseLocationRespDTO::getLocationCode, (a, b) -> b));
        for (BatchDemandRecordDO demandRecordDO : demandList) {
            McsCuttingToolDemandDTO mcsCuttingToolDemandDTO = new McsCuttingToolDemandDTO();
            mcsCuttingToolDemandDTO.setOrderNumber(demandRecordDO.getPreDistributionNumber());
            mcsCuttingToolDemandDTO.setNeedCount(demandRecordDO.getTotality());
            if (cuttingLocationMap.containsKey(demandRecordDO.getDeviceId())) {
                String location = cuttingLocationMap.get(demandRecordDO.getDeviceId());
                mcsCuttingToolDemandDTO.setTargetLocation(location);
                mcsCuttingToolDemandDTO.setTargetLocationCode(locationMap.get(location));
            }
            mcsCuttingToolDemandDTO.setDistributionDeadline(demandRecordDO.getPlanStartTime());
            String materialConfigId = demandRecordDO.getMaterialConfigId();
            if (materialConfigMap.containsKey(materialConfigId)) {
                MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(materialConfigId);
                mcsCuttingToolDemandDTO.setMaterialNumber(materialConfigRespDTO.getMaterialNumber());
                mcsCuttingToolDemandDTO.setMaterialName(materialConfigRespDTO.getMaterialName());
            }

            mcsCuttingToolDemandDTO.setMaterialConfigId(materialConfigId);
            list.add(mcsCuttingToolDemandDTO);
        }
        return result;
    }

    @Override
    public void updateCuttingToolDemand(McsCuttingToolDemandUptateDTO cuttingTool) {
        List<BatchDemandRecordDO> recordDOS = batchDemandRecordMapper.selectList(
                BatchDemandRecordDO::getPreDistributionNumber, cuttingTool.getOrderNumber(),
                BatchDemandRecordDO::getStatus, MCS_DEMAND_RECORD_STATUS_NEW
        );
        if (recordDOS.size() == 0) throw new ServiceException(5004, "未找到需求,单号:" + cuttingTool.getOrderNumber());
        if (recordDOS.size() > 1) throw new ServiceException(5005, "需求配送单号重复:" + cuttingTool.getOrderNumber());
        BatchDemandRecordDO demandRecordDO = recordDOS.get(0);
        MaterialStockRespDTO material;
        if (cuttingTool.getBarCode() == null) {
            material = warehouseRestService.getById(cuttingTool.getMaterialId());
        } else {
            material = warehouseRestService.getByBarCode(cuttingTool.getBarCode());
        }
        if (material == null) throw new ServiceException(5004, "未找到刀具,条码:" + cuttingTool.getBarCode());
        demandRecordDO.setMaterialUid(material.getId());
        demandRecordDO.setBarCode(material.getBarCode());
        demandRecordDO.setBatchNumber(material.getBatchNumber());
        demandRecordDO.setBatch(material.getMaterialManage() == null ? 1 : material.getMaterialManage());
        demandRecordDO.setDeliveryRequired(true);
        batchDemandRecordMapper.updateById(demandRecordDO);
    }
}
