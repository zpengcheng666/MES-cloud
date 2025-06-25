package com.miyu.module.wms.service.outwarehousedetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.api.order.dto.OrderUpdateDTO;
import com.miyu.module.wms.convert.outwarehousedetail.OutWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.CallMaterialServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.ToolDistributionServiceImpl;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.FilterMaterialUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.wms.controller.admin.outwarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.outwarehousedetail.OutWarehouseDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 出库详情 Service 实现类
 *
 * @author Qianjy
 */
@Service
@Validated
@Slf4j
@Transactional
public class OutWarehouseDetailServiceImpl implements OutWarehouseDetailService {

    @Resource
    private OutWarehouseDetailMapper outWarehouseDetailMapper;
    @Resource
    @Lazy
    private CallMaterialServiceImpl callMaterialService;
    @Resource
    @Lazy
    private WarehouseAreaService warehouseAreaService;
    @Resource
    @Lazy
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;
    @Resource
    @Lazy
    private ToolDistributionServiceImpl toolDistributionService;

    @Override
    public String createOutWarehouseDetail(OutWarehouseDetailSaveReqVO createReqVO) {
        if(materialStockService.validateMaterialInLocked(createReqVO.getMaterialStockId(),createReqVO.getStartWarehouseId(),createReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(createReqVO.getTargetWarehouseId(), createReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 插入
        OutWarehouseDetailDO outWarehouseDetail = BeanUtils.toBean(createReqVO, OutWarehouseDetailDO.class);
        outWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        outWarehouseDetailMapper.insert(outWarehouseDetail);
        // 返回
        return outWarehouseDetail.getId();
    }

    @Override
    public void updateOutWarehouseDetail(OutWarehouseDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateOutWarehouseDetailExists(updateReqVO.getId());
        if(materialStockService.validateMaterialInLocked(updateReqVO.getMaterialStockId(),updateReqVO.getStartWarehouseId(),updateReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(updateReqVO.getTargetWarehouseId(), updateReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 更新
        OutWarehouseDetailDO updateObj = BeanUtils.toBean(updateReqVO, OutWarehouseDetailDO.class);
        outWarehouseDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteOutWarehouseDetail(String id) {
        // 校验存在
        validateOutWarehouseDetailExists(id);
        // 删除
        outWarehouseDetailMapper.deleteById(id);
    }

    private void validateOutWarehouseDetailExists(String id) {
        if (outWarehouseDetailMapper.selectById(id) == null) {
            throw exception(OUT_WAREHOUSE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public OutWarehouseDetailDO getOutWarehouseDetail(String id) {
        return outWarehouseDetailMapper.selectById(id);
    }

    @Override
    public PageResult<OutWarehouseDetailDO> getOutWarehouseDetailPage(OutWarehouseDetailPageReqVO pageReqVO) {
        return outWarehouseDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OutWarehouseDetailDO> getOutWarehouseDetailList() {
        return outWarehouseDetailMapper.selectList();
    }

    @Override
    public List<OutWarehouseDetailDO> getOutWarehouseDetailListByOrderNumber(String orderNumber,Integer outState) {
        return outWarehouseDetailMapper.selectOutWarehouseDetailListByOrderNumber(orderNumber, outState);
    }

    @Override
    public List<String> getOutWarehouseDetailGroupByOrderNumberList(Integer outState) {
        List<OutWarehouseDetailDO> outWarehouseDetailList = outWarehouseDetailMapper.selectOutWarehouseDetailGroupByOrderNumberList(outState);
        return outWarehouseDetailList.stream().map(OutWarehouseDetailDO::getOrderNumber).collect(Collectors.toList());
    }

    @Override
    public List<OutWarehouseDetailDO> getOutWarehouseDetailListByMaterialStockContainer(List<MaterialStockDO> materialStockListOnContainer) {
        if(CollectionUtils.isAnyEmpty(materialStockListOnContainer)){
            return Collections.emptyList();
        }

        //获得托盘上的非容器类物料库存集合
//        List<MaterialStockDO> nonContainerMaterialStockList = materialStockService.getNonContainerMaterialStockListByMaterialStockContainer(materialStockListOnContainer);

        // 先把id集中起来
        List<String> allMaterialStockIds = materialStockListOnContainer.stream().map(MaterialStockDO::getId).collect(Collectors.toList());
        // 出库详情单
        return this.getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(allMaterialStockIds);
    }

    @Override
    public List<OutWarehouseDetailDO> getWaitOutWarehouseOutWarehouseDetailList() {
        return outWarehouseDetailMapper.selectWaitOutWarehouseOutWarehouseDetailList();
    }

    @Override
    public List<OutWarehouseDetailDO> getWaitApprovalOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return outWarehouseDetailMapper.selectOutWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_6);
    }
    @Override
    public List<OutWarehouseDetailDO> getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return outWarehouseDetailMapper.selectOutWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1);
    }

    @Override
    public List<OutWarehouseDetailDO> getWaitArriveOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return outWarehouseDetailMapper.selectOutWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_2);
    }

    @Override
    public List<OutWarehouseDetailDO> getWaitSignForOutWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = new ArrayList<>();
        outWarehouseDetailDOS.addAll(outWarehouseDetailMapper.selectOutWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1));
        outWarehouseDetailDOS.addAll(outWarehouseDetailMapper.selectOutWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_3));
        return outWarehouseDetailDOS;
    }


    @Override
    public boolean updateBatchOutWarehouseDetailStateByMaterialStockId(String materialStockId, Integer updateState) {
        //根据容器id（托盘或工装） 查询其上所有物料
        List<MaterialStockDO> allMaterialStockOnTrayList = materialStockService.getAllMaterialStockListByMaterialStockId(materialStockId);
        List<String> allMaterialStockOnTrayIds = allMaterialStockOnTrayList.stream().map(MaterialStockDO::getId).collect(Collectors.toList());

        // 如果此容器是工装 那么工装也需要更新状态
        MaterialStockDO materialStock = materialStockService.getMaterialStockAndMaterialTypeById(materialStockId);
        if(materialStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        // 如果此容器非托盘 那么此容器也得被签收
        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType())){
            allMaterialStockOnTrayIds.add(materialStockId);
        }

        // 若托盘上无物料库存，则直接返回true
        if(CollectionUtils.isAnyEmpty(allMaterialStockOnTrayIds)){
            return false;
        }

        // 更新未完成的出库详情单状态
        int i = outWarehouseDetailMapper.updateBatchNotFinish(allMaterialStockOnTrayIds, updateState);
        if(i < 1){
//            throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
            return false;
        }
        return true;

        /*// 更新出库详情单状态
        List<OutWarehouseDetailDO> outWarehouseDetailList = this.getUpdateListByMaterialStockIds(updateState,allMaterialStockOnTrayIds);

        // 按理说不会为空 说明有bug
        if(CollectionUtils.isAnyEmpty(outWarehouseDetailList)){
            throw exception(OUT_WAREHOUSE_MATERIAL_NOT_FOUND_DELIVERY_ORDER);
        }

        // 更新出库详情单状态
        outWarehouseDetailList.forEach(o -> o.setOutState(updateState));
        Boolean b = outWarehouseDetailMapper.updateBatch(outWarehouseDetailList);
        // 若出库详情单更新失败 则抛出异常
        if (b == null || !b){
            throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }

        // 若出库详情单更新成功 则返回true
        return true;*/
    }



    @Override
    public boolean updateBatchOutWarehouseDetail(OrderUpdateDTO orderUpdateDTO) {
        String materialStockId = orderUpdateDTO.getMaterialStockId();
        //根据容器id（托盘或工装） 查询其上所有物料
        List<MaterialStockDO> allMaterialStockOnTrayList = materialStockService.getAllMaterialStockListByMaterialStockId(materialStockId);
        List<String> allMaterialStockOnTrayIds = allMaterialStockOnTrayList.stream().map(MaterialStockDO::getId).collect(Collectors.toList());

        // 如果此容器是工装 那么工装也需要更新状态
        MaterialStockDO materialStock = materialStockService.getMaterialStockAndMaterialTypeById(materialStockId);
        // 如果此容器非托盘 那么此容器也得被签收
        if(materialStock != null && !DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType())){
            allMaterialStockOnTrayIds.add(materialStockId);
        }

        // 若托盘上无物料库存，则直接返回true
        if(CollectionUtils.isAnyEmpty(allMaterialStockOnTrayIds)){
            return true;
        }

        List<OutWarehouseDetailDO> waitOutWarehouseDetailList = getWaitApprovalOutWarehouseDetailListByMaterialStockIds(allMaterialStockOnTrayIds);
        if(!waitOutWarehouseDetailList.isEmpty()){
            // 如果起始和目标仓库位置一致 则直接更新为完成状态
            if(orderUpdateDTO.getTargetWarehouseId().equals(waitOutWarehouseDetailList.get(0).getStartWarehouseId())){
                int i = outWarehouseDetailMapper.updateBatchNotFinish(allMaterialStockOnTrayIds, DictConstants.WMS_ORDER_DETAIL_STATUS_4);
                if(i < 1){
                    throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
                }
                return true;
            }
        }

        if(CollectionUtils.isAnyEmpty(allMaterialStockOnTrayIds)){
            throw exception(OUT_WAREHOUSE_DETAIL_NOT_EXISTS);
        }
//        WarehouseDO targetWarehouse = warehouseService.getWarehouse(orderUpdateDTO.getTargetWarehouseId());
        // 如果目标位置是 立体库 则将出库单 改成移库单
        /*if(DictConstants.WMS_WAREHOUSE_TYPE_1.equals(targetWarehouse.getWarehouseType())
            || DictConstants.WMS_WAREHOUSE_TYPE_2.equals(targetWarehouse.getWarehouseType())){
            moveWarehouseDetailService.insetBatchMoveWarehouseDetail(waitOutWarehouseDetailList, DictConstants.WMS_MOVE_WAREHOUSE_TYPE_3, orderUpdateDTO.getOrderStatus(),orderUpdateDTO.getTargetWarehouseId());
            outWarehouseDetailMapper.deleteBatchIds(waitOutWarehouseDetailList.stream().map(OutWarehouseDetailDO::getId).collect(Collectors.toList()));
            return true;
        }*/


        // 更新未完成的出库详情单状态
        int i = outWarehouseDetailMapper.updateBatchNotFinish(allMaterialStockOnTrayIds, orderUpdateDTO.getOrderStatus(), orderUpdateDTO.getTargetWarehouseId());
        if(i < 1){
            throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        return true;
    }

    /**
     * 校验出库详情单
     * @param outWarehouseId
     * @param allMaterialStockMap
     */
    @Override
    public List<OutWarehouseDetailDO> checkOutWarehouseDetail(String outWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        // 找到所有出库详情单
        List<OutWarehouseDetailDO> allOutWarehouseDetailList = this.getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());
        // 先看数量对不对  -- 出库单数量和非容器类物料数量的应该是一样的
        if (CollectionUtils.isAnyEmpty(allOutWarehouseDetailList)
                || (allOutWarehouseDetailList.size() < FilterMaterialUtils.filter_Tp_Gz(allMaterialStockMap).size())) {
            throw exception(OUT_WAREHOUSE_MATERIAL_NOT_OUT_OUT_WAREHOUSE_ORDER);
        }

        for (OutWarehouseDetailDO outWarehouseDetail : allOutWarehouseDetailList) {
            // 看看出库单的目标仓库是否全部一致---outWarehouseId==null说明是刀具出库，刀具出库存在多个目标点
            if(outWarehouseId != null && !outWarehouseId.equals(outWarehouseDetail.getTargetWarehouseId())){
                throw exception(OUT_WAREHOUSE_DETAIL_NOT_MATCH_OUT_WAREHOUSE_ORDER);
            }
            String materialStockId = outWarehouseDetail.getMaterialStockId();
            // 如果物料库存id 不存在，说明此物料需要分拣
            if(StringUtils.isBlank(materialStockId)){
                // 此物料不满足入库状态，需人工分拣后再入库
                throw exception(OUT_WAREHOUSE_MATERIAL_NOT_MATCH_OUT_WAREHOUSE_STATUS);
            }

            if(allMaterialStockMap.containsKey(materialStockId)){
                MaterialStockDO materialStock = allMaterialStockMap.get(materialStockId);
                if(materialStock.getTotality() != outWarehouseDetail.getQuantity()){
                    // getMaterialStockId 此数据存在 则出库数量和库存数量肯定是一致的 不一致 则有bug
                    throw exception(BUG);
                }
            }else {
                // 物料库存不存在
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }
        return allOutWarehouseDetailList;
    }


    @Override
    public List<String> createBatchOutWarehouseDetail(List<OrderReqDTO> orderReqDTOList) {
        // 实体转换
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = OutWarehouseDetailConvert.INSTANCE.convertList(orderReqDTOList);
        // 查询此物料是否已存在移库单或其他单据
        List<String> failMaterialStockIds = this.checkStockAndCreateOutDetail(outWarehouseDetailDOS);
        // 返回
        return failMaterialStockIds;
    }

    @Override
    public List<String> checkStockAndCreateOutDetail(List<OutWarehouseDetailDO> outWarehouseDetailDOS) {
        Set<String> allMateialStockIdSet = CollectionUtils.convertSet(outWarehouseDetailDOS, OutWarehouseDetailDO::getChooseStockId);

        // 查询存在 单据的物料
        List<MaterialStockDO> haveOrderMaterialStockList = materialStockService.checkChooseStockOrderExists(allMateialStockIdSet);
        // key为物料id，value为创建单据占用的物料库存总量
        Map<String, Integer> occupyMaterialStockTotalityMap = CollectionUtils.convertMap(haveOrderMaterialStockList, MaterialStockDO::getId, MaterialStockDO::getOrderQuantity, (v1, v2) -> v1 + v2);

        // 生成单据失败的物料库存id 集合
        List<String> failMaterialStockIds = new ArrayList<>();

        // 根据id查询所有物料库存信息
        List<MaterialStockDO> allMaterialStockList = materialStockService.getMaterialStockByIds(allMateialStockIdSet);
        Map<String, MaterialStockDO> allMateialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

        // 物料类型id 和 物料批次号填入
        List<OutWarehouseDetailDO> successOutWarehouseDetailList = outWarehouseDetailDOS.stream().map(s-> {
            if(allMateialStockMap.containsKey(s.getChooseStockId())){
                MaterialStockDO materialStockDO = allMateialStockMap.get(s.getChooseStockId());

                Integer occupyTotality = occupyMaterialStockTotalityMap.getOrDefault(s.getChooseStockId(), 0);

                if(occupyTotality + s.getQuantity() > materialStockDO.getTotality() ){
                    // 出库数量大于库存数量
                    failMaterialStockIds.add(s.getChooseStockId());
                    throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
                }
                if(materialStockDO.getTotality() == s.getQuantity()){
                    // 如果出库数量等于库存数量，则绑定入库的库存id
                    s.setMaterialStockId(s.getChooseStockId());
                }
                // 填入物料类型
                s.setMaterialConfigId(materialStockDO.getMaterialConfigId());
                // 填入批次号
                s.setBatchNumber(materialStockDO.getBatchNumber());
                if(StringUtils.isBlank(s.getStartWarehouseId())){
                    WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
                    if(warehouseArea == null){
                        throw exception(WAREHOUSE_AREA_NOT_EXISTS);
                    }
                    // 物料不在存储区 不能出库
                    if(!DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(warehouseArea.getAreaType())){
                        throw exception(OUT_WAREHOUSE_ORDER_NOT_IN_STORAGE_AREA);
                    }
                    // 填入出库起始仓库
                    s.setStartWarehouseId(warehouseArea.getWarehouseId());
                }
                return s;
            }else {
                log.error("物料库存不存在，物料库存id:{}",s.getChooseStockId());
                failMaterialStockIds.add(s.getChooseStockId());
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // 插入失败 则加入失败的id集合
        if(!outWarehouseDetailMapper.insertBatch(successOutWarehouseDetailList)){
            failMaterialStockIds.addAll(new ArrayList<>(allMateialStockIdSet));
            throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        // 失败的返回
        return failMaterialStockIds;
    }


    private List<OutWarehouseDetailDO> getUpdateListByMaterialStockIds(Integer updateState, List<String> materialStockIds) {
        // 此出库单为 托盘上的所有非容器类物料的 出库单详情集合
        List<OutWarehouseDetailDO> outWarehouseDetailList = null;
        //如果更新状态为待送达 则查询 db 里的 待出库的出库详情单  并更新状态
        if (DictConstants.WMS_ORDER_DETAIL_STATUS_2.equals(updateState)) {
            // 获得物料待出库的出库详情单
            outWarehouseDetailList = getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(materialStockIds);
            log.info("待出库出库详情单数量："+outWarehouseDetailList.size());
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_3.equals(updateState)) {
            // 获得物料待送达出库详情单
            outWarehouseDetailList = getWaitArriveOutWarehouseDetailListByMaterialStockIds(materialStockIds);
            log.info("待送达出库详情单数量："+outWarehouseDetailList.size());
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)) {
            // 获得物料待签收出库详情单
            outWarehouseDetailList = getWaitSignForOutWarehouseDetailListByMaterialStockIds(materialStockIds);
            if(CollectionUtils.isAnyEmpty(outWarehouseDetailList)){
                outWarehouseDetailList = getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(materialStockIds);
            }
            log.info("待签收出库详情单数量："+outWarehouseDetailList.size());
        }
        return outWarehouseDetailList;
    }

    @Override
    public List<OutWarehouseDetailDO> selectWaitOutWarehouseDetail(String warehouseId) {
        return outWarehouseDetailMapper.selectOutWarehouseDetail(warehouseId);
    }


    @Override
    public List<OrderReqDTO> getBatchOutOrderList(List<String> outOrderNumbers) {
        if(CollectionUtils.isAnyEmpty(outOrderNumbers)){
            return Collections.emptyList();
        }
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = outWarehouseDetailMapper.selectBatchOutOrderList(outOrderNumbers);
        return OutWarehouseDetailConvert.INSTANCE.convertList2(outWarehouseDetailDOS);
    }

    @Override
    public List<OutWarehouseDetailDO> checkMaterialStockOrderExistsByBarCodes(String orderNumber, Collection<String> barCodes) {
        return outWarehouseDetailMapper.checkMaterialStockOrderExistsByBarCodes(orderNumber, barCodes);
    }

    @Override
    public int setOperatorInBatchOutWarehouseDetail(Set<String> orderIds) {
        return outWarehouseDetailMapper.setOperatorInBatchOutWarehouseDetail(orderIds);
    }

    @Override
    public List<OrderReqDTO> getOutOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber) {
        Integer outType = OutWarehouseDetailConvert.INSTANCE.convertOutType(orderType);
        if(outType == null || StringUtils.isBlank(batchNumber)){
            return Collections.emptyList();
        }
        List<OutWarehouseDetailDO> outWarehouseDetailDOS =  outWarehouseDetailMapper.selectOutOrderListByOrderTypeAndBatchNumber(outType, batchNumber);
        List<OrderReqDTO> orderReqDTOList = OutWarehouseDetailConvert.INSTANCE.convertList2(outWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public int updateById(OutWarehouseDetailDO outWarehouseDetail) {
        return outWarehouseDetailMapper.updateById(outWarehouseDetail);
    }

    @Override
    public int createLossOutWarehouseDetail(int lossTotality, MaterialStockDO materialStockDO, String warehouseId, String operator, LocalDateTime operateTime) {
        // 查询存在 单据的物料
        List<MaterialStockDO> haveOrderMaterialStockList = materialStockService.checkChooseStockOrderExists(Collections.singleton(materialStockDO.getId()));
        if(!haveOrderMaterialStockList.isEmpty()){
            throw exception(BILL_ALREADY_EXISTS);
        }
        OutWarehouseDetailDO outWarehouseDetail = new OutWarehouseDetailDO();
        outWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        outWarehouseDetail.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_10);
        outWarehouseDetail.setOutState(DictConstants.WMS_ORDER_DETAIL_STATUS_4);
        outWarehouseDetail.setStartWarehouseId(warehouseId);
        outWarehouseDetail.setMaterialStockId(materialStockDO.getId());
        outWarehouseDetail.setChooseStockId(materialStockDO.getId());
        outWarehouseDetail.setMaterialConfigId(materialStockDO.getMaterialConfigId());
        outWarehouseDetail.setBatchNumber(materialStockDO.getBatchNumber());
        outWarehouseDetail.setQuantity(lossTotality);
        outWarehouseDetail.setOperator(operator);
        outWarehouseDetail.setOperateTime(operateTime);
        outWarehouseDetail.setSigner(operator);
        outWarehouseDetail.setSignTime(operateTime);
        return this.outWarehouseDetailMapper.insert(outWarehouseDetail);
    }

    @Override
    public List<OutWarehouseDetailDO> getNotFinishedOutWarehouseDetailListByAreaId(String areaId) {
        return outWarehouseDetailMapper.selectNotFinishedOutWarehouseDetailListByAreaId(areaId);
    }

    @Override
    public Boolean updateBatch(Collection<OutWarehouseDetailDO> entities) {
        return outWarehouseDetailMapper.updateBatch(entities);
    }

    @Override
    public void callMaterial(String callMaterialStockId, String callLocationId) {
        // 判断呼叫库位是否为空位
        if (materialStockService.checkLocationIsVacant(callLocationId)) {
            throw exception(IN_WAREHOUSE_LOCATION_IS_OCCUPIED);
        }

        // 获取物料实体
        MaterialStockDO callMaterialStock = materialStockService.getMaterialStockAndMaterialTypeById(callMaterialStockId);
        if(callMaterialStock == null){
            throw exception(OUT_WAREHOUSE_CALL_LOCATION_NOT_EMPTY);
        }

        // 物料所在库位id
        String callMaterialAtLocationId = materialStockService.getLocationIdByMaterialStock(callMaterialStock);
        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationId(callMaterialAtLocationId);
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个容器 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);
        if (!Objects.equals(containerStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_TP)
                && !Objects.equals(containerStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_GZ)) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
        }

        // 校验呼叫位置是否有搬运任务
        List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);
        if(haveTaskLocationIdSet.contains(callLocationId)){
            log.info("此库位：{} -已生成搬运任务，请勿重复下发",callLocationId);
            throw exception(CARRYING_TASK_LOCATION_HAS_TRANSPORT_TASK);
        }

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(callLocationId);
        String targetWarehouseId = warehouseArea.getWarehouseId();

        {
            // 1. 获得 容器上除托盘外的物料库存
            List<MaterialStockDO> allMaterialStockList = callMaterialService.getAllMaterialStock(containerStock);
            // 2. 将其转成map
            Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

            // 3. 校验任务单
            Set<String> orderIds = callMaterialService.checkOrderTask(targetWarehouseId, allMaterialStockMap);

            // 4. 生成搬运任务
            callMaterialService.createCarryTaskLogic(null, containerStock, callLocationId);

            // 5. 填入操作人
            callMaterialService.setOperatorInOrderTask(orderIds);
        }
    }

    @Override
    public String tryGenerateOutWarehouseDetail(MaterialStockDO materialStock, String targetLocationId, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock) {

        if(!callMaterialService.createCarryTaskLogic(materialStock, targetLocationId, targetWarehouseId)){
            return null;
        }

        return generateOutWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseId, targetWarehouseId, targetLocationId, quantity, realMaterialStock);
    }

    @Override
    public String generateOutWarehouseDetail(Integer outState, String startWarehouseId, String targetWarehouseId, String targetLocationId, Integer quantity, MaterialStockDO realMaterialStock) {
        OutWarehouseDetailDO outWarehouseDetail = new OutWarehouseDetailDO();
        outWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        outWarehouseDetail.setOutType(DictConstants.WMS_OUT_WAREHOUSE_TYPE_4);
        outWarehouseDetail.setOutState(outState);
        outWarehouseDetail.setStartWarehouseId(startWarehouseId);
        outWarehouseDetail.setTargetWarehouseId(targetWarehouseId);
        outWarehouseDetail.setSignLocationId(targetLocationId);
        outWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
        outWarehouseDetail.setChooseStockId(realMaterialStock.getId());
        outWarehouseDetail.setMaterialConfigId(realMaterialStock.getMaterialConfigId());
        outWarehouseDetail.setBatchNumber(realMaterialStock.getBatchNumber());
        outWarehouseDetail.setQuantity(quantity);
        this.outWarehouseDetailMapper.insert(outWarehouseDetail);
        return outWarehouseDetail.getOrderNumber();
    }

    @Override
    public List<OrderReqDTO> getOutWarehouseDetailByChooseBarCode(String barCode) {
        if(StringUtils.isBlank(barCode)){
            return Collections.emptyList();
        }
        List<OutWarehouseDetailDO> outWarehouseDetailDOS =  outWarehouseDetailMapper.selectOutWarehouseDetailByChooseBarCode(barCode);
        List<OrderReqDTO> orderReqDTOList = OutWarehouseDetailConvert.INSTANCE.convertList2(outWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public void generateDistributionTask(MaterialStockDO containerStock) {
        List<MaterialStockDO> materialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(containerStock.getId());

        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            // 1. 获得 容器上除托盘外的物料库存
            List<MaterialStockDO> allMaterialStockList = toolDistributionService.getAllMaterialStock(containerStock);

            // 2. 除托盘外的物料库存集合
            Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

            // 3. 校验任务单
            List<OutWarehouseDetailDO> outWarehouseDetailDOS = toolDistributionService.checkOrderTask(allMaterialStockMap);
            Set<String> orderIds = new HashSet<>();
            // 有序的 需求库位id
            Set<String> needLocationIds = new LinkedHashSet<>();
            outWarehouseDetailDOS.forEach(item -> {
                orderIds.add(item.getId());
                needLocationIds.add(item.getNeedLocationId());
            });
            // 4. 生成搬运任务
            toolDistributionService.createCarryTaskLogic(null, containerStock, String.join(",", needLocationIds));

            // 5. 填入操作人
            toolDistributionService.setOperatorInOrderTask(orderIds);
        }else {
            throw exception(CARRYING_TASK_TRAY_NO_TOOL);
        }
    }

    @Override
    public List<String> getDistributionPath(MaterialStockDO containerStock) {
        List<MaterialStockDO> materialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(containerStock.getId());

        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            // 1. 获得 容器上除托盘外的物料库存
            List<MaterialStockDO> allMaterialStockList = toolDistributionService.getAllMaterialStock(containerStock);

            // 找到所有出库详情单
            Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

            List<OutWarehouseDetailDO> allOutWarehouseDetailList = this.getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());

            allOutWarehouseDetailList = allOutWarehouseDetailList.stream().filter(item -> StringUtils.isNotBlank(item.getNeedLocationId())).sorted(Comparator.comparing(OutWarehouseDetailDO::getDeliverySequence)).collect(Collectors.toList());
            // 有序的 需求库位id
            Set<String> needLocationIds = new LinkedHashSet<>();
            allOutWarehouseDetailList.forEach(item -> {
                needLocationIds.add(item.getNeedLocationId());
            });

            List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationListByIds(needLocationIds);

            Map<String, String> stringStringMap = CollectionUtils.convertMap(warehouseLocationList, WarehouseLocationDO::getId, WarehouseLocationDO::getLocationName);

            List<String> distributionPath = new ArrayList<>();
            needLocationIds.forEach(locationId -> {
                String locationName = stringStringMap.get(locationId);
                distributionPath.add(locationName);
            });

            return distributionPath;
        }else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<OutWarehouseDetailDO> getFinishOutWarehouseDetailList(LocalDateTime[] createTimeRange) {
        return outWarehouseDetailMapper.selectFinishOutWarehouseDetailList(createTimeRange);
    }

    @Override
    public List<OutWarehouseDetailDO> getNotFinishedOutWarehouseDetailList() {
        return outWarehouseDetailMapper.selectNotFinishedOutWarehouseDetailList();
    }

}
