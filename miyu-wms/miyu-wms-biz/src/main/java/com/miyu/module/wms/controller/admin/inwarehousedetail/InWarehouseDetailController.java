package com.miyu.module.wms.controller.admin.inwarehousedetail;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.wms.api.order.OrderDistributeApiImpl;
import com.miyu.module.wms.api.order.dto.ProductionOrderReqDTO;
import com.miyu.module.wms.api.order.dto.ProductionOrderRespDTO;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.InWarehouseDetailPageReqVO;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.InWarehouseDetailRespVO;
import com.miyu.module.wms.controller.admin.inwarehousedetail.vo.InWarehouseDetailSaveReqVO;
import com.miyu.module.wms.core.carrytask.service.impl.CallTrayServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.service.stockactive.StockActiveService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import com.miyu.module.wms.convert.inwarehousedetail.InWarehouseDetailConvert;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Tag(name = "管理后台 - 入库详情")
@RestController
@RequestMapping("/wms/in-warehouse-detail")
@Validated
@Slf4j
public class InWarehouseDetailController {

    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private StockActiveService stockActiveService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private OrderDistributeApiImpl orderDistributeApi;

    @PostMapping("/create")
    @Operation(summary = "创建入库详情")
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:create')")
    public CommonResult<String> createInWarehouseDetail(@Valid @RequestBody InWarehouseDetailSaveReqVO createReqVO) {
        if(StringUtils.isNotBlank(createReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStockById(createReqVO.getChooseStockId());
            createReqVO.setBatchNumber(materialStock.getBatchNumber());
            createReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(Objects.equals(materialStock.getTotality(), createReqVO.getQuantity())){
                createReqVO.setMaterialStockId(materialStock.getId());
                WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                createReqVO.setStartWarehouseId(warehouse.getId());
                if(StringUtils.isBlank(createReqVO.getTargetWarehouseId()))createReqVO.setTargetWarehouseId(materialStock.getDefaultWarehouseId());
            }
            if(materialStock.getTotality() < createReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        return success(inWarehouseDetailService.createInWarehouseDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库详情")
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:update')")
    public CommonResult<Boolean> updateInWarehouseDetail(@Valid @RequestBody InWarehouseDetailSaveReqVO updateReqVO) {
        if(StringUtils.isNotBlank(updateReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStockById(updateReqVO.getChooseStockId());
            updateReqVO.setBatchNumber(materialStock.getBatchNumber());
            updateReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(Objects.equals(materialStock.getTotality(), updateReqVO.getQuantity())){
                updateReqVO.setMaterialStockId(materialStock.getId());
                WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                updateReqVO.setStartWarehouseId(warehouse.getId());
                updateReqVO.setTargetWarehouseId(materialStock.getDefaultWarehouseId());
            }
            if(materialStock.getTotality() < updateReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        inWarehouseDetailService.updateInWarehouseDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:delete')")
    public CommonResult<Boolean> deleteInWarehouseDetail(@RequestParam("id") String id) {
        inWarehouseDetailService.deleteInWarehouseDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得入库详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:query')")
    public CommonResult<InWarehouseDetailRespVO> getInWarehouseDetail(@RequestParam("id") String id) {
        InWarehouseDetailDO inWarehouseDetail = inWarehouseDetailService.getInWarehouseDetail(id);
        return success(BeanUtils.toBean(inWarehouseDetail, InWarehouseDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库详情分页")
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:query')")
    public CommonResult<PageResult<InWarehouseDetailRespVO>> getInWarehouseDetailPage(@Valid InWarehouseDetailPageReqVO pageReqVO) {
        PageResult<InWarehouseDetailDO> pageResult = inWarehouseDetailService.getInWarehouseDetailPage(pageReqVO);
        //创建者
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), InWarehouseDetailDO::getCreator));
        Map<Long, AdminUserRespDTO> userMap = null;
        if (!CollectionUtils.isAnyEmpty(creatorIds)) {
            creatorIds = creatorIds.stream().distinct().collect(Collectors.toList());
            // 拼接数据
            userMap = userApi.getUserMap(creatorIds);
        }

        return success(new PageResult<>(InWarehouseDetailConvert.INSTANCE.convertList(pageResult.getList(), userMap), pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库详情 Excel")
    @PreAuthorize("@ss.hasPermission('wms:in-warehouse-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInWarehouseDetailExcel(@Valid InWarehouseDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InWarehouseDetailDO> list = inWarehouseDetailService.getInWarehouseDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库详情.xls", "数据", InWarehouseDetailRespVO.class,
                        BeanUtils.toBean(list, InWarehouseDetailRespVO.class));
    }


    /*******************************************************************************************************************/


    /**
     * 入库总流程
     * 1. 采购部门生成采购入库单
     * 2. 物料到货 根据采购入库单号进行物料收货   ---》 同时生成物料库存  ---》收货全部完成通知采购部门采购单完成
     * 3. 根据物料属性 QualityCheck 决定是否质检 生成质检任务
     * 4. 质检通过 物料入库  生成wms采购入库单  主要是填写入库仓库 无需审批   可根据采购入库单直接生成wms的采购入库单
     * 5. 库管人员根据入库单  呼叫AGV
     *                      -- 》 托盘抵达后
     *                      ---》进行物料绑定工作
     * 6. 绑定完成 点击入库按钮  AGV配送入库
     */


    /**
     * ①呼叫AGV流程
     * 1. 选择 工装或托盘或空 AGV呼叫
     *  1.1. 如果选择工装 或者托盘
     *   1.1.1. 如果其上绑定其他物料，则提示用户 请先解绑  todo 待实现
     * 2. 查询呼叫库位是否为空
     *  2.1. 若为空 则提示用户 请先选择库位
     *  2.2. 若不为空 则查询库位上是否绑定其他物料
     *   2.2.1. 若绑定 则提示用户 库位已被占用
     * 3. 判断容器所在库位 是否需要执行下架指令 -- 库区属性为WCS自动存储库区 则需要执行下架指令
     *  3.1. 如需下架 查询是否存在未完成的下架指令
     *   3.1.1. 若存在 提示用户 请先完成该指令
     * 4. 查询是否存在未完成的搬运任务
     * 5. 若存在 则提示用户 请先完成该任务
     * 6. 若不存在 则下发下架指令
     * 7. 若不存在 则生成待激活的搬运任务 （提前占用库位 防止任务库位被抢占）todo agv库位和人工库位不混用
     */
    // 废弃
/*    @PostMapping("/call-tray")
    @Operation(summary = "呼叫托盘")
    public CommonResult<Boolean> callTray(@RequestBody Map<String, String> map) {
        // 容器类型 物料
        String callTrayStockId = map.get("materialStockId");
        // 库位 -- 目标库位
        String callLocationId = map.get("locationId");

        inWarehouseDetailService.callTray(callTrayStockId, callLocationId);

        return success(true);
    }*/


    /**
     * ③入库单 入库流程 -- 点击入库 按钮 进行物料入库操作流程
     * 接收参数 库位ID
     * 1. 先判断起始库位的库区是不是AGV接驳库区 不是就不用扯了
     * 2. 查询库位上绑定的物料
     * 3. 首先此物料有且只有一个 一定是托盘 不是就抛出异常 提示用户
     * 4. 然后判断托盘上是否有物料
     *  4.1. 若没有物料 则根据默认存放仓库   -- 》 直接赋值目标库位
     *    4.2.2. 若不是容器类物料 则遍历物料  查询是否全部存在入库单
     *      4.2.2.1. 若全部存在入库单  再次判断是否 所有入库单的入库仓库都一致 若一致    --》则赋值目标库位
     *      4.2.2.2. 若不一致 则抛出异常 则抛出异常 提示用户
     *    4.2.3. 若有物料不存在入库单 则抛出异常 提示用户
     * 5. 完活 发车。。。。
     *
     * 搬运场景设想：
     *  1. 只搬运托盘   --》 有入库单入库(感觉没必要有入库单了，他都有默认存放仓库了)  或 --》无入库单回库
     *  2. 一个托盘上 放置多个工装  --》 工装有无入库单
     *  3. 一个托盘上 放置多个工装  工装上放置多个物料 --》 物料有无入库单
     *  4. 一个托盘上 放置多个物料 --》 物料有无入库单
     *
     */
    @PostMapping("/in-warehouse-action")
    @Operation(summary = "入库操作")
    public CommonResult<Boolean> inWarehouseAction(String locationId) {

      /*  // 获取自动调度单
        List<ProductionOrderRespDTO> cacheProductionOrderList = orderDistributeApi.getCacheProductionOrderList();
        // 获取库位上的所有物料
        List<MaterialStockDO> allMaterialStock = materialStockService.getAllMaterialStockByLocationId(locationId);
        if(!CollectionUtils.isAnyEmpty(cacheProductionOrderList, allMaterialStock)){
            // 获取托盘上的物料是否存在自动调度单
            long count = allMaterialStock.stream().filter(material -> cacheProductionOrderList.stream().anyMatch(productionOrder -> productionOrder.getBarCode().equals(material.getBarCode()))).count();
            if(count > 0){
                // 解锁库位
                warehouseLocationService.unlockLocation(locationId);
                // 返回false 用来给前端提示 物料存在自动调度单中，正在等待分配搬运任务
                return success(false);
            }
        }*/

        inWarehouseDetailService.inWarehouseAction(null, locationId);
        // 解锁库位
        warehouseLocationService.unlockLocation(locationId);
        return success(true);
    }

    @PostMapping("/check-in")
    @Operation(summary = "入库签入")
    public CommonResult<Boolean> checkIn(@RequestBody Map<String,String> map) {

        // 物料条码
        String barCode = map.get("barCode");
        // 签入储位
        String storageCode = map.get("storageCode");

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList.size() != 1){
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }
        MaterialStockDO checkInMaterialStock = materialStockList.get(0);
        if(Objects.equals(checkInMaterialStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_TP)){
            throw exception(MATERIAL_STOCK_CANNOT_SIGN_IN_TRAY);
        }

        stockActiveService.checkInMaterial(checkInMaterialStock, storageCode);
        return success(true);
    }


    @PostMapping("/check-in-cutter")
    @Operation(summary = "入库签入-刀具")
    public CommonResult<Boolean> checkInCutter(@RequestBody Map<String,String> map) {

        // 物料条码
        String barCode = map.get("barCode");
        // 签入储位
        String storageCode = map.get("storageCode");
        String trayId = map.get("trayId");

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList.size() != 1){
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }
        MaterialStockDO checkInMaterialStock = materialStockList.get(0);
        if(!Objects.equals(checkInMaterialStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_TP)
                && !Objects.equals(checkInMaterialStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_DJ)){
            throw exception(MATERIAL_STOCK_CANNOT_SIGN_IN_TRAY);
        }

        stockActiveService.checkInCutter(checkInMaterialStock, storageCode, trayId);
        return success(true);
    }


    @PostMapping("/check-in-assign-cutter")
    @Operation(summary = "入库签入-刀具")
    public CommonResult<Boolean> checkInAssignCutter(@RequestBody Map<String,String> map) {
        // 物料条码
        String barCode = map.get("barCode");
        // 签入储位
        String storageId = map.get("storageId");

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList.size() != 1){
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }
        MaterialStockDO checkInMaterialStock = materialStockList.get(0);
        if(!Objects.equals(checkInMaterialStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_TP)
                && !Objects.equals(checkInMaterialStock.getMaterialType(), DictConstants.WMS_MATERIAL_TYPE_DJ)){
            throw exception(MATERIAL_STOCK_CANNOT_SIGN_IN_TRAY);
        }

        stockActiveService.checkInCutter(checkInMaterialStock, storageId);
        return success(true);
    }

}

