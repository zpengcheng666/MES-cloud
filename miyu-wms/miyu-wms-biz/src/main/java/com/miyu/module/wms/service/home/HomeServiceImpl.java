package com.miyu.module.wms.service.home;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseRespVO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseStatisticsVO;
import com.miyu.module.wms.convert.inwarehousedetail.InWarehouseDetailConvert;
import com.miyu.module.wms.convert.outwarehousedetail.OutWarehouseDetailConvert;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;


    /**
     * 获取库位占用率
     * @param warehouseLocationDOS 仓库-库位列表
     * @return 库位占用率
     */
    @Override
    public Map<String, Map<String, Integer>> warehouseOccupancyRate (List<WarehouseLocationDO> warehouseLocationDOS){
        // 仓库 - 库位
        Map<String, List<WarehouseLocationDO>> warehouseLocationMap = getWarehouseNameLocationMap(warehouseLocationDOS);
        // 库位 - 物料
        Map<String, List<MaterialStockDO>> rootLocationMaterialMap = materialStockService.getRootLocationIdMaterialStockMap();

        // 占用
        Map<String, Map<String, Integer>> occupancyRate = new HashMap<>();
        warehouseLocationMap.forEach((warehouseName, warehouseLocations) -> {
            Map<String, Integer> occupancyRateMap = null;
            Integer total = 0;
            Integer locationOccupied = 0;
            Integer trayOccupied = 0;
            if(occupancyRate.containsKey(warehouseName)){
                occupancyRateMap = occupancyRate.get(warehouseName);
                total = occupancyRateMap.get("total");
                locationOccupied = occupancyRateMap.get("locationOccupied");
                trayOccupied = occupancyRateMap.get("trayOccupied");
            }else {
                occupancyRateMap = new HashMap<>();
                occupancyRate.put(warehouseName, occupancyRateMap);
            }


            for(WarehouseLocationDO warehouseLocation :warehouseLocations){
                total++;
                if(rootLocationMaterialMap.containsKey(warehouseLocation.getId())){
                    // 库位占用
                    locationOccupied++;
                    List<MaterialStockDO> materialStockDOList = rootLocationMaterialMap.get(warehouseLocation.getId());
                    boolean b = materialStockDOList.stream().anyMatch(materialStockDO -> !DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStockDO.getMaterialType()));
                    if(b){
                        // 库位上存在非托盘的物料  托盘占用
                        trayOccupied++;
                    }
                }

                // 总数
                occupancyRateMap.put("total", total);
                // 库位占用数
                occupancyRateMap.put("locationOccupied",locationOccupied);
                // 托盘占用数
                occupancyRateMap.put("trayOccupied",trayOccupied);
            };
        });

        return occupancyRate;
    }

    public JSONObject warehouseInOutAnalysis(LocalDateTime[] createTimeRange) {
        JSONObject result = new JSONObject();
        //获取库存统计结果
        long l = System.currentTimeMillis();
        WarehouseMaterialStockResult warehouseMaterialStockStatistics = getWarehouseMaterialStockStatistics(createTimeRange);
        System.err.println("getWarehouseMaterialStockStatistics time:" + (System.currentTimeMillis() - l));
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseList();
        Map<String, String> warehouseDOMap = CollectionUtils.convertMap(warehouseList, WarehouseDO::getId, WarehouseDO::getWarehouseName);

        List<InOutWarehouseStatisticsVO> warehouseInOutCountList = new ArrayList<>();
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap = new HashMap<>();
        long l1 = System.currentTimeMillis();
        // 取出入库统计的结果值
        warehouseMaterialStockStatistics.warehouseMaterialInDetailMap.forEach((warehouseId, materialInDetailMap) -> {
            Map<String, InOutWarehouseStatisticsVO> stringInOutWarehouseStatisticsVOMap;
            if(warehouseMaterialInDetailMap.containsKey(warehouseId)){
                stringInOutWarehouseStatisticsVOMap = warehouseMaterialInDetailMap.get(warehouseId);
            }else {
                stringInOutWarehouseStatisticsVOMap = new HashMap<>();
                warehouseMaterialInDetailMap.put(warehouseId, stringInOutWarehouseStatisticsVOMap);
            }
            materialInDetailMap.forEach((materialConfigId, inOutWarehouseStatisticsVO) -> {
                InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                if(!stringInOutWarehouseStatisticsVOMap.containsKey(materialConfigId)){
                    inOutWarehouseStatistics = inOutWarehouseStatisticsVO;
                    inOutWarehouseStatistics.setWarehouseName(warehouseDOMap.get(warehouseId));
                    stringInOutWarehouseStatisticsVOMap.put(materialConfigId, inOutWarehouseStatistics);
                    warehouseInOutCountList.add(inOutWarehouseStatistics);
                }
            });
        });

        // 取出出库统计的结果值
        warehouseMaterialStockStatistics.warehouseMaterialOutDetailMap.forEach((warehouseId, materialOutDetailMap) -> {
            Map<String, InOutWarehouseStatisticsVO> stringInOutWarehouseStatisticsVOMap;
            if(warehouseMaterialInDetailMap.containsKey(warehouseId)){
                stringInOutWarehouseStatisticsVOMap = warehouseMaterialInDetailMap.get(warehouseId);
            }else {
                stringInOutWarehouseStatisticsVOMap = new HashMap<>();
                warehouseMaterialInDetailMap.put(warehouseId, stringInOutWarehouseStatisticsVOMap);
            }
            materialOutDetailMap.forEach((materialConfigId, inOutWarehouseStatisticsVO) -> {
                InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                if(stringInOutWarehouseStatisticsVOMap.containsKey(materialConfigId)){
                    inOutWarehouseStatistics = stringInOutWarehouseStatisticsVOMap.get(materialConfigId);
                    inOutWarehouseStatistics.setOutTotalCount(inOutWarehouseStatisticsVO.getOutTotalCount());
                }else {
                    inOutWarehouseStatistics = inOutWarehouseStatisticsVO;
                    inOutWarehouseStatistics.setWarehouseName(warehouseDOMap.get(warehouseId));
                    stringInOutWarehouseStatisticsVOMap.put(materialConfigId, inOutWarehouseStatistics);
                    warehouseInOutCountList.add(inOutWarehouseStatistics);
                }
            });
        });

        // 取出库存统计的结果值
        warehouseMaterialStockStatistics.warehouseMaterialStockMap.forEach((warehouseId, materialStockMap) -> {
            Map<String, InOutWarehouseStatisticsVO> stringInOutWarehouseStatisticsVOMap;
            if(warehouseMaterialInDetailMap.containsKey(warehouseId)){
                stringInOutWarehouseStatisticsVOMap = warehouseMaterialInDetailMap.get(warehouseId);
            }else {
                stringInOutWarehouseStatisticsVOMap = new HashMap<>();
                warehouseMaterialInDetailMap.put(warehouseId, stringInOutWarehouseStatisticsVOMap);
            }

            materialStockMap.forEach((materialConfigId, inOutWarehouseStatisticsVO) -> {
                InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                if(stringInOutWarehouseStatisticsVOMap.containsKey(materialConfigId)){
                    inOutWarehouseStatistics = stringInOutWarehouseStatisticsVOMap.get(materialConfigId);
                    inOutWarehouseStatistics.setTotalStockCount(inOutWarehouseStatisticsVO.getTotalStockCount());
                }else {
                    inOutWarehouseStatistics = inOutWarehouseStatisticsVO;
                    inOutWarehouseStatistics.setWarehouseName(warehouseDOMap.get(warehouseId));
                    stringInOutWarehouseStatisticsVOMap.put(materialConfigId, inOutWarehouseStatistics);
                    warehouseInOutCountList.add(inOutWarehouseStatistics);
                }
            });
        });
        System.err.println("warehouseInOutCountList time:" + (System.currentTimeMillis() - l1));

        long l2 = System.currentTimeMillis();
        // 计算库存统计总数  仓库名称 (库存 - 入库 - 出库)
        List<InOutWarehouseStatisticsVO> totalWarehouseStatisticsList = new ArrayList<>();
        warehouseMaterialInDetailMap.forEach((warehouseId, materialInDetailMap) -> {
            InOutWarehouseStatisticsVO inOutWarehouseStatisticsVO = new InOutWarehouseStatisticsVO();
            if(warehouseMaterialStockStatistics.warehouseMaterialInDetailMap.containsKey(warehouseId))inOutWarehouseStatisticsVO.setInTotalCount(warehouseMaterialStockStatistics.warehouseMaterialInDetailMap.get(warehouseId).values().stream().mapToInt(InOutWarehouseStatisticsVO::getInTotalCount).sum());
            if(warehouseMaterialStockStatistics.warehouseMaterialOutDetailMap.containsKey(warehouseId))inOutWarehouseStatisticsVO.setOutTotalCount(warehouseMaterialStockStatistics.warehouseMaterialOutDetailMap.get(warehouseId).values().stream().mapToInt(InOutWarehouseStatisticsVO::getOutTotalCount).sum());
            if(warehouseMaterialStockStatistics.warehouseMaterialStockMap.containsKey(warehouseId))inOutWarehouseStatisticsVO.setTotalStockCount(warehouseMaterialStockStatistics.warehouseMaterialStockMap.get(warehouseId).values().stream().mapToInt(InOutWarehouseStatisticsVO::getTotalStockCount).sum());
            inOutWarehouseStatisticsVO.setWarehouseName(warehouseDOMap.get(warehouseId));
            inOutWarehouseStatisticsVO.setWarehouseId(warehouseId);
            totalWarehouseStatisticsList.add(inOutWarehouseStatisticsVO);
        });
        System.err.println("totalWarehouseStatisticsVOMap time:" + (System.currentTimeMillis() - l2));
        result.put("warehouseInOutCountList", warehouseInOutCountList);
        result.put("totalWarehouseStatisticsList", totalWarehouseStatisticsList);
        return result;
    }

    @Override
    public List<InOutWarehouseRespVO> getManualInList() {
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseList();
        Map<String, String> warehouseIdNameMap = new HashMap<>();
        Map<String, String> manualWarehouseIdNameMap = new HashMap<>();
        for (WarehouseDO warehouseDO : warehouseList) {
            if(DictConstants.WMS_WAREHOUSE_TYPE_2.equals(warehouseDO.getWarehouseType())){
                manualWarehouseIdNameMap.put(warehouseDO.getId(), warehouseDO.getWarehouseName());
            }
            warehouseIdNameMap.put(warehouseDO.getId(), warehouseDO.getWarehouseName());
        }
        List<InWarehouseDetailDO> waitInWarehouseInWarehouseDetailList = inWarehouseDetailService.getWaitInWarehouseInWarehouseDetailList();
        waitInWarehouseInWarehouseDetailList = waitInWarehouseInWarehouseDetailList.stream().filter(inWarehouseDetailDO -> (manualWarehouseIdNameMap.containsKey(inWarehouseDetailDO.getStartWarehouseId()) || manualWarehouseIdNameMap.containsKey(inWarehouseDetailDO.getTargetWarehouseId()))).collect(Collectors.toList());
        return new ArrayList<>(InWarehouseDetailConvert.INSTANCE.convertToHomeList(waitInWarehouseInWarehouseDetailList, warehouseIdNameMap));
    }


    @Override
    public List<InOutWarehouseRespVO> getManualOutList() {
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseList();
        Map<String, String> warehouseIdNameMap = new HashMap<>();
        Map<String, String> manualWarehouseIdNameMap = new HashMap<>();
        for (WarehouseDO warehouseDO : warehouseList) {
            if(DictConstants.WMS_WAREHOUSE_TYPE_2.equals(warehouseDO.getWarehouseType())){
                manualWarehouseIdNameMap.put(warehouseDO.getId(), warehouseDO.getWarehouseName());
            }
            warehouseIdNameMap.put(warehouseDO.getId(), warehouseDO.getWarehouseName());
        }

        List<OutWarehouseDetailDO> waitOutWarehouseOutWarehouseDetailList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailList();
        waitOutWarehouseOutWarehouseDetailList = waitOutWarehouseOutWarehouseDetailList.stream().filter(outWarehouseDetailDO -> (manualWarehouseIdNameMap.containsKey(outWarehouseDetailDO.getStartWarehouseId()) || manualWarehouseIdNameMap.containsKey(outWarehouseDetailDO.getTargetWarehouseId()))).collect(Collectors.toList());
        return new ArrayList<>(OutWarehouseDetailConvert.INSTANCE.convertToHomeList(waitOutWarehouseOutWarehouseDetailList, warehouseIdNameMap));
    }

    // 仓库 - 库存 - 出入库数量统计
    private WarehouseMaterialStockResult getWarehouseMaterialStockStatistics(LocalDateTime[] createTimeRange) {
        long l = System.currentTimeMillis();
        // 入库单
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.getFinishInWarehouseDetailList(createTimeRange);
        // 出库单
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = outWarehouseDetailService.getFinishOutWarehouseDetailList(createTimeRange);
        System.err.println("getFinishInWarehouseDetailList and getFinishOutWarehouseDetailList time:" + (System.currentTimeMillis() - l));

        // 库位
        List<WarehouseLocationDO> warehouseLocationDOS = warehouseLocationService.getWarehouseLocationByWarehouseType(DictConstants.WMS_WAREHOUSE_TYPE_1);
        long ll = System.currentTimeMillis();
        Map<String, List<WarehouseLocationDO>> warehouseIdLocationMap = getWarehouseIdLocationMap(warehouseLocationDOS);
        long l1 = System.currentTimeMillis();
        System.err.println("getWarehouseIdLocationMap time:" + (l1-ll));
        // 仓库id - 物料类型id - 入库详情单
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap = warehouseMaterialInDetail(inWarehouseDetailDOS, warehouseIdLocationMap);
        long l2 = System.currentTimeMillis();
        System.err.println("warehouseMaterialInDetail time:" + (l2-l1));
        // 仓库id - 物料类型id - 出库详情单
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialOutDetailMap = warehouseMaterialOutDetail(outWarehouseDetailDOS, warehouseIdLocationMap);
        long l3 = System.currentTimeMillis();
        System.err.println("warehouseMaterialOutDetail time:" + (l3-l2));
        // 仓库id - 物料类型id - 库存数量
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialStockMap = getWarehouseMaterialStock(warehouseIdLocationMap);
        System.err.println("getWarehouseMaterialStock time:" + (System.currentTimeMillis() - l3));

        log.info("仓库入库统计：{}", warehouseMaterialInDetailMap);
        log.info("仓库出库统计：{}", warehouseMaterialOutDetailMap);
        log.info("仓库库存统计：{}", warehouseMaterialStockMap);
        return new WarehouseMaterialStockResult(warehouseMaterialInDetailMap, warehouseMaterialOutDetailMap, warehouseMaterialStockMap);
    }


    // 仓库id - 库位
    private Map<String, List<WarehouseLocationDO>> getWarehouseIdLocationMap (List<WarehouseLocationDO> warehouseLocationDOS){
        // 仓库Id - 库位
        Map<String, List<WarehouseLocationDO>> warehouseLocationMap = new HashMap<>();
        warehouseLocationDOS.forEach(warehouseLocationDO -> {
            List<WarehouseLocationDO> locationS = null;
            if(warehouseLocationMap.containsKey(warehouseLocationDO.getWarehouseId())){
                locationS = warehouseLocationMap.get(warehouseLocationDO.getWarehouseId());
            }else {
                locationS = new ArrayList<>();
                warehouseLocationMap.put(warehouseLocationDO.getWarehouseId(), locationS);
            }
            locationS.add(warehouseLocationDO);
        });
        return warehouseLocationMap;
    }

    private Map<String, List<WarehouseLocationDO>> getWarehouseNameLocationMap (List<WarehouseLocationDO> warehouseLocationDOS){
        // 仓库名称 - 库位
        Map<String, List<WarehouseLocationDO>> warehouseLocationMap = new HashMap<>();
        warehouseLocationDOS.forEach(warehouseLocationDO -> {
            List<WarehouseLocationDO> locationS = null;
            if(warehouseLocationMap.containsKey(warehouseLocationDO.getWarehouseName())){
                locationS = warehouseLocationMap.get(warehouseLocationDO.getWarehouseName());
            }else {
                locationS = new ArrayList<>();
                warehouseLocationMap.put(warehouseLocationDO.getWarehouseName(), locationS);
            }
            locationS.add(warehouseLocationDO);
        });
        return warehouseLocationMap;
    }


    // 仓库内 物料类型 入库统计
    private Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetail(List<InWarehouseDetailDO> inWarehouseDetailDOS, Map<String, List<WarehouseLocationDO>> warehouseLocationMap) {
        // 仓库id - 物料类型 - 入库详情单
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap = new HashMap<>();

        inWarehouseDetailDOS.forEach(inWarehouseDetail -> {
            // 入库单的目标仓库 是否为自动化立体库 不是就不管了
            if(warehouseLocationMap.containsKey(inWarehouseDetail.getTargetWarehouseId())){
                Map<String, InOutWarehouseStatisticsVO> warehouseMaterialInDetail;
                if(warehouseMaterialInDetailMap.containsKey(inWarehouseDetail.getTargetWarehouseId())){
                    warehouseMaterialInDetail = warehouseMaterialInDetailMap.get(inWarehouseDetail.getTargetWarehouseId());
                }else {
                    warehouseMaterialInDetail = new HashMap<>();
                    warehouseMaterialInDetailMap.put(inWarehouseDetail.getTargetWarehouseId(), warehouseMaterialInDetail);
                }
                InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                if(warehouseMaterialInDetail.containsKey(inWarehouseDetail.getMaterialConfigId())){
                    inOutWarehouseStatistics = warehouseMaterialInDetail.get(inWarehouseDetail.getMaterialConfigId());
                    inOutWarehouseStatistics.setInTotalCount(inOutWarehouseStatistics.getInTotalCount() + inWarehouseDetail.getQuantity());
                }else {
                    inOutWarehouseStatistics = new InOutWarehouseStatisticsVO();
                    inOutWarehouseStatistics.setWarehouseId(inWarehouseDetail.getTargetWarehouseId());
                    inOutWarehouseStatistics.setBatchNumber(inWarehouseDetail.getBatchNumber());
                    inOutWarehouseStatistics.setMaterialNumber(inWarehouseDetail.getMaterialNumber());
                    inOutWarehouseStatistics.setInTotalCount(inWarehouseDetail.getQuantity());
                    warehouseMaterialInDetail.put(inWarehouseDetail.getMaterialConfigId(), inOutWarehouseStatistics);
                }
            }
        });
        return warehouseMaterialInDetailMap;
    }

    // 仓库内 物料类型 出库统计
    private Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialOutDetail(List<OutWarehouseDetailDO> outWarehouseDetailDOS, Map<String, List<WarehouseLocationDO>> warehouseLocationMap) {
        // 仓库 - 物料类型 - 入库详情单
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap = new HashMap<>();

        outWarehouseDetailDOS.forEach(outWarehouseDetail -> {
            // 入库单的目标仓库 是否为自动化立体库 不是就不管了
            if(warehouseLocationMap.containsKey(outWarehouseDetail.getStartWarehouseId())){
                Map<String, InOutWarehouseStatisticsVO> warehouseMaterialInDetail;
                if(warehouseMaterialInDetailMap.containsKey(outWarehouseDetail.getStartWarehouseId())){
                    warehouseMaterialInDetail = warehouseMaterialInDetailMap.get(outWarehouseDetail.getStartWarehouseId());
                }else {
                    warehouseMaterialInDetail = new HashMap<>();
                    warehouseMaterialInDetailMap.put(outWarehouseDetail.getStartWarehouseId(), warehouseMaterialInDetail);
                }
                InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                if(warehouseMaterialInDetail.containsKey(outWarehouseDetail.getMaterialConfigId())){
                    inOutWarehouseStatistics = warehouseMaterialInDetail.get(outWarehouseDetail.getMaterialConfigId());
                    inOutWarehouseStatistics.setOutTotalCount(inOutWarehouseStatistics.getOutTotalCount() + outWarehouseDetail.getQuantity());
                }else {
                    inOutWarehouseStatistics = new InOutWarehouseStatisticsVO();
                    inOutWarehouseStatistics.setWarehouseId(outWarehouseDetail.getTargetWarehouseId());
                    inOutWarehouseStatistics.setBatchNumber(outWarehouseDetail.getBatchNumber());
                    inOutWarehouseStatistics.setMaterialNumber(outWarehouseDetail.getMaterialNumber());
                    inOutWarehouseStatistics.setOutTotalCount(outWarehouseDetail.getQuantity());
                    warehouseMaterialInDetail.put(outWarehouseDetail.getMaterialConfigId(), inOutWarehouseStatistics);
                }
            }
        });
        return warehouseMaterialInDetailMap;
    }

    // 仓库内 物料类型 库存统计
    private Map<String,Map<String, InOutWarehouseStatisticsVO>> getWarehouseMaterialStock(Map<String, List<WarehouseLocationDO>> warehouseLocationMap) {
        // 库位 - 物料
        Map<String, List<MaterialStockDO>> rootLocationMaterialMap = materialStockService.getRootLocationIdMaterialStockMap();

        // 仓库 - 物料类型 - 库存数量
        Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialStockMap = new HashMap<>();

        warehouseLocationMap.forEach((warehouseId, warehouseLocationS) -> {
            Map<String, InOutWarehouseStatisticsVO> warehouseMaterialStock;
            if(warehouseMaterialStockMap.containsKey(warehouseId)){
                warehouseMaterialStock = warehouseMaterialStockMap.get(warehouseId);
            }else {
                warehouseMaterialStock = new HashMap<>();
                warehouseMaterialStockMap.put(warehouseId, warehouseMaterialStock);
            }

            warehouseLocationS.forEach(warehouseLocationDO -> {
                if(rootLocationMaterialMap.containsKey(warehouseLocationDO.getId())){
                    List<MaterialStockDO> materialStockDOS = rootLocationMaterialMap.get(warehouseLocationDO.getId());
                    materialStockDOS.forEach(materialStockDO -> {
                        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialStockDO.getMaterialType())){
                            InOutWarehouseStatisticsVO inOutWarehouseStatistics;
                            if(warehouseMaterialStock.containsKey(materialStockDO.getMaterialConfigId())){
                                inOutWarehouseStatistics = warehouseMaterialStock.get(materialStockDO.getMaterialConfigId());
                                inOutWarehouseStatistics.setTotalStockCount(inOutWarehouseStatistics.getTotalStockCount() + materialStockDO.getTotality());
                            }else {
                                inOutWarehouseStatistics = new InOutWarehouseStatisticsVO();
                                inOutWarehouseStatistics.setWarehouseId(warehouseId);
                                inOutWarehouseStatistics.setBatchNumber(materialStockDO.getBatchNumber());
                                inOutWarehouseStatistics.setMaterialNumber(materialStockDO.getMaterialNumber());
                                inOutWarehouseStatistics.setTotalStockCount(materialStockDO.getTotality());
                                warehouseMaterialStock.put(materialStockDO.getMaterialConfigId(), inOutWarehouseStatistics);
                            }
                        }
                    });
                }
            });
        });
        
        
        return warehouseMaterialStockMap;
    }

    // 仓库 - 库存 -出入库数量统计映射表
    private static class WarehouseMaterialStockResult {
        // 仓库 - 物料类型 - 入库详情单
        public final Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap;
        // 仓库 - 物料类型 - 出库详情单
        public final  Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialOutDetailMap;
        // 仓库 - 物料类型 - 库存数量
        public final Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialStockMap;
        public WarehouseMaterialStockResult(Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialInDetailMap, Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialOutDetailMap, Map<String,Map<String, InOutWarehouseStatisticsVO>> warehouseMaterialStockMap) {
            this.warehouseMaterialInDetailMap = warehouseMaterialInDetailMap;
            this.warehouseMaterialOutDetailMap = warehouseMaterialOutDetailMap;
            this.warehouseMaterialStockMap = warehouseMaterialStockMap;
        }
    }
}
