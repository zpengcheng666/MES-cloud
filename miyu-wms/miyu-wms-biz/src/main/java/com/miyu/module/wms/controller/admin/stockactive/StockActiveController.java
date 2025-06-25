package com.miyu.module.wms.controller.admin.stockactive;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockRespVO;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockSaveReqVO;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.instruction.InstructionService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.stockactive.StockActiveService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 物料操作")
@RestController
@RequestMapping("/wms/stock-active")
@Validated
public class StockActiveController {

    @Resource
    private StockActiveService stockActiveService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private InstructionService instructionService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private MaterialConfigAreaService materialConfigAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private CarryTaskService carryTaskService;

    @GetMapping("/on-shelf-page")
    @Operation(summary = "获得物料库存分页")
    @PreAuthorize("@ss.hasPermission('wms:stock-on-shelf:query')")
    public CommonResult<PageResult<MaterialStockRespVO>> getOnshelfPage(@Valid MaterialStockPageReqVO pageReqVO) {
        PageResult<MaterialStockDO> pageResult = stockActiveService.getOnshelfPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialStockRespVO.class));
    }
    @GetMapping("/off-shelf-page")
    @Operation(summary = "获得物料库存分页")
    @PreAuthorize("@ss.hasPermission('wms:stock-off-shelf:query')")
    public CommonResult<PageResult<MaterialStockRespVO>> getOffshelfPage(@Valid MaterialStockPageReqVO pageReqVO) {
        PageResult<MaterialStockDO> pageResult = stockActiveService.getOffshelfPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialStockRespVO.class));
    }

    @GetMapping("/stock-bind-page")
    @Operation(summary = "获得物料绑定分页")
    @PreAuthorize("@ss.hasPermission('wms:stock-bind:query')")
    public CommonResult<PageResult<MaterialStockRespVO>> getStockBindPage(@Valid MaterialStockPageReqVO pageReqVO) {
        // todo: QianJY 待更改查询条件
        PageResult<MaterialStockDO> pageResult = stockActiveService.getOnshelfPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialStockRespVO.class));
    }

    /**
     * 物料绑定流程  todo：QianJY 对于库存锁定功能的校验 待后续完善
     * 物料绑定发生场景 ： 1. 物料入库 2. 物料出库 3. 物料盘点 4. 物料移库
     * 1. 物料入库： 物料入库时，需要将物料绑定到库存中，并更新库存数量。-- 理论是这样 但是物料收货了，绑定功能就不用输入物料数量了
     * 2. 物料出库： 物料出库时，需要将物料从库存中移除，并更新库存数量。
     * 3. 物料盘点： 物料盘点时，需要将物料绑定到库存中，并更新库存数量。
     * 4. 物料移库： 物料移库时，需要将物料从库存中移除，并更新库存数量。
     *   1. 查询物料是否存在物料入库 或者 物料出库订单
     *      1.1. 如果不存在，则提示不能绑定 并提示用户
     */
    @PutMapping("/bind-material")
    @Operation(summary = "物料绑定")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:update')")
    public CommonResult<Boolean> bindMaterial(@Valid @RequestBody MaterialStockSaveReqVO materialStockSave) {
        // todo: QianJY 物料绑定校验 后续可能会增加一些的定的校验规则 待完善
        // 校验物料是否存在物料入库 或者 物料出库的详情订单
        List<MaterialStockDO> materialStockList = materialStockService.checkMaterialStockOrderExists(Arrays.asList(materialStockSave.getId()));
        // todo 查询其他订单 是否存在 待完善

        if(CollectionUtils.isAnyEmpty(materialStockList) && 1==1/* 其他条件待添加*/){
            throw exception(MATERIAL_STOCK_NO_MOVE_PERMISSION);
        }


        materialStockService.updateMaterialStock(materialStockSave);
        return success(true);
    }


    @GetMapping("/stock-off-shelf")
    @Operation(summary = "库存下架")
    @PreAuthorize("@ss.hasPermission('wms:stock-off-shelf:active')")
    public CommonResult<Boolean> stockOffShelf(String containerStockId, String offLocationId) {
        if (StringUtils.isBlank(offLocationId)) {
            throw exception(MATERIAL_STOCK_INVALID_DOWN_LOCATION);
        }

        // 下架得查是否有未完成的搬运任务 因为 可能有呼叫物料的情况，所以就不能让人工下架此物料了
        // 查询此容器是否存在未完成的搬运任务或指令
        carryTaskService.checkSubCarryTask(containerStockId);

        // 只能下架至 装夹区
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(offLocationId);
        if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_9 != warehouseArea.getAreaType()){
            throw exception(MATERIAL_STOCK_INVALID_DOWN_LOCATION);
        }

        MaterialStockDO materialStock = materialStockService.getMaterialStock(containerStockId);
        String startLocationId = materialStock.getLocationId();

        // 生成未开始的下架指令
        instructionService.offShelfInstruction( containerStockId, startLocationId, offLocationId);

        return success(true);
    }


    @GetMapping("/stock-on-shelf")
    @Operation(summary = "库存上架")
    @PreAuthorize("@ss.hasPermission('wms:stock-on-shelf:active')")
    public CommonResult<Boolean> stockOnShelf(String containerStockId) {
        InstructionDO notFinishedInstruction = instructionService.getNotFinishedInstructionByMaterialStockId(containerStockId);
        if(notFinishedInstruction != null){
            throw exception(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);
        }

        // 查询此容器是否存在未完成的搬运任务或指令
        carryTaskService.checkSubCarryTask(containerStockId);

        MaterialStockDO containerStock = materialStockService.getMaterialStock(containerStockId);
        String containerConfigId = containerStock.getMaterialConfigId();
        String onShelfStartLocationId = containerStock.getLocationId();

        // 只有在功能区的才能上架
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(onShelfStartLocationId);
        if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_9 != warehouseArea.getAreaType()){
            throw exception(MATERIAL_STOCK_CANNOT_PUT_ON);
        }

        // 获取物料所属仓库
        WarehouseDO warehouse = warehouseService.getWarehouseByLocationId(onShelfStartLocationId);
        String warehouseId = warehouse.getId();
        // 获得此仓库下的所有可用的 容器上架存储库区
        List<MaterialConfigAreaDO> containerConfigStorageArea = materialConfigAreaService.getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(containerConfigId, warehouseId);
        if(CollectionUtils.isAnyEmpty(containerConfigStorageArea)){
            throw exception(CARRYING_TASK_MATERIAL_NOT_FOUND_STORAGE_AREA);
        }

        List<String> availableAreaIds = containerConfigStorageArea.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList());
        // 可用库区配置不为空 那就随便找一个空库位就行了
        WarehouseLocationDO availableOtherLocation = warehouseLocationService.getAvailableStorageLocationId(availableAreaIds, onShelfStartLocationId);

        // 生成未开始的上架指令
        instructionService.onShelfInstruction(containerStockId, onShelfStartLocationId, availableOtherLocation.getId());

        return success(true);
    }



    /**
     * 签收物料 可以签收容器类物料 和 非容器物料
     * 1. 物料应该存在 待签收的 出库详情单 否则提示用户
     * 2. 如果签收容器类物料 则 绑定在其上的所有物料都将被签收
     * 3. 如果签收非容器类物料 则 只签收此物料的出库详情单
     *
     * 4. 签收成功后更新库位
     */
    @PostMapping("/sign-for-material")
    @Operation(summary = "签收物料")
    public CommonResult<Boolean> signForMaterial(@RequestBody Map<String,String> map) {
        // 物料
        String signForMaterialStockId = map.get("materialStockId");
        // 签收库位
        String signForLocationId = map.get("locationId");

        MaterialStockDO signForMaterialStock = materialStockService.getMaterialStockById(signForMaterialStockId);
        stockActiveService.signForAllWaitSignForMaterial(signForMaterialStock, signForLocationId);

        return success(true);

    }


    /**
     * 分拣物料  校验出入库单， 不存在出入库单的物料将无法分拣
     * @param map
     * @return
     */
    @PostMapping("/verify-pick-material")
    @Operation(summary = "分拣物料--校验出入库单")
    public CommonResult<Boolean> verifyPickMaterial(@RequestBody Map<String,String> map) {
        String orderId = map.get("orderId");
        String materialStockId = map.get("materialStockId");
        String locationId = map.get("locationId");
        String storageId = map.get("storageId");
        materialStockService.verifyMaterialPicking(orderId,materialStockId, locationId, storageId);
        return success(true);
    }

    /**
     * 分拣物料  不校验出入库单，随意分拣 --仅用于在产线内进行物料分拣装卸的开完工操作
     * @param map
     * @return
     */
    @PostMapping("/pick-material")
    @Operation(summary = "分拣物料")
    public CommonResult<Boolean> pickMaterial(@RequestBody Map<String,String> map) {
        String materialStockId = map.get("materialStockId");
        String locationId = map.get("locationId");
        String storageId = map.get("storageId");
        Integer pickQuantity = Integer.parseInt(map.get("pickQuantity"));
        materialStockService.materialPicking(materialStockId, pickQuantity, locationId, storageId);
        return success(true);
    }

}
