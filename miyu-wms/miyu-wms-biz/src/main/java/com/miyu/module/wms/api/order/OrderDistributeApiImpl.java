package com.miyu.module.wms.api.order;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.mateiral.dto.CarryTrayStatusDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.*;
import com.miyu.module.wms.convert.movewarehousedetail.MoveWarehouseDetailConvert;
import com.miyu.module.wms.convert.outwarehousedetail.OutWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.CallMaterialServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.CallTrayServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.ToolDistributionServiceImpl;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.DictConstants.*;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
@Slf4j
public class OrderDistributeApiImpl implements OrderApi {

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private CallMaterialServiceImpl callMaterialService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private CarryTaskService carryTaskService;
    @Resource
    private CallTrayServiceImpl callTrayService;
    @Resource
    private ToolDistributionServiceImpl toolDistributionService;
    @Getter
    private List<ProductionOrderRespDTO> cacheProductionOrderList = new ArrayList<>();
    // 入库搬运  物料类型 , 呼叫的托盘id 映射 同一类型物料(起始和目标仓库一致)入库,只呼叫一个托盘就行了
    private Map<String,String> materialConfigInTrayMap = new HashMap<>();


    @Override
    public CommonResult<List<String>> orderDistribute(List<OrderReqDTO> orderReqDTOList) {
        // 创建订单失败的物料库存ID
        List<String> failMaterialStockIds = new ArrayList<>();

        List<OrderReqDTO> moveOrderReqDTOList = new ArrayList<>();
        List<OrderReqDTO> outOrderReqDTOList = new ArrayList<>();
        List<OrderReqDTO> inOrderReqDTOList = new ArrayList<>();

        for (OrderReqDTO orderReqDTO : orderReqDTOList) {
            if(orderReqDTO == null || StringUtils.isBlank(orderReqDTO.getChooseStockId())){
                throw exception(PARAM_NOT_NULL);
            }
            if(StringUtils.isAnyBlank(orderReqDTO.getOrderNumber())
                    || orderReqDTO.getOrderType() == null
                    || orderReqDTO.getQuantity() < 1){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(PARAM_NOT_NULL);
            }
            Integer orderType = orderReqDTO.getOrderType();
            if(orderType == null
                    || orderType < WMS_ORDER_TYPE_PURCHASE_IN
                    || orderType > DictConstants.WMS_ORDER_TYPE_LOSS_OUT){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(PARAM_NOT_NULL);
            }


            String startWarehouseId = orderReqDTO.getStartWarehouseId();
            String targetWarehouseId = orderReqDTO.getTargetWarehouseId();

            if(materialStockService.validateMaterialInLocked(orderReqDTO.getChooseStockId(),orderReqDTO.getStartWarehouseId(),orderReqDTO.getTargetWarehouseId())){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(WAREHOUSE_LOCATION_LOCKED);
            }

            // 移库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE
                    && orderType <= DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE){

                //校验参数
                if(StringUtils.isAnyBlank(startWarehouseId)){
                    failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                    throw exception(PARAM_NOT_NULL);
                }
                //校验参数
                if(StringUtils.isAnyBlank(targetWarehouseId)){
                    failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                    throw exception(PARAM_NOT_NULL);
                }
                moveOrderReqDTOList.add(orderReqDTO);
            }
            // 出库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_SALE_OUT
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_OUT
                    || WMS_ORDER_TYPE_LOSS_OUT.equals(orderType)){

                //校验参数
                /*if(StringUtils.isAnyBlank(startWarehouseId)){
                    failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                    continue;
                }*/
                //校验参数
                /*if(StringUtils.isAnyBlank(targetWarehouseId)){
                    failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                    continue;
                }*/
                outOrderReqDTOList.add(orderReqDTO);
            }
            // 入库操作
            if(orderType >= WMS_ORDER_TYPE_PURCHASE_IN
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_IN
                    || WMS_ORDER_TYPE_MATERIAL_IN.equals(orderType)
                    || WMS_ORDER_TYPE_PROFIT_IN.equals(orderType)){

                //校验参数
//                if(StringUtils.isAnyBlank(targetWarehouseId)){
//                    failMaterialStockIds.add(orderReqDTO.getChooseStockId());
//                    throw exception(PARAM_NOT_NULL);
//                }
                inOrderReqDTOList.add(orderReqDTO);
            }
        }

        // 查看是否存在未审批的物料类型
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockByIds(orderReqDTOList.stream().map(OrderReqDTO::getChooseStockId).distinct().collect(Collectors.toList()));
        long count = materialStockList.stream().filter(materialStockDO -> !Objects.equals(materialStockDO.getApprovalStatus(),2)).count();
        if(count > 0){
            throw exception(MATERIAL_STOCK_NOT_APPROVED);
        }

        // 移库操作，创建移库单
        if(!moveOrderReqDTOList.isEmpty())failMaterialStockIds.addAll(moveWarehouseDetailService.createBatchMoveWarehouseDetail(moveOrderReqDTOList));
        if(!outOrderReqDTOList.isEmpty())failMaterialStockIds.addAll(outWarehouseDetailService.createBatchOutWarehouseDetail(outOrderReqDTOList));
        if(!inOrderReqDTOList.isEmpty())failMaterialStockIds.addAll(inWarehouseDetailService.createBatchInWarehouseDetail(inOrderReqDTOList));

        failMaterialStockIds = failMaterialStockIds.stream().distinct().collect(Collectors.toList());
        return CommonResult.success(failMaterialStockIds);
    }

    @Override
    public CommonResult<List<OrderReqDTO>> orderList(List<OrderReqDTO> orderReqDTOList) {
        List<OrderReqDTO> result = new ArrayList<>();

        List<String> moveOrderNumbers = new ArrayList<>();
        List<String> outOrderNumbers = new ArrayList<>();
        List<String> inOrderNumbers = new ArrayList<>();

        for (OrderReqDTO orderReqDTO : orderReqDTOList) {

            if(orderReqDTO == null
                    || orderReqDTO.getOrderType() == null
                    || StringUtils.isAnyBlank(orderReqDTO.getOrderNumber())){
                throw exception(PARAM_NOT_NULL);
            }

            Integer orderType = orderReqDTO.getOrderType();

            if(orderType < WMS_ORDER_TYPE_PURCHASE_IN
                    || orderType > DictConstants.WMS_ORDER_TYPE_LOSS_OUT){
                throw exception(UNKNOWN_STATUS);
            }

            // 移库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE
                    && orderType <= DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE){

                moveOrderNumbers.add(orderReqDTO.getOrderNumber());
            }
            // 出库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_SALE_OUT
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_OUT
                    || WMS_ORDER_TYPE_LOSS_OUT.equals(orderType)){

                outOrderNumbers.add(orderReqDTO.getOrderNumber());
            }
            // 入库操作
            if(orderType >= WMS_ORDER_TYPE_PURCHASE_IN
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_IN
                    || WMS_ORDER_TYPE_MATERIAL_IN.equals(orderType)
                    || WMS_ORDER_TYPE_PROFIT_IN.equals(orderType)){
                inOrderNumbers.add(orderReqDTO.getOrderNumber());
            }
        }

        if(!moveOrderNumbers.isEmpty())result.addAll(moveWarehouseDetailService.getBatchMoveOrderList(moveOrderNumbers));
        if(!outOrderNumbers.isEmpty())result.addAll(outWarehouseDetailService.getBatchOutOrderList(outOrderNumbers));
        if(!inOrderNumbers.isEmpty())result.addAll(inWarehouseDetailService.getBatchInOrderList(inOrderNumbers));

        return CommonResult.success(result);
    }

    @Override
    public CommonResult<List<OrderReqDTO>> getOrderListByChooseBarCode(String barCode, Integer type) {
        if(type == null || type < 1 || type > 3){
            List<OrderReqDTO> result = new ArrayList<>();
            result.addAll(moveWarehouseDetailService.getMoveWarehouseDetailByChooseBarCode(barCode));
            result.addAll(outWarehouseDetailService.getOutWarehouseDetailByChooseBarCode(barCode));
            result.addAll(inWarehouseDetailService.getInWarehouseDetailByChooseBarCode(barCode));
            return CommonResult.success(result);
        }
        if(type == 1){
            return CommonResult.success(inWarehouseDetailService.getInWarehouseDetailByChooseBarCode(barCode));
        }
        if(type == 2){
            return CommonResult.success(outWarehouseDetailService.getOutWarehouseDetailByChooseBarCode(barCode));
        }
        if(type == 3){
            return CommonResult.success(moveWarehouseDetailService.getMoveWarehouseDetailByChooseBarCode(barCode));
        }
        return CommonResult.error(UNKNOWN_TYPE);
    }

    @Override
    public CommonResult<List<OrderReqDTO>> getOrderListByOrderTypeAndBatchNumber(OrderReqDTO orderReqDTO) {
        List<OrderReqDTO> result = new ArrayList<>();
        if(orderReqDTO == null
                || orderReqDTO.getOrderType() == null
                || StringUtils.isAnyBlank(orderReqDTO.getBatchNumber())){
            return CommonResult.success(result);
        }
        Integer orderType = orderReqDTO.getOrderType();
        // 移库操作
        if(orderType >= DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE
                && orderType <= DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE){

            result = moveWarehouseDetailService.getMoveOrderListByOrderTypeAndBatchNumber(orderReqDTO.getOrderType(),orderReqDTO.getBatchNumber());
        }
        // 出库操作
        if(orderType >= DictConstants.WMS_ORDER_TYPE_SALE_OUT
                && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_OUT
                || WMS_ORDER_TYPE_LOSS_OUT.equals(orderType)){
            result = outWarehouseDetailService.getOutOrderListByOrderTypeAndBatchNumber(orderReqDTO.getOrderType(),orderReqDTO.getBatchNumber());
        }
        // 入库操作
        if(orderType >= WMS_ORDER_TYPE_PURCHASE_IN
                && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_IN
                || WMS_ORDER_TYPE_MATERIAL_IN.equals(orderType)
                || WMS_ORDER_TYPE_PROFIT_IN.equals(orderType)){
            result = inWarehouseDetailService.getInOrderListByOrderTypeAndBatchNumber(orderReqDTO.getOrderType(), orderReqDTO.getBatchNumber());
        }
        return CommonResult.success(result);
    }

    @Override
    public CommonResult<List<String>> orderPurchaseInDistribute(List<OrderReqDTO> orderReqDTOList) {
        // 创建订单失败的物料库存ID
        List<String> failMaterialStockIds = new ArrayList<>();

        List<OrderReqDTO> inOrderReqDTOList = new ArrayList<>();
        for (OrderReqDTO orderReqDTO : orderReqDTOList) {
            if(orderReqDTO == null || StringUtils.isBlank(orderReqDTO.getMaterialConfigId())){
                throw exception(PARAM_NOT_NULL);
            }

            if(StringUtils.isAnyBlank(orderReqDTO.getOrderNumber()) || orderReqDTO.getQuantity() < 1){
                failMaterialStockIds.add(orderReqDTO.getMaterialConfigId());
                throw exception(PARAM_NOT_NULL);
            }

            Integer orderType = orderReqDTO.getOrderType();
            // 入库操作
            if(!WMS_ORDER_TYPE_PURCHASE_IN.equals(orderType)
                    && !WMS_ORDER_TYPE_MATERIAL_IN.equals(orderType)){
                throw exception(UNKNOWN_STATUS);
            }

            // 入库操作
            inOrderReqDTOList.add(orderReqDTO);
        }

        // 采购入库单创建
        if(!inOrderReqDTOList.isEmpty())failMaterialStockIds.addAll(inWarehouseDetailService.createBatchInWarehouseDetail(inOrderReqDTOList));

        failMaterialStockIds = failMaterialStockIds.stream().distinct().collect(Collectors.toList());
        return CommonResult.success(failMaterialStockIds);
    }


    @Override
    public CommonResult<List<String>> orderPurchaseReturnInDistribute(List<OrderReqDTO> orderReqDTOList) {
        // 创建订单失败的物料库存ID
        List<String> failMaterialStockIds = new ArrayList<>();

        List<OrderReqDTO> inOrderReqDTOList = new ArrayList<>();
        for (OrderReqDTO orderReqDTO : orderReqDTOList) {
            if(orderReqDTO == null || StringUtils.isBlank(orderReqDTO.getChooseStockId())){
                throw exception(PARAM_NOT_NULL);
            }
            //采购退货入库
            orderReqDTO.setOrderType(WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN);
            if(StringUtils.isAnyBlank(orderReqDTO.getOrderNumber()) || orderReqDTO.getQuantity() < 1){
                failMaterialStockIds.add(orderReqDTO.getMaterialConfigId());
                throw exception(PARAM_NOT_NULL);
            }

            String targetWarehouseId = orderReqDTO.getTargetWarehouseId();
            //校验参数
            if(StringUtils.isAnyBlank(targetWarehouseId)){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(PARAM_NOT_NULL);
            }

            // 入库操作
            inOrderReqDTOList.add(orderReqDTO);
        }

        // 采购退货入库 创建
        if(!inOrderReqDTOList.isEmpty())failMaterialStockIds.addAll(inWarehouseDetailService.createBatchInWarehouseDetail(inOrderReqDTOList));

        failMaterialStockIds = failMaterialStockIds.stream().distinct().collect(Collectors.toList());
        return CommonResult.success(failMaterialStockIds);
    }


    @Override
    public CommonResult<List<String>> orderUpdateStatus(List<OrderReqDTO> orderReqDTOList) {
        // 创建订单失败的物料库存ID
        List<String> failMaterialStockIds = new ArrayList<>();

        List<OrderReqDTO> moveOrderReqDTOList = new ArrayList<>();
        List<OrderReqDTO> outOrderReqDTOList = new ArrayList<>();
        List<OrderReqDTO> inOrderReqDTOList = new ArrayList<>();

        for (OrderReqDTO orderReqDTO : orderReqDTOList) {
            if(orderReqDTO == null || StringUtils.isBlank(orderReqDTO.getChooseStockId())){
                continue;
            }
            Integer orderType = orderReqDTO.getOrderType();

            if(StringUtils.isAnyBlank(orderReqDTO.getChooseStockId())
                    || orderReqDTO.getOrderType() == null
                    || orderReqDTO.getOrderStatus() == null
                    || orderType == null){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(PARAM_NOT_NULL);
            }

            if(orderType < WMS_ORDER_TYPE_PURCHASE_IN
                    || orderType > DictConstants.WMS_ORDER_TYPE_LOSS_OUT){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(UNKNOWN_STATUS);
            }

            Integer orderStatus = orderReqDTO.getOrderStatus();
            if(orderStatus == WMS_ORDER_DETAIL_STATUS_2
                    || orderStatus == WMS_ORDER_DETAIL_STATUS_3
                    || orderStatus == WMS_ORDER_DETAIL_STATUS_4){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
                throw exception(UNKNOWN_STATUS);
            }

            // 移库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE
                    && orderType <= DictConstants.WMS_ORDER_TYPE_TRANSFER_MOVE){
                moveOrderReqDTOList.add(orderReqDTO);
            }
            // 出库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_SALE_OUT
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_OUT
                    || WMS_ORDER_TYPE_LOSS_OUT.equals(orderType)){
                outOrderReqDTOList.add(orderReqDTO);
            }
            // 入库操作
            if(orderType >= WMS_ORDER_TYPE_PURCHASE_IN
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_IN
                    || WMS_ORDER_TYPE_MATERIAL_IN.equals(orderType)
                    || WMS_ORDER_TYPE_PROFIT_IN.equals(orderType)){
                inOrderReqDTOList.add(orderReqDTO);
            }
        }

        // 移库操作，创建移库单
        for (OrderReqDTO orderReqDTO : moveOrderReqDTOList) {
            if(!moveWarehouseDetailService.updateBatchMoveWarehouseDetailStateByMaterialStockId(orderReqDTO.getChooseStockId(), orderReqDTO.getOrderStatus())){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
            }
        }
        for (OrderReqDTO orderReqDTO : outOrderReqDTOList) {
            // 更新状态待出库
            if(WMS_ORDER_DETAIL_STATUS_1.equals(orderReqDTO.getOrderStatus())){
                // 填入刀具的配送目的地
                toolDistributionService.fillInOutWarehouseDetailTargetLocation(orderReqDTO.getChooseStockId(), orderReqDTO.getTargetWarehouseId());
            }
            if(!outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(orderReqDTO.getChooseStockId(), orderReqDTO.getOrderStatus())){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
            }
        }
        for (OrderReqDTO orderReqDTO : inOrderReqDTOList) {
            if(!inWarehouseDetailService.updateBatchInWarehouseDetailStateByMaterialStockId(orderReqDTO.getChooseStockId(), orderReqDTO.getOrderStatus())){
                failMaterialStockIds.add(orderReqDTO.getChooseStockId());
            }
        }

        failMaterialStockIds = failMaterialStockIds.stream().distinct().collect(Collectors.toList());
        return CommonResult.success(failMaterialStockIds);
    }


    @Override
    public CommonResult<List<String>> updateTargetWarehouse(List<OrderUpdateDTO> orderUpdateDTOList) {
        // 创建订单失败的物料库存ID
        List<String> failMaterialStockIds = new ArrayList<>();

        List<OrderUpdateDTO> outOrderUpdateDTOList = new ArrayList<>();

        for (OrderUpdateDTO orderUpdateDTO : orderUpdateDTOList) {

            if(orderUpdateDTO == null
                    || StringUtils.isBlank(orderUpdateDTO.getMaterialStockId())
                    || orderUpdateDTO.getOrderType() == null){
                throw exception(PARAM_NOT_NULL);
            }
            Integer orderType = orderUpdateDTO.getOrderType();

            if(orderType < WMS_ORDER_TYPE_PURCHASE_IN || orderType > DictConstants.WMS_ORDER_TYPE_LOSS_OUT){
                failMaterialStockIds.add(orderUpdateDTO.getMaterialStockId());
                throw exception(UNKNOWN_STATUS);
            }

            // 出库操作
            if(orderType >= DictConstants.WMS_ORDER_TYPE_SALE_OUT
                    && orderType <= DictConstants.WMS_ORDER_TYPE_OTHER_OUT
                    || WMS_ORDER_TYPE_LOSS_OUT.equals(orderType)){
                outOrderUpdateDTOList.add(orderUpdateDTO);
            }

        }

        for (OrderUpdateDTO orderUpdateDTO : outOrderUpdateDTOList) {
            if(!outWarehouseDetailService.updateBatchOutWarehouseDetail(orderUpdateDTO)){
                failMaterialStockIds.add(orderUpdateDTO.getMaterialStockId());
                throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
            }
        }


        failMaterialStockIds = failMaterialStockIds.stream().distinct().collect(Collectors.toList());
        return CommonResult.success(failMaterialStockIds);
    }

    @Override
    public CommonResult<List<ProductionOrderRespDTO>> autoProductionDispatch(List<ProductionOrderReqDTO> productionOrderDTOList) {
        // 先置空 然后挨个塞
        this.cacheProductionOrderList.clear();
        if(CollectionUtils.isAnyEmpty(productionOrderDTOList)){
            return CommonResult.success(Collections.emptyList());
        }
        List<ProductionOrderRespDTO> result = new ArrayList<>();

        HashSet<String> warehouseIds = new HashSet<>();
        Map<String, ProductionOrderReqDTO> productionOrderReqDTOMap = new HashMap<>();
        productionOrderDTOList.forEach(productionOrderReqDTO -> {
            productionOrderReqDTOMap.put(productionOrderReqDTO.getBarCode(), productionOrderReqDTO);
            warehouseIds.add(productionOrderReqDTO.getTargetWarehouseId());
        });
        // 根据物料条码获取物料库存信息
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(productionOrderReqDTOMap.keySet());
        Map<String, MaterialStockDO> barCodeMaterialStockDOMap = new HashMap<>();
        Set<String> materialStockIds = new HashSet<>();
        materialStockList.forEach(materialStockDO -> {
            barCodeMaterialStockDOMap.put(materialStockDO.getBarCode(), materialStockDO);
            materialStockIds.add(materialStockDO.getId());
        });
        // 获取物料库存对应的出入库订单
        List<MaterialStockDO> materialStockHaveOrderList = materialStockService.checkChooseStockOrderExists(materialStockIds);

        // 根据仓库ID获取仓库信息
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseByIds(warehouseIds);
        Map<String, WarehouseDO> warehouseDOMap = CollectionUtils.convertMap(warehouseList, WarehouseDO::getId);

        one: for (ProductionOrderReqDTO productionOrderReqDTO : productionOrderDTOList) {
            if(!barCodeMaterialStockDOMap.containsKey(productionOrderReqDTO.getBarCode())){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
            MaterialStockDO materialStockDO = barCodeMaterialStockDOMap.get(productionOrderReqDTO.getBarCode());
            // 获取物料所在库区
            WarehouseAreaDO startWarehouseAreaDO = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
            if(startWarehouseAreaDO == null){
                throw exception(WAREHOUSE_NOT_EXISTS);
            }

            // 获取物料所在仓库
            WarehouseDO startWarehouseDO = warehouseService.getWarehouse(startWarehouseAreaDO.getWarehouseId());
            if(startWarehouseDO == null){
                throw exception(WAREHOUSE_NOT_EXISTS);
            }

            // step1: 在人工库 或 agv库 不做处理
            if(DictConstants.WMS_WAREHOUSE_TYPE_2.equals(startWarehouseDO.getWarehouseType())
                    ||DictConstants.WMS_WAREHOUSE_TYPE_4.equals(startWarehouseDO.getWarehouseType())){
                continue;
            }

            // 不在 存储位 接驳位 收货位 不做处理
            if(!DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(startWarehouseAreaDO.getAreaType())
                    && !DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(startWarehouseAreaDO.getAreaType())
                    && !DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(startWarehouseAreaDO.getAreaType())){
                // 等着啥时候在接驳区再说
                continue;
            }

            WarehouseDO targetWarehouseDO = null;
            if(productionOrderReqDTO.getTargetWarehouseId() != null){
                // step2: 校验物料所在仓库 是否与目标仓库一致  一致直接存起来，就当已经完成了
                if(startWarehouseDO.getId().equals(productionOrderReqDTO.getTargetWarehouseId())){
                    ProductionOrderRespDTO bean = BeanUtils.toBean(productionOrderReqDTO, ProductionOrderRespDTO.class);
                    result.add(bean);
                }
                // step3: 区分出库和移库   立体库→线体库 出库（去线体加工）  线体库→线体库 移库（机加工下一工序）  线体库→立体库 入库（去立体库装夹或卸夹）
                targetWarehouseDO = warehouseDOMap.get(productionOrderReqDTO.getTargetWarehouseId());
            }

            // 初始化
            ProductionOrderRespDTO resultProductionOrder = BeanUtils.toBean(productionOrderReqDTO, ProductionOrderRespDTO.class);
            // 起始仓库为 线体库  目标仓库为立体库 或 为null 则为入库
            if((DictConstants.WMS_WAREHOUSE_TYPE_3.equals(startWarehouseDO.getWarehouseType())
                    || DictConstants.WMS_WAREHOUSE_TYPE_5.equals(startWarehouseDO.getWarehouseType()))
                    && (targetWarehouseDO == null
                    || DictConstants.WMS_WAREHOUSE_TYPE_1.equals(targetWarehouseDO.getWarehouseType()))){
                // 入库
                resultProductionOrder.setOrderType(WMS_ORDER_TYPE_PRODUCE_IN);
            }
            if(!WMS_ORDER_TYPE_PRODUCE_IN.equals(resultProductionOrder.getOrderType()) && targetWarehouseDO == null){
                throw exception(PARAM_NOT_NULL);
            }
            if(
                    (
                        DictConstants.WMS_WAREHOUSE_TYPE_3.equals(startWarehouseDO.getWarehouseType())
                            || DictConstants.WMS_WAREHOUSE_TYPE_5.equals(startWarehouseDO.getWarehouseType())
                    )
                    && (
                        targetWarehouseDO == null
                            || DictConstants.WMS_WAREHOUSE_TYPE_3.equals(targetWarehouseDO.getWarehouseType())
                            || DictConstants.WMS_WAREHOUSE_TYPE_5.equals(targetWarehouseDO.getWarehouseType())
                    )
            ){
                if(!productionOrderReqDTO.getIsNeedMaterial()){
                    // 不要料 入库
                    // 入库
                    resultProductionOrder.setOrderType(WMS_ORDER_TYPE_PRODUCE_IN);
                    // 更新目标仓库 先置空  一会填
                    resultProductionOrder.setTargetWarehouseId(null);
                }else {
                    // 移库
                    resultProductionOrder.setOrderType(WMS_ORDER_TYPE_PRODUCE_MOVE);
                }
            }else if(
                    DictConstants.WMS_WAREHOUSE_TYPE_1.equals(startWarehouseDO.getWarehouseType())
                    && targetWarehouseDO!=null
                    && (
                        DictConstants.WMS_WAREHOUSE_TYPE_3.equals(targetWarehouseDO.getWarehouseType())
                        || DictConstants.WMS_WAREHOUSE_TYPE_5.equals(targetWarehouseDO.getWarehouseType())
            )){
                // 出库
                // 查看要料标识
                if(!productionOrderReqDTO.getIsNeedMaterial()){
                    //不要料 则不处理
                    // 等着啥时候要料再说
                    continue;
                }
                resultProductionOrder.setOrderType(WMS_ORDER_TYPE_PRODUCE_OUT);
            }else{
                throw exception(UNKNOWN_STATUS);
            }
            // step4: 校验物料是否已存在出入库单
            for (MaterialStockDO material : materialStockHaveOrderList) {
                if(material.getId().equals(materialStockDO.getId())){
                    // 如果存在出入库单
                    if(StringUtils.isBlank(material.getRealStockId())
                            || material.getRealStockId().equals(materialStockDO.getId())){
                        // 判断是否已被分拣，如果没被分拣，或者被分拣的就是此出库物料，则可以不做处理
                        // todo 等着分拣吧
                        continue one;
                    }
                }
            }

            {
                // 把自动调度单中可进行搬运的物料缓存起来---也可能还需要再拣选
                resultProductionOrder.setAtWarehouseId(startWarehouseDO.getId());
                resultProductionOrder.setMaterialStock(BeanUtils.toBean(materialStockDO, MaterialStockRespDTO.class));
                this.cacheProductionOrderList.add(resultProductionOrder);
            }

            // step5: 自动化线体库 从签收位 到 其他仓库的调度---生成任务之前要呼叫托盘
           /* if(DictConstants.WMS_WAREHOUSE_TYPE_5.equals(startWarehouseDO.getWarehouseType())
                    && DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(startWarehouseAreaDO.getAreaType())){


            }else {*/

            MaterialStockDO materialAtLocation = null;
            try{
                materialAtLocation = materialStockService.getMaterialAtLocationByMaterialStock(materialStockDO);
            }catch (Exception e){log.error(e.getMessage());continue one;}
            if(resultProductionOrder.getTargetWarehouseId() == null){
                if(StringUtils.isBlank(materialAtLocation.getDefaultWarehouseId())){
                    throw exception(MATERIAL_TYPE_DEFAULT_STORAGE_AREA_NOT_EXISTS);
                }
                // 填入默认存放仓库
                resultProductionOrder.setTargetWarehouseId(materialAtLocation.getDefaultWarehouseId());
                productionOrderReqDTO.setTargetWarehouseId(materialAtLocation.getDefaultWarehouseId());
                targetWarehouseDO = warehouseService.getWarehouse(resultProductionOrder.getTargetWarehouseId());
            }
            // step5: 查看此物料所在托盘上是否存在其他物料  并且在此自动调度单中
            // 获得容器上的所有物料库存
            List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(materialAtLocation.getId());

            for (MaterialStockDO stockDO : allMaterialStockList) {
                // 过滤出物料
                if(stockDO.getMaterialType().equals(WMS_MATERIAL_TYPE_GJ)){
                    // 看其是否在此自动调度单中 并且与此物料的目标仓库一致
                    if(!productionOrderReqDTOMap.containsKey(stockDO.getBarCode())
                            || !productionOrderReqDTOMap.get(stockDO.getBarCode()).getTargetWarehouseId().equals(productionOrderReqDTO.getTargetWarehouseId())){
                        // 不在此调度单中 则不处理  // 目标仓库不一致 则不处理
                        // todo 等着分拣吧
                        continue one;
                    }
                }
            }

            try {
                // 看看是不是已经生成搬运任务了
                carryTaskService.checkCarryTask(materialAtLocation.getId());
            }catch (Exception e){log.error(e.getMessage());continue;}

            String orderNumber = null;

            // 物料如果在收货区  只生成呼叫托盘的任务就完事了
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(startWarehouseAreaDO.getAreaType())){

                CommonResult<String> callTrayResult = callTrayService.autoCallTrayLogic(materialAtLocation, targetWarehouseDO.getId(), productionOrderReqDTO.getTargetLocationId());

                if(callTrayResult.isSuccess()){
                    if(resultProductionOrder.getOrderType() == WMS_ORDER_TYPE_PRODUCE_MOVE){
                        // 尝试生成移库搬运
                        orderNumber = moveWarehouseDetailService.generateMoveWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseDO.getId(), targetWarehouseDO.getId(), resultProductionOrder.getTargetLocationId(), resultProductionOrder.getQuantity(), materialStockDO, callTrayResult.getData());
                    }else if(resultProductionOrder.getOrderType() == WMS_ORDER_TYPE_PRODUCE_IN){
                        //尝试生成入库搬运
                        orderNumber = inWarehouseDetailService.generateInWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseDO.getId(), targetWarehouseDO.getId(), resultProductionOrder.getQuantity(), materialStockDO, callTrayResult.getData());
                    }
                }
            }else {
                // 尝试生成出库搬运
                if(resultProductionOrder.getOrderType() == WMS_ORDER_TYPE_PRODUCE_OUT){
                    orderNumber = outWarehouseDetailService.tryGenerateOutWarehouseDetail(materialAtLocation, resultProductionOrder.getTargetLocationId(), startWarehouseDO.getId(), targetWarehouseDO.getId(), resultProductionOrder.getQuantity(), materialStockDO);
                }else if(resultProductionOrder.getOrderType() == WMS_ORDER_TYPE_PRODUCE_MOVE){
                    // 尝试生成移库搬运
                    orderNumber = moveWarehouseDetailService.tryGenerateMoveWarehouseDetail(materialAtLocation, resultProductionOrder.getTargetLocationId(),startWarehouseDO.getId(), targetWarehouseDO.getId(), resultProductionOrder.getQuantity(), materialStockDO);
                }else if(resultProductionOrder.getOrderType() == WMS_ORDER_TYPE_PRODUCE_IN){
                    //尝试生成入库搬运
                    orderNumber = inWarehouseDetailService.tryGenerateInWarehouseDetail(materialAtLocation, startWarehouseDO.getId(), targetWarehouseDO.getId(), resultProductionOrder.getQuantity(), materialStockDO);
                }
            }
            if(orderNumber != null){
                resultProductionOrder.setOrderNumber(orderNumber);
                result.add(resultProductionOrder);
            }

//            }
        }


        return CommonResult.success(result);
    }

    @Override
    public CommonResult<List<SpecifiedTransportationRespDTO>> specifiedStorageSpaceTransportation(List<SpecifiedTransportationReqDTO> specifiedTransportationReqDTOList) {
        if(specifiedTransportationReqDTOList.isEmpty()){
            return CommonResult.success(Collections.emptyList());
        }
        List<SpecifiedTransportationRespDTO> result = new ArrayList<>();

        Map<String, SpecifiedTransportationReqDTO> specifiedTransportationReqDTOMap = new HashMap<>();
        specifiedTransportationReqDTOList.forEach(specifiedTransportationReqDTO -> {
            specifiedTransportationReqDTOMap.put(specifiedTransportationReqDTO.getBarCode(), specifiedTransportationReqDTO);
        });
        // 根据物料条码获取物料库存信息
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(specifiedTransportationReqDTOMap.keySet());
        Map<String, MaterialStockDO> barCodeMaterialStockDOMap = new HashMap<>();
        Set<String> materialStockIds = new HashSet<>();
        materialStockList.forEach(materialStockDO -> {
            barCodeMaterialStockDOMap.put(materialStockDO.getBarCode(), materialStockDO);
            materialStockIds.add(materialStockDO.getId());
        });
        // 获取物料库存对应的出入库订单
        List<MaterialStockDO> materialStockHaveOrderList = materialStockService.checkChooseStockOrderExists(materialStockIds);

        one: for (SpecifiedTransportationReqDTO specifiedTransportationReqDTO : specifiedTransportationReqDTOList) {
            if(!barCodeMaterialStockDOMap.containsKey(specifiedTransportationReqDTO.getBarCode())){
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
            MaterialStockDO materialStockDO = barCodeMaterialStockDOMap.get(specifiedTransportationReqDTO.getBarCode());
            // 获取物料所在库区
            WarehouseAreaDO startWarehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(specifiedTransportationReqDTO.getStartLocationId());
            if(startWarehouseArea == null){
                throw exception(WAREHOUSE_NOT_EXISTS);
            }
            // step1: 不在接驳区  不做处理
            if(!DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(startWarehouseArea.getAreaType())){
                // 等着啥时候在接驳区再说
                continue;
            }
            // 获取物料所在仓库
            WarehouseDO startWarehouseDO = warehouseService.getWarehouse(startWarehouseArea.getWarehouseId());
            if(startWarehouseDO == null){
                throw exception(WAREHOUSE_NOT_EXISTS);
            }
            WarehouseAreaDO targetWarehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(specifiedTransportationReqDTO.getTargetLocationId());
            if(targetWarehouseArea == null){
                throw exception(WAREHOUSE_NOT_EXISTS);
            }
            // step2: 校验物料所在仓库 是否与目标仓库一致  一致直接存起来，就当已经完成了
            if(startWarehouseDO.getId().equals(targetWarehouseArea.getWarehouseId())){
                SpecifiedTransportationRespDTO bean = BeanUtils.toBean(specifiedTransportationReqDTO, SpecifiedTransportationRespDTO.class);
                result.add(bean);
            }

            // step3: 校验物料是否已存在出入库单
            for (MaterialStockDO material : materialStockHaveOrderList) {
                if(material.getId().equals(materialStockDO.getId())){
                    // 如果存在出入库单
                    if(StringUtils.isBlank(material.getRealStockId())
                            || material.getRealStockId().equals(materialStockDO.getId())){
                        // 判断是否已被分拣，如果没被分拣，或者被分拣的就是此出库物料，则可以不做处理
                        // todo 等着分拣吧
                        continue one;
                    }
                }
            }


            // step4: 查看此物料所在托盘上是否存在其他物料  并且在此自动调度单中
            // 查询库位上绑定的物料库存
            List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationId(specifiedTransportationReqDTO.getStartLocationId());
            if(containerStockList.size() != 1){
                // 此物料所在库位绑定多个容器 不能呼叫
                throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
            }
            MaterialStockDO containerStock = containerStockList.get(0);

            // 获得容器上的所有物料库存
            List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(containerStock.getId());

            // step5: 校验物料是否在此自动调度单中 并且与目标库位一致
            for (MaterialStockDO stockDO : allMaterialStockList) {
                // 过滤出物料
                if(stockDO.getMaterialType().equals(WMS_MATERIAL_TYPE_GJ)){
                    // 看其是否在此自动调度单中 并且与此物料的目标库位一致
                    if(!specifiedTransportationReqDTOMap.containsKey(stockDO.getBarCode())
                            || !specifiedTransportationReqDTOMap.get(stockDO.getBarCode()).getTargetLocationId().equals(specifiedTransportationReqDTO.getTargetLocationId())){
                        // 不在此调度单中 则不处理  // 目标仓库不一致 则不处理
                        // todo 等着分拣吧
                        continue one;
                    }
                }
            }

            try {
                // step6: 看看是不是已经生成搬运任务了
                carryTaskService.checkCarryTask(containerStock.getId());
            }catch (Exception e){log.error(e.getMessage());continue;}

            // step7: 尝试生成移库搬运
            String  orderNumber = moveWarehouseDetailService.tryGenerateMoveWarehouseDetail(containerStock, specifiedTransportationReqDTO.getStartLocationId(), specifiedTransportationReqDTO.getTargetLocationId(), materialStockDO);

            if(orderNumber != null){
                SpecifiedTransportationRespDTO specifiedTransportationRespDTO = BeanUtils.toBean(specifiedTransportationReqDTO, SpecifiedTransportationRespDTO.class);
                specifiedTransportationRespDTO.setOrderNumber(orderNumber);
                result.add(specifiedTransportationRespDTO);
            }
        }
        return CommonResult.success(result);
    }


    /**
     * 线体库 自动调度
     * 入库：起始仓库为自动库
     * 出库：目标仓库为自动库
     * 移库：起始仓库为自动库
     */
    public void autoLine() {
        // 查询所有线体库
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseByTypeS(Arrays.asList(DictConstants.WMS_WAREHOUSE_TYPE_1,DictConstants.WMS_WAREHOUSE_TYPE_3, DictConstants.WMS_WAREHOUSE_TYPE_5));
        Set<String> autoWarehouseIds = CollectionUtils.convertSet(warehouseList, WarehouseDO::getId);
        List<InWarehouseDetailDO> waitInWarehouseList = inWarehouseDetailService.getWaitInWarehouseInWarehouseDetailList();
        waitInWarehouseList = waitInWarehouseList.stream().filter(i->autoWarehouseIds.contains(i.getStartWarehouseId())).collect(Collectors.toList());
        List<OutWarehouseDetailDO> waitOutWarehouseList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailList();
        waitOutWarehouseList = waitOutWarehouseList.stream().filter(o->autoWarehouseIds.contains(o.getTargetWarehouseId())).collect(Collectors.toList());
        List<MoveWarehouseDetailDO> waitMoveWarehouseList = moveWarehouseDetailService.getWaitMoveWarehouseMoveWarehouseDetailList();
        waitMoveWarehouseList = waitMoveWarehouseList.stream().filter(m->autoWarehouseIds.contains(m.getStartWarehouseId())).collect(Collectors.toList());

        // 入库
        for (InWarehouseDetailDO inWarehouseDetailDO : waitInWarehouseList) {
            String materialStockId = inWarehouseDetailDO.getMaterialStockId();
            MaterialStockDO materialStock = materialStockService.getMaterialStock(materialStockId);
            WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStock);
            // 物料在接驳库位 并且 物料所在仓库于入库单起始仓库一致
            if(warehouseAreaDO != null
                    && inWarehouseDetailDO.getStartWarehouseId().equals(warehouseAreaDO.getWarehouseId())){
                if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseAreaDO.getAreaType())){// 物料在接驳库区
                    try{
//                    if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(containerStock.getMaterialType())){
                        // 自动库 签入解锁 非自动库 点击入库解锁
                        if(warehouseLocationService.checkLocationLock(warehouseAreaDO.getJoinLocationId())){
                            continue;
                        }
                        CommonResult<String> inWarehouseResult = inWarehouseDetailService.inWarehouseAction(materialStock, warehouseAreaDO.getJoinLocationId());
                        if(inWarehouseResult.isSuccess() && inWarehouseResult.getData() != null){
                            inWarehouseDetailDO.setCarryTrayId(inWarehouseResult.getData());
                            inWarehouseDetailService.updateById(inWarehouseDetailDO);
                            // 解锁库位
//                            warehouseLocationService.unlockLocation(warehouseAreaDO.getJoinLocationId());
                        }
//                    }
                    }catch (Exception e){log.error(e.getMessage());}
                }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(warehouseAreaDO.getAreaType())){// 物料在收货库区
                    String mapKey = materialStock.getMaterialConfigId() + inWarehouseDetailDO.getStartWarehouseId() + inWarehouseDetailDO.getTargetWarehouseId();
                    try{
                        // 锁一下 key 防止操作过程中被删了
                        redissonClient.getLock(mapKey).tryLock(60,60, TimeUnit.SECONDS);
                        // 如果此类型物料(起始和目标仓库一致)已经呼叫过托盘 则不再呼叫
                        if(materialConfigInTrayMap.containsKey(mapKey)){
                            // 如果当前 入库单没有呼叫托盘 或者 呼叫的托盘与此入库单的托盘不一致 则 更新入库单的呼叫托盘id
                            if(StringUtils.isBlank(inWarehouseDetailDO.getCarryTrayId()) || !inWarehouseDetailDO.getCarryTrayId().equals(materialConfigInTrayMap.get(mapKey))){
                                inWarehouseDetailDO.setCarryTrayId(materialConfigInTrayMap.get(mapKey));
                                inWarehouseDetailService.updateById(inWarehouseDetailDO);
                            }
                            continue;
                        }
                        // 呼叫托盘
                        CommonResult<String> callTrayResult = callTrayService.autoCallTrayLogic(materialStock, inWarehouseDetailDO.getTargetWarehouseId(), warehouseAreaDO.getJoinLocationId());
                        if(callTrayResult.isSuccess()){
                            inWarehouseDetailDO.setCarryTrayId(callTrayResult.getData());
                            // 将此类型的物料的托盘id 存入map
                            materialConfigInTrayMap.put(mapKey, callTrayResult.getData());
                            inWarehouseDetailService.updateById(inWarehouseDetailDO);
                        }
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }finally {
                        redissonClient.getLock(mapKey).unlock();
                    }
                }
            }
        }

        // 出库
        for (OutWarehouseDetailDO outWarehouseDetailDO : waitOutWarehouseList) {
            String materialStockId = outWarehouseDetailDO.getMaterialStockId();
            MaterialStockDO materialStock = materialStockService.getMaterialStock(materialStockId);
            WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStock);
            // 物料在存储库位 并且 物料所在仓库于出库单起始仓库一致
            // 获取目标仓库
            String targetWarehouseId = outWarehouseDetailDO.getTargetWarehouseId();
            if(warehouseAreaDO != null
                    && DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(warehouseAreaDO.getAreaType())
                    && outWarehouseDetailDO.getStartWarehouseId().equals(warehouseAreaDO.getWarehouseId())){

                // 生成失败的位置就不再生成了
//                tryOutCarryWarehouseIds.add(targetWarehouseId);
                // 获取物料所在库位上的容器
                List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockListByLocationId(warehouseAreaDO.getJoinLocationId());
                if(containerStockList.size() == 1){
                    try{
                        MaterialStockDO containerStock = containerStockList.get(0);
                        // 1. 获得 容器上除托盘外的物料库存
                        List<MaterialStockDO> allMaterialStockList = callMaterialService.getAllMaterialStock(containerStock);
                        // 2. 将其转成map
                        Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);
                        // 3. 校验任务单
                        Set<String> orderIds = callMaterialService.checkOrderTask(targetWarehouseId, allMaterialStockMap);

                        // 4. 生成搬运任务
                        callMaterialService.createCarryTaskLogic(containerStockList.get(0), null, targetWarehouseId);

                        // 5. 填入操作人
                        callMaterialService.setOperatorInOrderTask(orderIds);
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                }

            }
        }

        // 移库
        for (MoveWarehouseDetailDO moveWarehouseDetailDO : waitMoveWarehouseList) {
            String materialStockId = moveWarehouseDetailDO.getMaterialStockId();
            MaterialStockDO materialStock = materialStockService.getMaterialStock(materialStockId);
            WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStock);
            // 物料在接驳库位 并且 物料所在仓库于入库单起始仓库一致
            // 获取目标仓库
            String targetWarehouseId = moveWarehouseDetailDO.getTargetWarehouseId();
            String targetLocationId = moveWarehouseDetailDO.getSignLocationId();
            if(warehouseAreaDO != null
                    && moveWarehouseDetailDO.getStartWarehouseId().equals(warehouseAreaDO.getWarehouseId())){
                if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(warehouseAreaDO.getAreaType())
                        || DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseAreaDO.getAreaType())){
                    try{
                        // 自动库 签入解锁 非自动库 点击入库解锁
                        if(warehouseLocationService.checkLocationLock(warehouseAreaDO.getJoinLocationId())){
                            continue;
                        }
                        moveWarehouseDetailService.moveWarehouseAction(warehouseAreaDO.getJoinLocationId(), targetWarehouseId, targetLocationId);
                        // 解锁库位
//                        warehouseLocationService.unlockLocation(warehouseAreaDO.getJoinLocationId());
                    }catch (Exception e){log.error(e.getMessage());}
                }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(warehouseAreaDO.getAreaType())){// 物料在收货库区
                    // 呼叫托盘
                    CommonResult<String> callTrayResult = callTrayService.autoCallTrayLogic(materialStock, moveWarehouseDetailDO.getTargetWarehouseId(),targetLocationId);
                    if(callTrayResult.isSuccess()){
                        moveWarehouseDetailDO.setCarryTrayId(callTrayResult.getData());
                        moveWarehouseDetailService.updateById(moveWarehouseDetailDO);
                    }
                }
            }
        }

    }


    @Override
    @Cacheable(value= "trayStatus#10s", key = "'callTrayStatus'", unless = "#result == null")
    public CommonResult<Map<String, CarryTrayStatusDTO>> getCallTrayStatus() {
        // 物料条码 呼叫托盘搬运状态 1 未知，2 搬运中，3 已抵达
        Map<String, CarryTrayStatusDTO> carryTrayStatusMap = new HashMap<>();
        // 查询所有待入库的订单
        List<InWarehouseDetailDO> waitInWarehouseList = inWarehouseDetailService.getWaitInWarehouseInWarehouseDetailList();
        // 查询所有待移库的订单
        List<MoveWarehouseDetailDO> waitMoveWarehouseList =moveWarehouseDetailService.getWaitMoveWarehouseMoveWarehouseDetailList();

        // 获得被呼叫的托盘
        Set<String> carryTrayIds = new HashSet<>();
        waitInWarehouseList.forEach(i-> {
            CarryTrayStatusDTO carryTrayStatusDTO = new CarryTrayStatusDTO();
            if(StringUtils.isNotBlank(i.getCarryTrayId())){
                carryTrayIds.add(i.getCarryTrayId());
                carryTrayStatusDTO.setTrayId(i.getCarryTrayId());
                carryTrayStatusDTO.setStartWarehouseId(i.getStartWarehouseId());
                carryTrayStatusMap.put(i.getChooseBarCode(), carryTrayStatusDTO);
            }else {
                carryTrayStatusMap.put(i.getChooseBarCode(), carryTrayStatusDTO);
            }
        });
        waitMoveWarehouseList.forEach(m-> {
            CarryTrayStatusDTO carryTrayStatusDTO = new CarryTrayStatusDTO();
            if(StringUtils.isNotBlank(m.getCarryTrayId())){
                carryTrayIds.add(m.getCarryTrayId());
                carryTrayStatusDTO.setTrayId(m.getCarryTrayId());
                carryTrayStatusDTO.setStartWarehouseId(m.getStartWarehouseId());
                carryTrayStatusMap.put(m.getChooseBarCode(), carryTrayStatusDTO);
            }else {
                carryTrayStatusMap.put(m.getChooseBarCode(), carryTrayStatusDTO);
            }
        });
        // 根据托盘ids 查询托盘所在仓库
        List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaListByMaterialStockIds(carryTrayIds);
        for (WarehouseAreaDO warehouseAreaDO : warehouseAreaList) {
            String trayAtWarehouseId = warehouseAreaDO.getWarehouseId();
            carryTrayStatusMap.forEach((barCode, carryTrayStatusDTO) -> {
                if(StringUtils.isNotBlank(carryTrayStatusDTO.getTrayId()) && carryTrayStatusDTO.getTrayId().equals(warehouseAreaDO.getMaterialStockId())){
                    if(trayAtWarehouseId.equals(carryTrayStatusDTO.getStartWarehouseId())){
                        // 在同一个仓库 已抵达
                        carryTrayStatusDTO.setStatus(DictConstants.WMS_CARRY_TRAY_STATUS_ARRIVED);
                    }else {
                        // 其他仓库 正在搬运
                        carryTrayStatusDTO.setStatus(DictConstants.WMS_CARRY_TRAY_STATUS_MOVING);
                    }
                }
            });

        }
        return CommonResult.success(carryTrayStatusMap);
    }

    @Override
    public CommonResult<List<OrderReqDTO>> getNotCompleteOrder() {
        List<OrderReqDTO> result = new ArrayList<>();
        List<OutWarehouseDetailDO> notCompleteOutWarehouseList = outWarehouseDetailService.getNotFinishedOutWarehouseDetailList();
        result.addAll(OutWarehouseDetailConvert.INSTANCE.convertList2(notCompleteOutWarehouseList));
        List<MoveWarehouseDetailDO> notCompleteMoveWarehouseList = moveWarehouseDetailService.getNotFinishedMoveWarehouseDetailList();
        result.addAll(MoveWarehouseDetailConvert.INSTANCE.convertList2(notCompleteMoveWarehouseList));
        return CommonResult.success(result);
    }


    public void removeMaterialConfigInTray(String key){
        try {
            redissonClient.getLock(key).tryLock(60,60, TimeUnit.SECONDS);
            materialConfigInTrayMap.remove(key);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            redissonClient.getLock(key).unlock();
        }
    }
}
