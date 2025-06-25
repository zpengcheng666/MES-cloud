package com.miyu.module.wms.controller.admin.operatingterminal;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentInfoDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentSignDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.MaterialStockInRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingInfoDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingOutDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockRespVO;
import com.miyu.module.wms.controller.admin.operatingterminal.vo.*;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationRespVO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.materialstock.MaterialStockMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.operatingterminal.OperatingTerminalService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 操作终端")
@RestController
@RequestMapping("/wms/operating-terminal")
@Validated
@Slf4j
public class OperatingTerminalController {

    @Resource
    private OperatingTerminalService operatingTerminalService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialConfigAreaService materialConfigAreaService;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private MaterialStockMapper materialStockMapper;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private WarehouseLocationService warehouseLocationService;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Resource
    private ShippingApi shippingApi;

    @Resource
    private OrderApi orderApi;

    /**
     * 根据库位id获取物料库存列表
     *
     * @param locationId
     * @return
     */
    @GetMapping("/getMaterialStockListByLocationId")
    @Operation(summary = "根据库位id获取物料库存列表")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockListByLocationId(@RequestParam("locationId") String locationId) {
        List<MaterialStockDO> materialStockList = materialStockService.getAllMaterialStockByLocationId(locationId);
        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }


    @GetMapping("/getWaitOrderDetailList")
    @Operation(summary = "待出入库单信息列表")
    public CommonResult<List<OrderVO>> getWaitOrderDetailList(String areaCode) {
        List<OrderVO> list = operatingTerminalService.getWaitOrderDetailList(areaCode);
        return success(list);
    }

    @GetMapping("/getCutterWaitOrderDetailList")
    @Operation(summary = "刀具待出库单信息列表")
    public CommonResult<List<CutterOrderVO>> getCutterWaitOrderDetailList(String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        String warehouseId = warehouseAreaDO.getWarehouseId();

        List<CutterOrderVO> list = operatingTerminalService.getCutterWaitOrderDetailList(warehouseId);
        return success(list);
    }

    @GetMapping("/getCutterWaitOutOrderDetailList")
    @Operation(summary = "刀具待出库单信息列表")
    public CommonResult<List<CutterOrderVO>> getCutterWaitOutOrderDetailList(String locationCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByLocationCode(locationCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
        }
        String warehouseId = warehouseAreaDO.getWarehouseId();

        List<CutterOrderVO> list = operatingTerminalService.getCutterWaitOrderDetailList(warehouseId);
        return success(list);
    }

    @GetMapping("/getCutterWaitInOrderDetailList")
    @Operation(summary = "刀具未完成的入库单信息列表")
    public CommonResult<List<CutterOrderVO>> getCutterWaitInOrderDetailList(String locationCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByLocationCode(locationCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        String warehouseId = warehouseAreaDO.getWarehouseId();
        List<CutterOrderVO> list = operatingTerminalService.getCutterWaitInOrderDetailList(warehouseId);
        return success(list);
    }

    // todo 待废弃
    @GetMapping("/getMaterialStockByLocationId")
    @Operation(summary = "根据库位查托盘信息")
    public CommonResult<MaterialStockRespVO> getMaterialStockBylocationId(@RequestParam("locationId") String locationId) {
        MaterialStockDO materialStock = operatingTerminalService.getMaterialStockByLocationIds(Collections.singletonList(locationId)).get(0);
        return success(BeanUtils.toBean(materialStock, MaterialStockRespVO.class));
    }


    @PostMapping("/getMaterialStockByTray")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockByTray(@RequestBody MaterialStockDO materialStockDO) {

        List<MaterialStockDO> materialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(materialStockDO.getId());

        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }

    /* AGV虚拟暂存库区4  半自动库区3  自动库区2  人工库区1*/
    @GetMapping("/getEmptyTrayList")
    public CommonResult<List<MaterialStockRespVO>> getEmptyTrayList() {
        List<MaterialStockDO> materialEmptTrayList = new ArrayList<>();
        /*
         * 查询 所有，在 （自动库区 and  存储区上） 或 （（自动库区or 半自动库区） and 接驳区）
         * 库位 未锁定 有效 的 所有没搬运任务的 托盘*/
        List<MaterialStockDO> materialStockList = operatingTerminalService.getEmptyTrayListByWareHouseArea();
        //任务
        List<CarryTaskDO> unfinishedCarryTask = carryTaskService.getUnfinishedCarryTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarryTask, CarryTaskDO::getReflectStockId);

        for (MaterialStockDO trayStock : materialStockList) {
            // 先看托盘上有没有物料
            List<MaterialStockDO> materialStockOnTray = materialStockService.getMaterialStockListByContainerId(trayStock.getId());
            if (!CollectionUtils.isAnyEmpty(materialStockOnTray)) {
                continue;
            }
            // 再看是否已存在任务
            if (haveTaskLocationIdSet.contains(trayStock.getId())) {
                continue;
            }
            materialEmptTrayList.add(trayStock);
        }

        return success(BeanUtils.toBean(materialEmptTrayList, MaterialStockRespVO.class));

    }


    /**
     * 查询此仓库出库详情单上的物料
     */
    @GetMapping("/getMaterialStockByOutWarehouseDetail")
    @Cacheable(value = "getMaterialStockByOutWarehouseDetail#5s", key = "#areaCode", unless = "#result == null")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockByOutWarehouseDetail(@RequestParam("areaCode") String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        // 获取待出库的物料
        List<MaterialStockDO> materialStockList = operatingTerminalService.getMaterialStockByOutWarehouseDetail(warehouseAreaDO.getWarehouseId());
        // 获取承载物料的托盘
        Map<String, MaterialStockDO> warehouseLocationListByMaterialStockList = materialStockService.getWarehouseLocationListByMaterialStockList(materialStockList);
        // 获取托盘ids
        Set<String> trayIds = CollectionUtils.convertSet(warehouseLocationListByMaterialStockList.values(), MaterialStockDO::getId);
        // 获取绑定此库区的物料库区配置
        List<MaterialConfigAreaDO> materialConfigArea = materialConfigAreaService.getTrayMaterialConfigAreaByAreaId(warehouseAreaDO.getId());
        // 获取配置了此库区的托盘类型
        List<String> trayConfigIds = materialConfigArea.stream().map(MaterialConfigAreaDO::getMaterialConfigId).collect(Collectors.toList());
        // 获取所有空的托盘
        List<MaterialStockDO> emptyTrayStockList = materialStockService.getEmptyTrayStockListByMaterialConfigIds(trayConfigIds, warehouseAreaDO.getId());
        Set<String> emptyTrayStockIdSet = CollectionUtils.convertSet(emptyTrayStockList, MaterialStockDO::getId);
        trayIds.addAll(emptyTrayStockIdSet);
        List<MaterialStockDO> trayStockList = materialStockService.getMaterialStockByIds(trayIds);
        LinkedList<MaterialStockDO> queueList = new LinkedList<>();
        trayStockList.forEach(t -> {
            if (emptyTrayStockIdSet.contains(t.getId())) {
                queueList.addLast(t);
            } else {
                queueList.addFirst(t);
            }
        });
        return success(BeanUtils.toBean(queueList, MaterialStockRespVO.class));

    }

    /**
     * 呼叫空托盘
     */
    @GetMapping("/getCallEmpryTrayList")
    public CommonResult<List<MaterialStockRespVO>> getCallEmpryTrayList(@RequestParam("areaCode") String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        // 获取绑定此库区的物料库区配置
        List<MaterialConfigAreaDO> materialConfigArea = materialConfigAreaService.getTrayMaterialConfigAreaByAreaId(warehouseAreaDO.getId());
        // 获取配置了此库区的托盘类型
        List<String> trayConfigIds = materialConfigArea.stream().map(MaterialConfigAreaDO::getMaterialConfigId).collect(Collectors.toList());
        // 获取所有空的托盘
        List<MaterialStockDO> emptyTrayStockList = materialStockService.getEmptyTrayStockListByMaterialConfigIds(trayConfigIds, warehouseAreaDO.getId());
        Set<String> emptyTrayStockIdSet = CollectionUtils.convertSet(emptyTrayStockList, MaterialStockDO::getId);
        List<MaterialStockDO> trayStockList = materialStockService.getMaterialStockByIds(emptyTrayStockIdSet);
        return success(BeanUtils.toBean(trayStockList, MaterialStockRespVO.class));

    }


    /**
     * 呼叫空托盘  根据库位id
     */
    @GetMapping("/getTrayListByLocationId")
    public CommonResult<List<MaterialStockRespVO>> getTrayListByLocationId(@RequestParam("locationId") String locationId) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByLocationId(locationId);
        if (warehouseAreaDO == null) {
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }
        // 获取绑定此库区的物料库区配置
        List<MaterialConfigAreaDO> materialConfigArea = materialConfigAreaService.getTrayMaterialConfigAreaByAreaId(warehouseAreaDO.getId());
        // 获取配置了此库区的托盘类型
        List<String> trayConfigIds = materialConfigArea.stream().map(MaterialConfigAreaDO::getMaterialConfigId).collect(Collectors.toList());
        // 获取所有空的托盘
        List<MaterialStockDO> emptyTrayStockList = materialStockService.getEmptyTrayStockListByMaterialConfigIds(trayConfigIds, warehouseAreaDO.getId());
        Set<String> emptyTrayStockIdSet = CollectionUtils.convertSet(emptyTrayStockList, MaterialStockDO::getId);
        List<MaterialStockDO> trayStockList = materialStockService.getMaterialStockByIds(emptyTrayStockIdSet);
        return success(BeanUtils.toBean(trayStockList, MaterialStockRespVO.class));

    }

    //迁入
    @GetMapping("/warehouseIn")
    public CommonResult<Boolean> warehouseIn(@RequestParam("locationId") String locationId, @RequestParam("trayId") String trayId, @RequestParam("barCode") String barCode) {
        if (StringUtils.isBlank(locationId)) {
            return error(new ErrorCode(400, "库位不能为空"));
        }
        MaterialStockDO materialStockDO = materialStockMapper.selectOne(new LambdaQueryWrapper<MaterialStockDO>().eq(MaterialStockDO::getBarCode, barCode));

        if (materialStockDO == null) {
            return error(new ErrorCode(400, "条码不存在"));
        }
        MaterialConfigDO materialConfigDO = operatingTerminalService.getMaterialConfigByBarcode(barCode);

        //库位上没有托盘,无托盘则条码一定为托盘
        if (StringUtils.isBlank(trayId)) {
            if (!DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialConfigDO.getMaterialType())) {
                return error(new ErrorCode(400, "扫描条码不是托盘"));
            } else {
                // 校验物料是否存在物料入库 或者 物料出库的详情订单
                List<MaterialStockDO> materialStockList = materialStockService.checkMaterialStockOrderExists(Arrays.asList(materialStockDO.getId()));
                if (CollectionUtils.isAnyEmpty(materialStockList) && 1 == 1/* 其他条件待添加*/) {
                    throw exception(MATERIAL_STOCK_NO_MOVE_PERMISSION);
                }
                // materialStockService.updateMaterialStock((MaterialStockSaveReqVO) materialStockDO);

            }
        } else {
            //barCode 为工件、工装
            if (DictConstants.WMS_MATERIAL_TYPE_TP.equals(materialConfigDO.getMaterialType())) {
                return error(new ErrorCode(400, "扫描条码不能是托盘"));
            }
            // materialStockService.updateMaterialStock((MaterialStockSaveReqVO) materialStockDO);

        }
        return success(true);

    }

    /**
     * 通过库区查询仓库出库单和单上物料
     */
    @GetMapping("/getOutWarehouseDetailAndStockInfo")
    @Cacheable(value = "outWarehouseDetailAndStockInfo#5s", key = "#areaCode", unless = "#result == null")
    public CommonResult<List<Map<String, Object>>> getOutWarehouseDetailAndStockInfo(@RequestParam("areaCode") String areaCode) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            return error(new ErrorCode(400, "库区不存在"));
        }
        List<OutWarehouseDetailDO> outWarehouseDetailDOList = outWarehouseDetailService.selectWaitOutWarehouseDetail(warehouseAreaDO.getWarehouseId());
        for (OutWarehouseDetailDO outWarehouseDetailDO : outWarehouseDetailDOList) {
            Map<String, Object> map = new HashMap<>();
            MaterialStockDO materialStockDO = materialStockService.getMaterialStockById(outWarehouseDetailDO.getMaterialStockId());
            map.put("outWarehouseDetail", outWarehouseDetailDO);
            map.put("materialStock", materialStockDO);
            resultList.add(map);
        }
        return success(resultList);
    }

    @GetMapping("/getMaterialStockByArea")
    @Cacheable(value = "getMaterialStockByArea#5s", key = "#areaCode", unless = "#result == null")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockByArea(@RequestParam("areaCode") String areaCode) {
//        System.err.println(SecurityFrameworkUtils.getLoginUserId());
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            return error(new ErrorCode(400, "库区不存在"));
        }

        List<MaterialStockDO> materialStockList = operatingTerminalService.getMaterialStockListByWarehouseId(warehouseAreaDO.getWarehouseId());
        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }

    //获取仓库下存储库区的托盘和工装
    @GetMapping("/getMaterialStockByAreaAndAreaTypeEq1")
    @Cacheable(value = "getMaterialStockByAreaAndAreaTypeEq1#5s", key = "#areaCode", unless = "#result == null")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockByAreaAndAreaTypeEq1(@RequestParam("areaCode") String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        if (warehouseAreaDO == null) {
            return error(new ErrorCode(400, "库区不存在"));
        }

        List<MaterialStockDO> materialStockList = operatingTerminalService.getMaterialStockListByWarehouseIdAndAreaTypeEq1(warehouseAreaDO.getWarehouseId());
        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }

    //通过库位id列表获得库位信息
    @GetMapping("/getWarehouseLocationListByIds")
    public CommonResult<List<WarehouseLocationDO>> getWarehouseLocationListByIds(@RequestParam("ids") List<String> ids) {
        List<WarehouseLocationDO> warehouseLocationDOList = warehouseLocationService.getWarehouseLocationListByIds(ids);
        return success(warehouseLocationDOList);
    }

    //上架操作
    @PostMapping("/available")
    public CommonResult<InstructionDO> available(@RequestBody Map<String, String> map) {
        return success(operatingTerminalService.available(map));
    }

    //下架操作
    @PostMapping("/palletUnloading")
    public CommonResult<InstructionDO> palletUnloading(@RequestBody Map<String, String> map) {
        return success(operatingTerminalService.palletUnloading(map));
    }

    @GetMapping("/getMaterialStockByLocationCode")
    @Operation(summary = "根据库位库位编码查物料信息")
    @Cacheable(value = "getMaterialStockByLocationCode#5s", key = "#locationCode", unless = "#result == null")
    public CommonResult<MaterialStockRespVO> getMaterialStockByLocationCode(@RequestParam("locationCode") String locationCode) {
        MaterialStockDO materialStock = operatingTerminalService.getMaterialStockByLocationCode(locationCode);
        return success(BeanUtils.toBean(materialStock, MaterialStockRespVO.class));
    }


    /**
     * 绑定托盘和物料-------拣选工位专用
     */
    @PostMapping("/changeBindTray")
    public CommonResult<Boolean> changeBindTray(@RequestBody Map<String, Object> map) {
        operatingTerminalService.changeMaterialStockStorage((String) map.get("materialId"), (String) map.get("trayId"), (String) map.get("orderId"), null);
        return success(true);
    }

    /**
     * 通过物料条码获取绑定的工装
     */
    @GetMapping("/getClampByMaterialBarCode")
    public CommonResult<String> getClampByMaterialBarCode(@RequestParam("materialBarCode") String materialBarCode) {
        MaterialStockDO materialStockDO = materialStockMapper.selectOne(MaterialStockDO::getBarCode, materialBarCode);
        if (materialStockDO == null) {
            return error(400, "条码不存在");
        }
        if (materialStockDO.getStorageId() == null || materialStockDO.getStorageId().isEmpty()) {
            //没绑定在储位上
            return error(400, "未绑定在储位上");
        }
        MaterialStorageDO materialStorageDO = materialStorageService.getMaterialStorage(materialStockDO.getStorageId());
        if (materialStorageDO == null) {
            return error(400, "库位不存在");
        }
        MaterialStockDO materialStockClamp = materialStockMapper.selectOne(MaterialStockDO::getId, materialStorageDO.getMaterialStockId());

        return success(materialStockClamp.getBarCode());
    }

    /**
     * 托盘绑定工件和工装，
     * 绑定成功后通知工序和工步任务开工
     */
    @PostMapping("/changeBindTrayLoadANDUnload")
    public CommonResult<Boolean> changeBindClamp(@RequestBody Map<String, Object> map) {
        operatingTerminalService.batchRecordStartForMCS(map);
        return success(true);
    }

    @GetMapping("/getWarehouseLocation")
    public CommonResult<List<WarehouseLocationRespVO>> getWarehouseLocationByareaCode(@RequestParam("areaCode") String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaService.getWarehouseAreaByAreaCode(areaCode);
        List<WarehouseLocationDO> list = warehouseLocationService.getWarehouseLocationByAreaCode(warehouseAreaDO);
        List<WarehouseLocationRespVO> locationRespVOList = BeanUtils.toBean(list, WarehouseLocationRespVO.class);
        List<MaterialStockDO> materialStockList = operatingTerminalService.getMaterialStockByLocationIds(locationRespVOList.stream().map(WarehouseLocationRespVO::getId).collect(Collectors.toList()));
        Map<String, MaterialStockDO> materialStockDOMap = CollectionUtils.convertMap(materialStockList, MaterialStockDO::getLocationId);
        for (WarehouseLocationRespVO locationRespVO : locationRespVOList) {
            TrayInfoVO trayInfo = new TrayInfoVO();
            trayInfo.setLocationId(locationRespVO.getId());
            trayInfo.setLocationName(locationRespVO.getLocationName());
            trayInfo.setLocationLocked(DictConstants.INFRA_BOOLEAN_TINYINT_YES.equals(locationRespVO.getLocked()));
            if (materialStockDOMap.containsKey(locationRespVO.getId()))
                trayInfo.setMaterialNumber(materialStockDOMap.get(locationRespVO.getId()).getMaterialNumber());
            if (materialStockDOMap.containsKey(locationRespVO.getId()))
                trayInfo.setBarCode(materialStockDOMap.get(locationRespVO.getId()).getBarCode());
            if (materialStockDOMap.containsKey(locationRespVO.getId()))
                trayInfo.setMaterialStockId(materialStockDOMap.get(locationRespVO.getId()).getId());
            if (materialStockDOMap.containsKey(locationRespVO.getId()))
                trayInfo.setMaterialStorage(materialStockDOMap.get(locationRespVO.getId()).getMaterialStorage());
            locationRespVO.setTrayInfo(trayInfo);
        }
        List<WarehouseLocationRespVO> result = locationRespVOList.stream().sorted((o1, o2) -> o1.getSite().compareTo(o2.getSite())).collect(Collectors.toList());
        return success(result);

    }

    @PostMapping("/call-tray")
    @Operation(summary = "呼叫托盘")
    public CommonResult<Boolean> callTray(@RequestBody Map<String, String> map) {
        String callStockId = map.get("materialStockId");
        String locationId = map.get("targetLocationId");
        String locationCode = map.get("targetLocationCode");
        if (StringUtils.isBlank(locationId)) {
            WarehouseLocationDO warehouseLocationDO = warehouseLocationService.getWarehouseLocationByLocationCode(locationCode);
            if (warehouseLocationDO == null) {
                throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
            }
            locationId = warehouseLocationDO.getId();
        }
        MaterialStockDO callStock = materialStockService.getMaterialStockById(callStockId);
        // 看看有没有绑定在此托盘上的物料
        List<MaterialStockDO> MaterialStockDOList = materialStockService.getMaterialStockListByContainerId(callStockId);
        // 托盘上无物料 呼叫托盘
        if (DictConstants.WMS_MATERIAL_TYPE_TP.equals(callStock.getMaterialType()) && CollectionUtils.isAnyEmpty(MaterialStockDOList)) {
            inWarehouseDetailService.callTray(callStockId, locationId);
        } else {
            // 托盘上有物料  呼叫物料
            outWarehouseDetailService.callMaterial(callStockId, locationId);
        }
        return success(true);
    }

    @GetMapping("/getSignConsignmentInfo")
    @Operation(summary = "获取待签收的收货单")
    public CommonResult<List<ConsignmentInfoRespVO>> getSignConsignmentInfo() {
        List<ConsignmentInfoDTO> consignmentList = purchaseConsignmentApi.getSignConsignmentInfo().getCheckedData();
        return success(BeanUtils.toBean(consignmentList, ConsignmentInfoRespVO.class));
    }

    @GetMapping("/getSignConsignmentDetail")
    @Operation(summary = "获取收货单条码信息")
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getSignConsignmentDetail(@RequestParam("consignmentInfoId") String consignmentInfoId) {
        List<PurchaseConsignmentDetailDTO> list = purchaseConsignmentApi.getSignConsignmentDetail(consignmentInfoId).getCheckedData();
        return success(list);
    }


    // 签收条码
    @PostMapping("/signMaterial")
    public CommonResult<String> signMaterial(@RequestBody ConsignmentSignDTO consignmentSignDTO) {
        return purchaseConsignmentApi.signMaterial(consignmentSignDTO);
    }

    // 签收数量
    @PostMapping("/signNumber")
    public CommonResult<String> signNumber(@RequestBody ConsignmentSignDTO consignmentSignDTO) {
        return purchaseConsignmentApi.signNumber(consignmentSignDTO);
    }

    @PostMapping("/returnConsignment")
    public CommonResult<String> returnConsignment(@RequestBody ConsignmentSignDTO consignmentSignDTO) {
        return purchaseConsignmentApi.returnConsignment(consignmentSignDTO);
    }

    @GetMapping("/getStockForIn")
    @Operation(summary = "获取待入库的库存信息")
    public CommonResult<List<MaterialStockInRespDTO>> getStockForIn(@RequestParam("locationId") String locationId) {
        List<MaterialStockInRespDTO> consignmentList = purchaseConsignmentApi.getStockForIn(locationId).getCheckedData();
        return success(consignmentList);
    }

    @GetMapping("/getSignShippingInfo")
    @Operation(summary = "获取待发货的发货单")
    public CommonResult<List<ShippingInfoDTO>> getSignShippingInfo() {
        List<ShippingInfoDTO> consignmentList = shippingApi.getOutboundingShippingInfo().getCheckedData();
        return success(consignmentList);
    }

    @GetMapping("/getShippingDetailByShippingInfoId")
    @Operation(summary = "根据发货单信息Id获取发货条码信息")
    public CommonResult<List<ShippingDetailDTO>> getShippingDetailByShippingInfoId(@RequestParam("shippingInfoId") String shippingInfoId) {
        List<ShippingDetailDTO> list = shippingApi.getShippingDetailByShippingInfoId(shippingInfoId).getCheckedData();
        return success(list);
    }

    // 签收条码
    @PostMapping("/signMaterialShipping")
    public CommonResult<String> signMaterialShipping(@RequestBody ShippingOutDTO shippingOutDTO) {
        return shippingApi.signMaterial(shippingOutDTO);
    }

    // 出库
    @PostMapping("/generatorOutBound")
    public CommonResult<String> generatorOutBound(@RequestBody ShippingOutDTO shippingOutDTO) {
        return shippingApi.generatorOutBound(shippingOutDTO);
    }

    // 绑定刀具到托盘储位
    @PostMapping("/bindCutterToTray")
    public CommonResult<Boolean> bindCutterToTray(@RequestBody Map<String, Object> map) {
        String cutterId = (String) map.get("cutterId");
        String pickTrayId = (String) map.get("pickTrayId");
        String storageCode = (String) map.get("storageCode");
        MaterialStorageDO materialStorage = materialStorageService.getMaterialStorageByStorageCode(storageCode);
        if (materialStorage == null) {
            throw exception(MATERIAL_STORAGE_NOT_EXISTS);
        }
        MaterialStockDO tray = materialStockService.getMaterialStockById(pickTrayId);
        if (tray == null) {
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        // 传入的托盘编码所属托盘必须为指定的托盘
        if (!pickTrayId.equals(materialStorage.getMaterialStockId())) {
            throw exception(MATERIAL_STOCK_SCAN_BIN_CODE_ERROR);
        }
        List<OutWarehouseDetailDO> waitOutWarehouseDetailList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(Collections.singletonList(cutterId));
        if (waitOutWarehouseDetailList.isEmpty()) {
            throw exception(OUT_WAREHOUSE_MATERIAL_NOT_OUT_OUT_WAREHOUSE_ORDER);
        }

        List<String> appointConfigIds = StringListUtils.stringToArrayList(tray.getContainerConfigIds());

        // 校验指定的容器类型 是否符合预设的配置
        if (!CollectionUtils.isAnyEmpty(appointConfigIds)) {
            if (appointConfigIds.stream().noneMatch(configId -> tray.getMaterialConfigId().contains(configId))) {
                throw exception(STOCK_CHECK_CONTAINER_NOT_MATCH_MATERIAL);
            }
        }

        // 绑定刀具到托盘
        boolean b = materialStockService.updateMaterialStorage(cutterId, materialStorage.getId());
        return success(b);
    }


    // 入库
    @PostMapping("/generatorInBound")
    public CommonResult<List<String>> generatorInBound(@RequestBody List<OrderReqDTO> orderReqDTOList) {

//        List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
//        OrderReqDTO orderReqDTO = new OrderReqDTO();
//        orderReqDTO.setOrderNumber(shippingDO.getNo());
//        orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
//        orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
//        orderReqDTO.setQuantity(1);
//        //目标仓库
//        orderReqDTO.setTargetWarehouseId(updateReqVO.getWarehouseId());
//        orderReqDTOList.add(orderReqDTO);
        return orderApi.orderDistribute(orderReqDTOList);
    }


    /**
     * 获取托盘储位上的刀具信息
     * @param map
     * @return
     */
    @PostMapping("/getCutterLocationInfo")
    public CommonResult<List<CutterTrayInfoVO>> getCutterLocationInfo(@RequestBody Map<String, Object> map){
        List<String> locationCodeS = (List<String>) map.get("locationCode");
        List<WarehouseLocationDO> warehouseLocationS = warehouseLocationService.getWarehouseLocationByLocationCodeS(locationCodeS);
        // 库位编码-库位id
        Map<String, String> locationCodeAndLocationIdMap = CollectionUtils.convertMap(warehouseLocationS, WarehouseLocationDO::getLocationCode, WarehouseLocationDO::getId);
        if(locationCodeS.size()!= warehouseLocationS.size()){
            throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
        }
        // 按照库位编码排序
        HashMap<String, String> sortLocationCodeAndLocationIdMap = new LinkedHashMap<>();
        locationCodeS.forEach(locationCode -> sortLocationCodeAndLocationIdMap.put(locationCode, locationCodeAndLocationIdMap.get(locationCode)));
        List<MaterialStockDO> trayList = materialStockService.getMaterialStockListByLocationIds(sortLocationCodeAndLocationIdMap.values());
        // 库位id-库位托盘信息
        Map<String, MaterialStockDO> trayMap = new HashMap<>();
        // 托盘id - 托盘储位详情           
        Map<String, List<MaterialStorageDO>> cutterTrayDetailMap;
        if(!trayList.isEmpty()){
            trayList.forEach(tray -> {
                if(trayMap.containsKey(tray.getLocationId())){
                    throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
                }
                if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(tray.getMaterialType())){
                    throw exception(WAREHOUSE_LOCATION_CAN_ONLY_BIND_TOOL_TRAY);
                }
                trayMap.put(tray.getLocationId(),tray);
            });

            List<MaterialStorageDO> occupyMaterialStockList = materialStorageService.getDetailMaterialStockListByTrayIds(trayList.stream().map(MaterialStockDO::getId).collect(Collectors.toList()));
            // 托盘id - 托盘储位详情           
            cutterTrayDetailMap = CollectionUtils.convertMap(
                    occupyMaterialStockList,
                    MaterialStorageDO::getMaterialStockId,
                    m -> {
                        List<MaterialStorageDO> s = new ArrayList<>();
                        s.add(m);
                        return s;
                    },
                    (v1, v2) -> {
                        v1.addAll(v2);
                        return v1;
                    });
        } else {
            cutterTrayDetailMap = new HashMap<>();
        }
        List<CutterTrayInfoVO> cutterLocationInfoS = new ArrayList<>();
        sortLocationCodeAndLocationIdMap.forEach((k, v) -> {
            CutterTrayInfoVO cutterLocationInfo = new CutterTrayInfoVO();
            cutterLocationInfo.setLocationId(v);
            cutterLocationInfo.setLocationCode(k);
            cutterLocationInfoS.add(cutterLocationInfo);
            if(trayMap.containsKey(v)){
                // 根据库位id获取托盘信息
                MaterialStockDO materialStockDO = trayMap.get(v);
                String trayId = materialStockDO.getId();
                int ll = materialStockDO.getMaterialLayer() == null ? 1 : materialStockDO.getMaterialLayer();
                int rr = materialStockDO.getMaterialRow() == null ? 1 : materialStockDO.getMaterialRow();
                int cc = materialStockDO.getMaterialCol() == null ? 1 : materialStockDO.getMaterialCol();
                if(!cutterTrayDetailMap.containsKey(trayId)){
                    // 储位信息错误
                    throw exception(MATERIAL_STORAGE_BIN_INFO_ERROR);
                }else {
                    // 根据托盘id 获取托盘储位详情
                    List<MaterialStorageDO> materialStorageList = cutterTrayDetailMap.get(trayId);
                    cutterLocationInfo.setCutterTrayId(trayId);
                    cutterLocationInfo.setCutterTrayCode(materialStockDO.getBarCode());
                    List<CutterTrayDetailInfoVO> cutterTrayDetailInfoVOList = new ArrayList<>();
                    cutterLocationInfo.setCutterTrayDetailInfoS(cutterTrayDetailInfoVOList);
                    // 排下序
                    for (int l = 1; l <= ll; l++) {
                        for (int r = 1; r <= rr; r++) {
                            for (int c = 1; c <= cc; c++) {
                                boolean flag = true;
                                for (MaterialStorageDO materialStorageDO : materialStorageList) {
                                    int layer = materialStorageDO.getLayer() == null ? 1 : materialStorageDO.getLayer();
                                    int row = materialStorageDO.getRow() == null ? 1 : materialStorageDO.getRow();
                                    int col = materialStorageDO.getCol() == null ? 1 : materialStorageDO.getCol();
                                    if(l == layer && r == row && c == col){
                                        CutterTrayDetailInfoVO cutterTrayDetailInfoVO = new CutterTrayDetailInfoVO();
                                        cutterTrayDetailInfoVO.setStorageId(materialStorageDO.getId());
                                        cutterTrayDetailInfoVO.setStorageCode(materialStorageDO.getStorageCode());
                                        cutterTrayDetailInfoVO.setBarCode(materialStorageDO.getBarCode());
                                        cutterTrayDetailInfoVOList.add(cutterTrayDetailInfoVO);
                                        flag = false;
                                        break;
                                    }
                                }
                                // 储位信息错误
                                if(flag)throw exception(MATERIAL_STORAGE_BIN_INFO_ERROR);
                            }
                        }
                    }
                }
            }
        });
        return CommonResult.success(cutterLocationInfoS);
    }

    /**
     * 获取刀具信息
     * @param barCode
     * @return
     */
    @GetMapping("/get-cutter-info")
    public CommonResult<JSONObject> getCutterInfo(@RequestParam("barCode") String barCode){
        return CommonResult.success(operatingTerminalService.getCutterInfo(barCode));
    }

}
