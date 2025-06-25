package com.miyu.cloud.mcs.service.orderform;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.dal.mysql.batchdemandrecord.BatchDemandRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorder.BatchOrderMapper;
import com.miyu.cloud.mcs.dal.mysql.batchorderdemand.BatchOrderDemandMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecordstep.BatchRecordStepMapper;
import com.miyu.cloud.mcs.dto.schedule.ScheduleResourceType;
import com.miyu.cloud.mcs.restServer.service.order.OrderRestService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.*;
import com.miyu.cloud.mcs.service.batchorder.BatchOrderService;
import com.miyu.cloud.mcs.service.batchorderdemand.BatchOrderDemandService;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.cloud.mcs.controller.admin.orderform.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.ErrorCodeConstants.*;

/**
 * 生产订单 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
@Transactional
public class OrderFormServiceImpl implements OrderFormService {

    @Resource
    private OrderFormMapper orderFormMapper;
    @Resource
    private BatchOrderMapper batchOrderMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private BatchOrderDemandMapper batchOrderDemandMapper;
    @Resource
    private BatchDemandRecordMapper batchDemandRecordMapper;
    @Resource
    private BatchRecordStepMapper batchRecordStepMapper;

    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private BatchOrderService batchOrderService;
    @Resource
    private TechnologyRestService technologyRestService;
    @Resource
    private OrderRestService orderRestService;
    @Resource
    private BatchOrderDemandService batchOrderDemandService;
    @Resource
    private DistributionApplicationService distributionApplicationService;

    @Override
    public String createOrderFormIntegral(OrderFormSaveReqVO createReqVO) {
        OrderFormDO orderForm = BeanUtils.toBean(createReqVO, OrderFormDO.class);
        orderFormMapper.insert(orderForm);
        createOrderFormDetail(orderForm);
        return orderForm.getId();
    }

    @Override
    public String createOrderForm(OrderFormSaveReqVO createReqVO) {
        OrderFormDO orderForm = BeanUtils.toBean(createReqVO, OrderFormDO.class);
        orderFormMapper.insert(orderForm);
        return orderForm.getId();
    }

    @Override
    public void createOrderFormDetail(OrderFormDO orderForm) {
        if (orderForm.getIsBatch()) {
            //批量 一个条码 多个数量
            BatchOrderDO batchOrderDO = new BatchOrderDO();
            batchOrderDO.setOrderId(orderForm.getId());
            batchOrderDO.setBatchNumber(orderForm.getOrderNumber() + "_B");
            batchOrderDO.setTechnologyId(orderForm.getTechnologyId());
            batchOrderDO.setProcesStatus(orderForm.getProcesStatus());
            batchOrderDO.setAidMill(orderForm.getAidMill());

            batchOrderDO.setCount(orderForm.getCount());
            batchOrderDO.setStatus(MCS_BATCH_STATUS_NEW);
            batchOrderDO.setSubmitStatus(MCS_BATCH_SUBMIT_STATUS_NEW);
            batchOrderDO.setIsBatch(true);
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderForm.getId(), orderForm.getTechnologyId());
            batchOrderDO.setTechnologyCode(technology.getProcessCode() + "_" + technology.getProcessSchemeCode() + "_" + technology.getPartVersion());
            if (orderForm.getBeginProcessNumber() != null) {
                List<ProcedureRespDTO> procedureList = technology.getProcedureList();
                for (ProcedureRespDTO procedureRespDTO : procedureList) {
                    if (orderForm.getBeginProcessNumber().equals(procedureRespDTO.getProcedureNum())) {
                        batchOrderDO.setBeginProcessId(procedureRespDTO.getId());
                        break;
                    }
                }
            }
            batchOrderMapper.insert(batchOrderDO);
            batchRecordService.createBatchRecordByBatch(batchOrderDO);
        } else {
            String[] barCodes = null;
            if (orderForm.getProcesStatus() == MCS_PROCES_STATUS_OUTSOURCING) {
                barCodes = orderForm.getMaterialCode().split(",");
            }
            for (int i = 1; i <= orderForm.getCount(); i++) {
                BatchOrderDO batchOrderDO = new BatchOrderDO();
                batchOrderDO.setOrderId(orderForm.getId());
                batchOrderDO.setBatchNumber(orderForm.getOrderNumber() + "_" + i);
                batchOrderDO.setTechnologyId(orderForm.getTechnologyId());
                batchOrderDO.setProcesStatus(orderForm.getProcesStatus());
                batchOrderDO.setAidMill(orderForm.getAidMill());
                batchOrderDO.setCount(1);
                batchOrderDO.setStatus(MCS_BATCH_STATUS_NEW);
                batchOrderDO.setSubmitStatus(MCS_BATCH_SUBMIT_STATUS_NEW);
                batchOrderDO.setIsBatch(false);
                ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderForm.getId(), orderForm.getTechnologyId());
                batchOrderDO.setTechnologyCode(technology.getProcessSchemeCode() + "_" + technology.getPartVersion());
                if (orderForm.getBeginProcessNumber() != null) {
                    List<ProcedureRespDTO> procedureList = technology.getProcedureList();
                    for (ProcedureRespDTO procedureRespDTO : procedureList) {
                        if (orderForm.getBeginProcessNumber().equals(procedureRespDTO.getProcedureNum())) {
                            batchOrderDO.setBeginProcessId(procedureRespDTO.getId());
                            break;
                        }
                    }
                }
                batchOrderMapper.insert(batchOrderDO);
                batchRecordService.createBatchRecordByBatch(batchOrderDO);
            }
        }
    }

    @Override
    public void updateOrderForm(OrderFormSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderFormExists(updateReqVO.getId());
        // 更新
        OrderFormDO updateObj = BeanUtils.toBean(updateReqVO, OrderFormDO.class);
        orderFormMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderForm(String id) {
        // 校验存在
        validateOrderFormExists(id);
        // 删除
        orderFormMapper.deleteById(id);
    }

    private void validateOrderFormExists(String id) {
        if (orderFormMapper.selectById(id) == null) {
            throw exception(ORDER_FORM_NOT_EXISTS);
        }
    }

    @Override
    public OrderFormDO getOrderForm(String id) {
        return orderFormMapper.selectById(id);
    }

    @Override
    public PageResult<OrderFormDO> getOrderFormPage(OrderFormPageReqVO pageReqVO) {
        return orderFormMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrderFormDO> list(Wrapper<OrderFormDO> wrapper) {
        return orderFormMapper.selectList(wrapper);
    }

    @Override
    public void updateById(OrderFormDO orderForm) {
        orderFormMapper.updateById(orderForm);
    }

    /**
     * 订单提交
     * @param id 订单id
     */
    @Override
    public void orderSubmit(String id) {
        OrderFormDO orderFormDO = orderFormMapper.selectById(id);
        orderFormDO.setStatus(MCS_ORDER_STATUS_SUBMIT);
        orderFormMapper.updateById(orderFormDO);
        LambdaQueryWrapper<BatchOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchOrderDO::getOrderId, id);
        //提交从起始 到分支
        queryWrapper.isNull(BatchOrderDO::getPreBatchId);
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(queryWrapper);
        batchOrderService.batchSubmit(batchOrderDOList);
        //生成需求
        if (orderFormDO.getProcesStatus() != MCS_PROCES_STATUS_OUTSOURCING) {
            batchOrderDemandService.createBatchOrderDemandByOrder(orderFormDO);
        }
    }

    @Override
    public List<OrderFormDO> getOrderFormSelectList(OrderFormSelectListRespVO listRespVO) {
        return orderFormMapper.mySelectList(listRespVO);
    }

    @Override
    public void orderCancel(String id) {
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(BatchRecordDO::getId);
        queryWrapper.eq(BatchRecordDO::getOrderId, id);
        queryWrapper.notIn(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_COMPLETED, MCS_BATCH_RECORD_STATUS_RESCINDED);
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
        Set<String> collect = batchRecordDOList.stream().map(BatchRecordDO::getId).collect(Collectors.toSet());
        LambdaUpdateWrapper<BatchRecordStepDO> updateWrapper0 = new LambdaUpdateWrapper<>();
        updateWrapper0.set(BatchRecordStepDO::getStatus, MCS_STEP_STATUS_RESCINDED);
        updateWrapper0.notIn(BatchRecordStepDO::getStatus, MCS_STEP_STATUS_COMPLETED, MCS_STEP_STATUS_RESCINDED);
        updateWrapper0.in(BatchRecordStepDO::getBatchRecordId, collect);
        batchRecordStepMapper.update(updateWrapper0);
        LambdaUpdateWrapper<BatchRecordDO> updateWrapper1 = new LambdaUpdateWrapper<>();
        updateWrapper1.set(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_RESCINDED);
        updateWrapper1.eq(BatchRecordDO::getOrderId, id);
        updateWrapper1.notIn(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_COMPLETED, MCS_BATCH_RECORD_STATUS_RESCINDED);
        batchRecordMapper.update(updateWrapper1);
        LambdaUpdateWrapper<BatchOrderDO> updateWrapper2 = new LambdaUpdateWrapper<>();
        updateWrapper2.set(BatchOrderDO::getStatus, MCS_BATCH_STATUS_RESCINDED);
        updateWrapper2.eq(BatchOrderDO::getOrderId, id);
        updateWrapper2.notIn(BatchOrderDO::getStatus, MCS_BATCH_STATUS_RESCINDED, MCS_BATCH_STATUS_COMPLETED);
        batchOrderMapper.update(updateWrapper2);
        LambdaUpdateWrapper<OrderFormDO> updateWrapper3 = new LambdaUpdateWrapper<>();
        updateWrapper3.set(OrderFormDO::getStatus, MCS_ORDER_STATUS_RESCINDED);
        updateWrapper3.eq(OrderFormDO::getId, id);
        updateWrapper3.ne(OrderFormDO::getStatus, MCS_ORDER_STATUS_COMPLETED);
        orderFormMapper.update(updateWrapper3);
        distributionApplicationService.applicationCancelByOrderId(id);
    }

    @Override
    public void orderDelete(String id) {
        batchRecordMapper.delete(BatchRecordDO::getOrderId, id);
        batchOrderMapper.delete(BatchOrderDO::getOrderId, id);
        orderFormMapper.delete(OrderFormDO::getId, id);
        batchOrderDemandMapper.delete(BatchOrderDemandDO::getOrderId, id);
        batchDemandRecordMapper.delete(BatchDemandRecordDO::getOrderId, id);
        distributionApplicationService.applicationCancelByOrderId(id);
    }

    @Override
    public void generateDemandByOrderIds(List<String> orderIdList) {
        for (String id : orderIdList) {
            orderSubmit(id);
        }
    }

    @Override
    public void orderIssued(String id) {
        OrderFormDO orderFormDO = orderFormMapper.selectById(id);
        orderFormDO.setIssued(true);
        orderFormMapper.updateById(orderFormDO);
        List<String> list = Collections.singletonList(orderFormDO.getMaterialCode());
        orderRestService.updateOrderMaterial(orderFormDO.getOrderNumber(), list);
        List<BatchOrderDO> batchOrderDOList = batchOrderMapper.selectList(BatchOrderDO::getOrderId, id);
        for (BatchOrderDO batchOrderDO : batchOrderDOList) {
            batchOrderService.batchIssuance(batchOrderDO.getId());
        }
    }

    @Override
    public List<ScheduleResourceType> getResourceDemandByOrderId(Collection<String> orderIdList) {
        List<OrderFormDO> list = orderFormMapper.selectBatchIds(orderIdList);
        List<ScheduleResourceType> result = new ArrayList<>();
        Set<String> typeSet = new HashSet<>();
        for (OrderFormDO orderFormDO : list) {
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
            for (ProcedureRespDTO process : technology.getProcedureList()) {
                if (ProcedureRespDTO.isIgnoreProcedure(process)) continue;
                for (ProcedureDetailRespDTO resource : process.getResourceList()) {
                    if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) continue;
                    if (typeSet.contains(resource.getResourcesTypeId())) continue;
                    typeSet.add(resource.getResourcesTypeId());
                    if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_TOOL) result.add(resourceToToolBean(resource));
                }
                for (StepRespDTO step : process.getStepList()) {
                    for (StepDetailRespDTO resource : step.getResourceList()) {
                        if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_DEVICE) continue;
                        if (typeSet.contains(resource.getResourcesTypeId())) continue;
                        typeSet.add(resource.getResourcesTypeId());
                        if (resource.getResourcesType() == PROCESS_RESOURCES_TYPE_CUTTING) result.add(resourceToCuttingBean(resource));
                    }
                }
            }
        }
        return result;
    }

    private ScheduleResourceType resourceToCuttingBean(StepDetailRespDTO resource) {
        ScheduleResourceType bean = new ScheduleResourceType();
        bean.setCode(resource.getCode());
        bean.setName(resource.getName());
        bean.setResourcesType(resource.getResourcesType());
        bean.setCutterGroupCode(resource.getCutterGroupCode());
        bean.setCutterNum(resource.getCutternum());
        bean.setTaperTypeName(resource.getHiltMark());
        bean.setResourcesTypeId(resource.getResourcesTypeId());
        return bean;
    }

    private ScheduleResourceType resourceToToolBean(ProcedureDetailRespDTO resource) {
        ScheduleResourceType bean = new ScheduleResourceType();
        bean.setCode(resource.getCode());
        bean.setName(resource.getName());
        bean.setResourcesType(resource.getResourcesType());
        bean.setMaterialName(resource.getMaterialName());
        bean.setResourcesTypeId(resource.getResourcesTypeId());
        bean.setMaterialNumber(resource.getMaterialNumber());
        return bean;
    }
}
