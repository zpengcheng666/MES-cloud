package com.miyu.module.wms.service.movewarehousedetail;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.convert.movewarehousedetail.MoveWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.MoveMaterialServiceImpl;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.util.FilterMaterialUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.miyu.module.wms.controller.admin.movewarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.movewarehousedetail.MoveWarehouseDetailMapper;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 库存移动详情 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MoveWarehouseDetailServiceImpl implements MoveWarehouseDetailService {

    @Resource
    private MoveWarehouseDetailMapper moveWarehouseDetailMapper;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    @Lazy
    private WarehouseAreaService warehouseAreaService;
    @Resource
    @Lazy
    private MoveMaterialServiceImpl moveMaterialService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Override
    public String createMoveWarehouseDetail(MoveWarehouseDetailSaveReqVO createReqVO) {
        if(materialStockService.validateMaterialInLocked(createReqVO.getMaterialStockId(),createReqVO.getStartWarehouseId(),createReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(createReqVO.getTargetWarehouseId(), createReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 插入
        MoveWarehouseDetailDO moveWarehouseDetail = BeanUtils.toBean(createReqVO, MoveWarehouseDetailDO.class);
        moveWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        moveWarehouseDetailMapper.insert(moveWarehouseDetail);
        // 返回
        return moveWarehouseDetail.getId();
    }

    @Override
    public void updateMoveWarehouseDetail(MoveWarehouseDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateMoveWarehouseDetailExists(updateReqVO.getId());
        if(materialStockService.validateMaterialInLocked(updateReqVO.getMaterialStockId(),updateReqVO.getStartWarehouseId(),updateReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(updateReqVO.getTargetWarehouseId(), updateReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 更新
        MoveWarehouseDetailDO updateObj = BeanUtils.toBean(updateReqVO, MoveWarehouseDetailDO.class);

        moveWarehouseDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteMoveWarehouseDetail(String id) {
        // 校验存在
        validateMoveWarehouseDetailExists(id);
        // 删除
        moveWarehouseDetailMapper.deleteById(id);
    }

    private void validateMoveWarehouseDetailExists(String id) {
        if (moveWarehouseDetailMapper.selectById(id) == null) {
            throw exception(STOCK_MOVE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public MoveWarehouseDetailDO getMoveWarehouseDetail(String id) {
        return moveWarehouseDetailMapper.selectById(id);
    }

    @Override
    public PageResult<MoveWarehouseDetailDO> getMoveWarehouseDetailPage(MoveWarehouseDetailPageReqVO pageReqVO) {
        return moveWarehouseDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MoveWarehouseDetailDO> getWaitOutMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return moveWarehouseDetailMapper.selectMoveWarehouseDetailListByMaterialStockIds(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1);
    }

    @Override
    public List<MoveWarehouseDetailDO> getMoveWarehouseDetailListByMaterialStockIds(List<String> materialStockIds, Integer moveState){
        return moveWarehouseDetailMapper.selectMoveWarehouseDetailListByMaterialStockIds(materialStockIds, moveState);
    }

    @Override
    public List<MoveWarehouseDetailDO> getMoveWarehouseDetailIds(List<String> inWarehouseIds) {
        return moveWarehouseDetailMapper.selectMoveWarehouseDetailListByInWarehouseIds(inWarehouseIds);
    }

    @Override
    public boolean updateBatchMoveWarehouseDetailStateByMaterialStockId(String materialStockId, Integer updateState) {
        //根据容器id（托盘或工装） 查询其上所有物料
        List<MaterialStockDO> allMaterialStockOnTrayList = materialStockService.getAllMaterialStockListByMaterialStockId(materialStockId);
        List<String> allMaterialStockOnTrayIds = allMaterialStockOnTrayList.stream().map(MaterialStockDO::getId).collect(Collectors.toList());

        // 如果此容器非托盘 那么此容器也得被签收
        MaterialStockDO materialStock = materialStockService.getMaterialStockAndMaterialTypeById(materialStockId);
        if(materialStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        // 如果此容器非托盘 那么此容器也得被签收
        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStock.getMaterialType())){
            allMaterialStockOnTrayIds.add(materialStockId);
        }

        // 若托盘上无物料库存，则直接返回true
        if(com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(allMaterialStockOnTrayIds)){
            return false;
        }

        // 更新未完成的移库详情单状态
        int i = moveWarehouseDetailMapper.updateBatchNotFinish(allMaterialStockOnTrayIds, updateState);
        if(i < 1){
            throw exception(MOVE_WAREHOUSE_DETAIL_UPDATE_ERROR);
        }
        return true;

      /*  List<MoveWarehouseDetailDO> moveWarehouseDetailList = this.getUpdateListByMaterialStockIds(updateState, allMaterialStockOnTrayIds);

        // 按理说不会为空 说明有bug
        if(CollectionUtils.isAnyEmpty(moveWarehouseDetailList)){
            throw exception(MOVE_WAREHOUSE_MATERIAL_NOT_FOUND_DELIVERY_ORDER);
        }

        // 更新移库详情单状态
        moveWarehouseDetailList.forEach(o -> o.setMoveState(updateState));
        Boolean b = moveWarehouseDetailMapper.updateBatch(moveWarehouseDetailList);
        // 若出库详情单更新失败 则抛出异常
        if (b == null || !b){
            throw exception(OUT_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }

        // 若出库详情单更新成功 则返回true
        return true;*/
    }

    @Override
    public List<MoveWarehouseDetailDO> getWaitMoveWarehouseMoveWarehouseDetailList() {
        return moveWarehouseDetailMapper.selectWaitMoveWarehouseMoveWarehouseDetailList();
    }

    private List<MoveWarehouseDetailDO> getUpdateListByMaterialStockIds(Integer updateState, List<String> materialStockIds) {
        // 此出库单为 托盘上的所有非容器类物料的 出库单详情集合
        List<MoveWarehouseDetailDO> moveWarehouseDetailList = null;
        //如果更新状态为待送达 则查询 db 里的 待出库的出库详情单  并更新状态
        if (DictConstants.WMS_ORDER_DETAIL_STATUS_2.equals(updateState)) {
            // 获得物料待出库的出库详情单
            moveWarehouseDetailList = getWaitOutWarehouseMoveWarehouseDetailListByMaterialStockIds(materialStockIds);
            log.info("待出库移库详情单数量："+moveWarehouseDetailList.size());
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_3.equals(updateState)) {
            // 获得物料待送达出库详情单
            moveWarehouseDetailList = getWaitArriveMoveWarehouseDetailListByMaterialStockIds(materialStockIds);
            log.info("待送达移库详情单数量："+moveWarehouseDetailList.size());
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)) {
            // 获得物料待签收出库详情单
            moveWarehouseDetailList = getWaitSignForMoveWarehouseDetailListByMaterialStockIds(materialStockIds);
            log.info("待签收移库详情单数量："+moveWarehouseDetailList.size());
        }
        return moveWarehouseDetailList;
    }

    @Override
    public List<MoveWarehouseDetailDO> getWaitOutWarehouseMoveWarehouseDetailListByMaterialStockIds(List<String> materialStockIds) {
        return moveWarehouseDetailMapper.selectMoveWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1);
    }

    @Override
    public List<MoveWarehouseDetailDO> getWaitArriveMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return moveWarehouseDetailMapper.selectMoveWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_2);
    }

    @Override
    public List<MoveWarehouseDetailDO> getWaitSignForMoveWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        List<MoveWarehouseDetailDO> moveWarehouseDetailList = new ArrayList<>();
        moveWarehouseDetailList.addAll(moveWarehouseDetailMapper.selectMoveWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_3));
        moveWarehouseDetailList.addAll(moveWarehouseDetailMapper.selectMoveWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1));
        return moveWarehouseDetailList;
    }

    @Override
    public boolean updateMoveWarehouseDetailStateByMaterialStockId(String containerStockId, Integer updateState) {
        return moveWarehouseDetailMapper.updateMoveWarehouseDetailStateByMaterialStockId(containerStockId, updateState) > 0;
    }

    @Override
    public List<String> createBatchMoveWarehouseDetail(List<OrderReqDTO> orderReqDTOList) {
        // 实体转换
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = MoveWarehouseDetailConvert.INSTANCE.convertList(orderReqDTOList);
        // 查询此物料是否已存在移库单或其他单据
        List<String> failMaterialStockIds = this.checkStockAndCreateMoveDetail(moveWarehouseDetailDOS);
        // 返回
        return failMaterialStockIds;
    }

    @Override
    public List<String> checkStockAndCreateMoveDetail(List<MoveWarehouseDetailDO> moveWarehouseDetailDOS) {
        Set<String> allMateialStockIdSet = CollectionUtils.convertSet(moveWarehouseDetailDOS, MoveWarehouseDetailDO::getChooseStockId);

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
        List<MoveWarehouseDetailDO> successMoveWarehouseDetailList = moveWarehouseDetailDOS.stream().map(s-> {
            if(allMateialStockMap.containsKey(s.getChooseStockId())){
                MaterialStockDO materialStockDO = allMateialStockMap.get(s.getChooseStockId());

                Integer occupyTotality = occupyMaterialStockTotalityMap.getOrDefault(s.getChooseStockId(), 0);

                if(occupyTotality +s.getQuantity() > materialStockDO.getTotality() ){
                    // 移库数量大于库存数量
                    failMaterialStockIds.add(s.getChooseStockId());
                    throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
                }

                if(materialStockDO.getTotality() == s.getQuantity()){
                    // 如果移库数量等于库存数量，则绑定入库的库存id
                    s.setMaterialStockId(s.getChooseStockId());
                }
                // 填入物料类型
                s.setMaterialConfigId(materialStockDO.getMaterialConfigId());
                // 填入批次号
                s.setBatchNumber(materialStockDO.getBatchNumber());
                if(StringUtils.isBlank(s.getStartWarehouseId())){
                    WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStockId(s.getChooseStockId());
                    // 填入移库起始仓库
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
        if(!moveWarehouseDetailMapper.insertBatch(successMoveWarehouseDetailList)){
            log.error("入库单详情插入失败,物料库存id:{}",allMateialStockIdSet);
            failMaterialStockIds.addAll(new ArrayList<>(allMateialStockIdSet));
            throw exception(MOVE_WAREHOUSE_DETAIL_UPDATE_ERROR);
        }
        // 失败的返回
        return failMaterialStockIds;
    }

    @Override
    public List<MoveWarehouseDetailDO> getNotFinishMoveDetailList() {
        return moveWarehouseDetailMapper.selectNotFinishMoveDetailList();
    }


    /**
     * 校验移库详情
     * @param targetWarehouseId
     * @param allMaterialStockMap
     */
    @Override
    public List<MoveWarehouseDetailDO> checkMoveWarehouseDetail(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        // 根据托盘上的物料库存 找到所有移库单
        List<MoveWarehouseDetailDO> allMoveWarehouseDetailList = this.getWaitOutMoveWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());
        // 先看数量对不对  -- 入库单数量和物料数量应该是一样的
        if (CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList)
                || (allMoveWarehouseDetailList.size() < FilterMaterialUtils.filter_Tp_Gz(allMaterialStockMap).size())) {
            throw exception(MOVE_WAREHOUSE_MATERIAL_NOT_MOVE_MOVE_WAREHOUSE_ORDER);
        }

        for (MoveWarehouseDetailDO moveWarehouseDetail : allMoveWarehouseDetailList) {
            // 校验移库仓库是否一致
            if(targetWarehouseId != null && !targetWarehouseId.equals(moveWarehouseDetail.getTargetWarehouseId())){
                throw exception(MOVE_WAREHOUSE_MATERIAL_NOT_MATCH_DEST_WAREHOUSE);
            }
            String materialStockId = moveWarehouseDetail.getMaterialStockId();
            // 如果物料库存id 不存在，说明此物料需要分拣
            if(StringUtils.isBlank(materialStockId)){
                // 此物料不满足入库状态，需人工分拣后再入库
                throw exception(MOVE_WAREHOUSE_MATERIAL_NOT_MATCH_MOVE_WAREHOUSE_STATUS);
            }
            if(allMaterialStockMap.containsKey(materialStockId)){
                MaterialStockDO materialStock = allMaterialStockMap.get(materialStockId);
                if(!Objects.equals(materialStock.getTotality(), moveWarehouseDetail.getQuantity())){
                    // getMaterialStockId 此数据存在 则移库数量和库存数量肯定是一致的 不一致 则有bug
                    throw exception(BUG);
                }
            }else {
                // 物料库存不存在
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }
        return allMoveWarehouseDetailList;
    }

    @Override
    public List<OrderReqDTO> getBatchMoveOrderList(List<String> moveOrderNumbers) {
        if(CollectionUtils.isAnyEmpty(moveOrderNumbers)){
            return Collections.emptyList();
        }
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailMapper.selectBatchMoveOrderList(moveOrderNumbers);
        List<OrderReqDTO> orderReqDTOList = MoveWarehouseDetailConvert.INSTANCE.convertList2(moveWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public int setOperatorInBatchMoveWarehouseDetail(Set<String> orderIds) {
        return moveWarehouseDetailMapper.setOperatorInBatchMoveWarehouseDetail(orderIds);
    }

    @Override
    public List<OrderReqDTO> getMoveOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber) {
        Integer moveType = MoveWarehouseDetailConvert.INSTANCE.convertMoveType(orderType);
        if(moveType == null || StringUtils.isBlank(batchNumber)){
            return Collections.emptyList();
        }
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailMapper.selectMoveOrderListByOrderTypeAndBatchNumber(moveType, batchNumber);
        List<OrderReqDTO> orderReqDTOList = MoveWarehouseDetailConvert.INSTANCE.convertList2(moveWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public int updateById(MoveWarehouseDetailDO moveWarehouseDetail) {
        return moveWarehouseDetailMapper.updateById(moveWarehouseDetail);
    }

    @Override
    public List<MoveWarehouseDetailDO> getNotFinishedMoveWarehouseDetailListByAreaId(String areaId) {
        return moveWarehouseDetailMapper.selectNotFinishedMoveWarehouseDetailListByAreaId(areaId);
    }

    @Override
    public String tryGenerateMoveWarehouseDetail(MaterialStockDO materialStock, String targetLocationId, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock) {
        // 4. 生成搬运任务
        if(!moveMaterialService.createCarryTaskLogic(materialStock, targetLocationId, targetWarehouseId)){
            return null;
        }

        return generateMoveWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseId, targetWarehouseId, null, quantity, realMaterialStock, null);
    }


    @Override
    public String tryGenerateMoveWarehouseDetail(MaterialStockDO toolingStock, String startLocationId, String targetLocationId, MaterialStockDO realMaterialStock) {
        // 4. 生成搬运任务
        if(!moveMaterialService.createCarryTaskLogic2(toolingStock, startLocationId, targetLocationId)){
            return null;
        }

        WarehouseAreaDO startWarehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(startLocationId);
        WarehouseAreaDO targetWarehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(targetLocationId);

        return generateMoveWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseArea.getWarehouseId(), targetWarehouseArea.getWarehouseId(), null, realMaterialStock.getTotality(), realMaterialStock, null);
    }

    @Override
    public String generateMoveWarehouseDetail(Integer moveState, String startWarehouseId, String targetWarehouseId, String targetLocationId, Integer quantity, MaterialStockDO realMaterialStock, String trayId) {
        MoveWarehouseDetailDO moveWarehouseDetail = new MoveWarehouseDetailDO();
        moveWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        moveWarehouseDetail.setMoveType(DictConstants.WMS_MOVE_WAREHOUSE_TYPE_1);
        moveWarehouseDetail.setMoveState(moveState);
        moveWarehouseDetail.setStartWarehouseId(startWarehouseId);
        moveWarehouseDetail.setTargetWarehouseId(targetWarehouseId);
        moveWarehouseDetail.setSignLocationId(targetLocationId);
        moveWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
        moveWarehouseDetail.setChooseStockId(realMaterialStock.getId());
        moveWarehouseDetail.setMaterialConfigId(realMaterialStock.getMaterialConfigId());
        moveWarehouseDetail.setBatchNumber(realMaterialStock.getBatchNumber());
        moveWarehouseDetail.setQuantity(quantity);
        moveWarehouseDetail.setCarryTrayId(trayId);
        this.moveWarehouseDetailMapper.insert(moveWarehouseDetail);
        return moveWarehouseDetail.getOrderNumber();
    }

    @Override
    public void moveWarehouseAction(String locationId, String targetWarehouseId, String targetLocationId) {
        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationId(locationId);
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个容器 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);
        if (!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())
            && !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(containerStock.getMaterialType())) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY);
        }


        {
            // 1. 获得 容器上除托盘外的物料库存
            List<MaterialStockDO> allMaterialStockList = moveMaterialService.getAllMaterialStock(containerStock);
            // 2.除托盘外的物料库存集合
            Map<String, MaterialStockDO> allMaterialStockMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);
    /*        // 2.1 过滤托盘和工装
            allMaterialStockMap = FilterMaterialUtils.filter_Tp_Gz(allMaterialStockMap);*/
            // 3. 校验任务单
            Set<String> orderIds = moveMaterialService.checkOrderTask(targetWarehouseId, allMaterialStockMap);

            // 4. 生成搬运任务
            moveMaterialService.createCarryTaskLogic(containerStock,targetLocationId, targetWarehouseId);

            // 5. 填入操作人 签入时已写入操作人--这里就不要再写了
//            moveWarehouseDetailService.setOperatorInBatchMoveWarehouseDetail(orderIds);
        }
    }

    @Override
    public void insetBatchMoveWarehouseDetail(List<OutWarehouseDetailDO> waitOutWarehouseDetailList, Integer moveType, Integer moveStatus, String targetWarehouseId) {
        List<MoveWarehouseDetailDO> moveWarehouseDetailList = new ArrayList<>();
        waitOutWarehouseDetailList.forEach(outWarehouseDetail -> {
            MoveWarehouseDetailDO moveWarehouseDetail = new MoveWarehouseDetailDO();
            moveWarehouseDetail.setOrderNumber(outWarehouseDetail.getOrderNumber());
            moveWarehouseDetail.setMoveType(moveType);
            moveWarehouseDetail.setMoveState(moveStatus);
            moveWarehouseDetail.setStartWarehouseId(outWarehouseDetail.getStartWarehouseId());
            moveWarehouseDetail.setTargetWarehouseId(targetWarehouseId);
            moveWarehouseDetail.setMaterialStockId(outWarehouseDetail.getMaterialStockId());
            moveWarehouseDetail.setChooseStockId(outWarehouseDetail.getMaterialStockId());
            moveWarehouseDetail.setMaterialConfigId(outWarehouseDetail.getMaterialConfigId());
            moveWarehouseDetail.setBatchNumber(outWarehouseDetail.getBatchNumber());
            moveWarehouseDetail.setQuantity(outWarehouseDetail.getQuantity());
            moveWarehouseDetail.setCarryTrayId(outWarehouseDetail.getMaterialStockId());
            moveWarehouseDetailList.add(moveWarehouseDetail);
        });
        moveWarehouseDetailMapper.insertBatch(moveWarehouseDetailList);
    }

    @Override
    public List<OrderReqDTO> getMoveWarehouseDetailByChooseBarCode(String barCode) {
        if(StringUtils.isBlank(barCode)){
            return Collections.emptyList();
        }
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailMapper.selectMoveWarehouseDetailByChooseBarCode(barCode);
        List<OrderReqDTO> orderReqDTOList = MoveWarehouseDetailConvert.INSTANCE.convertList2(moveWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public List<MoveWarehouseDetailDO> getNotFinishedMoveWarehouseDetailList() {
        return moveWarehouseDetailMapper.selectNotFinishedMoveWarehouseDetailList();
    }
}