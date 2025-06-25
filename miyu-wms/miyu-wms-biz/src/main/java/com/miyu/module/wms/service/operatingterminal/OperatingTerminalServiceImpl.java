package com.miyu.module.wms.service.operatingterminal;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordEventDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsStepResourceDTO;
import com.miyu.module.wms.api.order.OrderDistributeApiImpl;
import com.miyu.module.wms.controller.admin.operatingterminal.vo.CutterOrderVO;
import com.miyu.module.wms.controller.admin.operatingterminal.vo.OrderVO;
import com.miyu.module.wms.convert.inwarehousedetail.InWarehouseDetailConvert;
import com.miyu.module.wms.convert.movewarehousedetail.MoveWarehouseDetailConvert;
import com.miyu.module.wms.convert.outwarehousedetail.OutWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.materialconfig.MaterialConfigMapper;
import com.miyu.module.wms.dal.mysql.materialstock.MaterialStockMapper;
import com.miyu.module.wms.dal.mysql.materialstorage.MaterialStorageMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.instruction.InstructionService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Service
@Validated
public class OperatingTerminalServiceImpl implements OperatingTerminalService {

    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialStockMapper materialStockMapper;
    @Resource
    private MaterialConfigMapper materialConfigMapper;
    @Resource
    private MaterialStorageMapper materialStorageMapper;
    @Resource
    private MaterialConfigAreaService materialConfigAreaService;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    private WarehouseService warehouseService;


    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;


    @Override
    public List<OrderVO> getWaitOrderDetailList(String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        String warehouseId = warehouseAreaDO.getWarehouseId();

        List<InWarehouseDetailDO> waitInWarehouseList = inWarehouseDetailService.getWaitInWarehouseInWarehouseDetailList();
        List<MoveWarehouseDetailDO> waitMoveWarehouseList = moveWarehouseDetailService.getWaitMoveWarehouseMoveWarehouseDetailList();
        List<OutWarehouseDetailDO> waitOutWarehouseList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailList();
        // 入库起始仓库过滤
        List<OrderVO> orderVO1 = waitInWarehouseList.stream().filter(inWarehouseDetailDO -> warehouseId.equals(inWarehouseDetailDO.getStartWarehouseId())).map(inWarehouseDetailDO -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderType(InWarehouseDetailConvert.INSTANCE.convertStringType(inWarehouseDetailDO.getInType()));
            orderVO.setBatchNumber(inWarehouseDetailDO.getBatchNumber());
            orderVO.setMaterialNumber(inWarehouseDetailDO.getMaterialNumber());
            orderVO.setChooseBarCode(inWarehouseDetailDO.getChooseBarCode());
            orderVO.setQuantity(inWarehouseDetailDO.getQuantity());
            return orderVO;
        }).collect(Collectors.toList());
        // 移库起始仓库
        List<OrderVO> orderVO2 = waitMoveWarehouseList.stream().filter(moveWarehouseDetailDO -> warehouseId.equals(moveWarehouseDetailDO.getStartWarehouseId())).map(moveWarehouseDetailDO -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderType(MoveWarehouseDetailConvert.INSTANCE.convertStringType(moveWarehouseDetailDO.getMoveType()));
            orderVO.setBatchNumber(moveWarehouseDetailDO.getBatchNumber());
            orderVO.setMaterialNumber(moveWarehouseDetailDO.getMaterialNumber());
            orderVO.setChooseBarCode(moveWarehouseDetailDO.getChooseBarCode());
            orderVO.setQuantity(moveWarehouseDetailDO.getQuantity());
            return orderVO;
        }).collect(Collectors.toList());
        // 出库目标仓库过滤
        List<OrderVO> orderVO3 = waitOutWarehouseList.stream().filter(outWarehouseDetailDO -> warehouseId.equals(outWarehouseDetailDO.getTargetWarehouseId())).map(outWarehouseDetailDO -> {
            OrderVO orderVO = new OrderVO();
            orderVO.setOrderType(OutWarehouseDetailConvert.INSTANCE.convertStringType(outWarehouseDetailDO.getOutType()));
            orderVO.setBatchNumber(outWarehouseDetailDO.getBatchNumber());
            orderVO.setMaterialNumber(outWarehouseDetailDO.getMaterialNumber());
            orderVO.setChooseBarCode(outWarehouseDetailDO.getChooseBarCode());
            orderVO.setQuantity(outWarehouseDetailDO.getQuantity());
            return orderVO;
        }).collect(Collectors.toList());


        // 合并订单
        List<OrderVO> orderList = new ArrayList<>();
        orderList.addAll(orderVO1);
        orderList.addAll(orderVO2);
        orderList.addAll(orderVO3);

        /*// 自动调度单的内容也得加进去
        List<ProductionOrderRespDTO> cacheProductionOrderList = orderDistributeApi.getCacheProductionOrderList();
        cacheProductionOrderList.forEach(productionOrderReqDTO -> {
            boolean flag = false;
            OrderVO orderVO = new OrderVO();
            if (DictConstants.WMS_ORDER_TYPE_PRODUCE_OUT.equals(productionOrderReqDTO.getOrderType())) {
                // 出库目标仓库过滤
                if (warehouseId.equals(productionOrderReqDTO.getTargetWarehouseId())) {
                    flag = true;
                    orderVO.setOrderType("调度出库");
                }
            } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_IN.equals(productionOrderReqDTO.getOrderType())) {
                //入库起始仓库过滤
                if (warehouseId.equals(productionOrderReqDTO.getAtWarehouseId())) {
                    flag = true;
                    orderVO.setOrderType("调度入库");
                }
            } else if (DictConstants.WMS_ORDER_TYPE_PRODUCE_MOVE.equals(productionOrderReqDTO.getOrderType())) {
                //移库起始仓库
                if (warehouseId.equals(productionOrderReqDTO.getAtWarehouseId())) {
                    flag = true;
                    orderVO.setOrderType("调度移库");
                }
            }
            if (flag) {
                MaterialStockRespDTO materialStock = productionOrderReqDTO.getMaterialStock();
                orderVO.setBatchNumber(materialStock.getBatchNumber());
                orderVO.setMaterialNumber(materialStock.getMaterialNumber());
                orderVO.setChooseBarCode(materialStock.getBarCode());
                orderVO.setQuantity(materialStock.getTotality());
                orderList.add(orderVO);
            }

        });*/
        return orderList;
    }

    @Override
    public List<CutterOrderVO> getCutterWaitOrderDetailList(String warehouseId) {
        List<OutWarehouseDetailDO> waitOutWarehouseList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailList();
        Set<String> materials = new HashSet<>();
        Set<String> warehouseIds = new HashSet<>();
        Set<String> locationIds = new HashSet<>();
        waitOutWarehouseList = waitOutWarehouseList.stream().filter(outWarehouseDetailDO -> warehouseId.equals(outWarehouseDetailDO.getStartWarehouseId())).peek(outWarehouseDetailDO -> {
            materials.add(outWarehouseDetailDO.getChooseStockId());
            warehouseIds.add(outWarehouseDetailDO.getTargetWarehouseId());
            locationIds.add(outWarehouseDetailDO.getNeedLocationId());
        }).collect(Collectors.toList());

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockByIds(materials);
        Map<String, MaterialStockDO> materialStockMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(materialStockList, MaterialStockDO::getId);
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseByIds(warehouseIds);
        Map<String, WarehouseDO> warehouseMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(warehouseList, WarehouseDO::getId);
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationListByIds(locationIds);
        Map<String, WarehouseLocationDO> warehouseLocationDOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(warehouseLocationList, WarehouseLocationDO::getId);

        // 出库目标仓库过滤
        List<CutterOrderVO> orderList = waitOutWarehouseList.stream().map(outWarehouseDetailDO -> {
            CutterOrderVO cutterOrderVO = new CutterOrderVO();
            cutterOrderVO.setOrderType(OutWarehouseDetailConvert.INSTANCE.convertStringType(outWarehouseDetailDO.getOutType()));
            if (materialStockMap.containsKey(outWarehouseDetailDO.getChooseStockId())) {
                MaterialStockDO materialStockDO = materialStockMap.get(outWarehouseDetailDO.getChooseStockId());
                cutterOrderVO.setMaterialNumber(materialStockDO.getMaterialNumber());
                cutterOrderVO.setMaterialName(materialStockDO.getMaterialName());
                cutterOrderVO.setBarCode(materialStockDO.getBarCode());
            }
            if (warehouseMap.containsKey(outWarehouseDetailDO.getTargetWarehouseId())) {
                cutterOrderVO.setTargetWarehouseName(warehouseMap.get(outWarehouseDetailDO.getTargetWarehouseId()).getWarehouseName());
            }
            if (warehouseLocationDOMap.containsKey(outWarehouseDetailDO.getNeedLocationId())) {
                cutterOrderVO.setTargetLocationName(warehouseLocationDOMap.get(outWarehouseDetailDO.getNeedLocationId()).getLocationName());
            }
            cutterOrderVO.setQuantity(outWarehouseDetailDO.getQuantity());
            return cutterOrderVO;
        }).collect(Collectors.toList());

        return orderList;
    }


    @Override
    public List<CutterOrderVO> getCutterWaitInOrderDetailList(String warehouseId) {
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.getBatchNotFinishByTargetWarehouseId(warehouseId);
        Set<String> materials = new HashSet<>();
        Set<String> locationIds = new HashSet<>();
        Set<String> storageIds = new HashSet<>();
        inWarehouseDetailDOS.forEach(inWarehouseDetailDO -> {
            materials.add(inWarehouseDetailDO.getMaterialStockId());
        });

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockByIds(materials);
        materialStockList.forEach(materialStockDO -> {
            if(StringUtils.isNotBlank(materialStockDO.getLocationId()))locationIds.add(materialStockDO.getLocationId());
            if(StringUtils.isNotBlank(materialStockDO.getStorageId()))storageIds.add(materialStockDO.getStorageId());
        });

        List<WarehouseLocationDO> warehouseLocationList = locationIds.isEmpty() ? new ArrayList<>() : warehouseLocationService.getWarehouseLocationListByIds(locationIds);
        List<MaterialStorageDO> materialStorageList = storageIds.isEmpty() ? new ArrayList<>() : materialStorageMapper.selectBatchIds(storageIds);
        Map<String, MaterialStockDO> materialStockMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(materialStockList, MaterialStockDO::getId);
        Map<String, WarehouseLocationDO> warehouseLocationMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(warehouseLocationList, WarehouseLocationDO::getId);
        Map<String, MaterialStorageDO> materialStorageMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(materialStorageList, MaterialStorageDO::getId);

        return inWarehouseDetailDOS.stream().map(inWarehouseDetailDO -> {
            CutterOrderVO cutterOrderVO = new CutterOrderVO();
            cutterOrderVO.setOrderType(InWarehouseDetailConvert.INSTANCE.convertStringType(inWarehouseDetailDO.getInType()));
            if (materialStockMap.containsKey(inWarehouseDetailDO.getMaterialStockId())) {
                MaterialStockDO materialStockDO = materialStockMap.get(inWarehouseDetailDO.getMaterialStockId());
                cutterOrderVO.setMaterialNumber(materialStockDO.getMaterialNumber());
                cutterOrderVO.setMaterialName(materialStockDO.getMaterialName());
                cutterOrderVO.setBarCode(materialStockDO.getBarCode());
                if(StringUtils.isNotBlank(materialStockDO.getLocationId())){
                    cutterOrderVO.setLocationName(warehouseLocationMap.getOrDefault(materialStockDO.getLocationId(), new WarehouseLocationDO()).getLocationName());
                }else {
                    cutterOrderVO.setLocationName(materialStorageMap.getOrDefault(materialStockDO.getStorageId(), new MaterialStorageDO()).getStorageName());
                }
            }

            cutterOrderVO.setQuantity(inWarehouseDetailDO.getQuantity());
            return cutterOrderVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MaterialStockDO> getMaterialStockByLocationIds(Collection<String> locationIds) {
        List<MaterialStockDO> materialStockDOList = materialStockMapper.selectMaterialStockByLocationIds(locationIds);
        // 判断是否有库位上绑定多个物料
        Set<String> set = new HashSet<>();
        for (MaterialStockDO materialStockDO : materialStockDOList) {
            if (materialStockDO.getLocationId() != null) {
                if (!set.add(materialStockDO.getLocationId())) {
                    throw exception(MATERIAL_CONFIG_BIN_NOT_BIND_POSITION);
                }
            }
        }

        return materialStockDOList;
    }

    /**
     * 根据库位编码查物料信息
     *
     * @param locationCode 库位编码
     * @return 物料信息
     */
    @Override
    public MaterialStockDO getMaterialStockByLocationCode(String locationCode) {
        List<MaterialStockDO> materialStockDOList = materialStockService.getMaterialStockByLocationCode(locationCode);
        if (materialStockDOList == null || materialStockDOList.isEmpty()) {
            return null;
        }

        if (materialStockDOList.size() > 1) {
            throw exception(new ErrorCode(2_101_014_011, "一个库位上绑定了多个物料"));
        }
        return materialStockDOList.get(0);
    }

    @Override
    public List<MaterialStockDO> getEmptyTrayListByWareHouseArea() {
        return materialStockMapper.getEmptyTrayListByWareHouseArea();
    }

    @Override
    public List<MaterialStockDO> getMaterialStockByOutWarehouseDetail(String warehouseId) {
        //呼叫托盘 出库 物料的目标仓库为此仓库
        List<MaterialStockDO> out = materialStockMapper.getMaterialStockByOutWarehouseDetail(warehouseId);
        //呼叫托盘 移库 物料的目标仓库为仓此库
//        List<MaterialStockDO> move = materialStockMapper.getMaterialStockByMoveWarehouseDetail(warehouseId);
        ArrayList<MaterialStockDO> result = new ArrayList<>();
        result.addAll(out);
//        result.addAll(move);
        return result;
    }

    @Override
    public List<MaterialStockDO> getMaterialStockListByWarehouseId(String warehouseId) {
        return materialStockMapper.getMaterialStockListByWarehouseId(warehouseId);
    }

    //获取仓库下存储库区的托盘和工装
    @Override
    public List<MaterialStockDO> getMaterialStockListByWarehouseIdAndAreaTypeEq1(String warehouseId) {
        return materialStockService.getMaterialStockListByWarehouseIdAndAreaTypeEqContainer(warehouseId);
    }

    @Resource
    MaterialInServiceImpl dispatchCarryTaskLogicService;
    @Resource
    InstructionService instructionService;

    /**
     * 托盘入库(上架)操作
     *
     * @param map sourceLocationCode:源库位编码
     * @return
     */
    @Override
    public InstructionDO available(Map<String, String> map) {
        String sourceLocationCode = map.get("sourceLocationCode");
        //获得源库位
        WarehouseLocationDO sourceWarehouseLocation = warehouseLocationService.getWarehouseLocationByLocationCode(sourceLocationCode);
        if (sourceWarehouseLocation == null) {
            throw exception(new ErrorCode(100_100_100, "库位不存在"));
        }
        //获得源库区
        WarehouseAreaDO sourceWarehouseArea = warehouseAreaService.getWarehouseArea(sourceWarehouseLocation.getWarehouseAreaId());
        //获得物料库存
        MaterialStockDO materialStockDO = materialStockService.getContainerStockByLocationId(sourceWarehouseLocation.getId());
        if (materialStockDO == null) {
            throw exception(new ErrorCode(100_100_101, "物料库存不存在"));
        }

        //根据物料类型id和仓库id 获取物料类型关联--存储库区配置
        List<MaterialConfigAreaDO> materialConfigAreaDOList = materialConfigAreaService.getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(materialStockDO.getMaterialConfigId(), sourceWarehouseArea.getWarehouseId());
        if (materialConfigAreaDOList == null || materialConfigAreaDOList.isEmpty()) {
            throw exception(new ErrorCode(100_100_102, "适配库区不存在"));
        }

        //获得库区id列表
        List<String> areaIds = new ArrayList<>();
        for (MaterialConfigAreaDO data : materialConfigAreaDOList) {
            areaIds.add(data.getWarehouseAreaId());
        }

        //根据接驳库区 获取空闲的接驳库位  没有返回null  此方法不可抛出异常
        WarehouseLocationDO targetWarehouseLocationDO = dispatchCarryTaskLogicService.getFreeEmptyUpLocationByAreaIds(areaIds);
        if (targetWarehouseLocationDO == null) {
            throw exception(new ErrorCode(100_100_103, "空闲接驳库位不存在"));
        }

        //上架操作
        return instructionService.onShelfInstruction(materialStockDO.getId(), sourceWarehouseLocation.getId(), targetWarehouseLocationDO.getId());
    }

    /**
     * 选择托盘(下架)操作
     *
     * @param map materialStockId  物料id
     * @param map startLocationId  起始库位id
     * @param map targetLocationCode 目标库位编码
     * @return
     */
    @Override
    public InstructionDO palletUnloading(Map<String, String> map) {
        String materialStockId = map.get("materialStockId");
        String startLocationId = map.get("startLocationId");
        String targetLocationCode = map.get("targetLocationCode");

        if (materialStockId == null || startLocationId == null || targetLocationCode == null) {
            throw exception(new ErrorCode(100_100_100, "参数异常"));
        }

        WarehouseLocationDO targetWarehouseLocation = warehouseLocationService.getWarehouseLocationByLocationCode(targetLocationCode);
        if (targetWarehouseLocation == null) {
            throw exception(new ErrorCode(100_100_100, "库位不存在"));
        }

        return instructionService.offShelfInstruction(materialStockId, startLocationId, targetWarehouseLocation.getId());
    }

    /**
     * 将物料储位改为指定容器的储位
     *
     * @param materialStockId  物料id
     * @param storageStockId   容器id
     * @param orderId          拣选订单id
     * @param materialPosition 容器位号(容器上的位置),可为null
     */
    @Override
    public void changeMaterialStockStorage(String materialStockId, String storageStockId, String orderId, Integer materialPosition) {
        //物料库存
        MaterialStockDO materialStock = materialStockMapper.selectById(materialStockId);
        //容器库存
        MaterialStockDO storageStock = materialStockMapper.selectById(storageStockId);
        if (materialStock == null || storageStock == null) {
            throw exception(new ErrorCode(100_100_100, "物料或容器不存在"));
        }

        //物料类型
        MaterialConfigDO materialConfig = materialConfigMapper.selectById(materialStock.getMaterialConfigId());
        //容器类型
        MaterialConfigDO storageConfig = materialConfigMapper.selectById(storageStock.getMaterialConfigId());
        if (materialConfig == null || storageConfig == null) {
            throw exception(new ErrorCode(100_100_100, "物料或容器无类型"));
        }

        //物料能存放的容器类型
        List<String> containerConfigIds = StringListUtils.stringToArrayList(materialConfig.getContainerConfigIds());
        //校验物料指定的容器类型是否正确
        if (!containerConfigIds.isEmpty()) {
            if (!containerConfigIds.contains(storageStock.getMaterialConfigId())) {
                throw exception(new ErrorCode(100_100_100, "不满足物料指定的容器类型"));
            }
        }

        //获取储位列表 根据容器库存ID
        List<MaterialStorageDO> storageList = materialStorageService.getMaterialStorageListByContainerStockId(storageStock.getId());

        //根据容器类型分流(托盘还是工装)
        if (DictConstants.WMS_MATERIAL_TYPE_TP.equals(storageConfig.getMaterialType())) {
            if (storageList.size() != 1) {
                throw exception(new ErrorCode(100_100_100, "托盘对应储位数量错误"));
            }
            if (StringUtils.isNotBlank(orderId)) {
                materialStockService.verifyMaterialPicking(orderId, materialStock, null, storageList.get(0).getId());
            } else {
                //设置新库位/储位id
                materialStock.setStorageId(storageList.get(0).getId());
                materialStock.setLocationId("");
                //更新数据
                materialStockService.updateById(materialStock);
            }
        } else if (DictConstants.WMS_MATERIAL_TYPE_GZ.equals(storageConfig.getMaterialType())) {
            if (storageList.isEmpty()) {
                throw exception(MATERIAL_STORAGE_NOT_EXISTS);
            }
            //判断是否单储位（0否、1是）
            if (storageConfig.getMaterialStorage() == 1) {
                //设置新库位/储位id
                materialStock.setStorageId(storageList.get(0).getId());
                materialStock.setLocationId("");
                //更新数据
                materialStockService.updateById(materialStock);
            } else {
                //多储位
                if (materialPosition == null) {
                    throw exception(new ErrorCode(100_100_100, "容器位号为空"));
                }
                //获得储位id
                String storageId = materialStorageService.getStorageIdByMaterialStockIdAndSite(storageStock.getId(), materialPosition);
                //设置新库位/储位id
                materialStock.setStorageId(storageId);
                materialStock.setLocationId("");
                //更新数据
                materialStockService.updateById(materialStock);
            }
        } else {
            //不是托盘也不是工装
            throw exception(new ErrorCode(100_100_100, "不支持的容器类型"));
        }
    }

    @Override
    public MaterialConfigDO getMaterialConfigByBarcode(String barcode) {
        return materialConfigMapper.getMaterialConfigByBarcode(barcode);
    }

    @Override
    public List<MaterialConfigDO> getContainerIdListByMaterialId(String materialConfigId) {
        List<MaterialConfigDO> materialConfigDOList = new ArrayList<>();
        MaterialConfigDO materialConfigDO = materialConfigMapper.selectById(materialConfigId);

        String containerIds = materialConfigDO.getContainerConfigIds();
        if (containerIds == null) {
            return materialConfigDOList;
        }
        JSONArray jsonArray = JSONArray.parseArray(containerIds);
        jsonArray.forEach(id -> {
            MaterialConfigDO containerMaterialConfigDO = materialConfigMapper.selectById(id.toString());
            if (containerMaterialConfigDO != null) {
                materialConfigDOList.add(containerMaterialConfigDO);
            }
        });
        return materialConfigDOList;
    }

    @Override
    public Boolean bindTrayOrClamp(Map<String, Object> map) throws RuntimeException {
//        MaterialStockDO materialStockDO, MaterialStockDO trayMaterialStockDO, MaterialStockDO clampMaterialStockDO
        MaterialStockDO materialStockDO = (MaterialStockDO) map.get("materialStockDO");
        MaterialStockDO trayMaterialStockDO = (MaterialStockDO) map.get("trayMaterialStockDO");
        MaterialStockDO clampMaterialStockDO = (MaterialStockDO) map.get("clampMaterialStockDO");
        //工件指定容器
        List<MaterialConfigDO> materialConfigContainerList = this.getContainerIdListByMaterialId(materialStockDO.getMaterialConfigId());

        if (!CollectionUtils.isEmpty(materialConfigContainerList)) {
            if (clampMaterialStockDO == null && trayMaterialStockDO != null) {
                if (!materialConfigContainerList.contains(trayMaterialStockDO.getId())) {
                    throw new IllegalArgumentException("托盘不在工件指定容器内");
                }
            } else {
                if (!materialConfigContainerList.contains(clampMaterialStockDO.getId())) {
                    throw new IllegalArgumentException("工装不在工件指定容器内");
                }
            }
        }
        //查询托盘上绑定的物料
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByContainerId(trayMaterialStockDO.getId());


        MaterialStockDO creatMaterialStockDO = new MaterialStockDO();
        //总库存数量大于一，批量物料
        //更新数量，位置
        if (materialStockDO.getTotality() > 1) {
            BeanUtils.copyProperties(materialStockDO, creatMaterialStockDO);
            Integer installUnloadQuantity = Integer.parseInt((String) map.get("installUnloadQuantity"));

            if (installUnloadQuantity < materialStockDO.getTotality()) {
                materialStockDO.setTotality(materialStockDO.getTotality() - installUnloadQuantity);
                creatMaterialStockDO.setId(null);
                creatMaterialStockDO.setTotality(installUnloadQuantity);

                materialStockService.updateById(materialStockDO);
                materialStockService.insert(creatMaterialStockDO);
            }

        } else {
            creatMaterialStockDO = (MaterialStockDO) map.get("materialStockDO");
        }


        List<MaterialStorageDO> materialStorageList = null;
        //有选择被绑定工装,则绑定在工装上
        if (clampMaterialStockDO != null) {
            //判断托盘上是否有此工装
            if (!materialStockList.contains(clampMaterialStockDO.getId())) {
                throw new IllegalArgumentException("托盘上没有此工装");
            }
            materialStorageList = materialStorageMapper.selectList(MaterialStorageDO::getStorageCode, clampMaterialStockDO.getBarCode());

        } else {
            //没有选择被绑定工装,则绑定在托盘上
            materialStorageList = materialStorageMapper.selectList(MaterialStorageDO::getStorageCode, trayMaterialStockDO.getBarCode());
        }
        if (materialStorageList.size() != 1) {
            throw exception(MATERIAL_STOCK_BIN_CODE_ERROR);
        }
        creatMaterialStockDO.setStorageId(materialStorageList.get(0).getId());
        materialStockService.updateById(creatMaterialStockDO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRecordStartForMCS(Map<String, Object> map) {

        /* **************************************************基本数据********************************** */

        //任务类型,0:不符合要求,1:卸,2:常规安装,3:换装(更换工装),4:已完成(物料已绑定到工装上)
        Integer type = (Integer) map.get("type");
        if (type == null) throw exception(new ErrorCode(100_100_100, "没有任务类型"));
        //物料库存
        MaterialStockDO materialStock = materialStockService.getMaterialStockByBarCode((String) map.get("materialBarCode"));
        if (materialStock == null) throw exception(new ErrorCode(100_100_100, "物料条码错误"));

        //拣选位置容器库存
        MaterialStockDO pickerAreaStock = materialStockService.getMaterialStockById((String) map.get("pickerAreaTrayId"));
        //工装库存
        MaterialStockDO toolingStock = materialStockService.getMaterialStockByBarCode((String) map.get("toolingBarCode"));
        //工装位号
        String toolingPositionString = (String) map.get("toolingPosition");
        Integer toolingPosition = null;
        if (toolingPositionString != null && !toolingPositionString.isEmpty()) {
            try {
                toolingPosition = Integer.parseInt(toolingPositionString);
            } catch (Exception ignored) {
                throw exception(new ErrorCode(100_100_100, "工装位号应该为整数"));
            }
        }

        /* **************************************************任务类型分支********************************** */

        switch (type) {
            //不符合要求
            case 0: {
                throw exception(new ErrorCode(100_100_100, "不符合要求"));
            }
            //卸装
            case 1: {
                if (pickerAreaStock == null) throw exception(new ErrorCode(100_100_100, "拣选位置没有托盘"));
                //拣选位置容器类型
                MaterialConfigDO pickerAreaMaterialConfig = materialConfigMapper.selectById(pickerAreaStock.getMaterialConfigId());
                if (!Objects.equals(pickerAreaMaterialConfig.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_TP)) {
                    throw exception(new ErrorCode(100_100_100, "拣选位置容器不是托盘"));
                }
                //更换绑定
                changeMaterialStockStorage(materialStock.getId(), pickerAreaStock.getId(), null, null);
            }
            break;
            //常规安装
            case 2: {
                if (toolingStock == null) throw exception(new ErrorCode(100_100_100, "请输入正确的工装条码"));
                changeMaterialStockStorage(materialStock.getId(), toolingStock.getId(), null, toolingPosition);
            }
            break;
            //换装(更换工装)
            case 3: {
                if (toolingStock == null) throw exception(new ErrorCode(100_100_100, "请输入正确的工装条码"));
                if (materialStock.getStorageId().isEmpty())
                    throw exception(new ErrorCode(100_100_100, "物料未绑定到储位上"));
                //物料所在的储位
                MaterialStorageDO materialOnStorageDO = materialStorageMapper.selectById(materialStock.getStorageId());
                //物料所在容器的库存
                MaterialStockDO materialOnContainerStock = materialStockService.getMaterialStock(materialOnStorageDO.getMaterialStockId());
                //物料所在容器的类型
                MaterialConfigDO materialOnContainerConfig = materialConfigMapper.selectById(materialOnContainerStock.getMaterialConfigId());
                if (!Objects.equals(materialOnContainerConfig.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_GZ)) {
                    throw exception(new ErrorCode(100_100_100, "物料所在容器不是工装"));
                }
                //更换物料绑定
                changeMaterialStockStorage(materialStock.getId(), toolingStock.getId(), null, toolingPosition);
                //工装位置互换
                String location = toolingStock.getLocationId();
                String storage = toolingStock.getStorageId();
                toolingStock.setLocationId(materialOnContainerStock.getLocationId());
                toolingStock.setStorageId(materialOnContainerStock.getStorageId());
                materialOnContainerStock.setLocationId(location);
                materialOnContainerStock.setStorageId(storage);
                materialStockService.updateById(toolingStock);
                materialStockService.updateById(materialOnContainerStock);
            }
            break;
            //已完成(物料已绑定到工装上)
            case 4: {
                if (toolingStock == null) throw exception(new ErrorCode(100_100_100, "请输入正确的工装条码"));
                if (materialStock.getStorageId().isEmpty())
                    throw exception(new ErrorCode(100_100_100, "物料未绑定到储位上"));
                //物料所在的储位
                MaterialStorageDO materialOnStorageDO = materialStorageMapper.selectById(materialStock.getStorageId());
                //物料所在容器的库存
                MaterialStockDO materialOnContainerStock = materialStockService.getMaterialStock(materialOnStorageDO.getMaterialStockId());
                if (!Objects.equals(materialOnContainerStock.getId(), toolingStock.getId())) {
                    throw exception(new ErrorCode(100_100_100, "物料没有绑定到指定工装"));
                }
            }
            break;
            default: {
                throw exception(new ErrorCode(100_100_100, "未知任务类型"));
            }
        }

//        // 装卸
//        if (!this.bindTrayOrClamp(mapBindTrayOrClamp)) {
//            throw new IllegalArgumentException("托盘或工装绑定失败");
//        }
//        // 工件指定容器
//        List<MaterialConfigDO> materialConfigContainerList = getContainerIdListByMaterialId(materialStockDO.getMaterialConfigId());
//        if (!CollectionUtils.isEmpty(materialConfigContainerList)) {
//            if (clampMaterialStockDO == null) {
//                if (!materialConfigContainerList.contains(trayMaterialStockDO.getId())) {
//                    throw new IllegalArgumentException("托盘不在工件指定容器内");
//                }
//            } else {
//                if (!materialConfigContainerList.contains(clampMaterialStockDO.getId())) {
//                    throw new IllegalArgumentException("工装不在工件指定容器内");
//                }
//            }
//        }
//        // 装卸页面无订单号，托盘locationId 安装位置的托盘
//        materialStockService.verifyMaterialPicking(null, materialStockDO.getId(), trayMaterialStockDO.getLocationId(), clampMaterialStockDO.getStorageId());

        /* **************************************************工序********************************** */

        //工序数据
        McsBatchRecordEventDTO mcsBatchRecordEventDTO = new McsBatchRecordEventDTO();

        mcsBatchRecordEventDTO.setBatchRecordId((String) map.get("batchRecordId"));
        mcsBatchRecordEventDTO.setBarCode((String) map.get("materialBarCode"));
        mcsBatchRecordEventDTO.setDeviceUnitId((String) map.get("deviceId"));
        mcsBatchRecordEventDTO.setOperatorId((String) map.get("operatorId"));
        if (toolingStock != null) {
            McsStepResourceDTO mcsStepResourceDTO = new McsStepResourceDTO();
            mcsStepResourceDTO.setMaterialType(3);
            mcsStepResourceDTO.setBarCode(toolingStock.getBarCode());
            mcsStepResourceDTO.setBatchNumber(toolingStock.getBatchNumber());
            mcsStepResourceDTO.setMaterialConfigId(toolingStock.getMaterialConfigId());
            mcsStepResourceDTO.setTotality(1);
            mcsBatchRecordEventDTO.setResourceList(Collections.singletonList(mcsStepResourceDTO));
        }

        //工序开工
        mcsManufacturingControlApi.batchRecordStart(mcsBatchRecordEventDTO);

/*
        List<McsPlanStepDTO> stepList = (List<McsPlanStepDTO>) map.get("stepList");
          if(stepList.size() == 0){
            throw new IllegalArgumentException("工步不能为空");
        }
        List<McsBatchDetailRecordDTO> recordList = (List<McsBatchDetailRecordDTO>) map.get("recordList");

        if(recordList.size() == 0){
            mcsStepPlanEventDTO = stepList.get(0);
        }
        for (McsPlanStepDTO mcsPlanStepDTO : stepList) {
            recordList.stream().filter(record -> record.getStepId().equals(mcsPlanStepDTO.getId())).findFirst().ifPresent(record -> {
                McsBatchDetailRecordDTO mcsBatchDetailRecordDTO = new McsBatchDetailRecordDTO();
                mcsBatchDetailRecordDTO.setBatchRecordId(mcsBatchRecordEventDTO.getBatchRecordId());
                mcsBatchDetailRecordDTO.setStepId(mcsPlanStepDTO.getId());
                mcsBatchDetailRecordDTO.setStartTime(mcsPlanStepDTO.getStartTime());
                mcsBatchDetailRecordDTO.setEndTime(mcsPlanStepDTO.getEndTime());
                mcsBatchDetailRecordDTO.setOperatorId(mcsPlanStepDTO.getOperatorId());
                mcsBatchDetailRecordDTO.setOperatorName(mcsPlanStepDTO.getOperatorName());
                mcsBatchDetailRecordDTO.setRecordType(DictConstants.WMS_BATCH_RECORD_TYPE_START);
                mcsManufacturingControlApi.batchDetailRecord(mcsBatchDetailRecordDTO);
        });
        }

*/

    }

    @Override
    public JSONObject getCutterInfo(String barCode) {
        List<MaterialStockDO>  cutterList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(cutterList.size() != 1){
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }

        MaterialStockDO cutter = cutterList.get(0);

        MaterialConfigDO materialConfig = materialConfigMapper.selectById(cutter.getMaterialConfigId());

        JSONObject result = new JSONObject();
        JSONArray header = new JSONArray();
        JSONObject j1 = new JSONObject();
        j1.put("lable", "刀具编码");
        j1.put("value", cutter.getMaterialNumber());
        header.add(j1);
        JSONObject j2 = new JSONObject();
        j2.put("lable", "RFID");
        j2.put("value", cutter.getBarCode());
        header.add(j2);

        JSONArray body = new JSONArray();
        JSONObject j3 = new JSONObject();
        j3.put("lable", "刀具类型");
        j3.put("value", materialConfig.getMaterialCode());
        body.add(j3);
        JSONObject j4 = new JSONObject();
        j4.put("lable", "刀具长度");
        j4.put("value", "1233");
        body.add(j4);
        JSONObject j5 = new JSONObject();
        j5.put("lable", "刀具半径");
        j5.put("value", "1233");
        body.add(j5);
        JSONObject j6 = new JSONObject();
        j6.put("lable", "刀具规格");
        j6.put("value", materialConfig.getMaterialSpecification());
        body.add(j6);
        JSONObject j7 = new JSONObject();
        j7.put("lable", "刀具品牌");
        j7.put("value", materialConfig.getMaterialBrand());
        body.add(j7);
        JSONObject j8 = new JSONObject();
        j8.put("lable", "刀具类别");
        j8.put("value", materialConfig.getMaterialType());
        body.add(j8);

        result.put("header", header);
        result.put("body", body);
        return result;
    }
}
