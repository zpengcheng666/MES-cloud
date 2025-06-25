package com.miyu.cloud.mcs.service.distributionapplication;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.dal.mysql.linestationgroup.LineStationGroupMapper;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.api.McsDistributionApplicationApi;
import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.DistributionRecordRespVO;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorderdemand.BatchOrderDemandMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.cloud.mcs.service.distributionrecord.DistributionRecordService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.distributionapplication.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.distributionapplication.DistributionApplicationMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 物料配送申请 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class DistributionApplicationServiceImpl implements DistributionApplicationService {

    @Resource
    private DistributionApplicationMapper distributionApplicationMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;
    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private BatchDemandRecordMapper batchDemandRecordMapper;
    @Resource
    private LineStationGroupMapper lineStationGroupMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private DistributionRecordService distributionRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createDistributionApplication(DistributionApplicationSaveReqVO createReqVO) {
        // 插入
        DistributionApplicationDO distributionApplication = BeanUtils.toBean(createReqVO, DistributionApplicationDO.class);
        distributionApplicationMapper.insert(distributionApplication);

        // 插入子表
        createDistributionRecordList(distributionApplication.getId(), createReqVO.getDistributionRecords());
        // 返回
        return distributionApplication.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDistributionApplication(DistributionApplicationSaveReqVO updateReqVO) {
        // 校验存在
        validateDistributionApplicationExists(updateReqVO.getId());
        // 更新
        DistributionApplicationDO updateObj = BeanUtils.toBean(updateReqVO, DistributionApplicationDO.class);
        distributionApplicationMapper.updateById(updateObj);

        // 更新子表
        updateDistributionRecordList(updateReqVO.getId(), updateReqVO.getDistributionRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDistributionApplication(String id) {
        // 校验存在
        validateDistributionApplicationExists(id);
        // 删除
        distributionApplicationMapper.deleteById(id);

        // 删除子表
        deleteDistributionRecordByApplicationId(id);
    }

    private void validateDistributionApplicationExists(String id) {
        if (distributionApplicationMapper.selectById(id) == null) {
            throw exception(DISTRIBUTION_APPLICATION_NOT_EXISTS);
        }
    }

    @Override
    public DistributionApplicationDO getDistributionApplication(String id) {
        return distributionApplicationMapper.selectById(id);
    }

    @Override
    public PageResult<DistributionApplicationDO> getDistributionApplicationPage(DistributionApplicationPageReqVO pageReqVO) {
        return distributionApplicationMapper.selectPage(pageReqVO);
    }

    @Override
    public DistributionApplicationEditVO getRecordListByBatchAndType(DistributionApplicationEditVO editVO) {
        editVO.setDistributionRecordRespVOList(new ArrayList<>());
        String deviceUnitId = editVO.getDeviceUnitId();
        LineStationGroupDO lineStationGroupDO = lineStationGroupMapper.selectById(deviceUnitId);
        for (String batchRecordId : editVO.getBatchRecordIdList()) {
            BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecordId);

            LambdaQueryWrapper<DistributionRecordDO> queryWrapper = new LambdaQueryWrapperX<>();
            queryWrapper.eq(DistributionRecordDO::getBatchRecordId, batchRecordId);
            queryWrapper.in(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_REJECT);
            if (lineStationGroupDO == null) {
                queryWrapper.like(DistributionRecordDO::getDeviceId, deviceUnitId);
            } else {
                queryWrapper.eq(DistributionRecordDO::getProcessingUnitId, deviceUnitId);
                queryWrapper.ne(DistributionRecordDO::getResourceType, WMS_MATERIAL_TYPE_CUTTING);
            }
            if (editVO.getType() != null && editVO.getType().size() > 0) {
                queryWrapper.in(DistributionRecordDO::getResourceType, editVO.getType());
            }
            List<DistributionRecordDO> distributionRecordDOS = distributionRecordMapper.selectList(queryWrapper);
            List<DistributionRecordRespVO> distributionRecordRespVOS = BeanUtils.toBean(distributionRecordDOS, DistributionRecordRespVO.class);
            editVO.getDistributionRecordRespVOList().addAll(distributionRecordRespVOS);
            for (DistributionRecordRespVO distributionRecordRespVO : distributionRecordRespVOS) {
                distributionRecordRespVO.setBatchRecordNumber(batchRecordDO.getNumber());
            }
        }
        return editVO;
    }


    private void createDistributionRecordList(String applicationId, List<DistributionRecordDO> list) {
        list.forEach(o -> o.setApplicationId(applicationId));
        distributionRecordMapper.insertBatch(list);
    }

    private void updateDistributionRecordList(String applicationId, List<DistributionRecordDO> list) {
        deleteDistributionRecordByApplicationId(applicationId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createDistributionRecordList(applicationId, list);
    }

    private void deleteDistributionRecordByApplicationId(String applicationId) {
        distributionRecordMapper.deleteByApplicationId(applicationId);
    }

    @Override
    public String createApplication(DistributionApplicationEditVO createReqVO) {
        List<DistributionRecordRespVO> demandDeliveryList = createReqVO.getDemandDeliveryList();
        if (demandDeliveryList == null || demandDeliveryList.size() == 0) throw new ServiceException(5011, "请至少选择一条配送");
        String deviceUnitId = createReqVO.getDeviceUnitId();
        //当前只允许一条
        String batchRecordId = createReqVO.getBatchRecordIdList().get(0);
        BatchRecordDO batchRecordDO = batchRecordMapper.selectById(batchRecordId);
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());

        DistributionApplicationDO applicationDO = new DistributionApplicationDO();
        applicationDO.setApplicationNumber(createReqVO.getApplicationNumber());
        applicationDO.setProcessingUnitId(deviceUnitId);
        applicationDO.setStatus(MCS_DISTRIBUTION_APPLICATION_NEW);
        applicationDO.setOrderId(orderFormDO.getId());
        applicationDO.setBatchRecordId(batchRecordDO.getId());
        applicationDO.setOrderNumber(orderFormDO.getOrderNumber());
        applicationDO.setBatchRecordNumber(batchRecordDO.getNumber());
        distributionApplicationMapper.insert(applicationDO);

        String toolLocationId = null;
        String cuttingLocationId = null;
        String warehouseId = null;
        LineStationGroupDO deviceUnit = lineStationGroupMapper.selectById(deviceUnitId);
        if (deviceUnit == null) {
            warehouseId = ledgerService.getWarehouseByDevice(deviceUnitId);
            toolLocationId = ledgerService.getToolLocationByDevice(deviceUnitId);
            cuttingLocationId = ledgerService.getCuttingLocationByDevice(deviceUnitId);
        } else {
            warehouseId = deviceUnit.getLocation();
        }
        Set<DistributionRecordDO> otherSet = new HashSet<>();
        if (warehouseId == null) throw new RuntimeException("未找到目标仓库");
        Set<String> set = demandDeliveryList.stream().map(DistributionRecordRespVO::getId).collect(Collectors.toSet());
        List<DistributionRecordDO> distributionRecordDOS = distributionRecordMapper.selectBatchIds(set);
        for (DistributionRecordDO distributionRecordDO : distributionRecordDOS) {
            String resourceType = distributionRecordDO.getResourceType();
            distributionRecordDO.setApplicationId(applicationDO.getId());
            String materialLocation = warehouseRestService.getMaterialLocation(distributionRecordDO.getMaterialUid());
            distributionRecordDO.setStartLocationId(materialLocation);
            if (WMS_MATERIAL_TYPE_CUTTING.equals(resourceType)) {
                distributionRecordDO.setTargetLocationId(cuttingLocationId);
                distributionRecordDO.setTargetWarehouseId(warehouseId);
            } else {
                distributionRecordDO.setTargetLocationId(toolLocationId);
                distributionRecordDO.setTargetWarehouseId(warehouseId);
                otherSet.add(distributionRecordDO);
            }
        }
        distributionRecordMapper.updateBatch(distributionRecordDOS);
        if (otherSet.size() > 0) {
            warehouseRestService.updateDistributionWarehouse(otherSet);
        }
        return applicationDO.getId();
    }

    @Override
    public void submitApplication(String id) {
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(McsDistributionApplicationApi.PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(id)).getCheckedData();
        DistributionApplicationDO applicationDO = new DistributionApplicationDO();
        applicationDO.setId(id);
        applicationDO.setStatus(MCS_DISTRIBUTION_APPLICATION_SUBMIT);
        applicationDO.setProcessInstanceId(processInstanceId);
        distributionApplicationMapper.updateById(applicationDO);
        LambdaUpdateWrapper<DistributionRecordDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_APPLIED);
        updateWrapper.eq(DistributionRecordDO::getApplicationId, id);
        distributionRecordMapper.update(updateWrapper);
    }

    @Override
    public void updateApplicationStatus(String applicationId, Integer status) {
        int statusDR = -1;
        int statusBDR = -1;
        if (status == MCS_DISTRIBUTION_APPLICATION_ADOPT) {
            statusDR = MCS_DELIVERY_RECORD_STATUS_DELIVERY;
            statusBDR = MCS_DEMAND_RECORD_STATUS_DELIVERY;
        } else if (status == MCS_DISTRIBUTION_APPLICATION_REJECT) {
            statusDR = MCS_DELIVERY_RECORD_STATUS_REJECT;
            statusBDR = MCS_DEMAND_RECORD_STATUS_NEW;
        }
        if (statusDR == -1) return;
        DistributionApplicationDO applicationDO = distributionApplicationMapper.selectById(applicationId);
        if (applicationDO.getStatus() == MCS_DISTRIBUTION_APPLICATION_REVOKE) return;
        distributionApplicationMapper.updateById(applicationDO.setStatus(status));
        List<DistributionRecordDO> recordDOS = distributionRecordMapper.selectList(DistributionRecordDO::getApplicationId, applicationId);
        Set<String> demandRIds = recordDOS.stream().map(DistributionRecordDO::getDemandRecordId).collect(Collectors.toSet());
        UpdateWrapper<DistributionRecordDO> updateWrapperDR = new UpdateWrapper<>();
        updateWrapperDR.set("status", statusDR);
        updateWrapperDR.eq("application_id", applicationId);
        updateWrapperDR.in("status", MCS_DELIVERY_RECORD_STATUS_APPLIED, MCS_DELIVERY_RECORD_STATUS_NEW);
        distributionRecordMapper.update(updateWrapperDR);
        UpdateWrapper<BatchDemandRecordDO> updateWrapperBDR = new UpdateWrapper<>();
        updateWrapperBDR.set("status", statusBDR);
        updateWrapperBDR.in("id", demandRIds);
        batchDemandRecordMapper.update(updateWrapperBDR);
        if (statusDR == MCS_DELIVERY_RECORD_STATUS_DELIVERY) {
            warehouseRestService.adoptApplication(recordDOS);
        }
    }

    @Override
    public void applicationCancelByOrderId(String orderId) {
        LambdaQueryWrapper<DistributionApplicationDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DistributionApplicationDO::getOrderId, orderId);
        queryWrapper.in(DistributionApplicationDO::getStatus, MCS_DISTRIBUTION_APPLICATION_NEW, MCS_DISTRIBUTION_APPLICATION_SUBMIT);
        List<DistributionApplicationDO> applicationDOList = distributionApplicationMapper.selectList(queryWrapper);
        for (DistributionApplicationDO applicationDO : applicationDOList) {
            applicationDO.setStatus(MCS_DISTRIBUTION_APPLICATION_REVOKE);
            distributionApplicationMapper.updateById(applicationDO);
            LambdaQueryWrapper<DistributionRecordDO> queryWrapperDR = new LambdaQueryWrapper<>();
            queryWrapperDR.eq(DistributionRecordDO::getApplicationId, applicationDO.getId());
            queryWrapperDR.in(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_APPLIED, MCS_DELIVERY_RECORD_STATUS_REJECT);
            List<DistributionRecordDO> list = distributionRecordMapper.selectList(queryWrapperDR);
            for (DistributionRecordDO distributionRecordDO : list) {
                distributionRecordService.recordRevokeById(distributionRecordDO.getId());
            }
        }
    }
}
