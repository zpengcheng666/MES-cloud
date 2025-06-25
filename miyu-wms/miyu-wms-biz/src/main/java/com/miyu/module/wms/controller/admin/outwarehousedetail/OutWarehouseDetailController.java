package com.miyu.module.wms.controller.admin.outwarehousedetail;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.alibaba.fastjson.JSONObject;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.module.wms.core.carrytask.service.impl.CallMaterialServiceImpl;
import com.miyu.module.wms.core.carrytask.service.impl.ToolDistributionServiceImpl;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.service.stockactive.StockActiveService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import com.miyu.module.wms.convert.outwarehousedetail.OutWarehouseDetailConvert;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;
import com.miyu.module.wms.controller.admin.outwarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;

@Tag(name = "管理后台 - 出库详情")
@RestController
@RequestMapping("/wms/out-warehouse-detail")
@Validated
@Slf4j
public class OutWarehouseDetailController {

    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private StockActiveService stockActiveService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;

    @PostMapping("/create")
    @Operation(summary = "创建出库详情")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:create')")
    public CommonResult<String> createOutWarehouseDetail(@Valid @RequestBody OutWarehouseDetailSaveReqVO createReqVO) {
        if(StringUtils.isNotBlank(createReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStock(createReqVO.getChooseStockId());
            createReqVO.setBatchNumber(materialStock.getBatchNumber());
            createReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(materialStock.getTotality() == createReqVO.getQuantity()){
                createReqVO.setMaterialStockId(materialStock.getId());
                WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                createReqVO.setStartWarehouseId(warehouse.getId());
            }
            if(materialStock.getTotality() < createReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        return success(outWarehouseDetailService.createOutWarehouseDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新出库详情")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:update')")
    public CommonResult<Boolean> updateOutWarehouseDetail(@Valid @RequestBody OutWarehouseDetailSaveReqVO updateReqVO) {
        if(StringUtils.isNotBlank(updateReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStock(updateReqVO.getChooseStockId());
            updateReqVO.setBatchNumber(materialStock.getBatchNumber());
            updateReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(materialStock.getTotality() == updateReqVO.getQuantity()){
                updateReqVO.setMaterialStockId(materialStock.getId());
                if(StringUtils.isBlank(updateReqVO.getStartWarehouseId())){
                    WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                    updateReqVO.setStartWarehouseId(warehouse.getId());
                }
            }
            if(materialStock.getTotality() < updateReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        outWarehouseDetailService.updateOutWarehouseDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除出库详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:delete')")
    public CommonResult<Boolean> deleteOutWarehouseDetail(@RequestParam("id") String id) {
        outWarehouseDetailService.deleteOutWarehouseDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得出库详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:query')")
    public CommonResult<OutWarehouseDetailRespVO> getOutWarehouseDetail(@RequestParam("id") String id) {
        OutWarehouseDetailDO outWarehouseDetail = outWarehouseDetailService.getOutWarehouseDetail(id);
        return success(BeanUtils.toBean(outWarehouseDetail, OutWarehouseDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得出库详情分页")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:query')")
    public CommonResult<PageResult<OutWarehouseDetailRespVO>> getOutWarehouseDetailPage(@Valid OutWarehouseDetailPageReqVO pageReqVO) {
        PageResult<OutWarehouseDetailDO> pageResult = outWarehouseDetailService.getOutWarehouseDetailPage(pageReqVO);
        //创建者
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), OutWarehouseDetailDO::getCreator));
        Map<Long, AdminUserRespDTO> userMap = null;
        if(!CollectionUtils.isAnyEmpty(creatorIds)){
            creatorIds = creatorIds.stream().distinct().collect(Collectors.toList());
            // 拼接数据
            userMap = userApi.getUserMap(creatorIds);
        }

        return success(new PageResult<>(OutWarehouseDetailConvert.INSTANCE.convertList(pageResult.getList(), userMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出出库详情 Excel")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOutWarehouseDetailExcel(@Valid OutWarehouseDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OutWarehouseDetailDO> list = outWarehouseDetailService.getOutWarehouseDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "出库详情.xls", "数据", OutWarehouseDetailRespVO.class,
                        BeanUtils.toBean(list, OutWarehouseDetailRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得出库详情列表")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:query')")
    public CommonResult<List<OutWarehouseDetailRespVO>> getOutWarehouseDetailList() {
        List<OutWarehouseDetailDO> list = outWarehouseDetailService.getOutWarehouseDetailList();
        return success(BeanUtils.toBean(list, OutWarehouseDetailRespVO.class));
    }

    @GetMapping("/groupByOrderNumberList")
    @Operation(summary = "获得出库详情列表-分组")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:query')")
    public CommonResult<List<String>> getOutWarehouseDetailGroupByOrderNumberList() {
        List<String> list = outWarehouseDetailService.getOutWarehouseDetailGroupByOrderNumberList(DictConstants.WMS_ORDER_DETAIL_STATUS_1);
        return success(list);
    }

    @GetMapping("/listByOrderNumber")
    @Operation(summary = "获得出库详情列表")
    @PreAuthorize("@ss.hasPermission('wms:out-warehouse-detail:query')")
    public CommonResult<List<OutWarehouseDetailRespVO>> getOutWarehouseDetailListByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
        List<OutWarehouseDetailDO> list = outWarehouseDetailService.getOutWarehouseDetailListByOrderNumber(orderNumber, DictConstants.WMS_ORDER_DETAIL_STATUS_1);
        return success(BeanUtils.toBean(list, OutWarehouseDetailRespVO.class));
    }


    /*************************************************************************************************************/



    /**
     * 呼叫物料
     * 1. 判断出库类型 与 出库单的出库类型是否一致 不一致提示用户
     * 2. 查询呼叫库位是否为空位
     * 3. 根据物料 获取物料所在托盘 与 物料所在库位
     * 4. 判断托盘是否配置呼叫库位 的物料库区配置  ，如果没有配置 则提示用户配置
     * 5. 判断托盘上是否存在其他物料 并且是否全部存在出库单，如果托盘上有不存在出库单的非容器类物料，则提示用户
     * 6. 判断托盘是否在{存储位}上，不在则提示用户
     * 7. 获取此托盘可用的AGV接驳起始库位
     * 8. 下发下架指令 并且下发待激活的搬运任务
     */
    @PostMapping("/call-material")
    @Operation(summary = "呼叫物料")
    public CommonResult<Boolean> callMaterial(@RequestBody Map<String,Object> map) {
        // 出库类型
//        List<Integer> outTypes = (List<Integer>) map.get("outTypes");
        // 出库单号
//        String orderNumber = map.get("orderNumber").toString();
        // 物料
        String callMaterialStockId = map.get("materialStockId").toString();
        // 库位 -- 目标库位
        String callLocationId = map.get("locationId").toString();

        outWarehouseDetailService.callMaterial(callMaterialStockId, callLocationId);

        return success(true);
    }


    /**
     * 刀具配送
     * @param locationCode
     * @return
     */
    @GetMapping("/tool-distribution")
    @Operation(summary = "刀具配送")
    public CommonResult<Boolean> distributionTool(@RequestParam("locationCode") String locationCode) {
        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationCode(locationCode);
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个容器 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);
        if (!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY);
        }

        outWarehouseDetailService.generateDistributionTask(containerStock);

        // 解锁库位
        warehouseLocationService.unlockLocation(containerStock.getLocationId());
        return success(true);
    }

    // 获取刀具配送路径
    @GetMapping("/tool-distribution-path")
    @Operation(summary = "获取刀具配送路径")
    public CommonResult<List<String>> getDistributionPath(String locationCode) {
        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationCode(locationCode);
        if(containerStockList.isEmpty()){
            return success(Collections.emptyList());
        }
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个容器 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);
        if (!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())) {
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY);
        }

        List<String> pathList = outWarehouseDetailService.getDistributionPath(containerStock);
        return success(pathList);
    }

    @PostMapping("/check-out")
    @Operation(summary = "出库签出")
    public CommonResult<Boolean> checkOut(@RequestBody Map<String,Object> map) {

        // 签收的物料条码
        String barCode = map.get("barCode").toString();
        Object locationCode = map.get("locationCode");
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if (materialStockList.size() != 1) {
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }
        MaterialStockDO materialStockDO = materialStockList.get(0);

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStock(materialStockDO);
        // 物料所在仓库
        String materialAtWarehouseId = warehouseArea.getWarehouseId();
        String atLocationId = warehouseArea.getJoinLocationId();

        String signForLocationId = null;
        // 未指定签收库位时候 签收库位使用登录用户绑定的库位
        if(Objects.isNull(locationCode)){

            Object locationByUser = map.get("locationByUserData");
            if (!(locationByUser instanceof List)) {
                throw exception(USER_NOT_LOGIN);
            }

            List<String> locationByUserData = (List<String>) locationByUser;

            // 获得登陆人绑定的库区 库位列表
            List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaByLocationIds(locationByUserData);


            // 可以签收的库位
            List<WarehouseAreaDO> signWarehouseAreaList = warehouseAreaList.stream().filter(item -> item.getWarehouseId().equals(materialAtWarehouseId)).collect(Collectors.toList());
            // 检验登录人绑定的仓库与物料所在仓库是否一致
            if (CollectionUtils.isAnyEmpty(signWarehouseAreaList)) {
                throw exception(USER_NOT_BIND_THIS_WAREHOUSE_SIGN_AREA);
            }
            if (signWarehouseAreaList.size() != 1) {
                throw exception(USER_BIND_MULTI_SIGN_AREA_IN_THIS_WAREHOUSE);
            }
            signForLocationId = signWarehouseAreaList.get(0).getJoinLocationId();
        }else {
            // 指定签收库位
            WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocationByLocationCode(locationCode.toString());
            if (warehouseLocation == null) {
                throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
            }
            signForLocationId = warehouseLocation.getId();
        }

        stockActiveService.signForAllWaitSignForMaterial(materialStockDO, signForLocationId);

        WarehouseDO warehouse = warehouseService.getWarehouse(materialAtWarehouseId);
        // 自动化线体库 物料签收 自动解锁库位
        if(DictConstants.WMS_WAREHOUSE_TYPE_5.equals(warehouse.getWarehouseType())){
            warehouseLocationService.unlockLocation(atLocationId);
        }
        return success(true);
    }

    @PostMapping("/check-out-cutter")
    @Operation(summary = "出库签出-刀具")
    public CommonResult<Boolean> checkOutCutter(@RequestBody Map<String,Object> map) {

        // 签收的物料id
        String barCode = map.get("barCode").toString();
        // 签出库位id
        String locationCode = map.get("locationCode").toString();

        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList.size() != 1){
            throw exception(MATERIAL_STOCK_NOT_FOUND);
        }
        MaterialStockDO materialStockDO = materialStockList.get(0);

        stockActiveService.signForAllWaitSignForCutter(materialStockDO, locationCode);

        return success(true);
    }

}
