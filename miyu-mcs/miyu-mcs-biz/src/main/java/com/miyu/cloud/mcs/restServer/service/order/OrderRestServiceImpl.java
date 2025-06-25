package com.miyu.cloud.mcs.restServer.service.order;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.miyu.cloud.mcs.enums.DictConstants.PMS_MATERIAL_STATUS_PENDING_PROCESSING;

@Service
@Validated
public class OrderRestServiceImpl implements OrderRestService {

    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private OrderFormMapper orderFormMapper;

    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private TechnologyRestService technologyRestService;

    //首任务 外协通知项目
    @Override
    public void outsourcingBegin(BatchOrderDO batchOrderDO) {
        List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(BatchRecordDO::getBatchId, batchOrderDO.getId());
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchOrderDO.getOrderId());
        ProcessPlanDetailRespDTO technologyByIdCache = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        List<ProcedureRespDTO> procedureList = technologyByIdCache.getProcedureList();
        for (ProcedureRespDTO procedureRespDTO : procedureList) {
            if (ProcedureRespDTO.isIgnoreProcedure(procedureRespDTO)) continue;
            for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                orderMaterialUpdate(batchRecordDO.getBarCode(), orderFormDO, procedureRespDTO.getProcedureNum(), PMS_MATERIAL_STATUS_PENDING_PROCESSING);
            }
        }
    }

    //工步任务完成后 通知项目物料变更(物码,进度)
    @Override
    public void orderMaterialUpdate(String barCode, OrderFormDO orderFormDO, String procedureNum, Integer status) {
        OrderMaterialRelationUpdateDTO orderMaterial = new OrderMaterialRelationUpdateDTO();
        orderMaterial.setMaterialCode(barCode);
        orderMaterial.setUpdateCode(barCode);
        orderMaterial.setVariableCode(barCode);
        orderMaterial.setPlanItemId(orderFormDO.getOrderNumber());
        orderMaterial.setOrderNumber(orderFormDO.getOrderNumber());
        orderMaterial.setProductCode(orderFormDO.getPartNumber());
        orderMaterial.setMaterialStatus(status);
        orderMaterial.setStep(procedureNum);
        CommonResult<String> result = pmsOrderMaterialRelationApi.orderMaterialUpdate(orderMaterial);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
    }

    //一生多加工 编码添加
    @Override
    public void createMaterialInOrder(OrderFormDO orderFormDO, String barCode, String procedureNum) {
        OrderMaterialRelationUpdateDTO orderMaterial = new OrderMaterialRelationUpdateDTO();
        orderMaterial.setOrderNumber(orderFormDO.getOrderNumber());
        orderMaterial.setMaterialCode(barCode);
        orderMaterial.setStep(procedureNum);
        CommonResult<String> result = pmsOrderMaterialRelationApi.orderMaterialSlice(orderMaterial);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
    }

    //校验物料是否可使用
    @Override
    public boolean checkMaterialUsageInfo(Collection<String> barCodeList) {
        CommonResult<List<OrderMaterialRelationRespDTO>> result = pmsOrderMaterialRelationApi.selectByMaterialCodes(barCodeList);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        return result.getData().size() == 0;
    }

    //更新订单物料使用情况
    @Override
    public void updateOrderMaterial(String orderNumber, List<String> barCodeList) {
        CommonResult<String> result = pmsOrderMaterialRelationApi.orderMaterialFill(orderNumber, barCodeList);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
    }
}
