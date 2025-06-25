package com.miyu.module.wms.service.inwarehousedetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.module.wms.api.order.OrderDistributeApiImpl;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseStatisticsVO;
import com.miyu.module.wms.convert.inwarehousedetail.InWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.CallTrayServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.MoveMaterialServiceImpl;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.takedelivery.TakeDeliveryDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.materialstock.MaterialStockMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
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

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.inwarehousedetail.InWarehouseDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 入库详情 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class InWarehouseDetailServiceImpl implements InWarehouseDetailService {

    @Resource
    private InWarehouseDetailMapper inWarehouseDetailMapper;
    @Resource
    @Lazy
    private OrderDistributeApiImpl orderDistributeApi;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private MaterialStockMapper materialStockMapper;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private MoveMaterialServiceImpl moveMaterialService;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private CallTrayServiceImpl callTrayService;
    @Resource
    @Lazy
    private MaterialInServiceImpl materialInService;
    @Resource
    private ICodeGeneratorService codeGeneratorService;

    @Override
    public String createInWarehouseDetail(InWarehouseDetailSaveReqVO createReqVO) {
        if(StringUtils.isNotBlank(createReqVO.getMaterialStockId()) && materialStockService.validateMaterialInLocked(createReqVO.getMaterialStockId(),createReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(createReqVO.getTargetWarehouseId(), createReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 插入
        InWarehouseDetailDO inWarehouseDetail = BeanUtils.toBean(createReqVO, InWarehouseDetailDO.class);
        inWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        inWarehouseDetailMapper.insert(inWarehouseDetail);
        // 返回
        return inWarehouseDetail.getId();
    }

    @Override
    public void updateInWarehouseDetail(InWarehouseDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateInWarehouseDetailExists(updateReqVO.getId());
        if(StringUtils.isNotBlank(updateReqVO.getMaterialStockId()) && materialStockService.validateMaterialInLocked(updateReqVO.getMaterialStockId(),updateReqVO.getTargetWarehouseId())){
            throw exception(MATERIAL_CONFIG_AREA_IN_STOCK_CHECK);
        }
        if(Objects.equals(updateReqVO.getTargetWarehouseId(), updateReqVO.getStartWarehouseId())){
            throw exception(MATERIAL_CONFIG_IN_OUT_WAREHOUSE_NOT_MATCH);
        }
        // 更新
        InWarehouseDetailDO updateObj = BeanUtils.toBean(updateReqVO, InWarehouseDetailDO.class);
        inWarehouseDetailMapper.updateById(updateObj);
    }


    @Override
    public int updateInWarehouseDetailsByWarehouseId(Collection<String> inWarehouseIds, String warehouseId) {
        LambdaUpdateWrapper<InWarehouseDetailDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(InWarehouseDetailDO::getTargetWarehouseId, warehouseId);
        updateWrapper.in(InWarehouseDetailDO::getId, inWarehouseIds);
        return inWarehouseDetailMapper.update(updateWrapper);
    }

    @Override
    public void deleteInWarehouseDetail(String id) {
        // 校验存在
        validateInWarehouseDetailExists(id);
        // 删除
        inWarehouseDetailMapper.deleteById(id);
    }

    private void validateInWarehouseDetailExists(String id) {
        if (inWarehouseDetailMapper.selectById(id) == null) {
            throw exception(IN_WAREHOUSE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public InWarehouseDetailDO getInWarehouseDetail(String id) {
        return inWarehouseDetailMapper.selectById(id);
    }

    @Override
    public PageResult<InWarehouseDetailDO> getInWarehouseDetailPage(InWarehouseDetailPageReqVO pageReqVO) {
        return inWarehouseDetailMapper.selectPage(pageReqVO);
    }


    @Override
    public InWarehouseDetailDO getInWarehouseDetailByMaterialStockIdAndState(String materialStockId, Integer inWarehouseState) {
        return inWarehouseDetailMapper.selectInWarehouseDetailByMaterialStockIdAndState(materialStockId, inWarehouseState);
    }

    @Override
    public List<InWarehouseDetailDO> getWaitInWarehouseInWarehouseDetailList() {
        return inWarehouseDetailMapper.selectInWarehouseInWarehouseDetailList(DictConstants.WMS_ORDER_DETAIL_STATUS_1);
    }

    @Override
    public List<InWarehouseDetailDO> getFinishInWarehouseDetailList(LocalDateTime[] createTimeRange) {
        return inWarehouseDetailMapper.selectFinishInWarehouseDetailList(createTimeRange);
    }



    @Override
    public List<InWarehouseDetailDO> getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return inWarehouseDetailMapper.selectInWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_1);
    }

    @Override
    public List<InWarehouseDetailDO> getWaitArriveOutWarehouseInWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return inWarehouseDetailMapper.selectInWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_2);
    }

    @Override
    public List<InWarehouseDetailDO> getWaitOnShelfInWarehouseDetailListByMaterialStockIds(Collection<String> materialStockIds) {
        return inWarehouseDetailMapper.selectInWarehouseDetailByMaterialStockIdsAndState(materialStockIds, DictConstants.WMS_ORDER_DETAIL_STATUS_3);
    }

    @Override
    public List<InWarehouseDetailDO> getBatchNotFinishByTargetWarehouseId(String targetWarehouseId){
        return inWarehouseDetailMapper.selectBatchNotFinishByTargetWarehouseId(targetWarehouseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchInWarehouseDetailStateByMaterialStockId(String materialStockId, Integer updateState) {
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
        if (CollectionUtils.isAnyEmpty(allMaterialStockOnTrayIds)) {
            return false;
        }

        // 更新未完成的入库详情单状态
        int i = inWarehouseDetailMapper.updateBatchNotFinish(allMaterialStockOnTrayIds, updateState);
        if(i < 1){
            throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        return true;

       /* // 此入库单为 托盘上的所有非容器类物料的 入库单详情集合 可能分属于不同入库单
        List<InWarehouseDetailDO> inWarehouseDetailList = null;
        //如果更新状态为 待送达 则获取物料待入库入库详情单
        if (DictConstants.WMS_ORDER_DETAIL_STATUS_2.equals(updateState)) {
            // 获得物料-待入库 入库详情单
            inWarehouseDetailList = getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockOnTrayIds);
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_3.equals(updateState)) {
            //更新状态为 待上架  获得物料-待送达 入库详情单
            inWarehouseDetailList = getWaitArriveOutWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockOnTrayIds);
        } else if (DictConstants.WMS_ORDER_DETAIL_STATUS_4.equals(updateState)) {
            //更新状态为 已完成 获得物料-待上架 入库详情单
            inWarehouseDetailList = getWaitOnShelfInWarehouseDetailListByMaterialStockIds(allMaterialStockOnTrayIds);
            if(CollectionUtils.isEmpty(inWarehouseDetailList)){
                // 如果待上架的没找到 就直接找待入库的
                inWarehouseDetailList = getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockOnTrayIds);
            }
        }

        // 若入库详情单为空 则直接返回true
        if (CollectionUtils.isEmpty(inWarehouseDetailList)) {
            throw exception(IN_WAREHOUSE_MATERIAL_IN_WAREHOUSE_ORDER_NOT_IN_EQUAL_WAREHOUSE);
        }

        // 更新入库详情单状态
        inWarehouseDetailList.forEach(o -> o.setInState(updateState));
        Boolean b = inWarehouseDetailMapper.updateBatch(inWarehouseDetailList);
        // 若入库详情单更新失败 则抛出异常
        if (b == null || !b) {
            throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        // 若入库详情单更新成功 则返回true
        return true;*/
    }

    /**
     * 根据托盘上的物料库存集合 查询入库详情
     * 1.先查出托盘上的物料库存
     * 2.如果为空 代表是空托盘回库 直接返回null
     * 3.如果不为空 那他上边可能不为空啊 那就得先把他上边的东西拿出来 （1.全为非容器类物料2.全为容器类物料（工装）3.有容器类物料，也有非容器类物料，容器类物料上还有非物料（工装上的物料））
     * 4.托盘上的 非容器类物料
     * 5.托盘上的 容器类物料id
     * 6.找出托盘上的 容器类物料 和 非容器类物料 区分开来    -- 剩下的全是工装 ，但是工装上边可能还有物料 还得继续找
     * 7.容器类物料继续找他上边的 非容器类物料
     * 8.把托盘上的非容器物料 和 工装上的非容器物料 所有物料 放到一个集合里面  根据此集合的id 查询入库详情单
     *
     * @param materialStockListOnContainer
     * @return
     */
    @Override
    public List<InWarehouseDetailDO> getInWarehouseDetailListByMaterialStockContainer(List<MaterialStockDO> materialStockListOnContainer) {
        if (CollectionUtils.isAnyEmpty(materialStockListOnContainer)) {
            return Collections.emptyList();
        }

        //获得托盘上的非容器类物料库存集合 改版 废弃
//        List<MaterialStockDO> nonContainerMaterialStockList = materialStockService.getNonContainerMaterialStockListByMaterialStockContainer(materialStockListOnContainer);

        // 先把id集中起来
        List<String> allMaterialStockIds = materialStockListOnContainer.stream().map(MaterialStockDO::getId).collect(Collectors.toList());
        // 再查托盘上所有物料的 入库详情单
        return this.getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockIds);
    }

    @Override
    public String takeDeliveryUpdateInWarehouseDetail(TakeDeliveryDO takeDelivery, String materialStockId, MaterialConfigDO materialConfig) {
        MaterialStockDO materialStock = materialStockMapper.selectById(materialStockId);
        if (materialStock == null) throw exception(MATERIAL_STOCK_NOT_EXISTS);

        List<InWarehouseDetailDO> inWarehouseDetailList = this.getInWarehouseDetailListByOrderNumberAndMaterialConfigId(takeDelivery.getOrderNumber(), materialConfig.getId());
        if(CollectionUtils.isAnyEmpty(inWarehouseDetailList)){
            throw exception(IN_WAREHOUSE_DETAIL_NOT_EXISTS);
        }
        Optional<InWarehouseDetailDO> first = inWarehouseDetailList.stream().filter(o -> StringUtils.isBlank(o.getMaterialStockId())).findFirst();
        if (!first.isPresent()) {
            throw exception(IN_WAREHOUSE_DETAIL_NOT_EXISTS);
        }
        // 插入
        InWarehouseDetailDO inWarehouseDetail = first.get();
        inWarehouseDetail.setOrderNumber(takeDelivery.getOrderNumber());
        // todo QianJy 由 到货单号 请求 采购服务获取 仓库id 和 入库类型
//        inWarehouseDetail.setWarehouseId();
//        inWarehouseDetail.setInType();
        /*if (DictConstants.INFRA_BOOLEAN_TINYINT_YES == materialConfig.getMaterialQualityCheck()) {
            inWarehouseDetail.setInState(DictConstants.WMS_ORDER_DETAIL_STATUS_0);
        } else {
            inWarehouseDetail.setInState(DictConstants.WMS_ORDER_DETAIL_STATUS_1);
        }*/
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStock);
        if(warehouseArea == null){
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        inWarehouseDetail.setStartWarehouseId(warehouseArea.getWarehouseId());
        inWarehouseDetail.setBatchNumber(materialStock.getBatchNumber());
        inWarehouseDetail.setMaterialConfigId(materialStock.getMaterialConfigId());
        inWarehouseDetail.setMaterialStockId(materialStock.getId());
        inWarehouseDetail.setChooseStockId(materialStock.getId());
        inWarehouseDetail.setQuantity(takeDelivery.getTdQuantity());
        inWarehouseDetailMapper.updateById(inWarehouseDetail);
        // 返回
        return inWarehouseDetail.getId();
    }

    @Override
    public List<InWarehouseDetailDO> getInWarehouseDetailListByOrderNumberAndMaterialConfigId(String orderNumber, String materialConfigId) {
        return this.inWarehouseDetailMapper.selectInWarehouseDetailByOrderNumberAndMaterialConfigId(orderNumber, materialConfigId);
    }

    /**
     * 校验入库单
     * @param allMaterialStockMap
     */
    @Override
    public List<InWarehouseDetailDO> checkInWarehouseDetail(Map<String, MaterialStockDO> allMaterialStockMap) {
        // 获得入库单详情
        List<InWarehouseDetailDO> allInWarehouseDetailList = this.getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());

        // 入库单详情为空 或者 入库单详情数量与物料数量不一致 则报错
        if (CollectionUtils.isAnyEmpty(allInWarehouseDetailList) ||
                (allInWarehouseDetailList.size() < FilterMaterialUtils.filter_Tp_Gz(allMaterialStockMap).size())) {
            throw exception(IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_ORDER);
        }
        String inWarehouseId = allInWarehouseDetailList.get(0).getTargetWarehouseId();
        for (InWarehouseDetailDO inWarehouseDetail : allInWarehouseDetailList) {
            // 如果入库仓库不为空 需要校验入库仓库是否一致
            if (StringUtils.isNotBlank(inWarehouseId)
                    && !inWarehouseId.equals(inWarehouseDetail.getTargetWarehouseId())) {
                throw exception(IN_WAREHOUSE_DETAIL_NOT_MATCH_IN_WAREHOUSE_ORDER);
            }

            String materialStockId = inWarehouseDetail.getMaterialStockId();
            // 如果物料库存id 不存在，说明此物料需要分拣
            if(StringUtils.isBlank(materialStockId)){
                // 此物料不满足入库状态，需人工分拣后再入库
                throw exception(IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_STATUS);
            }

            if (allMaterialStockMap.containsKey(materialStockId)) {
                MaterialStockDO materialStock = allMaterialStockMap.get(materialStockId);
                // getMaterialStockId 此数据存在 则入库数量和库存数量肯定是一致的 不一致 则有bug
                if (materialStock.getTotality() != inWarehouseDetail.getQuantity()) {
                    throw exception(BUG);
                }
            } else {
                // 物料库存不存在
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }
        return allInWarehouseDetailList;
    }


    @Override
    public List<String> createBatchInWarehouseDetail(List<OrderReqDTO> orderReqDTOList) {
        if(CollectionUtils.isAnyEmpty(orderReqDTOList)){
            return Collections.emptyList();
        }
        // 实体转换
        List<InWarehouseDetailDO> inWarehouseDetailDOS = InWarehouseDetailConvert.INSTANCE.convertList(orderReqDTOList);
        // 普通入库单
        List<InWarehouseDetailDO> normalInWarehouseDetail = new ArrayList<>();
        // 采购入库单
        List<InWarehouseDetailDO> purchaseInWarehouseDetail = new ArrayList<>();
        // 采购退货入库单
        List<InWarehouseDetailDO> purchaseReturnInWarehouseDetail = new ArrayList<>();
        for (InWarehouseDetailDO inWarehouseDetailDO : inWarehouseDetailDOS) {
            if(DictConstants.WMS_IN_WAREHOUSE_TYPE_2 == inWarehouseDetailDO.getInType()
                    || DictConstants.WMS_IN_WAREHOUSE_TYPE_8 == inWarehouseDetailDO.getInType()){
                purchaseInWarehouseDetail.add(inWarehouseDetailDO);
            }else if(DictConstants.WMS_IN_WAREHOUSE_TYPE_5 == inWarehouseDetailDO.getInType()){
                purchaseReturnInWarehouseDetail.add(inWarehouseDetailDO);
            } else {
                normalInWarehouseDetail.add(inWarehouseDetailDO);
            }
        }
        List<String> failMaterialStockIds = new ArrayList<>();
        failMaterialStockIds.addAll(this.checkStockAndCreateInDetail(inWarehouseDetailDOS));

        // 查询此物料是否已存在移库单或其他单据
//        if(!normalInWarehouseDetail.isEmpty())failMaterialStockIds.addAll(this.checkStockAndCreateInDetail(inWarehouseDetailDOS));
        // 采购入库
//        if(!purchaseInWarehouseDetail.isEmpty())failMaterialStockIds.addAll(this.checkStockAndCreatePurchaseInDetail(inWarehouseDetailDOS));
//        // 采购退货入库
//        if(!purchaseReturnInWarehouseDetail.isEmpty())failMaterialStockIds.addAll(this.checkStockAndCreatePurchaseReturnInDetail(inWarehouseDetailDOS));
        // 返回
        return failMaterialStockIds;
    }

    @Override
    public List<String> checkStockAndCreateInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS) {
        // 获取选择入库的物料集合
        Set<String> allMateialStockIdSet = CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getChooseStockId);

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
        List<InWarehouseDetailDO>  successInWarehouseDetailList = inWarehouseDetailDOS.stream().map(s-> {
            if(allMateialStockMap.containsKey(s.getChooseStockId())){
                MaterialStockDO materialStockDO = allMateialStockMap.get(s.getChooseStockId());
                Integer occupyTotality = occupyMaterialStockTotalityMap.getOrDefault(s.getChooseStockId(), 0);
                if(occupyTotality + s.getQuantity() > materialStockDO.getTotality()){
                    // 入库数量大于库存数量
                    failMaterialStockIds.add(s.getChooseStockId());
                    throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
                }
                if(materialStockDO.getTotality() == s.getQuantity()){
                    // 如果入库数量等于库存数量，则绑定入库的库存id
                    s.setMaterialStockId(s.getChooseStockId());
                }
                // 填入物料类型
                s.setMaterialConfigId(materialStockDO.getMaterialConfigId());
                // 填入批次号
                s.setBatchNumber(materialStockDO.getBatchNumber());
                if(StringUtils.isBlank(s.getTargetWarehouseId())){
                    if(StringUtils.isBlank(materialStockDO.getDefaultWarehouseId())){
                        log.error("此物料所属类型未配置默认存放仓库，请先配置默认存放仓库再入库，或填写默认存放仓库");
                        failMaterialStockIds.add(s.getMaterialConfigId());
                        throw exception(MATERIAL_TYPE_DEFAULT_STORAGE_AREA_NOT_EXISTS);
                    }
                    WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
                    if(warehouseArea == null){
                        throw exception(WAREHOUSE_AREA_NOT_EXISTS);
                    }
                    s.setStartWarehouseId(warehouseArea.getWarehouseId());
                    // 赋值 入库仓库
                    s.setTargetWarehouseId(materialStockDO.getDefaultWarehouseId());
                }
                return s;
            }else {
                log.error("物料库存不存在，物料库存id:{}",s.getChooseStockId());
                failMaterialStockIds.add(s.getChooseStockId());
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());

        // 插入失败 则加入失败的id集合
        if(!inWarehouseDetailMapper.insertBatch(successInWarehouseDetailList)){
            log.error("入库单详情插入失败,物料库存id:{}",allMateialStockIdSet);
            failMaterialStockIds.addAll(new ArrayList<>(allMateialStockIdSet));
            throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        // 失败的返回
        return failMaterialStockIds;
    }


    // 采购入库
    @Override
    public List<String> checkStockAndCreatePurchaseInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS) {
        Set<String> allMateialConfigIdSet = CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getMaterialConfigId);

        // 查询存在 此物料类型的单据
        List<InWarehouseDetailDO> haveMarerialConfigOrderList = this.checkMaterialConfigsOrderExists(inWarehouseDetailDOS.get(0).getOrderNumber(),allMateialConfigIdSet);

        // 存在单据的id 集合
        Map<String, InWarehouseDetailDO> haveOrderMaterialConfigIdMap = CollectionUtils.convertMap(haveMarerialConfigOrderList, InWarehouseDetailDO::getMaterialConfigId);
        List<String> failMaterialConfigIds = new ArrayList<>(haveOrderMaterialConfigIdMap.keySet());

        List<InWarehouseDetailDO> successInWarehouseDetailList = inWarehouseDetailDOS.stream().filter(o -> {
            // 已存在入库单
            if(haveOrderMaterialConfigIdMap.containsKey(o.getMaterialConfigId())){
                throw exception(BILL_ALREADY_EXISTS);
            }
            if(StringUtils.isBlank(o.getTargetWarehouseId())){
                String materialConfigId = o.getMaterialConfigId();
                if(StringUtils.isBlank(materialConfigId)){
                    throw exception(MATERIAL_TYPE_NOT_EXISTS);
                }
                MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(materialConfigId);
                if(StringUtils.isBlank(materialConfig.getDefaultWarehouseId())){
                    log.error("此物料所属类型未配置默认存放仓库，请先配置默认存放仓库再入库，或填写默认存放仓库");
                    failMaterialConfigIds.add(o.getMaterialConfigId());
                    throw exception(MATERIAL_TYPE_DEFAULT_STORAGE_AREA_NOT_EXISTS);
                }

                // 赋值 入库仓库
                o.setTargetWarehouseId(materialConfig.getDefaultWarehouseId());
            }
            return true;
        }).collect(Collectors.toList());

        // 插入失败 则加入失败的id集合
        if(!inWarehouseDetailMapper.insertBatch(successInWarehouseDetailList)){
            log.error("入库单详情插入失败,物料类型id:{}",allMateialConfigIdSet);
            failMaterialConfigIds.addAll(new ArrayList<>(allMateialConfigIdSet));
            throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        // 失败的返回
        return failMaterialConfigIds;
    }

    // 采购退货入库
    @Override
    public List<String> checkStockAndCreatePurchaseReturnInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS) {
        // 获取选择入库的物料集合
        Set<String> allMateialStockIdSet = CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getChooseStockId);

        // 根据id查询所有物料库存信息
        List<MaterialStockDO> allMaterialStockList = materialStockService.getMaterialStockIncludeDeletedByIds(allMateialStockIdSet);
        List<MaterialStockDO> notDeletedMaterialStockList = new ArrayList<>();
        List<MaterialStockDO> deletedMaterialStockList = new ArrayList<>();
        for (MaterialStockDO materialStockDO : allMaterialStockList) {
            if(!materialStockDO.getIsExists()){
                deletedMaterialStockList.add(materialStockDO);
            }else {
                notDeletedMaterialStockList.add(materialStockDO);
            }
        }

        // 未删除的物料不能创建采购退货入库单
        List<String> failMaterialStockIds = new ArrayList<>(CollectionUtils.convertSet(notDeletedMaterialStockList, MaterialStockDO::getId));
        // 已删除的物料可以创建采购退货入库单
        Map<String, MaterialStockDO> deletedMateialStockMap = CollectionUtils.convertMap(deletedMaterialStockList, MaterialStockDO::getId);

        // 查询存在 单据的物料
        List<MaterialStockDO> haveOrderMaterialStockList = materialStockService.checkChooseStockOrderExists(deletedMateialStockMap.keySet());
        // key为物料id，value为创建单据占用的物料库存总量
        Map<String, Integer> occupyMaterialStockTotalityMap = CollectionUtils.convertMap(haveOrderMaterialStockList, MaterialStockDO::getId, MaterialStockDO::getOrderQuantity, (v1, v2) -> v1 + v2);



        List<MaterialStockDO> createMaterialStockList = new ArrayList<>();
        List<InWarehouseDetailDO> createInWarehouseDetailList = new ArrayList<>();
        // 物料类型id 和 物料批次号填入
        for(InWarehouseDetailDO inWarehouseDetail : inWarehouseDetailDOS) {
            // 是否存在此物料库存信息，存在的话，则填入物料类型和批次号
            if(!deletedMateialStockMap.containsKey(inWarehouseDetail.getChooseStockId())){
                log.error("物料库存不存在，物料库存id:{}",inWarehouseDetail.getChooseStockId());
                failMaterialStockIds.add(inWarehouseDetail.getChooseStockId());
                throw exception(MATERIAL_STOCK_NOT_EXISTS);
            }

            Integer occupyTotality = occupyMaterialStockTotalityMap.getOrDefault(inWarehouseDetail.getChooseStockId(), 0);

            MaterialStockDO deletedMaterialStockDO = deletedMateialStockMap.get(inWarehouseDetail.getChooseStockId());
            if(occupyTotality + inWarehouseDetail.getQuantity() > deletedMaterialStockDO.getTotality() ) {
                log.error("入库数量大于库存数量，物料库存id:{}", deletedMaterialStockDO.getId());
                failMaterialStockIds.add(deletedMaterialStockDO.getId());
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }


            String materialConfigId = deletedMaterialStockDO.getMaterialConfigId();
/*            String barCode = deletedMaterialStockDO.getBarCode();
            // 查询存在 此物料条码的单据
            List<InWarehouseDetailDO> haveMaterialConfigOrderList = this.checkMaterialStockOrderExistsByBarCodes(inWarehouseDetail.getOrderNumber(), Collections.singletonList(barCode));
            if (!CollectionUtils.isAnyEmpty(haveMaterialConfigOrderList)) {
                log.error("此物料类型已存在入库单，物料库存id:{}", deletedMaterialStockDO.getId());
                failMaterialStockIds.add(deletedMaterialStockDO.getId());
                continue;
            }*/

            // 填入物料类型
            inWarehouseDetail.setMaterialConfigId(materialConfigId);
            // 填入批次号
            inWarehouseDetail.setBatchNumber(deletedMaterialStockDO.getBatchNumber());

            {
                // 因为此物料库存不存在，则需要创建此物料库存信息
                MaterialStockDO createMaterialStock = new MaterialStockDO();
//                createMaterialStock.setBarCode(deletedMaterialStockDO.getBarCode());
                createMaterialStock.setMaterialConfigId(deletedMaterialStockDO.getMaterialConfigId());
                createMaterialStock.setBatchNumber(deletedMaterialStockDO.getBatchNumber());
                createMaterialStock.setTotality(inWarehouseDetail.getQuantity());
                createMaterialStockList.add(createMaterialStock);
                if(StringUtils.isBlank(inWarehouseDetail.getTargetWarehouseId())){
                    if(StringUtils.isBlank(deletedMaterialStockDO.getDefaultWarehouseId())){
                        log.error("此物料所属类型未配置默认存放仓库，请先配置默认存放仓库再入库，或填写默认存放仓库");
                        failMaterialStockIds.add(deletedMaterialStockDO.getId());
                        throw exception(MATERIAL_TYPE_DEFAULT_STORAGE_AREA_NOT_EXISTS);
                    }
                    WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(deletedMaterialStockDO);
                    if(warehouseArea == null){
                        throw exception(WAREHOUSE_AREA_NOT_EXISTS);
                    }
                    inWarehouseDetail.setStartWarehouseId(warehouseArea.getWarehouseId());
                    // 赋值 入库仓库
                    inWarehouseDetail.setTargetWarehouseId(deletedMaterialStockDO.getDefaultWarehouseId());
                }
                if (materialStockService.insert(createMaterialStock) > 0) {
                    inWarehouseDetail.setMaterialStockId(createMaterialStock.getId());
                    createInWarehouseDetailList.add(inWarehouseDetail);
                } else {
                    log.error("物料库存新增失败，物料库存id:{}", deletedMaterialStockDO.getId());
                    failMaterialStockIds.add(deletedMaterialStockDO.getId());
                    throw exception(MATERIAL_STOCK_UPDATE_ERROR);
                }
            }
        }

        if(!createInWarehouseDetailList.isEmpty()){
            // 插入失败 则加入失败的id集合
            if(!inWarehouseDetailMapper.insertBatch(createInWarehouseDetailList)){
                log.error("入库单详情插入失败,物料库存id:{}",allMateialStockIdSet);
                failMaterialStockIds.addAll(new ArrayList<>(allMateialStockIdSet));
                throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
                //手动回滚事务。
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }
        }else {
            log.error("入库单详情插入失败,物料库存id:{}",allMateialStockIdSet);
            failMaterialStockIds.addAll(new ArrayList<>(allMateialStockIdSet));
            throw exception(IN_WAREHOUSE_DETAIL_UPDATE_FAILED);
        }
        // 失败的返回
        return failMaterialStockIds;
    }

    @Override
    public List<InWarehouseDetailDO> checkMaterialConfigsOrderExists(String orderNumber, Collection<String> materialConfigIds) {
        if(StringUtils.isBlank(orderNumber) || CollectionUtils.isAnyEmpty(materialConfigIds)){
            return Collections.emptyList();
        }
        return inWarehouseDetailMapper.selectInWarehouseDetailByOrderNumberAndMaterialConfigIds(orderNumber, materialConfigIds);
    }

    @Override
    public List<InWarehouseDetailDO> checkMaterialStockOrderExistsByBarCodes(String orderNumber, Collection<String> barCodes) {
        if(StringUtils.isBlank(orderNumber) || CollectionUtils.isAnyEmpty(barCodes)){
            return Collections.emptyList();
        }
        return inWarehouseDetailMapper.selectInWarehouseDetailByOrderNumberAndBarCodes(orderNumber, barCodes);
    }

    @Override
    public Collection<OrderReqDTO> getBatchInOrderList(List<String> inOrderNumbers) {
        if(CollectionUtils.isAnyEmpty(inOrderNumbers)){
            return Collections.emptyList();
        }
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailMapper.selectBatchInOrderList(inOrderNumbers);
        List<OrderReqDTO> orderReqDTOList = InWarehouseDetailConvert.INSTANCE.convertList2(inWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public int setOperatorInBatchInWarehouseDetail(Set<String> orderIds) {
        return inWarehouseDetailMapper.updateOperatorInBatchInWarehouseDetail(orderIds);
    }

    @Override
    public List<OrderReqDTO> getInOrderListByOrderTypeAndBatchNumber(Integer orderType, String batchNumber) {
        Integer inType = InWarehouseDetailConvert.INSTANCE.convertInType(orderType);
        if(inType == null || StringUtils.isBlank(batchNumber)){
            return Collections.emptyList();
        }
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailMapper.selectInOrderListByOrderTypeAndBatchNumber(inType, batchNumber);
        List<OrderReqDTO> orderReqDTOList = InWarehouseDetailConvert.INSTANCE.convertList2(inWarehouseDetailDOS);
        return orderReqDTOList;
    }

    @Override
    public int updateById(InWarehouseDetailDO inWarehouseDetail) {
        return inWarehouseDetailMapper.updateById(inWarehouseDetail);
    }

    @Override
    public int createProfitInWarehouseDetail(int profitTotality, MaterialStockDO materialStockDO, String warehouseId, String operator, LocalDateTime operatorTime) {
        // 查询存在 单据的物料
        List<MaterialStockDO> haveOrderMaterialStockList = materialStockService.checkChooseStockOrderExists(Collections.singleton(materialStockDO.getId()));
        if(!haveOrderMaterialStockList.isEmpty()){
            throw exception(BILL_ALREADY_EXISTS);
        }
        InWarehouseDetailDO inWarehouseDetail = new InWarehouseDetailDO();
        inWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        inWarehouseDetail.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_9);
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
        if(warehouseArea == null){
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        inWarehouseDetail.setStartWarehouseId(warehouseArea.getWarehouseId());
        inWarehouseDetail.setTargetWarehouseId(warehouseId);
        inWarehouseDetail.setInState(DictConstants.WMS_ORDER_DETAIL_STATUS_4);
        inWarehouseDetail.setMaterialConfigId(materialStockDO.getMaterialConfigId());
        inWarehouseDetail.setBatchNumber(materialStockDO.getBatchNumber());
        inWarehouseDetail.setQuantity(profitTotality);
        inWarehouseDetail.setMaterialStockId(materialStockDO.getId());
        inWarehouseDetail.setChooseStockId(materialStockDO.getId());
        inWarehouseDetail.setOperator(operator);
        inWarehouseDetail.setOperateTime(operatorTime);
        return inWarehouseDetailMapper.insert(inWarehouseDetail);
    }

    @Override
    public List<InWarehouseDetailDO> getNotFinishedInWarehouseDetailListByAreaId(String areaId) {
        return inWarehouseDetailMapper.selectNotFinishedInWarehouseDetailListByAreaId(areaId);
    }

    @Override
    public void callTray(String callTrayStockId, String callLocationId) {

        // 判断库位是否为空
        if (StringUtils.isBlank(callLocationId)) {
            throw exception(IN_WAREHOUSE_LOCATION_NOT_EXISTS);
        }

        // 必须呼叫容器-- 不呼叫容器 那叫入库
        /*if (StringUtils.isBlank(callContainerStockId)) {
            throw exception(IN_WAREHOUSE_CONTAINER_NOT_SELECTED);
        }

        // 判断容器上是否绑定 非容器类物料
        if (materialStockService.hasNonContainerMaterial(callContainerStockId)) {
            throw exception(IN_WAREHOUSE_CONTAINER_IS_EXISTS_MATERIAL);
        }

        // 判断库位上是否绑定其他物料
        if (materialStockService.checkLocationIsVacant(callLocationId)) {
            throw exception(IN_WAREHOUSE_LOCATION_IS_OCCUPIED);
        }*/

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(callLocationId);
        String targetWarehouseId = warehouseArea.getWarehouseId();

        // 托盘实体
        MaterialStockDO callTrayStock = materialStockService.getMaterialStockById(callTrayStockId);
        if (callTrayStock == null || !DictConstants.WMS_MATERIAL_TYPE_TP.equals(callTrayStock.getMaterialType())) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY_CALL);
        }
        WarehouseLocationDO trayLocation = warehouseLocationService.getWarehouseLocation(callTrayStock.getLocationId());

        // 校验托盘所在库位是否有效
        if(trayLocation == null || trayLocation.getValid() == DictConstants.INFRA_BOOLEAN_TINYINT_NO || trayLocation.getLocked() == DictConstants.INFRA_BOOLEAN_TINYINT_YES){
            throw exception(CARRYING_TASK_TRAY_NOT_SUPPORT_CALL);
        }

        // 校验呼叫位置是否有搬运任务
        List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);
        if(haveTaskLocationIdSet.contains(callLocationId)){
            log.info("此库位：{} -已生成搬运任务，请勿重复下发",callLocationId);
            throw exception(CARRYING_TASK_LOCATION_HAS_TRANSPORT_TASK);
        }

        {
            // 1. 获得 容器上除托盘外的物料库存
            List<MaterialStockDO> allMaterialStockList = callTrayService.getAllMaterialStock(callTrayStock);
            // 2. 除托盘外的物料库存集合
            Map<String, MaterialStockDO> allMaterialStockMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

            // 3. 校验任务单
            callTrayService.checkOrderTask(targetWarehouseId, allMaterialStockMap);

            // 4. 生成搬运任务
            callTrayService.createCarryTaskLogic(null, callTrayStock, callLocationId);
        }

    }

    @Override
    public CommonResult<String> inWarehouseAction(MaterialStockDO materialStock, String locationId) {
        MaterialStockDO containerStock = null;
        if(materialStock == null){
            containerStock = materialStockService.getContainerStockByLocationId(locationId);
        }else {
            containerStock = materialStockService.getMaterialAtLocationByMaterialStock(materialStock);
        }

        // 三坐标 工装 在接驳位上
        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())
            && !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(containerStock.getMaterialType())){
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY);
        }
        // 1. 获得 容器上除托盘外的物料库存
        List<MaterialStockDO> allMaterialStockList =  materialStockService.getAllMaterialStockListByMaterialStockId(containerStock.getId());

        // 2. 除托盘外的物料库存集合
        Map<String, MaterialStockDO> allMaterialStockMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);

        if(!allMaterialStockMap.isEmpty()){

            // 根据托盘上的物料库存 找到所有移库单
            List<MoveWarehouseDetailDO> allMoveWarehouseDetailList = moveWarehouseDetailService.getWaitOutMoveWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());
            //移库
            if(!CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList)){

                // 3. 校验任务单
                List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveMaterialService.checkOrderTask2(allMaterialStockMap);
                String targetWarehouseId = moveWarehouseDetailDOS.get(0).getTargetWarehouseId();
                String targetLocationId = moveWarehouseDetailDOS.get(0).getSignLocationId();

                // 4. 生成搬运任务
                moveMaterialService.createCarryTaskLogic(containerStock, targetLocationId,targetWarehouseId);

                // 5. 填入操作人
                moveWarehouseDetailService.setOperatorInBatchMoveWarehouseDetail(CollectionUtils.convertSet(moveWarehouseDetailDOS, MoveWarehouseDetailDO::getId));
                return CommonResult.success(null);
            }

            List<InWarehouseDetailDO> allInWarehouseDetailList = this.getWaitInWarehouseInWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());

            // 入库
            if(!CollectionUtils.isAnyEmpty(allInWarehouseDetailList)){

                // 3. 校验任务单
                List<InWarehouseDetailDO> inWarehouseDetailDOS = materialInService.checkOrderTask2(allMaterialStockMap);
                InWarehouseDetailDO inWarehouseDetailDO = inWarehouseDetailDOS.get(0);
                String targetWarehouseId = inWarehouseDetailDOS.get(0).getTargetWarehouseId();

                // 4. 生成搬运任务
                materialInService.createCarryTaskLogic(null, containerStock,targetWarehouseId);

                // 5. 填入操作人
                materialInService.setOperatorInOrderTask(CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getId));

                // 入库 将呼叫托盘map 移除
                orderDistributeApi.removeMaterialConfigInTray(inWarehouseDetailDO.getMaterialConfigId() + inWarehouseDetailDO.getStartWarehouseId() + inWarehouseDetailDO.getTargetWarehouseId());

                return CommonResult.success(null);
            }
            // 存在无单据的物料 无法入库
            throw exception(IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_ORDER);
        }else {// 空托盘入库
            WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(containerStock);
            if(warehouseArea == null){
                throw exception(IN_WAREHOUSE_CONTAINER_NOT_IN_LOCATION);
            }
            String atWarehouseId =warehouseArea.getWarehouseId() ;
            materialInService.handleIdleTrayInWarehouse(containerStock, atWarehouseId);
        }
        return CommonResult.success(null);

    }

    @Override
    public String tryGenerateInWarehouseDetail(MaterialStockDO materialStock, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock) {
        if(!materialInService.createCarryTaskLogic(materialStock, targetWarehouseId)){
            return null;
        }

        return generateInWarehouseDetail(DictConstants.WMS_ORDER_DETAIL_STATUS_1, startWarehouseId, targetWarehouseId, quantity, realMaterialStock, null);
    }

    @Override
    public String generateInWarehouseDetail(Integer inState, String startWarehouseId, String targetWarehouseId, Integer quantity, MaterialStockDO realMaterialStock, String trayId) {
        InWarehouseDetailDO inWarehouseDetail = new InWarehouseDetailDO();
        inWarehouseDetail.setOrderNumber(codeGeneratorService.getIOM_Number());
        inWarehouseDetail.setInType(DictConstants.WMS_IN_WAREHOUSE_TYPE_4);
        inWarehouseDetail.setInState(inState);
        inWarehouseDetail.setStartWarehouseId(startWarehouseId);
        inWarehouseDetail.setTargetWarehouseId(targetWarehouseId);
        inWarehouseDetail.setMaterialStockId(realMaterialStock.getId());
        inWarehouseDetail.setChooseStockId(realMaterialStock.getId());
        inWarehouseDetail.setMaterialConfigId(realMaterialStock.getMaterialConfigId());
        inWarehouseDetail.setBatchNumber(realMaterialStock.getBatchNumber());
        inWarehouseDetail.setQuantity(quantity);
        inWarehouseDetail.setCarryTrayId(trayId);
        inWarehouseDetailMapper.insert(inWarehouseDetail);
        return inWarehouseDetail.getOrderNumber();
    }

    @Override
    public List<OrderReqDTO> getInWarehouseDetailByChooseBarCode(String barCode) {
        if(StringUtils.isBlank(barCode)){
            return Collections.emptyList();
        }
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailMapper.selectInWarehouseDetailByChooseBarCode(barCode);
        List<OrderReqDTO> orderReqDTOList = InWarehouseDetailConvert.INSTANCE.convertList2(inWarehouseDetailDOS);
        return orderReqDTOList;
    }


}